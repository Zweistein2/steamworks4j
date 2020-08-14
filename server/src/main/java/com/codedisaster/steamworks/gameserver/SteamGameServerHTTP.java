package com.codedisaster.steamworks.gameserver;

import com.codedisaster.steamworks.http.SteamHTTP;
import com.codedisaster.steamworks.http.SteamHTTPCallback;
import com.codedisaster.steamworks.http.SteamHTTPCallbackAdapter;

public class SteamGameServerHTTP extends SteamHTTP {

	public SteamGameServerHTTP(final SteamHTTPCallback callback) {
		super(SteamGameServerAPINative.getSteamGameServerHTTPPointer(),
				SteamGameServerHTTPNative.createCallback(new SteamHTTPCallbackAdapter(callback)));
	}

}
