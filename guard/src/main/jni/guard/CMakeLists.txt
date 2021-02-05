cmake_minimum_required(VERSION 3.4.1)
add_definitions(-DENABLE_ANDROID_LOG)
add_library(
        guard
        SHARED
#        main.cpp
        guard.cpp
)
target_include_directories(guard PRIVATE
        ${CMAKE_CURRENT_SOURCE_DIR}/utils/include
        )

find_library(
        log-lib
        log )

target_link_libraries(
        guard

        ${CMAKE_CURRENT_SOURCE_DIR}/utils/${ANDROID_ABI}/libutils.so
        ${log-lib} )