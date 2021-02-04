
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


}
setImmediate(main);
//frida -U -f com.hawksjamesf.spacecraft.debug --no-pause -l index.js