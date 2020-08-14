package com.codedisaster.steamworks.friends;

import com.codedisaster.steamworks.SteamCallbackAdapter;
import com.codedisaster.steamworks.SteamID;
import com.codedisaster.steamworks.SteamResult;

@SuppressWarnings("unused")
class SteamFriendsCallbackAdapter extends SteamCallbackAdapter<SteamFriendsCallback> {

	private static final SteamFriends.PersonaChange[] personaChangeValues = SteamFriends.PersonaChange.values();

	SteamFriendsCallbackAdapter(final SteamFriendsCallback callback) {
		super(callback);
	}

	void onSetPersonaNameResponse(final boolean success, final boolean localSuccess, final int result) {
		callback.onSetPersonaNameResponse(success, localSuccess, SteamResult.byValue(result));
	}

	void onPersonaStateChange(final long steamID, final int changeFlags) {
		final SteamID id = SteamID.createFromNativeHandle(steamID);
		for (final SteamFriends.PersonaChange value : personaChangeValues) {
			if (SteamFriends.PersonaChange.isSet(value, changeFlags)) {
				callback.onPersonaStateChange(id, value);
			}
		}
	}

	void onGameOverlayActivated(final boolean active) {
		callback.onGameOverlayActivated(active);
	}

	void onGameLobbyJoinRequested(final long steamIDLobby, final long steamIDFriend) {
		callback.onGameLobbyJoinRequested(SteamID.createFromNativeHandle(steamIDLobby), SteamID.createFromNativeHandle(steamIDFriend));
	}

	void onAvatarImageLoaded(final long steamID, final int image, final int width, final int height) {
		callback.onAvatarImageLoaded(SteamID.createFromNativeHandle(steamID), image, width, height);
	}

	void onFriendRichPresenceUpdate(final long steamIDFriend, final int appID) {
		callback.onFriendRichPresenceUpdate(SteamID.createFromNativeHandle(steamIDFriend), appID);
	}

	void onGameRichPresenceJoinRequested(final long steamIDFriend, final String connect) {
		callback.onGameRichPresenceJoinRequested(SteamID.createFromNativeHandle(steamIDFriend), connect);
	}

	void onGameServerChangeRequested(final String server, final String password) {
		callback.onGameServerChangeRequested(server, password);
	}
}
