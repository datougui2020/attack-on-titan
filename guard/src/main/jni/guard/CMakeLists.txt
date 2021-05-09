cmake_minimum_required(VERSION 3.4.1)
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS}   -Wno-format -Wno-sign-compare -Wno-unused-parameter -DHAVE_CONFIG_H")
include_directories(${PROJECT_SOURCE_DIR}/../common/src/main/cpp)
add_definitions(-DENABLE_ANDROID_LOG)
add_library(
        guard
        SHARED
        so_protector.cpp
        guard.cpp
        event.cpp
        mmap_ext.cpp
)
find_library(
        log-lib
        log )

target_link_libraries(
        guard
        ${log-lib} )