cmake_minimum_required(VERSION 3.4.1)
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS}   -Wno-format -Wno-sign-compare -Wno-unused-parameter -DHAVE_CONFIG_H")
message(utils  ${CMAKE_CXX_FLAGS})

add_library( # Sets the name of the library.
        utils
#        STATIC
        SHARED
        AssetUtil.cpp
        ThreadUtil.cpp
        ReflectUtil.cpp)
find_library( # Sets the name of the path variable.
        log-lib

        log )

target_link_libraries( # Specifies the target library.
        utils

        android z EGL jnigraphics mediandk
        ${log-lib} )