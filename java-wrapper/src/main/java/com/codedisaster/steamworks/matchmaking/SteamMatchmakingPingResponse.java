package com.codedisaster.steamworks.matchmaking;

import com.codedisaster.steamworks.SteamInterface;

public abstract class SteamMatchmakingPingResponse extends SteamInterface {

	protected SteamMatchmakingPingResponse() {
		super(~0L);
		callback = createProxy(this);
	}

	public abstract void serverResponded(SteamMatchmakingGameServerItem server);

	public abstract void serverFailedToRespond();

	// @off

	/*JNI
		#include "SteamMatchmakingPingResponse.h"
	*/

	private static native long createProxy(SteamMatchmakingPingResponse javaCallback); /*
		return (intp) new SteamMatchmakingPingResponse(env, javaCallback);
	*/

}
