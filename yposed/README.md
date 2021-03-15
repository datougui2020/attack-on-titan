hotfix方式:
- 编译插桩
- 修改classloader加载的类
- 运行时插桩:动态计算artmethod函数地址，然后replace.inline hook


### 双亲委派机制

bootstrap classsloader

extension classloader

application classloader(android BaseDexClassloder)


如果bootstrap加载器和extension classloader说加载不了那么BaseDexClassloader就会通过find尝试加载。

DexPathList#findClass 会遍历dex数组，如果发现class在dex文件中，BaseDexClassloader就会加载这个类