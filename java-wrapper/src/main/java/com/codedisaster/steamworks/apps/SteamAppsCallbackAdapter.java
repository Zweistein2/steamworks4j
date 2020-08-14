package com.codedisaster.steamworks.apps;

import com.codedisaster.steamworks.SteamCallbackAdapter;
import com.codedisaster.steamworks.SteamResult;

@SuppressWarnings("unused")
public class SteamAppsCallbackAdapter extends SteamCallbackAdapter<SteamAppsCallback> {
    SteamAppsCallbackAdapter(final SteamAppsCallback callback) {
        super(callback);
    }

    void onSteamAppsFileDetailsResult(final int result, final long fileSize, final byte[] fileSHA, final int flags) {
        callback.onSteamAppsFileDetailsResult(SteamResult.byValue(result), fileSize, fileSHA, flags);
    }
}
