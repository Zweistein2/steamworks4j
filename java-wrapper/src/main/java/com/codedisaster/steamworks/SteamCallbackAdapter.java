package com.codedisaster.steamworks;

abstract public class SteamCallbackAdapter<T> {

	protected final T callback;

	protected SteamCallbackAdapter(final T callback) {
		this.callback = callback;
	}
}
