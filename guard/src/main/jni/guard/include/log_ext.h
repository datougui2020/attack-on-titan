/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
#ifndef __SAMPLE_ANDROID_DEBUG_H__
#define __SAMPLE_ANDROID_DEBUG_H__

#include <android/log.h>

#define LOGV(tag, ...) __android_log_print(ANDROID_LOG_VERBOSE, tag, __VA_ARGS__)
#define LOGD(tag, ...) __android_log_print(ANDROID_LOG_DEBUG, tag, __VA_ARGS__)
//#define LOGI(tag, ...) __android_log_print(ANDROID_LOG_INFO, tag, __VA_ARGS__)
//#define LOGW(tag, ...) __android_log_print(ANDROID_LOG_WARN,tag, __VA_ARGS__)
//#define LOGE(tag, ...) __android_log_print(ANDROID_LOG_ERROR,tag, __VA_ARGS__)
#define LOGF(tag, ...) __android_log_print(ANDROID_LOG_FATAL,tag, __VA_ARGS__)

//#define ASSERT(tag, cond, ...) if (!(cond)) {__android_log_assert(#cond, tag, __VA_ARGS__);}
//内联函数必须放在 .h 文件中,如果内联函数比较短, 就直接放在 .h 中
inline int LogV(const char *tag, const char *fmt, ...) {
    va_list args;
    va_start (args, fmt);
    return __android_log_print(ANDROID_LOG_VERBOSE, tag,fmt, args);
    va_end (args);
}

inline int LogD(const char *tag, const char *fmt, ...) {
    va_list args;
    va_start (args, fmt);
    return __android_log_print(ANDROID_LOG_DEBUG, tag,fmt, args);
    va_end (args);
}
inline int LogI(const char *tag, const char *fmt, ...) {
    va_list args;
    va_start (args, fmt);
    return __android_log_print(ANDROID_LOG_INFO, tag,fmt, args);
    va_end (args);
}

inline int LogW(const char *tag, const char *fmt, ...) {
    va_list args;
    va_start (args, fmt);
    return __android_log_print(ANDROID_LOG_WARN, tag,fmt, args);
    va_end (args);
}
inline int LogE(const char *tag, const char *fmt, ...) {
    va_list args;
    va_start (args, fmt);
    return __android_log_print(ANDROID_LOG_ERROR, tag,fmt, args);
    va_end (args);
}
inline int LogF(const char *tag, const char *fmt, ...) {
    va_list args;
    va_start (args, fmt);
    return __android_log_print(ANDROID_LOG_FATAL, tag,fmt, args);
    va_end (args);
}
#endif // __SAMPLE_ANDROID_DEBUG_H__
