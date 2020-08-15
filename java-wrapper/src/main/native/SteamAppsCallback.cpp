#include "SteamAppsCallback.h"

SteamAppsCallback::SteamAppsCallback(JNIEnv* env, jobject callback)
	: SteamCallbackAdapter(env, callback)
	, m_CallbackSteamAppsDlcInstalled(this, &SteamAppsCallback::onSteamAppsDlcInstalled)
	, m_CallbackSteamAppsNewUrlLaunchParameters(this, &SteamAppsCallback::onSteamAppsNewUrlLaunchParameters)
	, m_CallbackSteamAppsTimedTrialStatus(this, &SteamAppsCallback::onSteamAppsTimedTrialStatus)
	, m_CallbackSteamAppsRegisterActivationCodeResponse(this, &SteamAppsCallback::onSteamAppsRegisterActivationCodeResponse)
	, m_CallbackSteamAppsAppProofOfPurchaseKeyResponse(this, &SteamAppsCallback::onSteamAppsAppProofOfPurchaseKeyResponse) {
}

SteamAppsCallback::~SteamAppsCallback() {

}

void SteamAppsCallback::onSteamAppsDlcInstalled(DlcInstalled_t* callback) {
	invokeCallback({
		callVoidMethod(env, "onSteamAppsDlcInstalled", "(I)V",
			(jint) callback->m_nAppID);
	});
}

void SteamAppsCallback::onSteamAppsNewUrlLaunchParameters(NewUrlLaunchParameters_t* callback) {
	invokeCallback({
		callVoidMethod(env, "onSteamAppsNewUrlLaunchParameters", "()V");
	});
}

void SteamAppsCallback::onSteamAppsTimedTrialStatus(TimedTrialStatus_t* callback) {
	invokeCallback({
		callVoidMethod(env, "onSteamAppsTimedTrialStatus", "(IZII)V",
            (jint) callback->m_unAppID, (jboolean) callback->m_bIsOffline, (jint) callback->m_unSecondsAllowed, (jint) callback->m_unSecondsPlayed);
	});
}

void SteamAppsCallback::onSteamAppsRegisterActivationCodeResponse(RegisterActivationCodeResponse_t* callback) {
	invokeCallback({
		callVoidMethod(env, "onSteamAppsRegisterActivationCodeResponse", "(II)V",
            (jint) callback->m_eResult, (jint) callback->m_unPackageRegistered);
	});
}

void SteamAppsCallback::onSteamAppsAppProofOfPurchaseKeyResponse(AppProofOfPurchaseKeyResponse_t* callback) {
	invokeCallback({
		callVoidMethod(env, "onSteamAppsAppProofOfPurchaseKeyResponse", "(IIILjava/lang/String;)V",
            (jint) callback->m_eResult, (jint) callback->m_nAppID, (jint) callback->m_cchKeyLength, env->NewStringUTF(callback->m_rgchKey));
	});
}

void SteamAppsCallback::onSteamAppsFileDetailsResult(FileDetailsResult_t* callback, bool error) {
	invokeCallback({
		callVoidMethod(env, "onSteamAppsFileDetailsResult", "(IJJ[B)V",
			(jint) callback->m_eResult, (jlong) callback->m_ulFileSize, (jbyteArray) callback->m_FileSHA, (jint) callback->m_unFlags);
	});
}

