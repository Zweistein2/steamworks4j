package com.codedisaster.steamworks.gameserver;

class SteamGameServerHTTPNative {

	// @off

	/*JNI
		#include "SteamGameServerHTTPCallback.h"
	*/

	static native long createCallback(Object javaCallback); /*
		return (intp) new SteamGameServerHTTPCallback(env, javaCallback);
	*/

}
