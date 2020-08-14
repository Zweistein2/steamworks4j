package com.codedisaster.steamworks.matchmakingservers;

import com.codedisaster.steamworks.SteamNativeIntHandle;

public class SteamServerQuery extends SteamNativeIntHandle {

	public static final SteamServerQuery INVALID = new SteamServerQuery(0xffffffff);

	SteamServerQuery(final int handle) {
		super(handle);
	}

}
