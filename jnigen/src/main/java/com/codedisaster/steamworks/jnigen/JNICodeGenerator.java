package com.codedisaster.steamworks.jnigen;

public class JNICodeGenerator {

	public static void main(final String[] arguments) {
		try {
			new NativeCodeGenerator().generate(
					"java-wrapper/src/main/java",
					"java-wrapper/target/classes",
					"java-wrapper/src/main/native",
					new String[] { "**/*.java" },
					new String[] { "**/SteamSharedLibraryLoader.java" });

			new NativeCodeGenerator().generate(
					"server/src/main/java",
					"server/target/classes",
					"server/src/main/native",
					new String[] { "**/*Native.java" },
					null);

		} catch (final Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}

	}
}
