package com.codedisaster.steamworks.user;

import com.codedisaster.steamworks.*;

@SuppressWarnings("unused")
public class SteamUserCallbackAdapter extends SteamCallbackAdapter<SteamUserCallback> {

	public SteamUserCallbackAdapter(final SteamUserCallback callback) {
		super(callback);
	}

	void onAuthSessionTicket(final long authTicket, final int result) {
		callback.onAuthSessionTicket(new SteamAuthTicket(authTicket), SteamResult.byValue(result));
	}

	void onValidateAuthTicket(final long steamID, final int authSessionResponse, final long ownerSteamID) {
		callback.onValidateAuthTicket(SteamID.createFromNativeHandle(steamID),
                                      SteamAuth.AuthSessionResponse.byOrdinal(authSessionResponse), SteamID.createFromNativeHandle(ownerSteamID));
	}

	void onMicroTxnAuthorization(final int appID, final long orderID, final boolean authorized) {
		callback.onMicroTxnAuthorization(appID, orderID, authorized);
	}

	void onEncryptedAppTicket(final int result) {
		callback.onEncryptedAppTicket(SteamResult.byValue(result));
	}

}
