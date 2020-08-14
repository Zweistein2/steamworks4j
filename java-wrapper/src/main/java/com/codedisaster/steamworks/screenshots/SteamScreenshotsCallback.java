package com.codedisaster.steamworks.screenshots;

import com.codedisaster.steamworks.SteamResult;

public interface SteamScreenshotsCallback {

	void onScreenshotReady(SteamScreenshotHandle local, SteamResult result);

	void onScreenshotRequested();

}
