package com.codedisaster.steamworks.friends;

import com.codedisaster.steamworks.SteamID;
import com.codedisaster.steamworks.SteamResult;

public interface SteamFriendsCallback {

	void onSetPersonaNameResponse(boolean success, boolean localSuccess, SteamResult result);

	void onPersonaStateChange(SteamID steamID, SteamFriends.PersonaChange change);

	void onGameOverlayActivated(boolean active);

	void onGameLobbyJoinRequested(SteamID steamIDLobby, SteamID steamIDFriend);

	void onAvatarImageLoaded(SteamID steamID, int image, int width, int height);

	void onFriendRichPresenceUpdate(SteamID steamIDFriend, int appID);

	void onGameRichPresenceJoinRequested(SteamID steamIDFriend, String connect);

	void onGameServerChangeRequested(String server, String password);
}
