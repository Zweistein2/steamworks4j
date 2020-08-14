package com.codedisaster.steamworks.matchmaking;

import com.codedisaster.steamworks.SteamInterface;

public abstract class SteamMatchmakingRulesResponse extends SteamInterface {

	protected SteamMatchmakingRulesResponse() {
		super(~0L);
		callback = createProxy(this);
	}

	public abstract void rulesResponded(String rule, String value);

	public abstract void rulesFailedToRespond();

	public abstract void rulesRefreshComplete();

	// @off

	/*JNI
		#include "SteamMatchmakingRulesResponse.h"
	*/

	private static native long createProxy(SteamMatchmakingRulesResponse javaCallback); /*
		return (intp) new SteamMatchmakingRulesResponse(env, javaCallback);
	*/

}
