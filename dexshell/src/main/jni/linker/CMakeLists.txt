cmake_minimum_required(VERSION 3.4.1)
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS}   -Wno-format -Wno-sign-compare -Wno-unused-parameter -DHAVE_CONFIG_H -fno-stack-protector  -Wstrict-overflow=5 -fvisibility=hidden -Wall -Wextra -Wunuse")
set(CMAKE_LINK_DEF_FILE_FLAG ${CMAKE_LINK_DEF_FILE_FLAG} -Wl,-Bsymbolic -Wl,--exclude-libs,ALL)
message(linker  ${CMAKE_CXX_FLAGS})

add_library( # Sets the name of the library.
        linker
#        SHARED
        STATIC
        Log.cpp
        linker.cpp
        linker_allocator.cpp
        linker_phdr.cpp)
find_library( # Sets the name of the path variable.
        log-lib

        log )

target_link_libraries( # Specifies the target library.
        linker

        android z EGL jnigraphics mediandk
        ${log-lib} )