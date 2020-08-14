package com.codedisaster.steamworks.networking;

import com.codedisaster.steamworks.SteamCallbackAdapter;
import com.codedisaster.steamworks.SteamID;

@SuppressWarnings("unused")
public class SteamNetworkingCallbackAdapter extends SteamCallbackAdapter<SteamNetworkingCallback> {

	public SteamNetworkingCallbackAdapter(final SteamNetworkingCallback callback) {
		super(callback);
	}

	void onP2PSessionConnectFail(final long steamIDRemote, final int sessionError) {
		final SteamID id = SteamID.createFromNativeHandle(steamIDRemote);
		callback.onP2PSessionConnectFail(id, SteamNetworking.P2PSessionError.byOrdinal(sessionError));
	}

	void onP2PSessionRequest(final long steamIDRemote) {
		final SteamID id = SteamID.createFromNativeHandle(steamIDRemote);
		callback.onP2PSessionRequest(id);
	}
}
