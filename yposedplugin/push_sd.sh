#!/usr/bin/env bash
adb push $1 $2
adb shell am force-stop 'com.jamesfchen.spacecraft'
adb  shell pm grant  "com.jamesfchen.spacecraft android.permission.READ_EXTERNAL_STORAGE"
adb  shell pm grant "com.jamesfchen.spacecraft  android.permission.WRITE_EXTERNAL_STORAGE"
adb shell am start -n 'lcom.jamesfchen.spacecraft/.MainActivity'