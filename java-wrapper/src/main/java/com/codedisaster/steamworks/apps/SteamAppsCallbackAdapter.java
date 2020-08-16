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

    void onSteamAppsDlcInstalled(final int appID) {
        callback.onSteamAppsDlcInstalled(appID);
    }

    void onSteamAppsNewUrlLaunchParameters() {
        callback.onSteamAppsNewUrlLaunchParameters();
    }

    void onSteamAppsTimedTrialStatus(final int appID, final boolean isOffline, final int secondsAllowed, final int secondsPlayed) {
        callback.onSteamAppsTimedTrialStatus(appID, isOffline, secondsAllowed, secondsPlayed);
    }

    void onSteamAppsRegisterActivationCodeResponse(final int result, final int packageRegistered) {
        callback.onSteamAppsRegisterActivationCodeResponse(RegisterActivationCodeResult.byOrdinal(result), packageRegistered);
    }

    void onSteamAppsAppProofOfPurchaseKeyResponse(final int result, final int appID, final int keyLength, final String key) {
        callback.onSteamAppsAppProofOfPurchaseKeyResponse(SteamResult.byValue(result), appID, keyLength, key);
    }
}
