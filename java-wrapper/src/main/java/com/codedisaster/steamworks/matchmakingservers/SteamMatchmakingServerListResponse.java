package com.codedisaster.steamworks.matchmakingservers;

import com.codedisaster.steamworks.SteamInterface;

public abstract class SteamMatchmakingServerListResponse extends SteamInterface {

	public enum Response {
		ServerResponded,
		ServerFailedToRespond,
		NoServersListedOnMasterServer;

		private static final Response[] values = values();

		static Response byOrdinal(final int ordinal) {
			return values[ordinal];
		}
	}

	protected SteamMatchmakingServerListResponse() {
		super(~0L);
		callback = createProxy(this);
	}

	public abstract void serverResponded(SteamServerListRequest request, int server);

	void serverResponded(final long request, final int server) {
		serverResponded(new SteamServerListRequest(request), server);
	}

	public abstract void serverFailedToRespond(SteamServerListRequest request, int server);

	void serverFailedToRespond(final long request, final int server) {
		serverFailedToRespond(new SteamServerListRequest(request), server);
	}

	public abstract void refreshComplete(SteamServerListRequest request, Response response);

	void refreshComplete(final long request, final int response) {
		refreshComplete(new SteamServerListRequest(request), Response.byOrdinal(response));
	}

	// @off

	/*JNI
		#include "SteamMatchmakingServerListResponse.h"
	*/

	private static native long createProxy(SteamMatchmakingServerListResponse javaCallback); /*
		return (intp) new SteamMatchmakingServerListResponse(env, javaCallback);
	*/

}
