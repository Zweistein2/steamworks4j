package com.codedisaster.steamworks.test.mixin;

import com.codedisaster.steamworks.*;
import com.codedisaster.steamworks.friends.SteamFriends;
import com.codedisaster.steamworks.friends.SteamFriendsCallback;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.codedisaster.steamworks.SteamNativeHandle.getNativeHandle;

public class FriendsMixin {

	private final SteamFriends friends;
	private final Map<Integer, SteamID> friendUserIDs = new ConcurrentHashMap<>();

	private final SteamFriendsCallback friendsCallback = new SteamFriendsCallback() {
		@Override
		public void onSetPersonaNameResponse(final boolean success, final boolean localSuccess, final SteamResult result) {
			System.out.println("Set persona name response: " +
					"success=" + success +
					", localSuccess=" + localSuccess +
					", result=" + result.name());
		}

		@Override
		public void onPersonaStateChange(final SteamID steamID, final SteamFriends.PersonaChange change) {
			switch (change) {
				case Name:
					System.out.println("Persona name received: " +
							"accountID=" + steamID.getAccountID() +
							", name='" + friends.getFriendPersonaName(steamID) + "'");
					break;
				default:
					System.out.println("Persona state changed (unhandled): " +
							"accountID=" + steamID.getAccountID() +
							", change=" + change.name());
					break;
			}
		}

		@Override
		public void onGameOverlayActivated(final boolean active) {

		}

		@Override
		public void onGameLobbyJoinRequested(final SteamID steamIDLobby, final SteamID steamIDFriend) {
			System.out.println("Game lobby join requested");
			System.out.println("  - lobby: " + Long.toHexString(getNativeHandle(steamIDLobby)));
			System.out.println("  - by friend accountID: " + steamIDFriend.getAccountID());
		}

		@Override
		public void onAvatarImageLoaded(final SteamID steamID, final int image, final int width, final int height) {

		}

		@Override
		public void onFriendRichPresenceUpdate(final SteamID steamIDFriend, final int appID) {

		}

		@Override
		public void onGameRichPresenceJoinRequested(final SteamID steamIDFriend, final String connect) {

		}

		@Override
		public void onGameServerChangeRequested(final String server, final String password)
		{
			
		}
	};

	public FriendsMixin() {
		friends = new SteamFriends(friendsCallback);
	}

	public void dispose() {
		friends.dispose();
	}

	public void processInput(final String input) {

		if (input.equals("persona get")) {
			System.out.println("persona name: " + friends.getPersonaName());
		} else if (input.startsWith("persona set ")) {
			final String personaName = input.substring("persona set ".length());
			friends.setPersonaName(personaName);
		} else if (input.equals("friends list")) {

			final int friendsCount = friends.getFriendCount(SteamFriends.FriendFlags.Immediate);
			System.out.println(friendsCount + " friends");

			for (int i = 0; i < friendsCount; i++) {

				final SteamID steamIDUser = friends.getFriendByIndex(i, SteamFriends.FriendFlags.Immediate);
				friendUserIDs.put(steamIDUser.getAccountID(), steamIDUser);

				final String personaName = friends.getFriendPersonaName(steamIDUser);
				final SteamFriends.PersonaState personaState = friends.getFriendPersonaState(steamIDUser);

				System.out.println("  - " + steamIDUser.getAccountID() + " (" +
						personaName + ", " + personaState.name() + ")");
			}
		}

	}

	public boolean isFriendAccountID(final int accountID) {
		return friendUserIDs.containsKey(accountID);
	}

	public SteamID getFriendSteamID(final int accountID) {
		return friendUserIDs.get(accountID);
	}

}
