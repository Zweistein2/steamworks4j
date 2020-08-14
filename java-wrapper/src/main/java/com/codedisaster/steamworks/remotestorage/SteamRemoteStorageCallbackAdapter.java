package com.codedisaster.steamworks.remotestorage;

import com.codedisaster.steamworks.*;
import com.codedisaster.steamworks.ugc.SteamUGCHandle;

@SuppressWarnings("unused")
class SteamRemoteStorageCallbackAdapter extends SteamCallbackAdapter<SteamRemoteStorageCallback> {

	SteamRemoteStorageCallbackAdapter(final SteamRemoteStorageCallback callback) {
		super(callback);
	}

	void onFileShareResult(final long fileHandle, final String fileName, final int result) {
		callback.onFileShareResult(new SteamUGCHandle(fileHandle),
                                   fileName, SteamResult.byValue(result));
	}

	void onDownloadUGCResult(final long fileHandle, final int result) {
		callback.onDownloadUGCResult(new SteamUGCHandle(fileHandle), SteamResult.byValue(result));
	}

	void onPublishFileResult(final long publishedFileID, final boolean needsToAcceptWLA, final int result) {
		callback.onPublishFileResult(new SteamPublishedFileID(publishedFileID),
				needsToAcceptWLA, SteamResult.byValue(result));
	}

	void onUpdatePublishedFileResult(final long publishedFileID, final boolean needsToAcceptWLA, final int result) {
		callback.onUpdatePublishedFileResult(new SteamPublishedFileID(publishedFileID),
				needsToAcceptWLA, SteamResult.byValue(result));
	}

	void onPublishedFileSubscribed(final long publishedFileID, final int appID) {
		callback.onPublishedFileSubscribed(new SteamPublishedFileID(publishedFileID), appID);
	}

	void onPublishedFileUnsubscribed(final long publishedFileID, final int appID) {
		callback.onPublishedFileUnsubscribed(new SteamPublishedFileID(publishedFileID), appID);
	}

	void onPublishedFileDeleted(final long publishedFileID, final int appID) {
		callback.onPublishedFileDeleted(new SteamPublishedFileID(publishedFileID), appID);
	}

	void onFileWriteAsyncComplete(final int result) {
		callback.onFileWriteAsyncComplete(SteamResult.byValue(result));
	}

	void onFileReadAsyncComplete(final long fileReadAsync, final int result, final int offset, final int read) {
		callback.onFileReadAsyncComplete(new SteamAPICall(fileReadAsync),
				SteamResult.byValue(result), offset, read);
	}

}
