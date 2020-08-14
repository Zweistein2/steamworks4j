package com.codedisaster.steamworks.gameserver;

import com.codedisaster.steamworks.*;

import java.nio.ByteBuffer;

public class SteamGameServer extends SteamInterface {

	public enum DenyReason {
		Invalid,
		InvalidVersion,
		Generic,
		NotLoggedOn,
		NoLicense,
		Cheater,
		LoggedInElseWhere,
		UnknownText,
		IncompatibleAnticheat,
		MemoryCorruption,
		IncompatibleSoftware,
		SteamConnectionLost,
		SteamConnectionError,
		SteamResponseTimedOut,
		SteamValidationStalled,
		SteamOwnerLeftGuestUser;

		private static final DenyReason[] values = values();

		static DenyReason byOrdinal(final int denyReason) {
			return values[denyReason];
		}
	}

	public SteamGameServer(final SteamGameServerCallback callback) {
		super(SteamGameServerAPINative.getSteamGameServerPointer(),
				SteamGameServerNative.createCallback(new SteamGameServerCallbackAdapter(callback)));
	}

	public void setProduct(final String product) {
		SteamGameServerNative.setProduct(pointer, product);
	}

	public void setGameDescription(final String gameDescription) {
		SteamGameServerNative.setGameDescription(pointer, gameDescription);
	}

	public void setModDir(final String modDir) {
		SteamGameServerNative.setModDir(pointer, modDir);
	}

	public void setDedicatedServer(final boolean dedicated) {
		SteamGameServerNative.setDedicatedServer(pointer, dedicated);
	}

	public void logOn(final String token) {
		SteamGameServerNative.logOn(pointer, token);
	}

	public void logOnAnonymous() {
		SteamGameServerNative.logOnAnonymous(pointer);
	}

	public void logOff() {
		SteamGameServerNative.logOff(pointer);
	}

	public boolean isLoggedOn() {
		return SteamGameServerNative.isLoggedOn(pointer);
	}

	public boolean isSecure() {
		return SteamGameServerNative.isSecure(pointer);
	}

	public SteamID getSteamID() {
		return SteamID.createFromNativeHandle(SteamGameServerNative.getSteamID(pointer));
	}

	public boolean wasRestartRequested() {
		return SteamGameServerNative.wasRestartRequested(pointer);
	}

	public void setMaxPlayerCount(final int playersMax) {
		SteamGameServerNative.setMaxPlayerCount(pointer, playersMax);
	}

	public void setBotPlayerCount(final int botplayers) {
		SteamGameServerNative.setBotPlayerCount(pointer, botplayers);
	}

	public void setServerName(final String serverName) {
		SteamGameServerNative.setServerName(pointer, serverName);
	}

	public void setMapName(final String mapName) {
		SteamGameServerNative.setMapName(pointer, mapName);
	}

	public void setPasswordProtected(final boolean passwordProtected) {
		SteamGameServerNative.setPasswordProtected(pointer, passwordProtected);
	}

	public void setSpectatorPort(final short spectatorPort) {
		SteamGameServerNative.setSpectatorPort(pointer, spectatorPort);
	}

	public void setSpectatorServerName(final String spectatorServerName) {
		SteamGameServerNative.setSpectatorServerName(pointer, spectatorServerName);
	}

	public void clearAllKeyValues() {
		SteamGameServerNative.clearAllKeyValues(pointer);
	}

	public void setKeyValue(final String key, final String value) {
		SteamGameServerNative.setKeyValue(pointer, key, value);
	}

	public void setGameTags(final String gameTags) {
		SteamGameServerNative.setGameTags(pointer, gameTags);
	}

	public void setGameData(final String gameData) {
		SteamGameServerNative.setGameData(pointer, gameData);
	}

	public void setRegion(final String region) {
		SteamGameServerNative.setRegion(pointer, region);
	}

	public boolean sendUserConnectAndAuthenticate(final int clientIP,
												  final ByteBuffer authBlob,
												  final SteamID steamIDUser) {

		final long[] ids = new long[1];

		if (SteamGameServerNative.sendUserConnectAndAuthenticate(
				pointer, clientIP, authBlob, authBlob.position(), authBlob.remaining(), ids)) {
			steamIDUser.handle = ids[0];
			return true;
		}

		return false;
	}

	public SteamID createUnauthenticatedUserConnection() {
		return SteamID.createFromNativeHandle(SteamGameServerNative.createUnauthenticatedUserConnection(pointer));
	}

	public void sendUserDisconnect(final SteamID steamIDUser) {
		SteamGameServerNative.sendUserDisconnect(pointer, steamIDUser.handle);
	}

	public boolean updateUserData(final SteamID steamIDUser, final String playerName, final int score) {
		return SteamGameServerNative.updateUserData(pointer, steamIDUser.handle, playerName, score);
	}

	public SteamAuthTicket getAuthSessionTicket(final ByteBuffer authTicket, final int[] sizeInBytes) throws SteamException {

		if (!authTicket.isDirect()) {
			throw new SteamException("Direct buffer required!");
		}

		final int ticket = SteamGameServerNative.getAuthSessionTicket(pointer, authTicket,
																	  authTicket.position(), authTicket.remaining(), sizeInBytes);

		if (ticket != SteamAuthTicket.AuthTicketInvalid) {
			// TODO: this doesn't match the rest of the API
			authTicket.limit(authTicket.position() + sizeInBytes[0]);
		}

		return new SteamAuthTicket(ticket);
	}

	public SteamAuth.BeginAuthSessionResult beginAuthSession(final ByteBuffer authTicket, final SteamID steamID) throws SteamException {

		if (!authTicket.isDirect()) {
			throw new SteamException("Direct buffer required!");
		}

		final int result = SteamGameServerNative.beginAuthSession(
				pointer, authTicket, authTicket.position(), authTicket.remaining(), steamID.handle);

		return SteamAuth.BeginAuthSessionResult.byOrdinal(result);
	}

	public void endAuthSession(final SteamID steamID) {
		SteamGameServerNative.endAuthSession(pointer, steamID.handle);
	}

	public void cancelAuthTicket(final SteamAuthTicket authTicket) {
		SteamGameServerNative.cancelAuthTicket(pointer, (int) authTicket.handle);
	}

	public SteamAuth.UserHasLicenseForAppResult userHasLicenseForApp(final SteamID steamID, final int appID) {
		return SteamAuth.UserHasLicenseForAppResult.byOrdinal(
				SteamGameServerNative.userHasLicenseForApp(pointer, steamID.handle, appID));
	}

	public boolean requestUserGroupStatus(final SteamID steamIDUser, final SteamID steamIDGroup) {
		return SteamGameServerNative.requestUserGroupStatus(pointer, steamIDUser.handle, steamIDGroup.handle);
	}

	public boolean handleIncomingPacket(final ByteBuffer data, final int srcIP, final short srcPort) {
		return SteamGameServerNative.handleIncomingPacket(
				pointer, data, data.position(), data.remaining(), srcIP, srcPort);
	}

	public int getNextOutgoingPacket(final ByteBuffer out, final int[] netAdr, final short[] port) {
		// TODO: improve return values (buffers? dedicated data type?)
		return SteamGameServerNative.getNextOutgoingPacket(
				pointer, out, out.position(), out.remaining(), netAdr, port);
	}

	public void enableHeartbeats(final boolean active) {
		SteamGameServerNative.enableHeartbeats(pointer, active);
	}

	public void setHeartbeatInterval(final int heartbeatInterval) {
		SteamGameServerNative.setHeartbeatInterval(pointer, heartbeatInterval);
	}

	public void forceHeartbeat() {
		SteamGameServerNative.forceHeartbeat(pointer);
	}

	public SteamAPICall associateWithClan(final SteamID steamIDClan) {
		return new SteamAPICall(SteamGameServerNative.associateWithClan(pointer, steamIDClan.handle));
	}

	public SteamAPICall computeNewPlayerCompatibility(final SteamID steamIDNewPlayer) {
		return new SteamAPICall(SteamGameServerNative.computeNewPlayerCompatibility(pointer, steamIDNewPlayer.handle));
	}

}
