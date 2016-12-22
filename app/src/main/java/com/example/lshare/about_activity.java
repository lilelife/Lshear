package com.example.lshare;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by m5033 on 2016/12/16.
 */

public class about_activity extends Activity {
    private Button back_about;
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);

        back_about=(Button)findViewById(R.id.back_about);

        back_about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });
    }
}