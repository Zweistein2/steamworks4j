package com.codedisaster.steamworks;

public class SteamAPICall extends SteamNativeHandle {

	public SteamAPICall(final long handle) {
		super(handle);
	}

	public boolean isValid() {
		return handle != 0;
	}
}
