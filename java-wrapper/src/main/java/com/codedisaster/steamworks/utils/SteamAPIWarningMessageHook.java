package com.codedisaster.steamworks.utils;

public interface SteamAPIWarningMessageHook {
	void onWarningMessage(int severity, String message);
}
