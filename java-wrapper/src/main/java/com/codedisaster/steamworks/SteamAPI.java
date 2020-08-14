package com.codedisaster.steamworks;

import java.io.PrintStream;

public class SteamAPI {

	private static boolean isRunning = false;
	private static boolean isNativeAPILoaded = false;

	public static void loadLibraries() throws SteamException {
		loadLibraries(null);
	}

	public static void loadLibraries(final String libraryPath) throws SteamException {
		if (isNativeAPILoaded) {
			return;
		}

		if (libraryPath == null && SteamSharedLibraryLoader.DEBUG) {
			final String sdkPath = SteamSharedLibraryLoader.getSdkRedistributableBinPath();
			SteamSharedLibraryLoader.loadLibrary("steam_api", sdkPath);
		} else {
			SteamSharedLibraryLoader.loadLibrary("steam_api", libraryPath);
		}

		SteamSharedLibraryLoader.loadLibrary("steamworks4j", libraryPath);

		isNativeAPILoaded = true;
	}

	public static void skipLoadLibraries() {
		isNativeAPILoaded = true;
	}

	public static boolean restartAppIfNecessary(final int appId) throws SteamException {

		if (!isNativeAPILoaded) {
			throw new SteamException("Native libraries not loaded.\nEnsure to call SteamAPI.loadLibraries() first!");
		}

		return nativeRestartAppIfNecessary(appId);
	}

	public static boolean init() throws SteamException {
		if (!isNativeAPILoaded) {
			throw new SteamException("Native libraries not loaded.\nEnsure to call SteamAPI.loadLibraries() first!");
		}

		isRunning = nativeInit();

		return isRunning;
	}

	public static void shutdown() {
		isRunning = false;
		nativeShutdown();
	}

	/**
	 * According to the documentation, SteamAPI_IsSteamRunning() should "only be used in very
	 * specific cases". Also, there seems to be an issue with leaking OS resources on Mac OS X.
	 * For these reasons, the default behaviour of this function has been changed to <i>not</i>
	 * call the native function.
	 *
	 * @see SteamAPI#isSteamRunning(boolean)
	 */
	public static boolean isSteamRunning() {
		return isSteamRunning(false);
	}

	public static boolean isSteamRunning(final boolean checkNative) {
		return isRunning && (!checkNative || isSteamRunningNative());
	}

	public static void printDebugInfo(final PrintStream stream) {
		stream.println("  Steam API initialized: " + isRunning);
		stream.println("  Steam client active: " + isSteamRunning());
	}

	static boolean isIsNativeAPILoaded() {
		return isNativeAPILoaded;
	}

	// @off

	/*JNI
		#include <steam_api.h>

		static JavaVM* staticVM = 0;
	*/

	private static native boolean nativeRestartAppIfNecessary(int appId); /*
		return SteamAPI_RestartAppIfNecessary(appId);
	*/

	public static native void releaseCurrentThreadMemory(); /*
		SteamAPI_ReleaseCurrentThreadMemory();
	*/

	private static native boolean nativeInit(); /*
		if (env->GetJavaVM(&staticVM) != 0) {
			return false;
		}

		return SteamAPI_Init();
	*/

	private static native void nativeShutdown(); /*
		SteamAPI_Shutdown();
	*/

	public static native void runCallbacks(); /*
		SteamAPI_RunCallbacks();
	*/

	private static native boolean isSteamRunningNative(); /*
		return SteamAPI_IsSteamRunning();
	*/

	protected static native long getSteamAppsPointer(); /*
		return (intp) SteamApps();
	*/

	protected static native long getSteamControllerPointer(); /*
		return (intp) SteamController();
	*/

	protected static native long getSteamFriendsPointer(); /*
		return (intp) SteamFriends();
	*/

	protected static native long getSteamHTTPPointer(); /*
		return (intp) SteamHTTP();
	*/

	protected static native long getSteamInventoryPointer(); /*
		return (intp) SteamInventory();
	*/

	protected static native long getSteamMatchmakingPointer(); /*
		return (intp) SteamMatchmaking();
	*/

	protected static native long getSteamMatchmakingServersPointer(); /*
		return (intp) SteamMatchmakingServers();
	*/

	protected static native long getSteamNetworkingPointer(); /*
		return (intp) SteamNetworking();
	*/

	protected static native long getSteamRemoteStoragePointer(); /*
		return (intp) SteamRemoteStorage();
	*/

	protected static native long getSteamScreenshotsPointer(); /*
		return (intp) SteamScreenshots();
	*/

	protected static native long getSteamUGCPointer(); /*
		return (intp) SteamUGC();
	*/

	protected static native long getSteamUserPointer(); /*
		return (intp) SteamUser();
	*/

	protected static native long getSteamUserStatsPointer(); /*
		return (intp) SteamUserStats();
	*/

	protected static native long getSteamUtilsPointer(); /*
		return (intp) SteamUtils();
	*/

}
