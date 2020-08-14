package com.codedisaster.steamworks;

public class SteamAuthTicket extends SteamNativeHandle {

	public static final long AuthTicketInvalid = 0;

	public SteamAuthTicket(final long handle) {
		super(handle);
	}

	public boolean isValid() {
		return handle != AuthTicketInvalid;
	}

}
