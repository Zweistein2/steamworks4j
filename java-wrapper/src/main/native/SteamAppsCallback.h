#pragma once

#include "SteamCallbackAdapter.h"
#include <steam_api.h>

class SteamAppsCallback : public SteamCallbackAdapter {

public:
	SteamAppsCallback(JNIEnv* env, jobject callback);
	~SteamAppsCallback();

	STEAM_CALLBACK(SteamAppsCallback, onSteamAppsDlcInstalled, DlcInstalled_t, m_CallbackSteamAppsDlcInstalled);
	STEAM_CALLBACK(SteamAppsCallback, onSteamAppsNewUrlLaunchParameters, NewUrlLaunchParameters_t, m_CallbackSteamAppsNewUrlLaunchParameters);
	STEAM_CALLBACK(SteamAppsCallback, onSteamAppsTimedTrialStatus, TimedTrialStatus_t, m_CallbackSteamAppsTimedTrialStatus);
	STEAM_CALLBACK(SteamAppsCallback, onSteamAppsRegisterActivationCodeResponse, RegisterActivationCodeResponse_t, m_CallbackSteamAppsRegisterActivationCodeResponse);
	STEAM_CALLBACK(SteamAppsCallback, onSteamAppsAppProofOfPurchaseKeyResponse, AppProofOfPurchaseKeyResponse_t, m_CallbackSteamAppsAppProofOfPurchaseKeyResponse);

	void onSteamAppsFileDetailsResult(FileDetailsResult_t* callback, bool error);
	CCallResult<SteamAppsCallback, FileDetailsResult_t> onSteamAppsFileDetailsResultCall;
};