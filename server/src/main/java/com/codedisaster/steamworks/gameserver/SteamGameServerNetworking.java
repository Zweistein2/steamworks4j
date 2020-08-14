package com.codedisaster.steamworks.gameserver;

import com.codedisaster.steamworks.networking.SteamNetworking;
import com.codedisaster.steamworks.networking.SteamNetworkingCallback;
import com.codedisaster.steamworks.networking.SteamNetworkingCallbackAdapter;

public class SteamGameServerNetworking extends SteamNetworking {

	public SteamGameServerNetworking(final SteamNetworkingCallback callback) {
		super(SteamGameServerAPINative.getSteamGameServerNetworkingPointer(),
				SteamGameServerNetworkingNative.createCallback(new SteamNetworkingCallbackAdapter(callback)));
	}

}
