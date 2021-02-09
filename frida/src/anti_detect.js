/**
    frida -f com.taobao.trip -l anti_frida/anti_detect.js -U --no-pause
*/

//bypass_root_detection()
bypass_frida_detection()
// bypass_anti_debug();

// ================ 绕过ida反调试 ================
function bypass_anti_debug() {
  var openPtr = Module.findExportByName(null, "open");
  var open = new NativeFunction(openPtr, "int", ["pointer", "int"]);
  var pid;
  var fds = {};
  Java.perform(() => {
    var Process = Java.use("android.os.Process");
    pid = Process["myPid"].call(Process);
    console.warn("current pid:", pid);
  });

  /**
   * step1: 替换 /proc/下的关键文件，绕过检测
   * 调用前须知：
   *      强制退出，正常情况下(非debug, 非hook)启动app
   *      adb shell pidof com.taobao.trip 获取到当前app进程id -> pid
   *      cp /proc/self/status /data/local/tmp/status
   *      cp /proc/16748/stat /data/local/tmp/stat
   *      cp /proc/16748/wchan /data/local/tmp/wchan
   */
  Interceptor.attach(Module.findExportByName(null, "open"), {
    onEnter: function (args) {
      var fname = args[0].readCString();

      // fname.indexOf('/proc/' + pid + "/maps")
      if (
        fname.indexOf("/proc/self/status") != -1 ||
        fname.indexOf("/proc/" + pid + "/stat") != -1 ||
        fname.indexOf("/proc/" + pid + "/wchan") != -1
      ) {
        this.flag = true;
        this.fname = fname;
      }
    },

    onLeave: function (retval) {
      if (this.flag) {
        fds[retval] = this.fname;
        var fake_fd;
        if (this.fname.indexOf("/proc/self/status") != -1) {
          fake_fd = open(Memory.allocUtf8String("/data/local/tmp/status"), 0);
        } else if (this.fname.indexOf("/proc/" + pid + "/stat") != -1) {
          fake_fd = open(Memory.allocUtf8String("/data/local/tmp/stat"), 0);
        } else if (this.fname.indexOf("/proc/" + pid + "/wchan") != -1) {
          fake_fd = open(Memory.allocUtf8String("/data/local/tmp/wchan"), 0);
        }

        console.warn(this.fname, "描述符 ->", retval);
        retval.replace(ptr(fake_fd));
        console.error("\t替换为 ->", ptr(fake_fd));
      }
    },
  });

  ["read", "pread", "readv"].forEach((fnc) => {
    Interceptor.attach(Module.findExportByName(null, fnc), {
      onEnter: function (args) {
        var fd = args[0];
        if (fd in fds) {
          //console.log(`${fnc}: ${fds[fd]}\t`);
        }
      },
    });
  });
}

// ================ 绕过root检测 ================
function bypass_root_detection() {
  // cond1: 检测system、sbin、data 目录下的su文件是否存在 + 一些root软件如Superuser是否存在
  /**
        /sbin/su  
        /system/bin/su  
        /system/bin/failsafe/su  
        /system/xbin/su  
        /system/xbin/busybox  
        /system/sd/xbin/su  
        /data/local/su  
        /data/local/xbin/su  
        /data/local/bin/su

        /system/app/Superuser.apk
        /system/etc/init.d/99SuperSUDaemon
        /dev/com.koushikdutta.superuser.daemon/
        /system/xbin/daemonsu
     */
  Java.perform(() => {
    Java.use("java.io.File")["exists"].implementation = function () {
      var ret = this.exists();
      var path = this.path.value;

      if (
        path.endsWith("/su") ||
        path.endsWith("/busybox") ||
        path.indexOf("uperuser.") != -1 ||
        path.indexOf("/daemonsu" != -1) ||
        path.startsWith("/data")
      ) {
        console.log("detecting root", this.path.value);
        return false;
      }
      return ret;
    };
  });
}

// ================ frida 绕过检测 ================
function bypass_frida_detection() {
  // cond1: 通过关键词查询是否包含 frida/xposed
  Interceptor.attach(Module.findExportByName("libc.so", "strstr"), {
    onEnter: function (args) {
      var haystack = args[0];
      var needle = args[1];
      this["frida"] = Boolean(0);

      haystack = Memory.readUtf8String(haystack);
      needle = Memory.readUtf8String(needle);

      if (haystack.indexOf("frida") != -1 || haystack.indexOf("xposed") != -1) {
        // console.log("detecting:", haystack, " -> ", needle)
        // console.log('so 调用链:\n' + Thread.backtrace(this.context, Backtracer.ACCURATE).map(DebugSymbol.fromAddress).join('\n'), 'white', 'stacktrace');

        this["frida"] = Boolean(1);
      }
    },

    onLeave: function (retVal) {
      if (this.frida) {
        retVal.replace(0);
      }
      return retVal;
    },
  });

  // cond2: 避免打开 /proc/self(pid)/maps，发现frida-agent-32.so
  Interceptor.attach(Module.findExportByName("libc.so", "fopen"), {
    onEnter: function (args) {
      var name = args[0].readCString();
      this["name"] = name;
      console.log(name);
    },

    onLeave: function (retVal) {
      var name = this["name"];
      if (name.endsWith("/maps")) {
        // todo...
        //retVal.replace(0x0)
      }

      return retVal;
    },
  });
}

/*Process.getModuleByName('libc.so').enumerateExports().filter(ex => ex.type === 'function' &&
[
    'connect',
    //'recv',
    //'send',
    //'read',
    //'write'
]
.some(prefix => ex.name.indexOf(prefix) === 0)).forEach(ex => {

    Interceptor.attach(ex.address, {
        onEnter: function (args) {
            var fd = args[0].toInt32();
            var address = Socket.localAddress(fd);
            //if (address === null || address.ip === null) return;
            if (address != null) {

                console.log(fd, Socket.type(fd), ex.name, address.ip + ':' + address.port);
            } else {
                console.log(fd, Socket.type(fd), ex.name);
            }
        }
    })

})*/

/*Java.perform(() => {

    Java.use("com.meituan.android.common.mtguard.wtscore.adapter.MTGAdapterImpl")['initNative'].implementation = function() {
        console.log("initNative")
        return true
    }

    Java.use("com.meituan.android.common.mtguard.wtscore.plugin.collection.Additional")['deviceInfo'].implementation = function() {
        console.log("deviceInfo")
        return null
    }

    Java.use("com.meituan.android.common.mtguard.wtscore.plugin.detection.EnvCheck")['checkEnvWithParams'].implementation = function(p1, p2) {
        console.log("checkEnvWithParams")
        return this.checkEnvWithParams(p1, p2)
    }

    Java.use("com.meituan.android.common.mtguard.wtscore.plugin.encryption.WTCrypt")['crypt'].implementation = function(p1, p2, p3) {
        console.log("crypt")
        return this.crypt(p1, p2, p3)
    }

    Java.use("com.meituan.android.common.mtguard.wtscore.plugin.sign.core.WTSign")['makeHeader'].implementation = function(p1, p2) {
        console.log("makeHeader", p1, p2)
        var ret = this.makeHeader(p1, p2)
        console.log("ret: ", ret)
        return ret
    }


    Java.use("com.meituan.android.common.mtguard.wtscore.plugin.store.WTStore")['save'].implementation = function(p1, p2, p3) {
        console.log("save")
        return this.crypt(p1, p2, p3)
    }

    Java.use("com.meituan.android.common.mtguard.wtscore.plugin.store.WTStore")['load'].implementation = function(p1, p2) {
        console.log("load")
        return this.crypt(p1, p2)
    }


    Java.use("com.meituan.android.common.mtguard.NBridge")['main'].implementation = function(p1, p2) {
        console.log(p1, p2)
        var ret = this.main(p1, p2)
        console.log("ret", res)
        return ret
    }
})*/
