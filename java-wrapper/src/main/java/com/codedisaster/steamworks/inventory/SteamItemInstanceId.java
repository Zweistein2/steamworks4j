package com.codedisaster.steamworks.inventory;

import com.codedisaster.steamworks.SteamNativeHandle;

public class SteamItemInstanceId extends SteamNativeHandle {

    public static final SteamItemInstanceId INVALID = new SteamItemInstanceId(~0);

    public SteamItemInstanceId(final long handle) {
        super(handle);
    }

    public boolean isValid() {
        return handle != INVALID.handle;
    }
}
