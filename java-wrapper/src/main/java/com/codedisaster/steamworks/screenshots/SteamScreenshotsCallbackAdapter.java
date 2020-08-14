package com.codedisaster.steamworks.screenshots;

import com.codedisaster.steamworks.SteamCallbackAdapter;
import com.codedisaster.steamworks.SteamResult;

@SuppressWarnings("unused")
public class SteamScreenshotsCallbackAdapter extends SteamCallbackAdapter<SteamScreenshotsCallback> {

	SteamScreenshotsCallbackAdapter(final SteamScreenshotsCallback callback) {
		super(callback);
	}

	void onScreenshotReady(final int local, final int result) {
		callback.onScreenshotReady(new SteamScreenshotHandle(local), SteamResult.byValue(result));
	}

	void onScreenshotRequested() {
		callback.onScreenshotRequested();
	}

}
