package com.codedisaster.steamworks.matchmakingservers;

public class SteamMatchmakingServerNetAdr {

	short connectionPort;
	short queryPort;
	int ip;

	public SteamMatchmakingServerNetAdr() {

	}

	public SteamMatchmakingServerNetAdr(final int ip, final short queryPort, final short connectionPort) {
		this.ip = ip;
		this.queryPort = queryPort;
		this.connectionPort = connectionPort;
	}

	public short getConnectionPort() {
		return connectionPort;
	}

	public short getQueryPort() {
		return queryPort;
	}

	public int getIP() {
		return ip;
	}

	public String getConnectionAddressString() {
		return toString(ip, connectionPort);
	}

	public String getQueryAddressString() {
		return toString(ip, queryPort);
	}

	private static String toString(final int ip, final short port) {
		return String.format("%d.%d.%d.%d:%d",
				(ip >> 24) & 0xff, (ip >> 16) & 0xff, (ip >> 8) & 0xff, ip & 0xff, port);
	}

}
