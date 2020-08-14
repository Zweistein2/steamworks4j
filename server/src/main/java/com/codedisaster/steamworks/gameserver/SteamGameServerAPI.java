package com.codedisaster.steamworks.gameserver;

import com.codedisaster.steamworks.SteamAPI;
import com.codedisaster.steamworks.SteamException;
import com.codedisaster.steamworks.SteamID;
import com.codedisaster.steamworks.SteamSharedLibraryLoader;

public class SteamGameServerAPI {

	public enum ServerMode {
		Invalid,
		NoAuthentication,
		Authentication,
		AuthenticationAndSecure
	}

	private static boolean isRunning = false;
	private static boolean isNativeAPILoaded = false;

	public static void loadLibraries() throws SteamException {
		loadLibraries(null);
	}
	
	public static void loadLibraries(final String libraryPath) throws SteamException {

		if (isNativeAPILoaded) {
			return;
		}

		SteamAPI.loadLibraries(libraryPath);

		SteamSharedLibraryLoader.loadLibrary("steamworks4j-server", libraryPath);

		isNativeAPILoaded = true;
	}

	public static void skipLoadLibraries() {
		isNativeAPILoaded = true;
	}

	public static boolean init(final int ip, final short steamPort, final short gamePort, final short queryPort,
                               final ServerMode serverMode, final String versionString) throws SteamException {

		if (!isNativeAPILoaded) {
			throw new SteamException("Native server libraries not loaded.\nEnsure to call SteamGameServerAPI.loadLibraries() first!");
		}

		isRunning = SteamGameServerAPINative.nativeInit(
				ip, steamPort, gamePort, queryPort, serverMode.ordinal(), versionString);

		return isRunning;
	}

	public static void shutdown() {
		isRunning = false;
		SteamGameServerAPINative.nativeShutdown();
	}

	public static void runCallbacks() {
		SteamGameServerAPINative.runCallbacks();
	}

	public static boolean isSecure() {
		return SteamGameServerAPINative.isSecure();
	}

	public static SteamID getSteamID() {
		return SteamID.createFromNativeHandle(SteamGameServerAPINative.nativeGetSteamID());
	}

}
