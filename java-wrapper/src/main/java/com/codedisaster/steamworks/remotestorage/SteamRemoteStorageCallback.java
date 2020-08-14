package com.codedisaster.steamworks.remotestorage;

import com.codedisaster.steamworks.SteamAPICall;
import com.codedisaster.steamworks.SteamPublishedFileID;
import com.codedisaster.steamworks.SteamResult;
import com.codedisaster.steamworks.ugc.SteamUGCHandle;

public interface SteamRemoteStorageCallback {

	void onFileShareResult(SteamUGCHandle fileHandle, String fileName, SteamResult result);

	void onDownloadUGCResult(SteamUGCHandle fileHandle, SteamResult result);

	void onPublishFileResult(SteamPublishedFileID publishedFileID, boolean needsToAcceptWLA, SteamResult result);

	void onUpdatePublishedFileResult(SteamPublishedFileID publishedFileID, boolean needsToAcceptWLA, SteamResult result);

	void onPublishedFileSubscribed(SteamPublishedFileID publishedFileID, int appID);

	void onPublishedFileUnsubscribed(SteamPublishedFileID publishedFileID, int appID);

	void onPublishedFileDeleted(SteamPublishedFileID publishedFileID, int appID);

	void onFileWriteAsyncComplete(SteamResult result);

	void onFileReadAsyncComplete(SteamAPICall fileReadAsync, SteamResult result, int offset, int read);

}
