package com.example.lshare;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by m5033 on 2016/12/16.
 */

public class introduce_activity extends Activity{
    private Button back_introduce;
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.introduce);

        back_introduce=(Button)findViewById(R.id.back_introduce);

        back_introduce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* Intent intent=new Intent(introduce_activity.this,MainActivity.class);
                startActivity(intent);*/
                finish();

            }
        });
    }
}
