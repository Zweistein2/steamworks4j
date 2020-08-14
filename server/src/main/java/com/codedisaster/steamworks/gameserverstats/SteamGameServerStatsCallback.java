package com.codedisaster.steamworks.gameserverstats;

import com.codedisaster.steamworks.SteamID;
import com.codedisaster.steamworks.SteamResult;

public interface SteamGameServerStatsCallback {

	void onStatsReceived(SteamResult result, SteamID steamIDUser);

	void onStatsStored(SteamResult result, SteamID steamIDUser);

	void onStatsUnloaded(SteamID steamIDUser);
}
