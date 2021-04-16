cmake_minimum_required(VERSION 3.4.1)
set(COMMON_PROJECT ${PROJECT_SOURCE_DIR}/../common/src/main/cpp)
#==============================fake linker module======================================
aux_source_directory(${COMMON_PROJECT}/fake_linker SOURCE_DIR_FAKE_LINKER)
aux_source_directory(${COMMON_PROJECT}/fake_linker/build SOURCE_DIR_BUILD)
aux_source_directory(${COMMON_PROJECT}/fake_linker/sig_safe_write SOURCE_DIR_SIG_SAFE_WRITE)
aux_source_directory(${COMMON_PROJECT}/fake_linker/sigmux SOURCE_DIR_SIGMUX)
aux_source_directory(${COMMON_PROJECT}/fake_linker/lss SOURCE_DIR_LSS)
include_directories(${COMMON_PROJECT}/fake_linker/build)
include_directories(${COMMON_PROJECT}/fake_linker/sigmux)
include_directories(${COMMON_PROJECT}/fake_linker/sig_safe_write)
include_directories(${COMMON_PROJECT}/fake_linker/lss)
add_library(
        fake_linker
        STATIC
        ${SOURCE_DIR_FAKE_LINKER} ${SOURCE_DIR_SIG_SAFE_WRITE} ${SOURCE_DIR_SIGMUX} ${SOURCE_DIR_BUILD} ${SOURCE_DIR_LSS})
target_link_libraries(fake_linker)

#==============================util module======================================
aux_source_directory(${COMMON_PROJECT}/util SOURCE_DIR_UTIL)
add_library(
        util
        STATIC
        ${SOURCE_DIR_UTIL})
target_link_libraries(util log)
#==============================breakpad module======================================
aux_source_directory(${COMMON_PROJECT}/breakpad SOURCE_DIR_BREAKPAD)
aux_source_directory(${COMMON_PROJECT}/fb SOURCE_DIR_FB)
#include_directories(${COMMON_PROJECT}/fb)
add_library(
        breakpad
        STATIC
        ${SOURCE_DIR_FB}
        ${SOURCE_DIR_BREAKPAD})
target_link_libraries(breakpad log)
#==============================trampoline module======================================
aux_source_directory(trampoline SOURCE_DIR_TRAMPOLINE)
#if (${ANDROID_ABI} STREQUAL "x86")
#    set(TRAMPOLINE_ABI trampoline/trampoline_x86.c)
#else ()
#    set(TRAMPOLINE_ABI trampoline/trampoline_arm.c)
#endif ()
add_library(
        trampoline
        STATIC
        ${SOURCE_DIR_TRAMPOLINE} ${TRAMPOLINE_ABI})
target_link_libraries(trampoline)
#==============================cjni module======================================
aux_source_directory(${COMMON_PROJECT}/cjni SOURCE_DIR_CJNI)
add_library(
        cjni
        STATIC
        ${SOURCE_DIR_CJNI})
target_link_libraries(cjni log)
#==============================gotplthook module======================================
#set(CMAKE_CXX_EXTENSIONS OFF)
#set(CMAKE_POSITION_INDEPENDENT_CODE TRUE)
#set(CMAKE_C_FLAGS "${CMAKE_C_FLAGS}  ${WARNING_FLAGS}  -std=gnu11 -pthread -marm ")
#set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} ${WARNING_FLAGS} -std=c++14 -pthread ")
#set(CMAKE_EXE_LINKER_FLAGS "${CMAKE_EXE_LINKER_FLAGS_RELEASE} -Wl,--hash-style=gun")
include_directories(${COMMON_PROJECT})
include_directories(${PROJECT_SOURCE_DIR}/src/main/jni/gotplthook)
aux_source_directory(${PROJECT_SOURCE_DIR}/src/main/jni/gotplthook SOURCE_DIR)
if (${ANDROID_ABI} STREQUAL "x86")
    set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} ${WARNING_FLAGS} -std=c++14 -pthread  -D__X86__")
else ()
    set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} ${WARNING_FLAGS} -std=c++14 -pthread -D__ARM__ ")
endif ()
add_library(
        gotplthook
        SHARED
        ${SOURCE_DIR})
target_link_libraries(
        gotplthook fake_linker util breakpad cjni trampoline)