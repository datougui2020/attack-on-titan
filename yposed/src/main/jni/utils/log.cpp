//
// Created by jf.chen on 2021/2/5.
//

#define LOGV(tag, ...) __android_log_print(ANDROID_LOG_VERBOSE, tag, __VA_ARGS__)
#define LOGD(tag, ...) __android_log_print(ANDROID_LOG_DEBUG, tag, __VA_ARGS__)
#define LOGI(tag, ...) __android_log_print(ANDROID_LOG_INFO, tag, __VA_ARGS__)
#define LOGW(tag, ...) __android_log_print(ANDROID_LOG_WARN,tag, __VA_ARGS__)
#define LOGE(tag, ...) __android_log_print(ANDROID_LOG_ERROR,tag, __VA_ARGS__)
#define LOGF(tag, ...) __android_log_print(ANDROID_LOG_FATAL,tag, __VA_ARGS__)

#define ASSERT(tag, cond, ...) if (!(cond)) {__android_log_assert(#cond, tag, __VA_ARGS__);}