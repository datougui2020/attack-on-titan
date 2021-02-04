package com.hawksjamesf.yposed

import android.app.Activity
import android.os.Bundle

class YPosedActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_yposed)
    }
    fun stringFromJava() = "string  from java"
}
