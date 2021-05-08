package com.jamesfchen.yposedplugin3;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

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
        Button textView = new Button(this);
//        textView.setTextColor(Color.WHITE);
        textView.setText("my activity 3");
        textView.setBackgroundColor(Color.BLUE);
        setContentView(R.layout.layout);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("content://com.jamesfchen.yposedplugin3.my_provider");
                H.a(uri, MyActivity.this, "yposedplugin3");
                startActivity(new Intent(MyActivity.this,MyActivity2.class));
            }
        });
    }
}