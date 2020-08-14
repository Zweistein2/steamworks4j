package com.codedisaster.steamworks.utils;

public enum SteamUniverse {
	Invalid(0),
	Public(1),
	Beta(2),
	Internal(3),
	Dev(4);

	private final int value;
	private static final SteamUniverse[] values = values();

	SteamUniverse(final int value) {
		this.value = value;
	}

	static SteamUniverse byValue(final int value) {
		for (final SteamUniverse type : values) {
			if (type.value == value) {
				return type;
			}
		}
		return Invalid;
	}
}
