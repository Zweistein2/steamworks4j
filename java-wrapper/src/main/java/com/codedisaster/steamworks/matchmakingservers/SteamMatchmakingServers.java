package com.codedisaster.steamworks.matchmakingservers;

import com.codedisaster.steamworks.SteamAPI;
import com.codedisaster.steamworks.SteamInterface;
import com.codedisaster.steamworks.matchmaking.*;

public class SteamMatchmakingServers extends SteamInterface {

	public SteamMatchmakingServers() {
		super(SteamAPI.getSteamMatchmakingServersPointer());
	}

	public SteamServerListRequest requestInternetServerList(final int appID, final SteamMatchmakingKeyValuePair[] filters,
                                                            final SteamMatchmakingServerListResponse requestServersResponse) {

		return new SteamServerListRequest(requestInternetServerList(pointer, appID,
				filters, filters.length, requestServersResponse.callback));
	}

	public SteamServerListRequest requestLANServerList(final int appID,
                                                       final SteamMatchmakingServerListResponse requestServersResponse) {

		return new SteamServerListRequest(requestLANServerList(pointer, appID, requestServersResponse.callback));
	}

	public SteamServerListRequest requestFriendsServerList(final int appID, final SteamMatchmakingKeyValuePair[] filters,
                                                           final SteamMatchmakingServerListResponse requestServersResponse) {

		return new SteamServerListRequest(requestFriendsServerList(pointer, appID,
				filters, filters.length, requestServersResponse.callback));
	}

	public SteamServerListRequest requestFavoritesServerList(final int appID, final SteamMatchmakingKeyValuePair[] filters,
                                                             final SteamMatchmakingServerListResponse requestServersResponse) {

		return new SteamServerListRequest(requestFavoritesServerList(pointer, appID,
				filters, filters.length, requestServersResponse.callback));
	}

	public SteamServerListRequest requestHistoryServerList(final int appID, final SteamMatchmakingKeyValuePair[] filters,
                                                           final SteamMatchmakingServerListResponse requestServersResponse) {

		return new SteamServerListRequest(requestHistoryServerList(pointer, appID,
				filters, filters.length, requestServersResponse.callback));
	}

	public SteamServerListRequest requestSpectatorServerList(final int appID, final SteamMatchmakingKeyValuePair[] filters,
                                                             final SteamMatchmakingServerListResponse requestServersResponse) {

		return new SteamServerListRequest(requestSpectatorServerList(pointer, appID,
				filters, filters.length, requestServersResponse.callback));
	}

	public void releaseRequest(final SteamServerListRequest request) {
		releaseRequest(pointer, request.handle);
	}

	public boolean getServerDetails(final SteamServerListRequest request, final int server, final SteamMatchmakingGameServerItem details) {
		return getServerDetails(pointer, request.handle, server, details);
	}

	public void cancelQuery(final SteamServerListRequest request) {
		cancelQuery(pointer, request.handle);
	}

	public void refreshQuery(final SteamServerListRequest request) {
		refreshQuery(pointer, request.handle);
	}

	public boolean isRefreshing(final SteamServerListRequest request) {
		return isRefreshing(pointer, request.handle);
	}

	public int getServerCount(final SteamServerListRequest request) {
		return getServerCount(pointer, request.handle);
	}

	public void refreshServer(final SteamServerListRequest request, final int server) {
		refreshServer(pointer, request.handle, server);
	}

	public SteamServerQuery pingServer(final int ip, final short port, final SteamMatchmakingPingResponse requestServersResponse) {
		return new SteamServerQuery(pingServer(pointer, ip, port, requestServersResponse.callback));
	}

	public SteamServerQuery playerDetails(final int ip, final short port, final SteamMatchmakingPlayersResponse requestServersResponse) {
		return new SteamServerQuery(playerDetails(pointer, ip, port, requestServersResponse.callback));
	}

	public SteamServerQuery serverRules(final int ip, final short port, final SteamMatchmakingRulesResponse requestServersResponse) {
		return new SteamServerQuery(serverRules(pointer, ip, port, requestServersResponse.callback));
	}

	public void cancelServerQuery(final SteamServerQuery serverQuery) {
		cancelServerQuery(pointer, serverQuery.handle);
	}

	// @off

	/*JNI
		#include <steam_api.h>
		#include "SteamMatchmakingGameServerItem.h"
		#include "SteamMatchmakingKeyValuePair.h"
		#include "SteamMatchmakingPingResponse.h"
		#include "SteamMatchmakingPlayersResponse.h"
		#include "SteamMatchmakingRulesResponse.h"
		#include "SteamMatchmakingServerListResponse.h"

		#define MAX_FILTERS 16
	*/

	private static native long requestInternetServerList(long pointer, int appID,
                                                         SteamMatchmakingKeyValuePair[] filters, int count,
                                                         long requestServersResponse); /*

		MatchMakingKeyValuePair_t _pairs[MAX_FILTERS];
		MatchMakingKeyValuePair_t* pairs = _pairs;
		count = convertKeyValuePairArray(env, filters, count, pairs, MAX_FILTERS);

		ISteamMatchmakingServers* servers = (ISteamMatchmakingServers*) pointer;
		SteamMatchmakingServerListResponse* response = (SteamMatchmakingServerListResponse*) requestServersResponse;
		return (jlong) servers->RequestInternetServerList(appID, &pairs, count, response);
	*/

	private static native long requestLANServerList(long pointer, int appID,
													long requestServersResponse); /*

		ISteamMatchmakingServers* servers = (ISteamMatchmakingServers*) pointer;
		SteamMatchmakingServerListResponse* response = (SteamMatchmakingServerListResponse*) requestServersResponse;
		return (jlong) servers->RequestLANServerList(appID, response);
	*/

	private static native long requestFriendsServerList(long pointer, int appID,
														SteamMatchmakingKeyValuePair[] filters, int count,
														long requestServersResponse); /*

		MatchMakingKeyValuePair_t _pairs[MAX_FILTERS];
		MatchMakingKeyValuePair_t* pairs = _pairs;
		count = convertKeyValuePairArray(env, filters, count, pairs, MAX_FILTERS);

		ISteamMatchmakingServers* servers = (ISteamMatchmakingServers*) pointer;
		SteamMatchmakingServerListResponse* response = (SteamMatchmakingServerListResponse*) requestServersResponse;
		return (jlong) servers->RequestFriendsServerList(appID, &pairs, count, response);
	*/

	private static native long requestFavoritesServerList(long pointer, int appID,
														  SteamMatchmakingKeyValuePair[] filters, int count,
														  long requestServersResponse); /*

		MatchMakingKeyValuePair_t _pairs[MAX_FILTERS];
		MatchMakingKeyValuePair_t* pairs = _pairs;
		count = convertKeyValuePairArray(env, filters, count, pairs, MAX_FILTERS);

		ISteamMatchmakingServers* servers = (ISteamMatchmakingServers*) pointer;
		SteamMatchmakingServerListResponse* response = (SteamMatchmakingServerListResponse*) requestServersResponse;
		return (jlong) servers->RequestFavoritesServerList(appID, &pairs, count, response);
	*/

	private static native long requestHistoryServerList(long pointer, int appID,
														SteamMatchmakingKeyValuePair[] filters, int count,
														long requestServersResponse); /*

		MatchMakingKeyValuePair_t _pairs[MAX_FILTERS];
		MatchMakingKeyValuePair_t* pairs = _pairs;
		count = convertKeyValuePairArray(env, filters, count, pairs, MAX_FILTERS);

		ISteamMatchmakingServers* servers = (ISteamMatchmakingServers*) pointer;
		SteamMatchmakingServerListResponse* response = (SteamMatchmakingServerListResponse*) requestServersResponse;
		return (jlong) servers->RequestHistoryServerList(appID, &pairs, count, response);
	*/

	private static native long requestSpectatorServerList(long pointer, int appID,
														  SteamMatchmakingKeyValuePair[] filters, int count,
														  long requestServersResponse); /*

		MatchMakingKeyValuePair_t _pairs[MAX_FILTERS];
		MatchMakingKeyValuePair_t* pairs = _pairs;
		count = convertKeyValuePairArray(env, filters, count, pairs, MAX_FILTERS);

		ISteamMatchmakingServers* servers = (ISteamMatchmakingServers*) pointer;
		SteamMatchmakingServerListResponse* response = (SteamMatchmakingServerListResponse*) requestServersResponse;
		return (jlong) servers->RequestSpectatorServerList(appID, &pairs, count, response);
	*/

	private static native void releaseRequest(long pointer, long request); /*
		ISteamMatchmakingServers* servers = (ISteamMatchmakingServers*) pointer;
		servers->ReleaseRequest((HServerListRequest) request);
	*/

	private static native boolean getServerDetails(long pointer, long request, int server,
												   SteamMatchmakingGameServerItem details); /*
		ISteamMatchmakingServers* servers = (ISteamMatchmakingServers*) pointer;
		const gameserveritem_t* item = servers->GetServerDetails((HServerListRequest) request, server);
		if (item != nullptr) {
			convertGameServerItem(details, env, *item);
			return true;
		}
		return false;
	*/

	private static native void cancelQuery(long pointer, long request); /*
		ISteamMatchmakingServers* servers = (ISteamMatchmakingServers*) pointer;
		servers->CancelQuery((HServerListRequest) request);
	*/

	private static native void refreshQuery(long pointer, long request); /*
		ISteamMatchmakingServers* servers = (ISteamMatchmakingServers*) pointer;
		servers->RefreshQuery((HServerListRequest) request);
	*/

	private static native boolean isRefreshing(long pointer, long request); /*
		ISteamMatchmakingServers* servers = (ISteamMatchmakingServers*) pointer;
		return servers->IsRefreshing((HServerListRequest) request);
	*/

	private static native int getServerCount(long pointer, long request); /*
		ISteamMatchmakingServers* servers = (ISteamMatchmakingServers*) pointer;
		return servers->GetServerCount((HServerListRequest) request);
	*/

	private static native void refreshServer(long pointer, long request, int server); /*
		ISteamMatchmakingServers* servers = (ISteamMatchmakingServers*) pointer;
		servers->RefreshServer((HServerListRequest) request, server);
	*/

	private static native int pingServer(long pointer, int ip, short port, long requestServersResponse); /*
		ISteamMatchmakingServers* servers = (ISteamMatchmakingServers*) pointer;
		SteamMatchmakingPingResponse* response = (SteamMatchmakingPingResponse*) requestServersResponse;
		return servers->PingServer(ip, port, response);
	*/

	private static native int playerDetails(long pointer, int ip, short port, long requestServersResponse); /*
		ISteamMatchmakingServers* servers = (ISteamMatchmakingServers*) pointer;
		SteamMatchmakingPlayersResponse* response = (SteamMatchmakingPlayersResponse*) requestServersResponse;
		return servers->PlayerDetails(ip, port, response);
	*/

	private static native int serverRules(long pointer, int ip, short port, long requestServersResponse); /*
		ISteamMatchmakingServers* servers = (ISteamMatchmakingServers*) pointer;
		SteamMatchmakingRulesResponse* response = (SteamMatchmakingRulesResponse*) requestServersResponse;
		return servers->ServerRules(ip, port, response);
	*/

	private static native void cancelServerQuery(long pointer, int serverQuery); /*
		ISteamMatchmakingServers* servers = (ISteamMatchmakingServers*) pointer;
		return servers->CancelServerQuery((HServerQuery) serverQuery);
	*/

}
