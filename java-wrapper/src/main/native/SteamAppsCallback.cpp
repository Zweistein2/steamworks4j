#include "SteamAppsCallback.h"

SteamAppsCallback::SteamAppsCallback(JNIEnv* env, jobject callback)
	: SteamCallbackAdapter(env, callback) {
}

SteamAppsCallback::~SteamAppsCallback() {

}

void SteamAppsCallback::onSteamAppsFileDetailsResult(FileDetailsResult_t* callback, bool error) {
	invokeCallback({
		callVoidMethod(env, "onSteamAppsFileDetailsResult", "(IJJ[B)V",
			(jint) callback->m_eResult, (jlong) callback->m_ulFileSize, (jbyteArray) callback->m_FileSHA, (jint) callback->m_unFlags);
	});
}
