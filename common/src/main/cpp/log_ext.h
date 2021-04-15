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
#ifndef __SAMPLE_ANDROID_LOG_EXT_H__
#define __SAMPLE_ANDROID_LOG_EXT_H__

#include <android/log.h>

#define LOG_V(tag, ...) __android_log_print(ANDROID_LOG_VERBOSE, tag, __VA_ARGS__)
#define LOG_D(tag, ...) __android_log_print(ANDROID_LOG_DEBUG, tag, __VA_ARGS__)
#define LOG_I(tag, ...) __android_log_print(ANDROID_LOG_INFO, tag, __VA_ARGS__)
#define LOG_W(tag, ...) __android_log_print(ANDROID_LOG_WARN,tag, __VA_ARGS__)
#define LOG_E(tag, ...) __android_log_print(ANDROID_LOG_ERROR,tag, __VA_ARGS__)
#define LOG_F(tag, ...) __android_log_print(ANDROID_LOG_FATAL,tag, __VA_ARGS__)

#define ASSERT(tag, cond, ...) if (!(cond)) {__android_log_assert(#cond, tag, __VA_ARGS__);}
//内联函数必须放在 .h 文件中,如果内联函数比较短, 就直接放在 .h 中
#endif // __SAMPLE_ANDROID_LOG_EXT_H__
