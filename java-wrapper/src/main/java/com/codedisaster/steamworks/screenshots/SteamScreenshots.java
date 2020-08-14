package com.codedisaster.steamworks.screenshots;

import com.codedisaster.steamworks.SteamAPI;
import com.codedisaster.steamworks.SteamID;
import com.codedisaster.steamworks.SteamInterface;
import com.codedisaster.steamworks.SteamPublishedFileID;

import java.nio.ByteBuffer;

public class SteamScreenshots extends SteamInterface {

	public SteamScreenshots(final SteamScreenshotsCallback callback) {
		super(SteamAPI.getSteamScreenshotsPointer(), createCallback(new SteamScreenshotsCallbackAdapter(callback)));
	}

	public SteamScreenshotHandle writeScreenshot(final ByteBuffer rgb, final int width, final int height) {
		return new SteamScreenshotHandle(writeScreenshot(pointer, rgb, rgb.remaining(), width, height));
	}

	public SteamScreenshotHandle addScreenshotToLibrary(final String file, final String thumbnail, final int width, final int height) {
		return new SteamScreenshotHandle(addScreenshotToLibrary(pointer, file, thumbnail, width, height));
	}

	public void triggerScreenshot() {
		triggerScreenshot(pointer);
	}

	public void hookScreenshots(final boolean hook) {
		hookScreenshots(pointer, hook);
	}

	public boolean setLocation(final SteamScreenshotHandle screenshot, final String location) {
		return setLocation(pointer, screenshot.handle, location);
	}

	public boolean tagUser(final SteamScreenshotHandle screenshot, final SteamID steamID) {
		return tagUser(pointer, screenshot.handle, steamID.handle);
	}

	public boolean tagPublishedFile(final SteamScreenshotHandle screenshot, final SteamPublishedFileID publishedFileID) {
		return tagPublishedFile(pointer, screenshot.handle, publishedFileID.handle);
	}

	public boolean isScreenshotsHooked() {
		return isScreenshotsHooked(pointer);
	}

	// @off

	/*JNI
		#include "SteamScreenshotsCallback.h"
	*/

	private static native long createCallback(SteamScreenshotsCallbackAdapter javaCallback); /*
		return (intp) new SteamScreenshotsCallback(env, javaCallback);
	*/

	private static native int writeScreenshot(long pointer, ByteBuffer imageData,
											  int imageSize, int width, int height); /*

		ISteamScreenshots* screenshots = (ISteamScreenshots*) pointer;
		return screenshots->WriteScreenshot(imageData, imageSize, width, height);
	*/

	private static native int addScreenshotToLibrary(long pointer, String file,
													 String thumbnail, int width, int height); /*

		ISteamScreenshots* screenshots = (ISteamScreenshots*) pointer;
		return screenshots->AddScreenshotToLibrary(file, thumbnail, width, height);
	*/

	private static native void triggerScreenshot(long pointer); /*
		ISteamScreenshots* screenshots = (ISteamScreenshots*) pointer;
		screenshots->TriggerScreenshot();
	*/

	private static native void hookScreenshots(long pointer, boolean hook); /*
		ISteamScreenshots* screenshots = (ISteamScreenshots*) pointer;
		screenshots->HookScreenshots(hook);
	*/

	private static native boolean setLocation(long pointer, int screenshot, String location); /*
		ISteamScreenshots* screenshots = (ISteamScreenshots*) pointer;
		return screenshots->SetLocation(screenshot, location);
	*/

	private static native boolean tagUser(long pointer, int screenshot, long steamID); /*
		ISteamScreenshots* screenshots = (ISteamScreenshots*) pointer;
		return screenshots->TagUser(screenshot, (uint64) steamID);
	*/

	private static native boolean tagPublishedFile(long pointer, int screenshot, long publishedFileID); /*
		ISteamScreenshots* screenshots = (ISteamScreenshots*) pointer;
		return screenshots->TagPublishedFile(screenshot, publishedFileID);
	*/

	private static native boolean isScreenshotsHooked(long pointer); /*
		ISteamScreenshots* screenshots = (ISteamScreenshots*) pointer;
		return screenshots->IsScreenshotsHooked();
	*/

}
