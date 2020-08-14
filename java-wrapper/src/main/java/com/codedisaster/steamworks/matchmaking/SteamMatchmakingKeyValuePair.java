package com.codedisaster.steamworks.matchmaking;

public class SteamMatchmakingKeyValuePair {

	private String key;
	private String value;

	public SteamMatchmakingKeyValuePair() {

	}

	public SteamMatchmakingKeyValuePair(final String key, final String value) {
		this.key = key;
		this.value = value;
	}

	public String getKey() {
		return key;
	}

	public String getValue() {
		return value;
	}

}
