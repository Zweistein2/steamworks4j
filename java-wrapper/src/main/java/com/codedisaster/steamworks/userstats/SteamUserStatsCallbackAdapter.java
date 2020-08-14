package com.codedisaster.steamworks.userstats;

import com.codedisaster.steamworks.*;

@SuppressWarnings("unused")
class SteamUserStatsCallbackAdapter extends SteamCallbackAdapter<SteamUserStatsCallback> {

	SteamUserStatsCallbackAdapter(final SteamUserStatsCallback callback) {
		super(callback);
	}

	void onUserStatsReceived(final long gameId, final long steamIDUser, final int result) {
		callback.onUserStatsReceived(gameId, SteamID.createFromNativeHandle(steamIDUser), SteamResult.byValue(result));
	}

	void onUserStatsStored(final long gameId, final int result) {
		callback.onUserStatsStored(gameId, SteamResult.byValue(result));
	}

	void onUserStatsUnloaded(final long steamIDUser) {
		callback.onUserStatsUnloaded(SteamID.createFromNativeHandle(steamIDUser));
	}

	void onUserAchievementStored(final long gameId, final boolean isGroupAchievement, final String achievementName,
                                 final int curProgress, final int maxProgress) {
		callback.onUserAchievementStored(gameId, isGroupAchievement, achievementName, curProgress, maxProgress);
	}

	void onLeaderboardFindResult(final long handle, final boolean found) {
		callback.onLeaderboardFindResult(new SteamLeaderboardHandle(handle), found);
	}

	void onLeaderboardScoresDownloaded(final long handle, final long entries, final int numEntries) {
		callback.onLeaderboardScoresDownloaded(new SteamLeaderboardHandle(handle),
                                               new SteamLeaderboardEntriesHandle(entries), numEntries);
	}

	void onLeaderboardScoreUploaded(final boolean success, final long handle, final int score, final boolean changed,
                                    final int globalRankNew, final int globalRankPrevious) {
		callback.onLeaderboardScoreUploaded(success, new SteamLeaderboardHandle(handle), score, changed,
				globalRankNew, globalRankPrevious);
	}

	void onGlobalStatsReceived(final long gameId, final int result) {
		callback.onGlobalStatsReceived(gameId, SteamResult.byValue(result));
	}

}
