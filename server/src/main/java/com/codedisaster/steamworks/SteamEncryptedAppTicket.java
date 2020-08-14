package com.codedisaster.steamworks;

import java.nio.ByteBuffer;

public class SteamEncryptedAppTicket extends SteamInterface {

	public static final int SymmetricKeyLen = 32;

	public static void loadLibraries() throws SteamException {
		loadLibraries(null);
	}

	public static void loadLibraries(final String libraryPath) throws SteamException {

		if (libraryPath == null && SteamSharedLibraryLoader.DEBUG) {
			final String sdkPath = SteamSharedLibraryLoader.getSdkLibraryPath();
			SteamSharedLibraryLoader.loadLibrary("sdkencryptedappticket", sdkPath);
		} else {
			SteamSharedLibraryLoader.loadLibrary("sdkencryptedappticket", libraryPath);
		}

		SteamSharedLibraryLoader.loadLibrary("steamworks4j-encryptedappticket", libraryPath);
	}

	public SteamEncryptedAppTicket() {
		super(~0L);
	}

	public static boolean decryptTicket(final ByteBuffer ticketEncrypted, final ByteBuffer ticketDecrypted,
										final byte[] key, final int[] ticketDecryptedOutputSize) throws SteamException {

		checkBuffer(ticketEncrypted);
		checkBuffer(ticketDecrypted);
		checkArray(key, SymmetricKeyLen);

		return SteamEncryptedAppTicketNative.decryptTicket(
				ticketEncrypted, ticketEncrypted.position(), ticketEncrypted.remaining(),
				ticketDecrypted, ticketDecrypted.position(), ticketDecrypted.remaining(),
				key, SymmetricKeyLen, ticketDecryptedOutputSize);
	}

	public static boolean isTicketForApp(final ByteBuffer ticketDecrypted, final int appID) throws SteamException {

		checkBuffer(ticketDecrypted);

		return SteamEncryptedAppTicketNative.isTicketForApp(
				ticketDecrypted, ticketDecrypted.position(), ticketDecrypted.remaining(), appID);
	}

	public static int getTicketIssueTime(final ByteBuffer ticketDecrypted) throws SteamException {

		checkBuffer(ticketDecrypted);

		return SteamEncryptedAppTicketNative.getTicketIssueTime(
				ticketDecrypted, ticketDecrypted.position(), ticketDecrypted.remaining());
	}

	public static SteamID getTicketSteamID(final ByteBuffer ticketDecrypted) throws SteamException {

		checkBuffer(ticketDecrypted);

		return SteamID.createFromNativeHandle(SteamEncryptedAppTicketNative.getTicketSteamID(
				ticketDecrypted, ticketDecrypted.position(), ticketDecrypted.remaining()));
	}

	public static int getTicketAppID(final ByteBuffer ticketDecrypted) throws SteamException {

		checkBuffer(ticketDecrypted);

		return SteamEncryptedAppTicketNative.getTicketAppID(
				ticketDecrypted, ticketDecrypted.position(), ticketDecrypted.remaining());
	}

	public static boolean userOwnsAppInTicket(final ByteBuffer ticketDecrypted, final int appID) throws SteamException {

		checkBuffer(ticketDecrypted);

		return SteamEncryptedAppTicketNative.userOwnsAppInTicket(
				ticketDecrypted, ticketDecrypted.position(), ticketDecrypted.remaining(), appID);
	}

	public static boolean userIsVacBanned(final ByteBuffer ticketDecrypted) throws SteamException {

		checkBuffer(ticketDecrypted);

		return SteamEncryptedAppTicketNative.userIsVacBanned(
				ticketDecrypted, ticketDecrypted.position(), ticketDecrypted.remaining());
	}

	public static int getUserVariableData(final ByteBuffer ticketDecrypted, final ByteBuffer userData) throws SteamException {

		checkBuffer(ticketDecrypted);
		checkBuffer(userData);

		return SteamEncryptedAppTicketNative.getUserVariableData(
				ticketDecrypted, ticketDecrypted.position(), ticketDecrypted.remaining(),
				userData, userData.position(), userData.remaining());
	}

}
