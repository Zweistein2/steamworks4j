package com.codedisaster.steamworks.apps;

import com.codedisaster.steamworks.SteamResult;

public interface SteamAppsCallback {
    void onSteamAppsFileDetailsResult(SteamResult result, long fileSize, byte[] fileSHA, int flags);

    void onSteamAppsDlcInstalled(int appID);

    void onSteamAppsNewUrlLaunchParameters();

    void onSteamAppsTimedTrialStatus(int appID, boolean isOffline, int secondsAllowed, int secondsPlayed);

    /**
     * Only used internally in Steam
     *
     * response to RegisterActivationCode()
     *
     * @param result the {@link RegisterActivationCodeResult} returned by the callback
     * @param packageRegistered the package that was registered. Only set on success
     */
    void onSteamAppsRegisterActivationCodeResponse(RegisterActivationCodeResult result, int packageRegistered);

    /**
     * Only used internally in Steam
     *
     * response to RequestAppProofOfPurchaseKey()/RequestAllProofOfPurchaseKeys()
     * for supporting third-party CD keys, or other proof-of-purchase systems.
     *
     * @param result the {@link SteamResult} returned by the callback
     * @param appID the appID
     * @param keyLength the keyLength
     * @param key the key
     */
    void onSteamAppsAppProofOfPurchaseKeyResponse(SteamResult result, int appID, int keyLength, String key);
}
