package com.codedisaster.steamworks.ugc;

import com.codedisaster.steamworks.SteamPublishedFileID;
import com.codedisaster.steamworks.SteamResult;

public interface SteamUGCCallback {

	void onUGCQueryCompleted(SteamUGCQuery query, int numResultsReturned, int totalMatchingResults,
							 boolean isCachedData, SteamResult result);

	void onSubscribeItem(SteamPublishedFileID publishedFileID, SteamResult result);

	void onUnsubscribeItem(SteamPublishedFileID publishedFileID, SteamResult result);
	
	void onRequestUGCDetails(SteamUGCDetails details, SteamResult result);

	void onCreateItem(SteamPublishedFileID publishedFileID, boolean needsToAcceptWLA, SteamResult result);

	void onSubmitItemUpdate(SteamPublishedFileID publishedFileID,
							boolean needsToAcceptWLA, SteamResult result);

	void onDownloadItemResult(int appID, SteamPublishedFileID publishedFileID, SteamResult result);

	void onUserFavoriteItemsListChanged(SteamPublishedFileID publishedFileID,
										boolean wasAddRequest, SteamResult result);

	void onSetUserItemVote(SteamPublishedFileID publishedFileID, boolean voteUp, SteamResult result);

	void onGetUserItemVote(SteamPublishedFileID publishedFileID, boolean votedUp,
						   boolean votedDown, boolean voteSkipped, SteamResult result);

	void onStartPlaytimeTracking(SteamResult result);

	void onStopPlaytimeTracking(SteamResult result);

	void onStopPlaytimeTrackingForAllItems(SteamResult result);

	void onDeleteItem(SteamPublishedFileID publishedFileID, SteamResult result);
}
