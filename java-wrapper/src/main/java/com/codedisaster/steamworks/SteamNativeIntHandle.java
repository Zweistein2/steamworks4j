package com.codedisaster.steamworks;

public abstract class SteamNativeIntHandle {

	public final int handle;

	protected SteamNativeIntHandle(final int handle) {
		this.handle = handle;
	}

	/**
	 * Returns the unsigned 32-bit value wrapped by this handle, cast to Java's signed int.
	 */
	public static <T extends SteamNativeIntHandle> int getNativeHandle(final T handle) {
		return handle.handle;
	}

	@Override
	public int hashCode() {
		return Integer.valueOf(handle).hashCode();
	}

	@Override
	public boolean equals(final Object other) {
		if (other instanceof SteamNativeIntHandle) {
			return handle == ((SteamNativeIntHandle) other).handle;
		}
		return false;
	}

	@Override
	public String toString() {
		return Integer.toHexString(handle);
	}

}
