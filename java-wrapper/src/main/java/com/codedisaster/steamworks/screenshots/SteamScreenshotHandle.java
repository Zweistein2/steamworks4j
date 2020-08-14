package com.codedisaster.steamworks.screenshots;

import com.codedisaster.steamworks.SteamNativeIntHandle;

public class SteamScreenshotHandle extends SteamNativeIntHandle {

	public static final SteamScreenshotHandle INVALID = new SteamScreenshotHandle(0);

	SteamScreenshotHandle(final int handle) {
		super(handle);
	}

}
