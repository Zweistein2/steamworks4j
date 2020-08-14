package com.codedisaster.steamworks.networking;

import com.codedisaster.steamworks.SteamID;

public interface SteamNetworkingCallback {

	void onP2PSessionConnectFail(SteamID steamIDRemote, SteamNetworking.P2PSessionError sessionError);

	void onP2PSessionRequest(SteamID steamIDRemote);
}
