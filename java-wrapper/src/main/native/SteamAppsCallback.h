#pragma once

#include "SteamCallbackAdapter.h"
#include <steam_api.h>

class SteamAppsCallback : public SteamCallbackAdapter {

public:
	SteamAppsCallback(JNIEnv* env, jobject callback);
	~SteamAppsCallback();

	void onSteamAppsFileDetailsResult(FileDetailsResult_t* callback, bool error);
	CCallResult<SteamAppsCallback, FileDetailsResult_t> onSteamAppsFileDetailsResultCall;
};