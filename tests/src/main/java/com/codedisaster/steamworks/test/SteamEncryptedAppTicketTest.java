package com.codedisaster.steamworks.test;

import com.codedisaster.steamworks.*;
import com.codedisaster.steamworks.user.SteamUser;
import com.codedisaster.steamworks.user.SteamUserCallback;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Expects a binary file <i>encryptedappticket.key</i> in the working directory. The file should contain
 * exactly {@link SteamEncryptedAppTicket#SymmetricKeyLen} bytes, which match the private key for the
 * AppID specified in <i>steam_appid.txt</i>.
 */
public class SteamEncryptedAppTicketTest extends SteamTestApp {

	private SteamUser user;
	private SteamEncryptedAppTicket encryptedAppTicket;

	private final ByteBuffer ticketEncrypt = ByteBuffer.allocateDirect(1024);
	private final ByteBuffer ticketDecrypt = ByteBuffer.allocateDirect(1024);

	private static final byte[] privateKey = new byte[SteamEncryptedAppTicket.SymmetricKeyLen];

	private final SteamUserCallback userCallback = new SteamUserCallback() {
		@Override
		public void onAuthSessionTicket(final SteamAuthTicket authTicket, final SteamResult result) {

		}

		@Override
		public void onValidateAuthTicket(final SteamID steamID,
										 final SteamAuth.AuthSessionResponse authSessionResponse,
										 final SteamID ownerSteamID) {

		}

		@Override
		public void onMicroTxnAuthorization(final int appID, final long orderID, final boolean authorized) {

		}

		@Override
		public void onEncryptedAppTicket(final SteamResult result) {
			System.out.println("app ticket encrypted: " + result.name());

			try {
				final int[] ticketSize = new int[1];
				ticketEncrypt.clear();
				ticketDecrypt.clear();

				if (user.getEncryptedAppTicket(ticketEncrypt, ticketSize)) {
					System.out.println("encrypted app ticket size: " + ticketSize[0]);
					ticketEncrypt.limit(ticketSize[0]);

					if (SteamEncryptedAppTicket.decryptTicket(ticketEncrypt, ticketDecrypt, privateKey, ticketSize)) {
						System.out.println("decrypted app ticket size: " + ticketSize[0]);
						ticketDecrypt.limit(ticketSize[0]);

						final ByteBuffer dataIncluded = ByteBuffer.allocateDirect(100);
						final int dataLength = SteamEncryptedAppTicket.getUserVariableData(ticketDecrypt, dataIncluded);
						if (dataLength > 0) {
							dataIncluded.limit(dataLength);
							final byte[] userData = new byte[dataLength];
							dataIncluded.get(userData);
							final String userString = new String(userData, Charset.defaultCharset());
							System.out.println("included user data: " + dataLength + " bytes, '" + userString + "'");
						}
					} else {
						System.err.println("failed to decrypt app ticket");
					}
				} else {
					System.err.println("failed to get encrypted app ticket");
				}
			} catch (final SteamException e) {
				e.printStackTrace();
			}
		}
	};

	@Override
	protected void registerInterfaces() throws SteamException {
		SteamEncryptedAppTicket.loadLibraries();
		user = new SteamUser(userCallback);
		encryptedAppTicket = new SteamEncryptedAppTicket();
	}

	@Override
	protected void unregisterInterfaces() throws SteamException {
		user.dispose();
		encryptedAppTicket.dispose();
	}

	@Override
	protected void processUpdate() throws SteamException {

	}

	@Override
	protected void processInput(final String input) throws SteamException {
		if (input.equals("encrypt")) {
			final byte[] ident = "Hello World!".getBytes(Charset.defaultCharset());
			final ByteBuffer dataToInclude = ByteBuffer.allocateDirect(ident.length);
			dataToInclude.put(ident);
			dataToInclude.flip();
			user.requestEncryptedAppTicket(dataToInclude);
		}
	}

	public static void main(final String[] arguments) {

		try {
			final byte[] key = Files.readAllBytes(Paths.get("encryptedappticket.key"));
			System.arraycopy(key, 0, privateKey, 0, Math.min(key.length, privateKey.length));
		} catch (final IOException e) {
			System.err.println("failed to read encrypted app ticket key");
		}

		new SteamEncryptedAppTicketTest().clientMain(arguments);
	}

}
