package com.codedisaster.steamworks.apps;

import com.codedisaster.steamworks.SteamResult;

public interface SteamAppsCallback {
    void onSteamAppsFileDetailsResult(SteamResult result, long fileSize, byte[] fileSHA, int flags);
}
