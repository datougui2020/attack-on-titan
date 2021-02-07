function hookMethodOnArtLib(methodName, sign,onEnter, onLeave) {
  hookMethod("libart.so",methodName,sign,onEnter,onLeave)
}
function hookMethodOnSpecialLib(module, methodName,sig, onEnter, onLeave) {
  hookMethod(module,methodName,sig,onEnter,onLeave)
}
function hookMethod(module, methodName,sign, onEnter, onLeave) {
  const methodAddr = Module.getExportByName(module, methodName);
  Interceptor.attach(methodAddr, {
    onEnter(args) {
      console.log("Context information:");
      console.log("Context  : " + JSON.stringify(this.context));
      console.log("Return   : " + this.returnAddress);
      console.log("ThreadId : " + this.threadId);
      console.log("Depth    : " + this.depth);
      console.log("Errornr  : " + this.err);

      // Save arguments for processing in onLeave.
      this.fd = args[0].toInt32();
      this.buf = args[1];
      this.count = args[2].toInt32();
    },
    onLeave: function (result) {
      console.log("----------");
      // Show argument 1 (buf), saved during onEnter.
      const numBytes = result.toInt32();
      if (numBytes > 0) {
        console.log(hexdump(this.buf, { length: numBytes, ansi: true }));
      }
      console.log("Result   : " + numBytes);
      var env = Java.vm.getEnv();
      var newRetval = env.newStringUtf(
        "stringFromJNI native函数 被frida hook了"
      );
      result.replace(ptr(newRetval));
    },
  });
}
function hookRegisterNatives(methodName, onEnter, onLeave) {
  var symbols = Module.enumerateSymbolsSync(art_shared_lib);

  for (var i = 0; i < symbols.length; i++) {
    var symbol = symbols[i];
    const address = symbol.address;
    const name = symbol.name;
    const type = symbol.type;
    const isGlobal = symbol.isGlobal;
    const section = symbol.section;
    if (
      name.indexOf("art") >= 0 &&
      name.indexOf("JNI") >= 0 &&
      name.indexOf("RegisterNatives") >= 0 &&
      name.indexOf("CheckJNI") < 0
    ) {
      //_ZN3art3JNI15RegisterNativesEP7_JNIEnvP7_jclassPK15JNINativeMethodi
      console.log(address, name, "RegisterNatives", type, isGlobal, section);
      /**
       * jint RegisterNatives(jclass clazz, const JNINativeMethod* methods,jint nMethods)
       * args[0] 类或者struct
       * args[1] args[2] args[3] 类或者struct 形参1，形参2，形参3
       */
      Interceptor.attach(address, {
        onEnter: function (args) {
          var env = args[0];
          var className = Java.vm.tryGetEnv().getClassName(args[1]);
          var methodsPtr = ptr(args[2]);
          var methodCount = parseInt(args[3]);
          console.log("[RegisterNatives] method_count:", methodCount);
          for (var i = 0; i < methodCount; i++) {
            var namePtr = Memory.readPointer(
              methodsPtr.add(i * Process.pointerSize * 3)
            );
            var sigPtr = Memory.readPointer(
              methodsPtr.add(i * Process.pointerSize * 3 + Process.pointerSize)
            );
            var fnPtr = Memory.readPointer(
              methodsPtr.add(
                i * Process.pointerSize * 3 + Process.pointerSize * 2
              )
            );

            var name = Memory.readCString(namePtr);
            var sig = Memory.readCString(sigPtr);
            var findModule = Process.findModuleByAddress(fnPtr);
            if (name != methodName) continue;
            const param_type_list = [className, name, sig, fnPtr, findModule];
            onEnter(param_type_list);
          }
        },
        onLeave: function (result) {
          onLeave(result);
        },
      });
    }
  }
}

export { hookRegisterNatives };
