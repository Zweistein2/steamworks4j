package com.codedisaster.steamworks.ugc;

import com.codedisaster.steamworks.SteamCallbackAdapter;
import com.codedisaster.steamworks.SteamPublishedFileID;
import com.codedisaster.steamworks.SteamResult;

@SuppressWarnings("unused")
class SteamUGCCallbackAdapter extends SteamCallbackAdapter<SteamUGCCallback> {

	SteamUGCCallbackAdapter(final SteamUGCCallback callback) {
		super(callback);
	}

	void onUGCQueryCompleted(final long handle, final int numResultsReturned, final int totalMatchingResults,
                             final boolean isCachedData, final int result) {

		callback.onUGCQueryCompleted(new SteamUGCQuery(handle), numResultsReturned,
                                     totalMatchingResults, isCachedData, SteamResult.byValue(result));
	}

	void onSubscribeItem(final long publishedFileID, final int result) {
		callback.onSubscribeItem(new SteamPublishedFileID(publishedFileID), SteamResult.byValue(result));
	}
	
	void onUnsubscribeItem(final long publishedFileID, final int result) {
		callback.onUnsubscribeItem(new SteamPublishedFileID(publishedFileID), SteamResult.byValue(result));
	}
	
	void onRequestUGCDetails(final long publishedFileID, final int result, final String title, final String description,
                             final long fileHandle, final long previewFileHandle, final String fileName,
                             final boolean cachedData, final int votesUp, final int votesDown, final long ownerID,
                             final int timeCreated, final int timeUpdated) {

		final SteamUGCDetails details = new SteamUGCDetails();
		details.publishedFileID = publishedFileID;
		details.result = result;
		details.title = title;
		details.description = description;
		details.fileHandle = fileHandle;
		details.previewFileHandle = previewFileHandle;
		details.fileName = fileName;
		details.votesUp = votesUp;
		details.votesDown = votesDown;
		details.ownerID = ownerID;
		details.timeCreated = timeCreated;
		details.timeUpdated = timeUpdated;
		
		callback.onRequestUGCDetails(details, SteamResult.byValue(result));
	}

	void onCreateItem(final long publishedFileID, final boolean needsToAcceptWLA, final int result) {
		callback.onCreateItem(new SteamPublishedFileID(publishedFileID), needsToAcceptWLA, SteamResult.byValue(result));
	}

	void onSubmitItemUpdate(final long publishedFileID, final boolean needsToAcceptWLA, final int result) {
		callback.onSubmitItemUpdate(new SteamPublishedFileID(publishedFileID),
				needsToAcceptWLA, SteamResult.byValue(result));
	}

	void onDownloadItemResult(final int appID, final long publishedFileID, final int result) {
		callback.onDownloadItemResult(appID, new SteamPublishedFileID(publishedFileID), SteamResult.byValue(result));
	}

	void onUserFavoriteItemsListChanged(final long publishedFileID, final boolean wasAddRequest, final int result) {
		callback.onUserFavoriteItemsListChanged(new SteamPublishedFileID(publishedFileID),
				wasAddRequest, SteamResult.byValue(result));
	}

	void onSetUserItemVote(final long publishedFileID, final boolean voteUp, final int result) {
		callback.onSetUserItemVote(new SteamPublishedFileID(publishedFileID), voteUp, SteamResult.byValue(result));
	}

	void onGetUserItemVote(final long publishedFileID, final boolean votedUp, final boolean votedDown, final boolean voteSkipped, final int result) {
		callback.onGetUserItemVote(new SteamPublishedFileID(publishedFileID),
				votedUp, votedDown, voteSkipped, SteamResult.byValue(result));
	}

	void onStartPlaytimeTracking(final int result) {
		callback.onStartPlaytimeTracking(SteamResult.byValue(result));
	}

	void onStopPlaytimeTracking(final int result) {
		callback.onStopPlaytimeTracking(SteamResult.byValue(result));
	}

	void onStopPlaytimeTrackingForAllItems(final int result) {
		callback.onStopPlaytimeTrackingForAllItems(SteamResult.byValue(result));
	}
	
	void onDeleteItem(final long publishedFileID, final int result) {
		callback.onDeleteItem(new SteamPublishedFileID(publishedFileID), SteamResult.byValue(result));
	}
}
