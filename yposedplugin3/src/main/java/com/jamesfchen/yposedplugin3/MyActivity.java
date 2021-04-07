package com.jamesfchen.yposedplugin3;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;

/**
 * Copyright ® $ 2021
 * All right reserved.
 *
 * @author: jamesfchen
 * @email: hawksjamesf@gmail.com
 * @since: 三月/15/2021  星期一
 */
public class MyActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView textView = new TextView(this);
//        textView.setTextColor(Color.WHITE);
        textView.setText("my activity 3");
        textView.setBackgroundColor(Color.WHITE);
        setContentView(textView);
    }
}
