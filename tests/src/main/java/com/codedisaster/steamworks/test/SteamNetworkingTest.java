package com.codedisaster.steamworks.test;

import com.codedisaster.steamworks.*;
import com.codedisaster.steamworks.networking.SteamNetworking;
import com.codedisaster.steamworks.networking.SteamNetworkingCallback;
import com.codedisaster.steamworks.test.mixin.FriendsMixin;
import com.codedisaster.steamworks.user.SteamUser;
import com.codedisaster.steamworks.user.SteamUserCallback;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SteamNetworkingTest extends SteamTestApp {

	private static final int defaultChannel = 1;
	private static final Charset messageCharset = StandardCharsets.UTF_8;

	private static final int readBufferCapacity = 4096;
	private static final int sendBufferCapacity = 4096;

	private FriendsMixin friends;
	private SteamNetworking networking;

	private final ByteBuffer packetReadBuffer = ByteBuffer.allocateDirect(readBufferCapacity);
	private final ByteBuffer packetSendBuffer = ByteBuffer.allocateDirect(sendBufferCapacity);

	private SteamUser user;
	private final Map<Integer, SteamID> remoteUserIDs = new ConcurrentHashMap<>();

	private SteamAuthTicket userAuthTicket;
	private final ByteBuffer userAuthTicketData = ByteBuffer.allocateDirect(256);

	private SteamID remoteAuthUser;
	private final ByteBuffer remoteAuthTicketData = ByteBuffer.allocateDirect(256);

	private final byte[] AUTH = "AUTH".getBytes(Charset.defaultCharset());

	private final SteamUserCallback userCallback = new SteamUserCallback() {
		@Override
		public void onAuthSessionTicket(final SteamAuthTicket authTicket, final SteamResult result) {

		}

		@Override
		public void onValidateAuthTicket(final SteamID steamID,
										 final SteamAuth.AuthSessionResponse authSessionResponse,
										 final SteamID ownerSteamID) {

			System.out.println("Auth session response for userID " + steamID.getAccountID() + ": " +
					authSessionResponse.name() + ", borrowed=" + (steamID.equals(ownerSteamID) ? "yes" : "no"));

			if (authSessionResponse == SteamAuth.AuthSessionResponse.AuthTicketCanceled) {
				// ticket owner has cancelled the ticket, end the session
				endAuthSession();
			}
		}

		@Override
		public void onMicroTxnAuthorization(final int appID, final long orderID, final boolean authorized) {

		}

		@Override
		public void onEncryptedAppTicket(final SteamResult result) {

		}
	};

	private final SteamNetworkingCallback peer2peerCallback = new SteamNetworkingCallback() {
		@Override
		public void onP2PSessionConnectFail(final SteamID steamIDRemote, final SteamNetworking.P2PSessionError sessionError) {
			System.out.println("P2P connection failed: userID=" + steamIDRemote.getAccountID() +
					", error: " + sessionError);

			unregisterRemoteSteamID(steamIDRemote);
		}

		@Override
		public void onP2PSessionRequest(final SteamID steamIDRemote) {
			System.out.println("P2P connection requested by userID " + steamIDRemote.getAccountID());
			registerRemoteSteamID(steamIDRemote);
			networking.acceptP2PSessionWithUser(steamIDRemote);
		}
	};

	@Override
	protected void registerInterfaces() {
		user = new SteamUser(userCallback);

		friends = new FriendsMixin();
		networking = new SteamNetworking(peer2peerCallback);

		networking.allowP2PPacketRelay(true);
	}

	@Override
	protected void unregisterInterfaces() {
		cancelAuthTicket();
		user.dispose();
		friends.dispose();
		networking.dispose();
	}

	@Override
	protected void processUpdate() throws SteamException {

		final int packetSize = networking.isP2PPacketAvailable(defaultChannel);

		if (packetSize > 0) {

			final SteamID steamIDSender = new SteamID();

			packetReadBuffer.clear();

			if (networking.readP2PPacket(steamIDSender, packetReadBuffer, defaultChannel) > 0) {

				// register, if unknown
				registerRemoteSteamID(steamIDSender);

				final int bytesReceived = packetReadBuffer.limit();
				System.out.println("Rcv packet: userID=" + steamIDSender.getAccountID() + ", " + bytesReceived + " bytes");

				final byte[] bytes = new byte[bytesReceived];
				packetReadBuffer.get(bytes);

				// check for magic bytes first
				final int magicBytes = checkMagicBytes(packetReadBuffer, AUTH);
				if (magicBytes > 0) {
					// extract ticket
					remoteAuthTicketData.clear();
					remoteAuthTicketData.put(bytes, magicBytes, bytesReceived - magicBytes);
					remoteAuthTicketData.flip();
					System.out.println("Auth ticket received: " + remoteAuthTicketData.toString() +
							" [hash: " + remoteAuthTicketData.hashCode() + "]");
					// auth
					beginAuthSession(steamIDSender);
				} else {
					// plain text message
					final String message = new String(bytes, messageCharset);
					System.out.println("Rcv message: \"" + message + "\"");
				}
			}

		}

	}

	@Override
	protected void processInput(final String input) throws SteamException {

		if (input.startsWith("p2p send ")) {
			final String[] params = input.substring("p2p send ".length()).split(" ");
			final int receiverID = Integer.parseInt(params[0]);

			SteamID steamIDReceiver = null;
			if (remoteUserIDs.containsKey(receiverID)) {
				steamIDReceiver = remoteUserIDs.get(receiverID);
			} else if (friends.isFriendAccountID(receiverID)) {
				steamIDReceiver = friends.getFriendSteamID(receiverID);
			} else {
				System.out.println("Error: unknown userID " + receiverID + " (no friend, not connected)");
			}

			if (steamIDReceiver != null) {

				packetSendBuffer.clear(); // pos=0, limit=cap

				for (int i = 1; i < params.length; i++) {
					final byte[] bytes = params[i].getBytes(messageCharset);
					if (i > 1) {
						packetSendBuffer.put((byte) 0x20);
					}
					packetSendBuffer.put(bytes);
				}

				packetSendBuffer.flip(); // limit=pos, pos=0

				networking.sendP2PPacket(steamIDReceiver, packetSendBuffer,
						SteamNetworking.P2PSend.Unreliable, defaultChannel);
			}
		} else if (input.startsWith("p2p close ")) {
			final int remoteID = Integer.parseInt(input.substring("p2p close ".length()));

			SteamID steamIDRemote = null;
			if (remoteUserIDs.containsKey(remoteID)) {
				steamIDRemote = remoteUserIDs.get(remoteID);
			} else {
				System.out.println("Error: unknown remote ID " + remoteID + " (not connected)");
			}

			if (steamIDRemote != null) {
				networking.closeP2PSessionWithUser(steamIDRemote);
				unregisterRemoteSteamID(steamIDRemote);
			}
		} else if (input.equals("p2p list")) {
			System.out.println("P2P connected users:");
			if (remoteUserIDs.size() == 0) {
				System.out.println("  none");
			}
			for (final SteamID steamIDUser : remoteUserIDs.values()) {
				System.out.println("  " + steamIDUser.getAccountID());
			}
		} else if (input.startsWith("auth ticket ")) {
			final String authCmd = input.substring("auth ticket ".length());
			switch(authCmd) {
				case "get":
					getAuthTicket();
					break;
				case "cancel":
					cancelAuthTicket();
					break;
				case "send":
					broadcastAuthTicket();
					break;
				case "end":
					endAuthSession();
					break;
			}
		}

		friends.processInput(input);
	}

	private void registerRemoteSteamID(final SteamID steamIDUser) {
		if (!remoteUserIDs.containsKey(steamIDUser.getAccountID())) {
			remoteUserIDs.put(steamIDUser.getAccountID(), steamIDUser);
		}
	}

	private void unregisterRemoteSteamID(final SteamID steamIDUser) {
		remoteUserIDs.remove(steamIDUser.getAccountID());
	}

	private void getAuthTicket() throws SteamException {
		cancelAuthTicket();
		userAuthTicketData.clear();
		final int[] sizeRequired = new int[1];
		userAuthTicket = user.getAuthSessionTicket(userAuthTicketData, sizeRequired);
		if (userAuthTicket.isValid()) {
			final int numBytes = userAuthTicketData.limit();
			System.out.println("Auth session ticket length: " + numBytes);
			System.out.println("Auth ticket created: " + userAuthTicketData.toString() +
					" [hash: " + userAuthTicketData.hashCode() + "]");
		} else {
			if (sizeRequired[0] < userAuthTicketData.capacity()) {
				System.out.println("Error: failed creating auth ticket");
			} else {
				System.out.println("Error: buffer too small for auth ticket, need " + sizeRequired[0] + " bytes");
			}
		}
	}

	private void cancelAuthTicket() {
		if (userAuthTicket != null && userAuthTicket.isValid()) {
			System.out.println("Auth ticket cancelled");
			user.cancelAuthTicket(userAuthTicket);
			userAuthTicket = null;
		}
	}

	private void beginAuthSession(final SteamID steamIDSender) throws SteamException {
		endAuthSession();
		System.out.println("Starting auth session with user: " + steamIDSender.getAccountID());
		remoteAuthUser = steamIDSender;
		user.beginAuthSession(remoteAuthTicketData, remoteAuthUser);
	}

	private void endAuthSession() {
		if (remoteAuthUser != null) {
			System.out.println("End auth session with user: " + remoteAuthUser.getAccountID());
			user.endAuthSession(remoteAuthUser);
			remoteAuthUser = null;
		}
	}

	private void broadcastAuthTicket() throws SteamException {
		if (userAuthTicket == null || !userAuthTicket.isValid()) {
			System.out.println("Error: won't broadcast nil auth ticket");
			return;
		}

		for (final Map.Entry<Integer, SteamID> remoteUser : remoteUserIDs.entrySet()) {

			System.out.println("Send auth to remote user: " + remoteUser.getKey() +
					"[hash: " + userAuthTicketData.hashCode() + "]");

			packetSendBuffer.clear(); // pos=0, limit=cap

			packetSendBuffer.put(AUTH); // magic bytes
			packetSendBuffer.put(userAuthTicketData);

			userAuthTicketData.flip(); // limit=pos, pos=0
			packetSendBuffer.flip(); // limit=pos, pos=0

			networking.sendP2PPacket(remoteUser.getValue(), packetSendBuffer,
                                     SteamNetworking.P2PSend.Reliable, defaultChannel);
		}
	}

	private static int checkMagicBytes(final ByteBuffer buffer, final byte[] magicBytes) {
		for (int b = 0; b < magicBytes.length; b++) {
			if (buffer.get(b) != magicBytes[b]) {
				return 0;
			}
		}
		return magicBytes.length;
	}

	public static void main(final String[] arguments) {
		new SteamNetworkingTest().clientMain(arguments);
	}

}
