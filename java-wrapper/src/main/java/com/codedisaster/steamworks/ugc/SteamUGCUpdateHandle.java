package com.codedisaster.steamworks.ugc;

import com.codedisaster.steamworks.SteamNativeHandle;

public class SteamUGCUpdateHandle extends SteamNativeHandle {

	SteamUGCUpdateHandle(final long handle) {
		super(handle);
	}

	public boolean isValid() {
		return handle != -1;
	}
}
