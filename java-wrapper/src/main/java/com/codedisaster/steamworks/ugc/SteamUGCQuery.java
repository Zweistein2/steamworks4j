package com.codedisaster.steamworks.ugc;

import com.codedisaster.steamworks.SteamNativeHandle;

public class SteamUGCQuery extends SteamNativeHandle {

	public SteamUGCQuery(final long handle) {
		super(handle);
	}

	public boolean isValid() {
		return handle != -1;
	}
}
