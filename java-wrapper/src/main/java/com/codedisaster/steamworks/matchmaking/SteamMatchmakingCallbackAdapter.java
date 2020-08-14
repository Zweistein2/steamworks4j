package com.codedisaster.steamworks.matchmaking;

import com.codedisaster.steamworks.SteamCallbackAdapter;
import com.codedisaster.steamworks.SteamID;
import com.codedisaster.steamworks.SteamResult;

@SuppressWarnings("unused")
class SteamMatchmakingCallbackAdapter extends SteamCallbackAdapter<SteamMatchmakingCallback> {

	private static final SteamMatchmaking.ChatMemberStateChange[] stateChangeValues =
			SteamMatchmaking.ChatMemberStateChange.values();

	SteamMatchmakingCallbackAdapter(final SteamMatchmakingCallback callback) {
		super(callback);
	}

	void onFavoritesListChanged(final int ip, final int queryPort, final int connPort, final int appID, final int flags, final boolean add, final int accountID) {
		callback.onFavoritesListChanged(ip, queryPort, connPort, appID, flags, add, accountID);
	}

	void onLobbyInvite(final long steamIDUser, final long steamIDLobby, final long gameID) {
		callback.onLobbyInvite(SteamID.createFromNativeHandle(steamIDUser), SteamID.createFromNativeHandle(steamIDLobby), gameID);
	}

	void onLobbyEnter(final long steamIDLobby, final int chatPermissions, final boolean blocked, final int response) {
		callback.onLobbyEnter(SteamID.createFromNativeHandle(steamIDLobby), chatPermissions, blocked,
				SteamMatchmaking.ChatRoomEnterResponse.byValue(response));
	}

	void onLobbyDataUpdate(final long steamIDLobby, final long steamIDMember, final boolean success) {
		callback.onLobbyDataUpdate(SteamID.createFromNativeHandle(steamIDLobby), SteamID.createFromNativeHandle(steamIDMember), success);
	}

	void onLobbyChatUpdate(final long steamIDLobby, final long steamIDUserChanged, final long steamIDMakingChange, final int stateChange) {
		final SteamID lobby = SteamID.createFromNativeHandle(steamIDLobby);
		final SteamID userChanged = SteamID.createFromNativeHandle(steamIDUserChanged);
		final SteamID makingChange = SteamID.createFromNativeHandle(steamIDMakingChange);
		for (final SteamMatchmaking.ChatMemberStateChange value : stateChangeValues) {
			if (SteamMatchmaking.ChatMemberStateChange.isSet(value, stateChange)) {
				callback.onLobbyChatUpdate(lobby, userChanged, makingChange, value);
			}
		}
	}

	void onLobbyChatMessage(final long steamIDLobby, final long steamIDUser, final int entryType, final int chatID) {
		callback.onLobbyChatMessage(SteamID.createFromNativeHandle(steamIDLobby), SteamID.createFromNativeHandle(steamIDUser),
				SteamMatchmaking.ChatEntryType.byValue(entryType), chatID);
	}

	void onLobbyGameCreated(final long steamIDLobby, final long steamIDGameServer, final int ip, final short port) {
		callback.onLobbyGameCreated(SteamID.createFromNativeHandle(steamIDLobby), SteamID.createFromNativeHandle(steamIDGameServer), ip, port);
	}

	void onLobbyMatchList(final int lobbiesMatching) {
		callback.onLobbyMatchList(lobbiesMatching);
	}

	void onLobbyKicked(final long steamIDLobby, final long steamIDAdmin, final boolean kickedDueToDisconnect) {
		callback.onLobbyKicked(SteamID.createFromNativeHandle(steamIDLobby), SteamID.createFromNativeHandle(steamIDAdmin), kickedDueToDisconnect);
	}

	void onLobbyCreated(final int result, final long steamIDLobby) {
		callback.onLobbyCreated(SteamResult.byValue(result), SteamID.createFromNativeHandle(steamIDLobby));
	}

	void onFavoritesListAccountsUpdated(final int result) {
		callback.onFavoritesListAccountsUpdated(SteamResult.byValue(result));
	}

}
