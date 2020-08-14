package com.codedisaster.steamworks.user;

import com.codedisaster.steamworks.SteamAuth;
import com.codedisaster.steamworks.SteamAuthTicket;
import com.codedisaster.steamworks.SteamID;
import com.codedisaster.steamworks.SteamResult;

public interface SteamUserCallback {

	void onAuthSessionTicket(SteamAuthTicket authTicket, SteamResult result);

	void onValidateAuthTicket(SteamID steamID,
                              SteamAuth.AuthSessionResponse authSessionResponse,
                              SteamID ownerSteamID);

	void onMicroTxnAuthorization(int appID, long orderID, boolean authorized);

	void onEncryptedAppTicket(SteamResult result);

}
