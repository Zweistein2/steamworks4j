package com.codedisaster.steamworks.test;

import com.codedisaster.steamworks.*;
import com.codedisaster.steamworks.matchmaking.SteamMatchmakingGameServerItem;
import com.codedisaster.steamworks.matchmaking.SteamMatchmakingKeyValuePair;
import com.codedisaster.steamworks.matchmakingservers.SteamMatchmakingServerListResponse;
import com.codedisaster.steamworks.matchmakingservers.SteamMatchmakingServers;
import com.codedisaster.steamworks.matchmakingservers.SteamServerListRequest;

public class SteamMatchmakingServerTest extends SteamTestApp {

	private SteamMatchmakingServers servers;

	private SteamMatchmakingServerListResponse serverListResponse;

	@Override
	protected void registerInterfaces() throws SteamException {
		servers = new SteamMatchmakingServers();

		serverListResponse = new SteamMatchmakingServerListResponse() {
			@Override
			public void serverResponded(final SteamServerListRequest request, final int server) {
				final SteamMatchmakingGameServerItem serverInfo = new SteamMatchmakingGameServerItem();
				if (servers.getServerDetails(request, server, serverInfo)) {
					printServerInfo(server, serverInfo, "responded");
				} else {
					System.err.println("failed to get server info for #" + server);
				}
			}

			@Override
			public void serverFailedToRespond(final SteamServerListRequest request, final int server) {
				final SteamMatchmakingGameServerItem serverInfo = new SteamMatchmakingGameServerItem();
				if (servers.getServerDetails(request, server, serverInfo)) {
					printServerInfo(server, serverInfo, "failed to respond");
				} else {
					System.err.println("failed to get server info for #" + server);
				}
			}

			@Override
			public void refreshComplete(final SteamServerListRequest request, final Response response) {
				System.out.println("server list refresh complete: " + response.name());
				servers.releaseRequest(request);
			}

			private void printServerInfo(final int server, final SteamMatchmakingGameServerItem serverItem, final String status) {
				System.out.println("server info for #" + server + " (" + status + ")");
				System.out.println("  name: " + serverItem.getServerName());
			}
		};
	}

	@Override
	protected void unregisterInterfaces() throws SteamException {
		serverListResponse.dispose();
		servers.dispose();
	}

	@Override
	protected void processUpdate() throws SteamException {

	}

	@Override
	protected void processInput(final String input) throws SteamException {
		if (input.startsWith("request ")) {
			final SteamMatchmakingKeyValuePair[] filters = {
					new SteamMatchmakingKeyValuePair("gamedir", "spacewar"),
					new SteamMatchmakingKeyValuePair("secure", "1")
			};
			final SteamServerListRequest serverListRequest;
			final String type = input.substring("request ".length());
			switch(type) {
				case "lan":
					serverListRequest = servers.requestLANServerList(clientUtils.getAppID(), serverListResponse);
					break;
				case "history":
					serverListRequest = servers.requestHistoryServerList(clientUtils.getAppID(), filters, serverListResponse);
					break;
				case "friends":
					serverListRequest = servers.requestFriendsServerList(clientUtils.getAppID(), filters, serverListResponse);
					break;
				default:
					serverListRequest = servers.requestInternetServerList(clientUtils.getAppID(), filters, serverListResponse);
					break;
			}
			if (!serverListRequest.isValid()) {
				System.err.println("request failed, null return value");
			}
		}
	}

	public static void main(final String[] arguments) {
		new SteamMatchmakingServerTest().clientMain(arguments);
	}

}
