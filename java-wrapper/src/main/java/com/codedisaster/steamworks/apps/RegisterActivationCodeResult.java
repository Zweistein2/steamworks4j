package com.codedisaster.steamworks.apps;

public enum RegisterActivationCodeResult {
    OK,
    Fail,
    AlreadyRegistered,
    Timeout,
    AlreadyOwned;

    private static final RegisterActivationCodeResult[] values = values();

    static RegisterActivationCodeResult byOrdinal(final int value) {
        return values[value];
    }
}
