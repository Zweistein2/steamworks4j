package com.codedisaster.steamworks.inventory;

import com.codedisaster.steamworks.SteamNativeHandle;

public class SteamInventoryUpdateHandle extends SteamNativeHandle {

    public static final SteamInventoryUpdateHandle INVALID = new SteamInventoryUpdateHandle(0xffffffffffffffffL);

    SteamInventoryUpdateHandle(final long handle) {
        super(handle);
    }

    public boolean isValid() {
        return handle != INVALID.handle;
    }
}
