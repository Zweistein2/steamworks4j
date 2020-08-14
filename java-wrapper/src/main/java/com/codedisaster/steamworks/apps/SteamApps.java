package com.codedisaster.steamworks.apps;

import com.codedisaster.steamworks.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SteamApps extends SteamInterface {

	public SteamApps() {
		super(SteamAPI.getSteamAppsPointer());
	}

	public boolean getDLCDataByIndex(final int index, final List<Integer> appIDs, final List<Boolean> dlcAvailables, final List<String> values, final int valueBufferSize) {
		final SteamStringValue value = new SteamStringValue();
		final Integer[] appIDsArray = new Integer[1];
		final Boolean[] dlcAvailablesArray = new Boolean[1];

		final boolean success = getDLCDataByIndex(pointer, index, appIDsArray, dlcAvailablesArray, value, valueBufferSize);

		if(success) {
			values.add(value.getValue());
			dlcAvailables.addAll(Arrays.stream(dlcAvailablesArray).collect(Collectors.toList()));
			appIDs.addAll(Arrays.stream(appIDsArray).collect(Collectors.toList()));
		}

		return success;
	}

	public boolean isAppInstalled(final int appID) {
		return isAppInstalled(pointer, appID);
	}

	public boolean isCybercafe() {
		return isCybercafe(pointer);
	}

	public boolean isDlcInstalled(final int appID) {
		return isDlcInstalled(pointer, appID);
	}

	public boolean isLowViolence() {
		return isLowViolence(pointer);
	}

	public boolean isSubscribed() {
		return isSubscribed(pointer);
	}

	public boolean isSubscribedApp(final int appID) {
		return isSubscribedApp(pointer, appID);
	}

	public boolean isSubscribedFromFamilySharing() {
		return isSubscribedFromFamilySharing(pointer);
	}

	public boolean isSubscribedFromFreeWeekend() {
		return isSubscribedFromFreeWeekend(pointer);
	}

	public boolean isVACBanned() {
		return isVACBanned(pointer);
	}

	public int getAppBuildId() {
		return getAppBuildId(pointer);
	}

	public int getAppInstallDir(final int appID, final List<String> folders, final int folderBufferSize) {
		final SteamStringValue value = new SteamStringValue();

		final int copiedBytes = getAppInstallDir(pointer, appID, value, folderBufferSize);

		if(copiedBytes > 0) {
			folders.add(value.getValue());
		}

		return copiedBytes;
	}

	public SteamID getAppOwner() {
		return SteamID.createFromNativeHandle(getAppOwner(pointer));
	}

	public String getAvailableGameLanguages() {
		return getAvailableGameLanguages(pointer);
	}

	public boolean getCurrentBetaName(final List<String> betaNames, final int betaNameBufferSize) {
		final SteamStringValue value = new SteamStringValue();

		final boolean success = getCurrentBetaName(pointer, value, betaNameBufferSize);

		if(success) {
			betaNames.add(value.getValue());
		}

		return success;
	}
	
	public String getCurrentGameLanguage() {
		return getCurrentGameLanguage(pointer);
	}

	public int getDLCCount() {
		return getDLCCount(pointer);
	}

	public boolean getDlcDownloadProgress(final int appID, final long[] bytesDownloaded, final long[] BytesTotal) {
		return getDlcDownloadProgress(pointer, appID, bytesDownloaded, BytesTotal);
	}

	public int getEarliestPurchaseUnixTime(final int appID) {
		return getEarliestPurchaseUnixTime(pointer, appID);
	}

	public SteamAPICall getFileDetails(final String fileName) {
		return new SteamAPICall(getFileDetails(pointer, callback, fileName));
	}

	public int getInstalledDepots(final int appID, final Integer[] depotIDs, final int maxDepots) {
		return getInstalledDepots(pointer, appID, depotIDs, maxDepots);
	}

	public int getLaunchCommandLine(final List<String> commandLines, final int commandLineSizeBuffer) {
		final SteamStringValue value = new SteamStringValue();

		final int copiedBytes = getLaunchCommandLine(pointer, value, commandLineSizeBuffer);

		if(copiedBytes > 0) {
			commandLines.add(value.getValue());
		}

		return copiedBytes;
	}

	public String getLaunchQueryParam(final String key) {
		return getLaunchQueryParam(pointer, key);
	}

	public void installDLC(final int appID) {
		installDLC(pointer, appID);
	}

	public boolean markContentCorrupt(final boolean missingFilesOnly) {
		return markContentCorrupt(pointer, missingFilesOnly);
	}

	@Deprecated
	public void requestAllProofOfPurchaseKeys() {
		requestAllProofOfPurchaseKeys(pointer);
	}

	@Deprecated
	public void requestAppProofOfPurchaseKey(final int appID) {
		requestAppProofOfPurchaseKey(pointer, appID);
	}

	public void uninstallDLC(final int appID) {
		uninstallDLC(pointer, appID);
	}

	// @off

	/*JNI
		#include <steam_api.h>
		#include "SteamAppsCallback.h"
		#include <vector>
	*/

	private static native boolean getDLCDataByIndex(long pointer, int index, Integer[] appID, Boolean[] available, SteamStringValue value, int valueBufferSize); /*
		ISteamApps* apps = (ISteamApps*) pointer;
		char *valueBuffer = (char*) malloc(valueBufferSize);

		bool success = apps->BGetDLCDataByIndex(index, (AppId_t*) appID, (bool*) available, valueBuffer, valueBufferSize);

        jclass valueClazz = env->GetObjectClass(value);

        jfieldID field = env->GetFieldID(valueClazz, "value", "Ljava/lang/String;");
	    env->SetObjectField(value, field, env->NewStringUTF(valueBuffer));

		return success;
	*/

	private static native boolean isAppInstalled(long pointer, int appID); /*
		ISteamApps* apps = (ISteamApps*) pointer;
		return apps->BIsAppInstalled((AppId_t) appID);
	*/

	private static native boolean isCybercafe(long pointer); /*
		ISteamApps* apps = (ISteamApps*) pointer;
		return apps->BIsCybercafe();
	*/

	private static native boolean isDlcInstalled(long pointer, int appID); /*
		ISteamApps* apps = (ISteamApps*) pointer;
		return apps->BIsDlcInstalled((AppId_t) appID);
	*/

	private static native boolean isLowViolence(long pointer); /*
		ISteamApps* apps = (ISteamApps*) pointer;
		return apps->BIsLowViolence();
	*/

	private static native boolean isSubscribed(long pointer); /*
		ISteamApps* apps = (ISteamApps*) pointer;
		return apps->BIsSubscribed();
	*/

	private static native boolean isSubscribedApp(long pointer, int appID); /*
		ISteamApps* apps = (ISteamApps*) pointer;
		return apps->BIsSubscribedApp((AppId_t) appID);
	*/

	private static native boolean isSubscribedFromFamilySharing(long pointer); /*
		ISteamApps* apps = (ISteamApps*) pointer;
		return apps->BIsSubscribedFromFamilySharing();
	*/

	private static native boolean isSubscribedFromFreeWeekend(long pointer); /*
		ISteamApps* apps = (ISteamApps*) pointer;
		return apps->BIsSubscribedFromFreeWeekend();
	*/

	private static native boolean isVACBanned(long pointer); /*
		ISteamApps* apps = (ISteamApps*) pointer;
		return apps->BIsVACBanned();
	*/

	private static native int getAppBuildId(long pointer); /*
        ISteamApps* apps = (ISteamApps*) pointer;
        return apps->GetAppBuildId();
	*/

	private static native int getAppInstallDir(long pointer, int appID, SteamStringValue value, int valueBufferSize); /*
        ISteamApps* apps = (ISteamApps*) pointer;
		char *valueBuffer = (char*) malloc(valueBufferSize);

        uint32 bytesCopied = apps->GetAppInstallDir((AppId_t) appID, valueBuffer, valueBufferSize);

        jclass valueClazz = env->GetObjectClass(value);

        jfieldID field = env->GetFieldID(valueClazz, "value", "Ljava/lang/String;");
	    env->SetObjectField(value, field, env->NewStringUTF(valueBuffer));

		return bytesCopied;
	*/

	private static native long getAppOwner(long pointer); /*
        ISteamApps* apps = (ISteamApps*) pointer;
		CSteamID steamID = apps->GetAppOwner();
		return (int64) steamID.ConvertToUint64();
	*/

	private static native String getAvailableGameLanguages(long pointer); /*
        ISteamApps* apps = (ISteamApps*) pointer;
        jstring language = env->NewStringUTF(apps->GetAvailableGameLanguages());
        return language;
	*/

	private static native boolean getCurrentBetaName(long pointer, SteamStringValue value, int valueBufferSize); /*
		ISteamApps* apps = (ISteamApps*) pointer;
		char *valueBuffer = (char*) malloc(valueBufferSize);

        bool success = apps->GetCurrentBetaName(valueBuffer, valueBufferSize);

        jclass valueClazz = env->GetObjectClass(value);

        jfieldID field = env->GetFieldID(valueClazz, "value", "Ljava/lang/String;");
	    env->SetObjectField(value, field, env->NewStringUTF(valueBuffer));

		return success;
	*/

	private static native String getCurrentGameLanguage(long pointer); /*
		ISteamApps* apps = (ISteamApps*) pointer;
        jstring language = env->NewStringUTF(apps->GetCurrentGameLanguage());
        return language;
	*/

	private static native int getDLCCount(long pointer); /*
		ISteamApps* apps = (ISteamApps*) pointer;
		return apps->GetDLCCount();
	*/

	private static native boolean getDlcDownloadProgress(long pointer, int appID, long[] bytesDownloaded, long[] BytesTotal); /*
		ISteamApps* apps = (ISteamApps*) pointer;
		return apps->GetDlcDownloadProgress((AppId_t) appID, (uint64*) bytesDownloaded, (uint64*) BytesTotal);
	*/

	private static native int getEarliestPurchaseUnixTime(long pointer, int appID); /*
		ISteamApps* apps = (ISteamApps*) pointer;
		return apps->GetEarliestPurchaseUnixTime((AppId_t) appID);
	*/

	private static native long getFileDetails(long pointer, long callback, String fileName); /*
		ISteamApps* apps = (ISteamApps*) pointer;
		SteamAppsCallback* cb = (SteamAppsCallback*) callback;

		SteamAPICall_t handle = apps->GetFileDetails(fileName);
		cb->onSteamAppsFileDetailsResultCall.Set(handle, cb, &SteamAppsCallback::onSteamAppsFileDetailsResult);
		return handle;
	*/

	private static native int getInstalledDepots(long pointer, int appID, Integer[] depotIDs, int maxDepots); /*
		ISteamApps* apps = (ISteamApps*) pointer;
		return apps->GetInstalledDepots((AppId_t) appID, (DepotId_t*) depotIDs, maxDepots);
	*/

	private static native int getLaunchCommandLine(long pointer, SteamStringValue value, int valueBufferSize); /*
		ISteamApps* apps = (ISteamApps*) pointer;
		char *valueBuffer = (char*) malloc(valueBufferSize);

        uint32 bytesCopied = apps->GetLaunchCommandLine(valueBuffer, valueBufferSize);

        jclass valueClazz = env->GetObjectClass(value);

        jfieldID field = env->GetFieldID(valueClazz, "value", "Ljava/lang/String;");
	    env->SetObjectField(value, field, env->NewStringUTF(valueBuffer));

		return bytesCopied;
	*/

	private static native String getLaunchQueryParam(long pointer, String key); /*
		ISteamApps* apps = (ISteamApps*) pointer;
		return env->NewStringUTF(apps->GetLaunchQueryParam(key));
	*/

	private static native void installDLC(long pointer, int appID); /*
		ISteamApps* apps = (ISteamApps*) pointer;
		apps->InstallDLC((AppId_t) appID);
	*/

	private static native boolean markContentCorrupt(long pointer, boolean missingFilesOnly); /*
		ISteamApps* apps = (ISteamApps*) pointer;
		return apps->MarkContentCorrupt(missingFilesOnly);
    */

	private static native void requestAllProofOfPurchaseKeys(long pointer); /*
		ISteamApps* apps = (ISteamApps*) pointer;
		apps->RequestAllProofOfPurchaseKeys();
    */

	private static native void requestAppProofOfPurchaseKey(long pointer, int appID); /*
		ISteamApps* apps = (ISteamApps*) pointer;
		apps->RequestAppProofOfPurchaseKey((AppId_t) appID);
    */

	private static native void uninstallDLC(long pointer, int appID); /*
		ISteamApps* apps = (ISteamApps*) pointer;
		apps->UninstallDLC((AppId_t) appID);
    */

}
