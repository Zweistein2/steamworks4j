package com.codedisaster.steamworks.matchmakingservers;

import com.codedisaster.steamworks.SteamNativeHandle;

public class SteamServerListRequest extends SteamNativeHandle {

	SteamServerListRequest(final long handle) {
		super(handle);
	}

	public boolean isValid() {
		return handle != 0L;
	}

}
