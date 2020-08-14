package com.codedisaster.steamworks.http;

import com.codedisaster.steamworks.SteamCallbackAdapter;

@SuppressWarnings("unused")
public class SteamHTTPCallbackAdapter extends SteamCallbackAdapter<SteamHTTPCallback> {

	public SteamHTTPCallbackAdapter(final SteamHTTPCallback callback) {
		super(callback);
	}

	void onHTTPRequestCompleted(final long request, final long contextValue, final boolean requestSuccessful,
                                final int statusCode, final int bodySize) {

		callback.onHTTPRequestCompleted(new SteamHTTPRequestHandle(request), contextValue,
				requestSuccessful, SteamHTTP.HTTPStatusCode.byValue(statusCode), bodySize);
	}

	void onHTTPRequestHeadersReceived(final long request, final long contextValue) {
		callback.onHTTPRequestHeadersReceived(new SteamHTTPRequestHandle(request), contextValue);
	}

	void onHTTPRequestDataReceived(final long request, final long contextValue, final int offset, final int bytesReceived) {
		callback.onHTTPRequestDataReceived(new SteamHTTPRequestHandle(request), contextValue, offset, bytesReceived);
	}

}
