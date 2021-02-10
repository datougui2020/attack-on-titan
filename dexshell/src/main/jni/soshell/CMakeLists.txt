cmake_minimum_required(VERSION 3.4.1)
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS}   -Wno-format -Wno-sign-compare -Wno-unused-parameter -DHAVE_CONFIG_H -fno-stack-protector  -Wstrict-overflow=5 -fvisibility=hidden -Wall -Wextra -Wunuse")
set(CMAKE_LINK_DEF_FILE_FLAG ${CMAKE_LINK_DEF_FILE_FLAG} -Wl,-Bsymbolic -Wl,--exclude-libs,ALL)
message(soshell  ${CMAKE_CXX_FLAGS})

add_library( # Sets the name of the library.
        soshell
#        STATIC
        SHARED
        gdlfcn.cpp
        Utils.cpp
        Shell.cpp)
find_library( # Sets the name of the path variable.
        log-lib

        log )
target_include_directories(soshell PUBLIC ../linker/)
target_link_libraries( # Specifies the target library.
        soshell

        android z EGL jnigraphics mediandk
        linker
        ${log-lib} )