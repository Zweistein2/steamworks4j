package com.codedisaster.steamworks;

@SuppressWarnings("unused")
public class SteamException extends Exception {

	public SteamException() {
		super();
	}

	public SteamException(final String message) {
		super(message);
	}

	public SteamException(final String message, final Throwable throwable) {
		super(message, throwable);
	}

	public SteamException(final Throwable throwable) {
		super(throwable);
	}

}
