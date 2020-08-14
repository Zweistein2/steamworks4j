package com.codedisaster.steamworks.inventory;

import com.codedisaster.steamworks.SteamNativeIntHandle;

import java.util.*;
import java.util.stream.Collectors;

public class SteamInventoryHandle extends SteamNativeIntHandle {
    public static final int INVALID_VALUE = -1;

    public static final SteamInventoryHandle INVALID = new SteamInventoryHandle(INVALID_VALUE);

    SteamInventoryHandle(final int handle) {
        super(handle);
    }

    public static List<SteamInventoryHandle> mapToHandles(final int[] handlesAsInt) {
        return Arrays.stream(handlesAsInt).filter(value -> value != INVALID_VALUE).boxed().map(SteamInventoryHandle::new).collect(Collectors.toList());
    }
}
