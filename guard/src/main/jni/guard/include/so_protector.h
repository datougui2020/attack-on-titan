//
// Created by guard.jamesf on 12/19/19.
//

#ifndef SPACECRAFTANDROID_GUARD_H
#define SPACECRAFTANDROID_GUARD_H

#include <android/log.h>

namespace so_protector {
    //驼峰命名规则
    class Guard {

    };

    bool entry();

}

//模块构造器 include是就会被调用
__attribute__((constructor())) void module_init();

// 模块析构器
__attribute__((destructor)) void module_fini();

#endif //SPACECRAFTANDROID_GUARD_H
