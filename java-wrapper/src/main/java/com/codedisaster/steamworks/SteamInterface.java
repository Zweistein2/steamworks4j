package com.codedisaster.steamworks;

import java.nio.Buffer;

abstract public class SteamInterface extends SteamAPI {

	protected final long pointer;
	public long callback;

	public SteamInterface(final long pointer) {
		this(pointer, 0L);
	}

	protected SteamInterface(final long pointer, final long callback) {
		if (pointer == 0L) {
			throw new RuntimeException("Steam interface created with null pointer." +
							" Always check result of SteamAPI.init(), or with SteamAPI.isSteamRunning()!");
		}
		this.pointer = pointer;
		this.callback = callback;
	}

	public void dispose() {
		deleteCallback(callback);
	}

	static void checkBuffer(final Buffer buffer) throws SteamException {
		if (!buffer.isDirect()) {
			throw new SteamException("Direct buffer required.");
		}
	}

	static void checkArray(final byte[] array, final int length) throws SteamException {
		if (array.length < length) {
			throw new SteamException("Array too small, " + array.length + " found but " + length + " expected.");
		}
	}

	/*JNI
		#include "SteamCallbackAdapter.h"
	*/

	protected static native void deleteCallback(long callback); /*
		delete (SteamCallbackAdapter*) callback;
	*/

}
