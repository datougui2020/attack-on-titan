cmake_minimum_required(VERSION 3.4.1)
set(CMAKE_CXX_EXTENSIONS OFF)
set(CMAKE_POSITION_INDEPENDENT_CODE TRUE)
set(CMAKE_C_FLAGS "${CMAKE_C_FLAGS}  ${WARNING_FLAGS}  -std=gnu11 -pthread -marm ")
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} ${WARNING_FLAGS} -std=c++11 -pthread ")
set(CMAKE_EXE_LINKER_FLAGS "${CMAKE_EXE_LINKER_FLAGS_RELEASE} -Wl,--hash-style=gun")
#set(CMAKE_LIBRARY_OUTPUT_DIRECTORY ${CMAKE_CURRENT_SOURCE_DIR}/ypose/main/jniLibs/${ANDROID_ABI})
#set(CMAKE_LIBRARY_OUTPUT_DIRECTORY ${PROJECT_SOURCE_DIR}})
aux_source_directory(${PROJECT_SOURCE_DIR}/src/main/cpp/fake_linker SOURCE_DIR)
aux_source_directory(${PROJECT_SOURCE_DIR}/src/main/cpp/fake_linker/build SOURCE_DIR_BUILD)
aux_source_directory(${PROJECT_SOURCE_DIR}/src/main/cpp/fake_linker/sig_safe_write SOURCE_DIR_SIG_SAFE_WRITE)
aux_source_directory(${PROJECT_SOURCE_DIR}/src/main/cpp/fake_linker/sigmux SOURCE_DIR_SIGMUX)
include_directories(${PROJECT_SOURCE_DIR}/src/main/cpp/fake_linker/build)
include_directories(${PROJECT_SOURCE_DIR}/src/main/cpp/fake_linker/sigmux)
include_directories(${PROJECT_SOURCE_DIR}/src/main/cpp/fake_linker/sig_safe_write)
add_library(
        fake_linker
        SHARED
        ${SOURCE_DIR} ${SOURCE_DIR_SIG_SAFE_WRITE} ${SOURCE_DIR_SIGMUX} ${SOURCE_DIR_BUILD})
find_library(
        log-lib
        log)
target_link_libraries(
        fake_linker
        ${log-lib})