package com.codedisaster.steamworks.test;

import com.codedisaster.steamworks.*;
import com.codedisaster.steamworks.gameserver.SteamGameServerAPI;
import com.codedisaster.steamworks.utils.SteamAPIWarningMessageHook;
import com.codedisaster.steamworks.utils.SteamUtils;
import com.codedisaster.steamworks.utils.SteamUtilsCallback;

import java.util.Scanner;

public abstract class SteamTestApp {

	protected SteamUtils clientUtils;

	protected static final int MS_PER_TICK = 1000 / 15;

	private final SteamAPIWarningMessageHook clMessageHook = (severity, message) -> System.err.println("[client debug message] (" + severity + ") " + message);

	private final SteamUtilsCallback clUtilsCallback = () -> System.err.println("Steam client requested to shut down!");

	private class InputHandler implements Runnable {

		private volatile boolean alive;
		private final Thread mainThread;
		private final Scanner scanner;

		public InputHandler(final Thread mainThread) {
			this.alive = true;
			this.mainThread = mainThread;

			this.scanner = new Scanner(System.in, "UTF-8");
			scanner.useDelimiter("[\r\n\t]");
		}

		@Override
		public void run() {
			try {
				while (alive && mainThread.isAlive()) {
					if (scanner.hasNext()) {
						final String input = scanner.next();
						if (input.equals("quit") || input.equals("exit")) {
							alive = false;
						} else {
							try {
								processInput(input);
							} catch (final NumberFormatException e) {
								e.printStackTrace();
							}
						}
					}
				}
			} catch (final SteamException e) {
				e.printStackTrace();
			}
		}

		public boolean alive() {
			return alive;
		}
	}

	protected abstract void registerInterfaces() throws SteamException;

	protected abstract void unregisterInterfaces() throws SteamException;

	protected abstract void processUpdate() throws SteamException;

	protected abstract void processInput(String input) throws SteamException;

	private boolean runAsClient(@SuppressWarnings("unused") final String[] arguments) throws SteamException {

		System.out.println("Load native libraries ...");

		SteamAPI.loadLibraries();

		System.out.println("Initialise Steam client API ...");

		if (!SteamAPI.init()) {
			SteamAPI.printDebugInfo(System.err);
			return false;
		}

		SteamAPI.printDebugInfo(System.out);

		registerInterfaces();

		clientUtils = new SteamUtils(clUtilsCallback);
		clientUtils.setWarningMessageHook(clMessageHook);

		// doesn't make much sense here, as normally you would call this before
		// SteamAPI.init() with your (kn)own app ID
		if (SteamAPI.restartAppIfNecessary(clientUtils.getAppID())) {
			System.out.println("SteamAPI_RestartAppIfNecessary returned 'false'");
		}

		final InputHandler inputHandler = new InputHandler(Thread.currentThread());

		final Thread inputThread = new Thread(inputHandler);
		inputThread.start();

		while (inputHandler.alive() && SteamAPI.isSteamRunning()) {

			SteamAPI.runCallbacks();

			processUpdate();

			try {
				// sleep a little (Steam says it should poll at least 15 times/second)
				Thread.sleep(MS_PER_TICK);
			} catch (final InterruptedException e) {
				// ignore
			}
		}

		System.out.println("Shutting down Steam client API ...");

		try {
			inputThread.join();
		} catch (final InterruptedException e) {
			throw new SteamException(e);
		}

		clientUtils.dispose();

		unregisterInterfaces();

		SteamAPI.shutdown();

		return true;
	}

	protected void clientMain(final String[] arguments) {

		// development mode, read Steamworks libraries from ./sdk folder
		System.setProperty("com.codedisaster.steamworks.Debug", "true");

		try {

			if (!runAsClient(arguments)) {
				System.exit(-1);
			}

			System.out.println("Bye!");

		} catch (final Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}

	private boolean runAsGameServer(final String[] arguments) throws SteamException {

		boolean dedicated = false;

		for (final String arg : arguments) {
			if(arg.equals("--dedicated")) {
				dedicated = true;
				break;
			}
		}

		System.out.println("Load native libraries ...");

		SteamGameServerAPI.loadLibraries();

		if (!dedicated) {

			System.out.println("Initialise Steam client API ...");

			if (!SteamAPI.init()) {
				SteamAPI.printDebugInfo(System.err);
				return false;
			}
		}

		System.out.println("Initialise Steam GameServer API ...");

		if (!SteamGameServerAPI.init((127 << 24) + 1, (short) 27015, (short) 27016, (short) 27017,
				SteamGameServerAPI.ServerMode.NoAuthentication, "0.0.1")) {
			System.err.println("SteamGameServerAPI.init() failed");
			return false;
		}

		registerInterfaces();

		final InputHandler inputHandler = new InputHandler(Thread.currentThread());

		final Thread inputThread = new Thread(inputHandler);
		inputThread.start();

		while (inputHandler.alive()) {

			if (!dedicated) {
				SteamAPI.runCallbacks();
			}

			SteamGameServerAPI.runCallbacks();

			processUpdate();

			try {
				// sleep a little (Steam says it should poll at least 15 times/second)
				Thread.sleep(MS_PER_TICK);
			} catch (final InterruptedException e) {
				// ignore
			}
		}

		System.out.println("Shutting down Steam GameServer API ...");

		try {
			inputThread.join();
		} catch (final InterruptedException e) {
			throw new SteamException(e);
		}

		unregisterInterfaces();

		SteamGameServerAPI.shutdown();

		if (!dedicated) {
			System.out.println("Shutting down Steam client API ...");
			SteamAPI.shutdown();
		}

		return true;
	}

	protected void serverMain(final String[] arguments) {

		// development mode, read Steamworks libraries from ./sdk folder
		System.setProperty("com.codedisaster.steamworks.Debug", "true");

		try {

			if (!runAsGameServer(arguments)) {
				System.exit(-1);
			}

			System.out.println("Bye!");

		} catch (final Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}

}
