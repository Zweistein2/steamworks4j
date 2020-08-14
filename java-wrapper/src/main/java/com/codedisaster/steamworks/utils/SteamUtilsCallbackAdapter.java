package com.codedisaster.steamworks.utils;

import com.codedisaster.steamworks.SteamCallbackAdapter;

@SuppressWarnings("unused")
class SteamUtilsCallbackAdapter extends SteamCallbackAdapter<SteamUtilsCallback> {

	private SteamAPIWarningMessageHook messageHook;

	SteamUtilsCallbackAdapter(final SteamUtilsCallback callback) {
		super(callback);
	}

	void setWarningMessageHook(final SteamAPIWarningMessageHook messageHook) {
		this.messageHook = messageHook;
	}

	void onWarningMessage(final int severity, final String message) {
		if (messageHook != null) {
			messageHook.onWarningMessage(severity, message);
		}
	}

	void onSteamShutdown() {
		callback.onSteamShutdown();
	}

}
