
function getFieldValue(object, fieldName) {
    var field = object.class.getDeclaredField(fieldName);
    field.setAccessible(true)
    var fieldValue = field.get(object)
    if (null == fieldValue) {
        return null;
    }
    var FieldClazz = Java.use(fieldValue.$className)
    var fieldValueWapper = Java.cast(fieldValue, FieldClazz)
    return fieldValueWapper
}
function setFieldValue(object, fieldName, fieldValue) {
    var field = object.class.getDeclaredField(fieldName)
    field.setAccessible(true)
    field.set(object, fieldValue)
}

function main() {
    console.log("Enter the Script!");
    Java.perform(function () {
        console.log("Inside Java perform");
        var TestGuardActivity = Java.use("com.jamesfchen.guard.TestGuardActivity");
        TestGuardActivity.stringFromJNI.overload().implementation = function () {
            var s = this.stringFromJNI()
            console.log("stringFromJNI：" + s);
            return 'hook stringFromJNI sucessful from frida'
        };

    })
    var symbols = Module.enumerateSymbolsSync("libart.so");
    symbols.forEach(symbol => {
        // console.log(symbol.name)
        if (symbol.name.indexOf("art") >= 0 && symbol.name.indexOf("JNI") >= 0 && symbol.name.indexOf("CheckJNI") < 0) {
            
        //     // jni函数名称
        //     var name = symbol.name
        //     // jni函数绝对地址
        //     var address = symbol.address

        //     // 对指定的地址进行hook
        //     Interceptor.attach(address, {
        //         onEnter: function(args) {
        //             console.log(args[0])
        //         },
        //         onLeave: function(retVal) {
        //             console.log(retVal)
        //         }
        //     });        

        }
    })



}
setImmediate(main);
// setTimeout(() => {
//     console.log("setTimeout")
// }, 10)
//frida -U -f com.hawksjamesf.spacecraft.debug --no-pause -l index.js