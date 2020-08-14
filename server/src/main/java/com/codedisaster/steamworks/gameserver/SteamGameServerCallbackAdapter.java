package com.codedisaster.steamworks.gameserver;

import com.codedisaster.steamworks.SteamAuth;
import com.codedisaster.steamworks.SteamCallbackAdapter;
import com.codedisaster.steamworks.SteamID;
import com.codedisaster.steamworks.SteamResult;

@SuppressWarnings("unused")
class SteamGameServerCallbackAdapter extends SteamCallbackAdapter<SteamGameServerCallback> {

	SteamGameServerCallbackAdapter(final SteamGameServerCallback callback) {
		super(callback);
	}

	void onValidateAuthTicketResponse(final long steamID, final int authSessionResponse, final long ownerSteamID) {
		callback.onValidateAuthTicketResponse(SteamID.createFromNativeHandle(steamID),
                                              SteamAuth.AuthSessionResponse.byOrdinal(authSessionResponse), SteamID.createFromNativeHandle(ownerSteamID));
	}

	void onSteamServersConnected() {
		callback.onSteamServersConnected();
	}

	void onSteamServerConnectFailure(final int result, final boolean stillRetrying) {
		callback.onSteamServerConnectFailure(SteamResult.byValue(result), stillRetrying);
	}

	void onSteamServersDisconnected(final int result) {
		callback.onSteamServersDisconnected(SteamResult.byValue(result));
	}

	void onClientApprove(final long steamID, final long ownerSteamID) {
		callback.onClientApprove(SteamID.createFromNativeHandle(steamID), SteamID.createFromNativeHandle(ownerSteamID));
	}

	void onClientDeny(final long steamID, final int denyReason, final String optionalText) {
		callback.onClientDeny(SteamID.createFromNativeHandle(steamID), SteamGameServer.DenyReason.byOrdinal(denyReason), optionalText);
	}

	void onClientKick(final long steamID, final int denyReason) {
		callback.onClientKick(SteamID.createFromNativeHandle(steamID), SteamGameServer.DenyReason.byOrdinal(denyReason));
	}

	void onClientGroupStatus(final long steamID, final long steamIDGroup, final boolean isMember, final boolean isOfficer) {
		callback.onClientGroupStatus(SteamID.createFromNativeHandle(steamID), SteamID.createFromNativeHandle(steamIDGroup), isMember, isOfficer);
	}

	void onAssociateWithClanResult(final int result) {
		callback.onAssociateWithClanResult(SteamResult.byValue(result));
	}

	void onComputeNewPlayerCompatibilityResult(final int result,
                                               final int playersThatDontLikeCandidate,
                                               final int playersThatCandidateDoesntLike,
                                               final int clanPlayersThatDontLikeCandidate,
                                               final long steamIDCandidate) {
		callback.onComputeNewPlayerCompatibilityResult(SteamResult.byValue(result), playersThatDontLikeCandidate,
				playersThatCandidateDoesntLike, clanPlayersThatDontLikeCandidate, SteamID.createFromNativeHandle(steamIDCandidate));
	}
}
