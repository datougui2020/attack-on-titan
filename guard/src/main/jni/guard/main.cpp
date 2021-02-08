#include <string.h>
#include <jni.h>
#include <string.h>
#include <inttypes.h>
#include <pthread.h>
#include <android/log.h>
#include <assert.h>
#include <time.h>
#include <sched.h>
#include <cstdio>
#include <cstdlib>
#include <unistd.h>
#define   LOG_TAG    "cjf_defense_jni"
#define   LOGE(...)  __android_log_print(ANDROID_LOG_ERROR,LOG_TAG,__VA_ARGS__)
jboolean verify(JNIEnv *env, jobject caller, jobject contextObject) {
    LOGE("cjf main:verify");
    return false;
}

