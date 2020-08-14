package com.codedisaster.steamworks.gameserverstats;

import com.codedisaster.steamworks.SteamCallbackAdapter;
import com.codedisaster.steamworks.SteamID;
import com.codedisaster.steamworks.SteamResult;

@SuppressWarnings("unused")
class SteamGameServerStatsCallbackAdapter extends SteamCallbackAdapter<SteamGameServerStatsCallback> {

	SteamGameServerStatsCallbackAdapter(final SteamGameServerStatsCallback callback) {
		super(callback);
	}

	void onStatsReceived(final int result, final long steamIDUser) {
		callback.onStatsReceived(SteamResult.byValue(result), SteamID.createFromNativeHandle(steamIDUser));
	}

	void onStatsStored(final int result, final long steamIDUser) {
		callback.onStatsStored(SteamResult.byValue(result), SteamID.createFromNativeHandle(steamIDUser));
	}
	
	void onStatsUnloaded(final long steamIDUser) {
		callback.onStatsUnloaded(SteamID.createFromNativeHandle(steamIDUser));
	}
}
