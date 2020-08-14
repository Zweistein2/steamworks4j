package com.codedisaster.steamworks.gameserver;

class SteamGameServerNetworkingNative {

	// @off

	/*JNI
		#include "SteamGameServerNetworkingCallback.h"
	*/

	static native long createCallback(Object javaCallback); /*
		return (intp) new SteamGameServerNetworkingCallback(env, javaCallback);
	*/

}
