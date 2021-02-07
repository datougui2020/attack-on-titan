import {
  js_log,
  j_v,
  j_debug,
  j_info,
  j_warn,
  j_error,
  c_v,
  c_debug,
  c_info,
  c_warn,
  c_error,
} from "./utils/p";
import { hookRegisterNatives } from "./utils/hooker";
function main() {
  Java.perform(function () {
    js_log("cjf", "sakura");
    j_error("cjf", "j_error");
    j_warn("cjf", "j_warn");
    j_info("cjf", "j_info");
    j_debug("cjf", "j_debug");
    j_v("cjf", "j_v");
    c_error("cjf", "c_error");
    c_warn("cjf", "c_warn");
    c_info("cjf", "c_info");
    c_debug("cjf", "c_debug");
    c_v("cjf", "c_v");
    hookRegisterNatives(
      "verify",
      (enter_args: Array<any>) => {
         let className = enter_args[0];
         let name = enter_args[1];
         let sig = enter_args[2];
         let fnPtr = enter_args[3];
         let findModule = enter_args[4];
         console.log(
           "[RegisterNatives] java_class:",
           className,
           "name:",
           name,
           "sig:",
           sig,
           "fnPtr:",
           fnPtr,
           "module_name:",
           findModule.name,
           "module_base:",
           findModule.base,
           "offset:",
           ptr(fnPtr).sub(findModule.base)
         );
      },
      (result:any) => {
          const numBytes = result.toInt32();
          // if (numBytes > 0) {
          //   console.log(hexdump(this.buf, { length: numBytes, ansi: true }));
          // }
          console.log("Result   : " + numBytes);
          var env = Java.vm.getEnv();
          var newRetval = env.newStringUtf(
            "stringFromJNI native函数 被frida hook了"
          );
          result.replace(ptr(newRetval));
      }
    );
  });
}
setImmediate(main);
