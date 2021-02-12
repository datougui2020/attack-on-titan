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
#include "include/so_protector.h"

#define   LOG_TAG    "cjf_defense_jni"
#define   LOGE(...)  __android_log_print(ANDROID_LOG_ERROR,LOG_TAG,__VA_ARGS__)

jboolean verify(JNIEnv *env, jobject caller, jobject contextObject) {
    LOGE("cjf main:verify");
    return false;
}

using namespace so_protector;

void module_init() {
    LOGE("cjf main module:init");
}

bool so_protector::entry() {
    LOGE("cjf main module:entry");
    return true;
}

void module_fini() {
    LOGE("cjf main module:fini");
}
