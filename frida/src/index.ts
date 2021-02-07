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
  });
}
setImmediate(main);
