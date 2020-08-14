package com.codedisaster.steamworks.userstats;

import com.codedisaster.steamworks.SteamID;

@SuppressWarnings("WeakerAccess")
public class SteamLeaderboardEntry {

	/* Contains only a subset of LeaderboardEntry_t. */

	long steamIDUser;
	int globalRank;
	int score;
	int details;

	public SteamID getSteamIDUser() {
		return SteamID.createFromNativeHandle(steamIDUser);
	}

	public int getGlobalRank() {
		return globalRank;
	}

	public int getScore() {
		return score;
	}

	public int getNumDetails() {
		return details;
	}

}
