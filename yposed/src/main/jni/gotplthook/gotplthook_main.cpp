#include <jni.h>
#include "log_ext.h"
#include <linux/elf.h>

const char *kTag = "plthook";
extern "C" {

JNIEXPORT void JNICALL
Java_com_jamesfchen_yposed_YPosedActivity_plthook_1init(JNIEnv *env, jobject thiz) {
    LOG_E(kTag, "plt hook init");
}
JNIEXPORT void JNICALL
Java_com_jamesfchen_yposed_YPosedActivity_gothook_1init(JNIEnv *env, jobject thiz) {
    LOG_E(kTag, "got hook init");
}

}
