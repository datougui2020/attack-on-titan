cmake_minimum_required(VERSION 3.4.1)
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS}   -Wno-format -Wno-sign-compare -Wno-unused-parameter -DHAVE_CONFIG_H")
add_definitions(-DENABLE_ANDROID_LOG)
add_library(
        dynamic_so
        SHARED
        main.cpp
)
find_library(
        log-lib
        log )

target_link_libraries(
        dynamic_so
        ${log-lib} )