package com.codedisaster.steamworks.gameserverstats;

import com.codedisaster.steamworks.SteamAPICall;
import com.codedisaster.steamworks.SteamID;
import com.codedisaster.steamworks.SteamInterface;
import com.codedisaster.steamworks.gameserver.SteamGameServerAPINative;

public class SteamGameServerStats extends SteamInterface {

	public SteamGameServerStats(final SteamGameServerStatsCallback callback) {
		super(SteamGameServerAPINative.getSteamGameServerStatsPointer(),
              SteamGameServerStatsNative.createCallback(new SteamGameServerStatsCallbackAdapter(callback)));
	}

	public SteamAPICall requestUserStats(final SteamID steamIDUser) {
		return new SteamAPICall(SteamGameServerStatsNative.requestUserStats(pointer, steamIDUser.handle));
	}

	public int getUserStatI(final SteamID steamIDUser, final String name, final int defaultValue) {
		final int[] values = new int[1];
		if (SteamGameServerStatsNative.getUserStat(pointer, steamIDUser.handle, name, values)) {
			return values[0];
		}
		return defaultValue;
	}

	public float getUserStatF(final SteamID steamIDUser, final String name, final float defaultValue) {
		final float[] values = new float[1];
		if (SteamGameServerStatsNative.getUserStat(pointer, steamIDUser.handle, name, values)) {
			return values[0];
		}
		return defaultValue;
	}

	public boolean getUserAchievement(final SteamID steamIDUser, final String name, final boolean defaultValue) {
		final boolean[] achieved = new boolean[1];
		if (SteamGameServerStatsNative.getUserAchievement(pointer, steamIDUser.handle, name, achieved)) {
			return achieved[0];
		}
		return defaultValue;
	}

	public boolean setUserStatI(final SteamID steamIDUser, final String name, final int value) {
		return SteamGameServerStatsNative.setUserStat(pointer, steamIDUser.handle, name, value);
	}

	public boolean setUserStatF(final SteamID steamIDUser, final String name, final float value) {
		return SteamGameServerStatsNative.setUserStat(pointer, steamIDUser.handle, name, value);
	}

	public boolean updateUserAvgRateStat(final SteamID steamIDUser, final String name, final float countThisSession, final double sessionLength) {
		return SteamGameServerStatsNative.updateUserAvgRateStat(
				pointer, steamIDUser.handle, name, countThisSession, sessionLength);
	}

	public boolean setUserAchievement(final SteamID steamIDUser, final String name) {
		return SteamGameServerStatsNative.setUserAchievement(pointer, steamIDUser.handle, name);
	}

	public boolean clearUserAchievement(final SteamID steamIDUser, final String name) {
		return SteamGameServerStatsNative.clearUserAchievement(pointer, steamIDUser.handle, name);
	}

	public SteamAPICall storeUserStats(final SteamID steamIDUser) {
		return new SteamAPICall(SteamGameServerStatsNative.storeUserStats(pointer, steamIDUser.handle));
	}

}
