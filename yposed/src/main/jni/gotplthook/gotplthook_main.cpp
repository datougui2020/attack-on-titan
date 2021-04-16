#include <jni.h>
#include "util/log_ext.h"
#include <linux/elf.h>
#include "fake_linker/bionic_linker.h"

const char *kTag = "plthook";
extern "C" {

JNIEXPORT void JNICALL
Java_com_jamesfchen_yposed_YPosedActivity_plthook_1init(JNIEnv *env, jobject thiz) {
    LOG_E(kTag, "plt hook init");
//    soinfo;
}
JNIEXPORT void JNICALL
Java_com_jamesfchen_yposed_YPosedActivity_gothook_1init(JNIEnv *env, jobject thiz) {
    LOG_E(kTag, "got hook init");

}

}
