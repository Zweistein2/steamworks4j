/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class com_codedisaster_steamworks_SteamUserStats */

#ifndef _Included_com_codedisaster_steamworks_SteamUserStats
#define _Included_com_codedisaster_steamworks_SteamUserStats
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     com_codedisaster_steamworks_SteamUserStats
 * Method:    registerCallback
 * Signature: (Lcom/codedisaster/steamworks/SteamUserStatsCallback;)Z
 */
JNIEXPORT jboolean JNICALL Java_com_codedisaster_steamworks_SteamUserStats_registerCallback
  (JNIEnv *, jclass, jobject);

/*
 * Class:     com_codedisaster_steamworks_SteamUserStats
 * Method:    requestCurrentStats
 * Signature: (J)Z
 */
JNIEXPORT jboolean JNICALL Java_com_codedisaster_steamworks_SteamUserStats_requestCurrentStats
  (JNIEnv *, jclass, jlong);

/*
 * Class:     com_codedisaster_steamworks_SteamUserStats
 * Method:    getStat
 * Signature: (JLjava/lang/String;[I)Z
 */
JNIEXPORT jboolean JNICALL Java_com_codedisaster_steamworks_SteamUserStats_getStat__JLjava_lang_String_2_3I
  (JNIEnv *, jclass, jlong, jstring, jintArray);

/*
 * Class:     com_codedisaster_steamworks_SteamUserStats
 * Method:    setStat
 * Signature: (JLjava/lang/String;I)Z
 */
JNIEXPORT jboolean JNICALL Java_com_codedisaster_steamworks_SteamUserStats_setStat__JLjava_lang_String_2I
  (JNIEnv *, jclass, jlong, jstring, jint);

/*
 * Class:     com_codedisaster_steamworks_SteamUserStats
 * Method:    getStat
 * Signature: (JLjava/lang/String;[F)Z
 */
JNIEXPORT jboolean JNICALL Java_com_codedisaster_steamworks_SteamUserStats_getStat__JLjava_lang_String_2_3F
  (JNIEnv *, jclass, jlong, jstring, jfloatArray);

/*
 * Class:     com_codedisaster_steamworks_SteamUserStats
 * Method:    setStat
 * Signature: (JLjava/lang/String;F)Z
 */
JNIEXPORT jboolean JNICALL Java_com_codedisaster_steamworks_SteamUserStats_setStat__JLjava_lang_String_2F
  (JNIEnv *, jclass, jlong, jstring, jfloat);

/*
 * Class:     com_codedisaster_steamworks_SteamUserStats
 * Method:    getAchievement
 * Signature: (JLjava/lang/String;[Z)Z
 */
JNIEXPORT jboolean JNICALL Java_com_codedisaster_steamworks_SteamUserStats_getAchievement
  (JNIEnv *, jclass, jlong, jstring, jbooleanArray);

/*
 * Class:     com_codedisaster_steamworks_SteamUserStats
 * Method:    setAchievement
 * Signature: (JLjava/lang/String;)Z
 */
JNIEXPORT jboolean JNICALL Java_com_codedisaster_steamworks_SteamUserStats_setAchievement
  (JNIEnv *, jclass, jlong, jstring);

/*
 * Class:     com_codedisaster_steamworks_SteamUserStats
 * Method:    clearAchievement
 * Signature: (JLjava/lang/String;)Z
 */
JNIEXPORT jboolean JNICALL Java_com_codedisaster_steamworks_SteamUserStats_clearAchievement
  (JNIEnv *, jclass, jlong, jstring);

/*
 * Class:     com_codedisaster_steamworks_SteamUserStats
 * Method:    storeStats
 * Signature: (J)Z
 */
JNIEXPORT jboolean JNICALL Java_com_codedisaster_steamworks_SteamUserStats_storeStats
  (JNIEnv *, jclass, jlong);

/*
 * Class:     com_codedisaster_steamworks_SteamUserStats
 * Method:    indicateAchievementProgress
 * Signature: (JLjava/lang/String;II)Z
 */
JNIEXPORT jboolean JNICALL Java_com_codedisaster_steamworks_SteamUserStats_indicateAchievementProgress
  (JNIEnv *, jclass, jlong, jstring, jint, jint);

/*
 * Class:     com_codedisaster_steamworks_SteamUserStats
 * Method:    getNumAchievements
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_com_codedisaster_steamworks_SteamUserStats_getNumAchievements
  (JNIEnv *, jclass, jlong);

/*
 * Class:     com_codedisaster_steamworks_SteamUserStats
 * Method:    getAchievementName
 * Signature: (JI)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_codedisaster_steamworks_SteamUserStats_getAchievementName
  (JNIEnv *, jclass, jlong, jint);

/*
 * Class:     com_codedisaster_steamworks_SteamUserStats
 * Method:    resetAllStats
 * Signature: (JZ)Z
 */
JNIEXPORT jboolean JNICALL Java_com_codedisaster_steamworks_SteamUserStats_resetAllStats
  (JNIEnv *, jclass, jlong, jboolean);

#ifdef __cplusplus
}
#endif
#endif