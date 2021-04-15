cmake_minimum_required(VERSION 3.4.1)
#aux_source_directory(${PROJECT_SOURCE_DIR}/../common/src/main/cpp)
include_directories(${PROJECT_SOURCE_DIR}/../common/src/main/cpp)
add_library(
        gotplthook
        SHARED
        gotplthook_main.cpp)
find_library(
        log-lib
        log)
target_link_libraries(
        gotplthook
        ${log-lib})