function getFieldValue(object, fieldName) {
  var field = object.class.getDeclaredField(fieldName);
  field.setAccessible(true);
  var fieldValue = field.get(object);
  if (null == fieldValue) {
    return null;
  }
  var FieldClazz = Java.use(fieldValue.$className);
  var fieldValueWapper = Java.cast(fieldValue, FieldClazz);
  return fieldValueWapper;
}
function setFieldValue(object, fieldName, fieldValue) {
  var field = object.class.getDeclaredField(fieldName);
  field.setAccessible(true);
  field.set(object, fieldValue);
}
function hook_libguard() {
  console.log("---------------hook_libguard");
  var m = Module.load("libguard.so");
  console.log(JSON.stringify(m));
  console.log(hexdump(m.base, { ansi: true }));

  const pattern = "00 00 00 00 ?? 13 37 ?? 42";
  Memory.scan(m.base, m.size, pattern, {
    onMatch(address, size) {
      console.log("Memory.scan() found match at", address, "with size", size);

      // Optionally stop scanning early:
      return "stop";
    },
    onComplete() {
      console.log("Memory.scan() complete");
    },
  });
  const results = Memory.scanSync(m.base, m.size, pattern);
  console.log("Memory.scanSync() result:\n" + JSON.stringify(results));
  m.enumerateSymbols().forEach((symbol) => {
    console.log(symbol);
  });
  console.log(JSON.stringify(m.enumerateSymbols()));

  const stringFromJNI_absaddr = Module.getExportByName(
    "libguard.so",
    "Java_com_jamesfchen_guard_TestGuardActivity_stringFromJNI"
  );
  console.log(
    stringFromJNI_absaddr,
    "Java_com_jamesfchen_guard_TestGuardActivity_stringFromJNI"
  );
  Interceptor.attach(stringFromJNI_absaddr, {
    onEnter(args) {
      // console.log(
      //   "get_sign_addr called from:\n" +
      //     Thread.backtrace(this.context, Backtracer.ACCURATE)
      //       .map(DebugSymbol.fromAddress)
      //       .join("\n") +
      //     "\n"
      // );
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

function main() {
  // Java.perform(function () {
  //   console.log("Inside Java perform");
  //   var TestGuardActivity = Java.use("com.jamesfchen.guard.TestGuardActivity");
  //   TestGuardActivity.stringFromJNI.overload().implementation = function () {
  //     var s = this.stringFromJNI();
  //     console.log("stringFromJNI：" + s);
  //     return "hook stringFromJNI sucessful from frida";
  //   };
  // });

  var modules = Process.enumerateModules();
  console.log("modules:" + modules.length);
  modules.forEach((m) => {
    const name = m.name;
    if (name == "libguard.so" || name == "libyahfa.so") {
      console.log(m.name);
    }
  });

  hook_libguard();
}
setImmediate(main);
// setTimeout(main, 0);
// setTimeout(() => {
//     console.log("setTimeout")
// }, 10)
//frida -U -f com.hawksjamesf.spacecraft.debug --no-pause -l index.js
