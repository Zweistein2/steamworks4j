package com.codedisaster.steamworks.test;

import com.codedisaster.steamworks.*;
import com.codedisaster.steamworks.matchmaking.SteamMatchmaking;
import com.codedisaster.steamworks.matchmaking.SteamMatchmakingCallback;
import com.codedisaster.steamworks.matchmaking.SteamMatchmakingKeyValuePair;
import com.codedisaster.steamworks.test.mixin.FriendsMixin;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static com.codedisaster.steamworks.SteamNativeHandle.getNativeHandle;

public class SteamMatchmakingTest extends SteamTestApp {

	private FriendsMixin friends;
	private SteamMatchmaking matchmaking;
	private final Map<Long, SteamID> lobbies = new HashMap<>();

	private static final String LobbyDataKey = "[test-key]";
	private static final String LobbyDataValue = "[test-value]";

	private final SteamMatchmakingCallback matchmakingCallback = new SteamMatchmakingCallback() {

		private final ByteBuffer chatMessage = ByteBuffer.allocateDirect(4096);
		private final SteamMatchmaking.ChatEntry chatEntry = new SteamMatchmaking.ChatEntry();
		private final Charset messageCharset = StandardCharsets.UTF_8;

		@Override
		public void onFavoritesListChanged(final int ip, final int queryPort, final int connPort, final int appID, final int flags, final boolean add, final int accountID) {
			System.out.println("[onFavoritesListChanged]");
		}

		@Override
		public void onLobbyInvite(final SteamID steamIDUser, final SteamID steamIDLobby, final long gameID) {
			System.out.println("Lobby invite received for " + steamIDLobby);
			System.out.println("  - from user: " + steamIDUser.getAccountID());
			System.out.println("  - for game: " + gameID);

			System.out.println("  - auto-joining lobby ...");

			lobbies.put(getNativeHandle(steamIDLobby), steamIDLobby);
			matchmaking.joinLobby(steamIDLobby);
		}

		@Override
		public void onLobbyEnter(final SteamID steamIDLobby, final int chatPermissions, final boolean blocked, final SteamMatchmaking.ChatRoomEnterResponse response) {
			System.out.println("Lobby entered: " + steamIDLobby);
			System.out.println("  - response: " + response);

			final int numMembers = matchmaking.getNumLobbyMembers(steamIDLobby);
			System.out.println("  - " + numMembers + " members in lobby");
			for (int i = 0; i < numMembers; i++) {
				final SteamID member = matchmaking.getLobbyMemberByIndex(steamIDLobby, i);
				System.out.println("    - " + i + ": accountID=" + member.getAccountID());
			}
		}

		@Override
		public void onLobbyDataUpdate(final SteamID steamIDLobby, final SteamID steamIDMember, final boolean success) {
			System.out.println("Lobby data update for " + steamIDLobby);
			System.out.println("  - member: " + steamIDMember.getAccountID());
			System.out.println("  - success: " + success);
		}

		@Override
		public void onLobbyChatUpdate(final SteamID steamIDLobby, final SteamID steamIDUserChanged, final SteamID steamIDMakingChange, final SteamMatchmaking.ChatMemberStateChange stateChange) {
			System.out.println("Lobby chat update for " + steamIDLobby);
			System.out.println("  - user changed: " + steamIDUserChanged.getAccountID());
			System.out.println("  - made by user: " + steamIDMakingChange.getAccountID());
			System.out.println("  - state changed: " + stateChange.name());
		}

		@Override
		public void onLobbyChatMessage(final SteamID steamIDLobby, final SteamID steamIDUser, final SteamMatchmaking.ChatEntryType entryType, final int chatID) {
			System.out.println("Lobby chat message for " + steamIDLobby);
			System.out.println("  - from user: " + steamIDUser.getAccountID());
			System.out.println("  - chat entry type: " + entryType);
			System.out.println("  - chat id: #" + chatID);

			try {

				final int size = matchmaking.getLobbyChatEntry(steamIDLobby, chatID, chatEntry, chatMessage);
				System.out.println("Lobby chat message #" + chatID + " has " + size + " bytes");

				final byte[] bytes = new byte[size];
				chatMessage.get(bytes);

				final String message = new String(bytes, messageCharset);

				System.out.println("  - from user: " + chatEntry.getSteamIDUser().getAccountID());
				System.out.println("  - chat entry type: " + chatEntry.getChatEntryType());
				System.out.println("  - content: \"" + message + "\"");

			} catch (final SteamException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void onLobbyGameCreated(final SteamID steamIDLobby, final SteamID steamIDGameServer, final int ip, final short port) {
			System.out.println("[onLobbyGameCreated]");
		}

		@Override
		public void onLobbyMatchList(final int lobbiesMatching) {
			System.out.println("Found " + lobbiesMatching + " matching lobbies.");

			lobbies.clear();
			for (int i = 0; i < lobbiesMatching; i++) {
				final SteamID lobby = matchmaking.getLobbyByIndex(i);
				lobbies.put(getNativeHandle(lobby), lobby);
			}

			listLobbies();
		}

		@Override
		public void onLobbyKicked(final SteamID steamIDLobby, final SteamID steamIDAdmin, final boolean kickedDueToDisconnect) {
			System.out.println("Kicked from lobby: " + steamIDLobby);
			System.out.println("  - by user (admin): " + steamIDAdmin.getAccountID());
			System.out.println("  - kicked due to disconnect: " + (kickedDueToDisconnect ? "yes" : "no"));
		}

		@Override
		public void onLobbyCreated(final SteamResult result, final SteamID steamIDLobby) {
			System.out.println("Lobby created: " + steamIDLobby);
			System.out.println("  - result: " + result.name());
			if (result == SteamResult.OK) {
				lobbies.put(getNativeHandle(steamIDLobby), steamIDLobby);
				matchmaking.setLobbyData(steamIDLobby, LobbyDataKey, LobbyDataValue);
			}
		}

		@Override
		public void onFavoritesListAccountsUpdated(final SteamResult result) {
			System.out.println("[onFavoritesListAccountsUpdated]");
		}
	};

	private void listLobbies() {
		for (final Map.Entry<Long, SteamID> lobby : lobbies.entrySet()) {
			System.out.print("  " + Long.toHexString(lobby.getKey()) + ": ");
			if (lobby.getValue().isValid()) {
				final int members = matchmaking.getNumLobbyMembers(lobby.getValue());
				System.out.println(members + " members");
			} else {
				System.err.println("invalid SteamID!");
			}
		}
	}

	@Override
	protected void registerInterfaces() {
		friends = new FriendsMixin();
		matchmaking = new SteamMatchmaking(matchmakingCallback);
	}

	@Override
	protected void unregisterInterfaces() {
		friends.dispose();
		matchmaking.dispose();
	}

	@Override
	protected void processUpdate() throws SteamException {

	}

	@Override
	protected void processInput(final String input) throws SteamException {

		if (input.equals("lobby list")) {
			listLobbies();
		} else if (input.startsWith("lobby request ")) {
			final String[] parameters = input.substring("lobby request ".length()).split(" ");
			final int limit = Integer.parseInt(parameters[0]);
			System.out.println("  - requesting up to " + limit + " lobbies");
			matchmaking.addRequestLobbyListResultCountFilter(limit);
			matchmaking.addRequestLobbyListStringFilter(LobbyDataKey, LobbyDataValue, SteamMatchmaking.LobbyComparison.Equal);
			matchmaking.requestLobbyList();
		} else if (input.startsWith("lobby create ")) {
			final int maxMembers = Integer.parseInt(input.substring("lobby create ".length()));
			System.out.println("  creating lobby for " + maxMembers + " players.");
			matchmaking.createLobby(SteamMatchmaking.LobbyType.Public, maxMembers);
		} else if (input.startsWith("lobby join ")) {
			final long id = Long.parseLong(input.substring("lobby join ".length()), 16);
			if (lobbies.containsKey(id)) {
				System.out.println("  joining lobby " + Long.toHexString(id));
				matchmaking.joinLobby(lobbies.get(id));
			} else {
				System.err.println("No lobby found: " + Long.toHexString(id));
			}
		} else if (input.startsWith("lobby leave ")) {
			final long id = Long.parseLong(input.substring("lobby leave ".length()), 16);
			if (lobbies.containsKey(id)) {
				System.out.println("  leaving lobby " + Long.toHexString(id));
				matchmaking.leaveLobby(lobbies.get(id));
				lobbies.remove(id);
			} else {
				System.err.println("No lobby found: " + Long.toHexString(id));
			}
		} else if (input.startsWith("lobby invite ")) {
			final String[] ids = input.substring("lobby invite ".length()).split(" ");
			if (ids.length == 2) {
				final long lobbyID = Long.parseLong(ids[0], 16);
				final int playerAccountID = Integer.parseInt(ids[1]);
				if (lobbies.containsKey(lobbyID)) {
					System.out.println("  inviting player " + playerAccountID + " to lobby " + Long.toHexString(lobbyID));
					if (friends.isFriendAccountID(playerAccountID)) {
						matchmaking.inviteUserToLobby(lobbies.get(lobbyID), friends.getFriendSteamID(playerAccountID));
					} else {
						System.err.println("No player (friend) found: " + playerAccountID);
					}
				} else {
					System.err.println("No lobby found: " + Long.toHexString(lobbyID));
				}
			} else {
				System.err.println("Expecting: 'lobby invite <lobbyID> <userID>'");
			}
		} else if (input.startsWith("lobby data ")) {
			final long id = Long.parseLong(input.substring("lobby data ".length()), 16);
			if (lobbies.containsKey(id)) {
				final SteamID steamIDLobby = lobbies.get(id);
				final int count = matchmaking.getLobbyDataCount(steamIDLobby);
				System.out.println("  " + count + " lobby data for " + Long.toHexString(id));
				final SteamMatchmakingKeyValuePair pair = new SteamMatchmakingKeyValuePair();
				for (int i = 0; i < count; i++) {
					if (matchmaking.getLobbyDataByIndex(steamIDLobby, i, pair)) {
						System.out.println("  - " + pair.getKey() + " : " + pair.getValue());
					} else {
						System.err.println("Error retrieving lobby data #" + i);
					}
				}
			} else {
				System.err.println("No lobby found: " + Long.toHexString(id));
			}
		} else if (input.startsWith("lobby chat ")) {
			final String[] content = input.substring("lobby chat ".length()).split(" ");
			if (content.length == 2) {
				final long lobbyID = Long.parseLong(content[0], 16);
				final String message = content[1];
				if (lobbies.containsKey(lobbyID)) {
					System.out.println("  sending message \"" + message + "\"");
					matchmaking.sendLobbyChatMsg(lobbies.get(lobbyID), message);
				} else {
					System.err.println("No lobby found: " + Long.toHexString(lobbyID));
				}
			} else {
				System.err.println("Expecting: 'lobby chat <lobbyID> <message>'");
			}
		} else if (input.startsWith("get lobby member data ")) {
			final String[] content = input.substring("get lobby member data ".length()).split(" ");
			if (content.length == 3) {
				final SteamID lobbyId = SteamID.createFromNativeHandle(Long.parseLong(content[0], 16));
				final SteamID userId = SteamID.createFromNativeHandle(Long.parseLong(content[1]));
				final String key = content[2];
				final String value = matchmaking.getLobbyMemberData(lobbyId, userId, key);
				System.out.printf("Member data for userId:%s in lobbyId:%s for key:%s has value:%s%n",
								  userId.toString(), lobbyId.toString(), key, value);
			} else {
				System.out.println("Expecting: 'get lobby member data <lobbyId> <userId> <key>'");
			}
		} else if (input.startsWith("set lobby member data ")) {
			final String[] content = input.substring("set lobby member data ".length()).split(" ");
			if (content.length == 3) {
				final SteamID lobbyId = SteamID.createFromNativeHandle(Long.parseLong(content[0], 16));
				final String key = content[1];
				final String value = content[2];
				matchmaking.setLobbyMemberData(lobbyId, key, value);
			} else {
				System.out.println("Expecting: 'set lobby member data <lobbyId> <key> <value>'");
			}
		}

		friends.processInput(input);
	}

	public static void main(final String[] arguments) {
		new SteamMatchmakingTest().clientMain(arguments);
	}

}
