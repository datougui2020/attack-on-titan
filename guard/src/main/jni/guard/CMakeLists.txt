cmake_minimum_required(VERSION 3.4.1)
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS}   -Wno-format -Wno-sign-compare -Wno-unused-parameter -DHAVE_CONFIG_H")
add_definitions(-DENABLE_ANDROID_LOG)
add_library(
        guard
        SHARED
#        main.cpp
        guard.cpp
)
target_include_directories(guard PRIVATE
        utils/include
        )

find_library(
        log-lib
        log )

target_link_libraries(
        guard
        utils/${ANDROID_ABI}/libutils.so
        ${log-lib} )