package com.example.PersonInfoSettings;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.Services.SettingToServer;
import com.example.lshare.R;

/**
 * Created by m5033 on 2016/12/14.
 */

public class settings_activity extends Activity {
    private Button sure;
    //private Button cancel;
//    private EditText nickname11,signature11,personalInfo1;
//    private TextView nickname1,signature1,personalInfo;
//    private TextView nickname,signature;
//    private ListView list;

    public static final String action = "jason.broadcast.action";
    String userName_now=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        sure = (Button) findViewById(R.id.sure);
      //  cancel=(Button)findViewById(R.id.cancel) ;
        Intent intent=this.getIntent();
        userName_now=intent.getStringExtra("userName");
        sure.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                EditText et = (EditText)findViewById(R.id.nickname11);//获取edittext组件
                EditText et1=(EditText)findViewById(R.id.signature11);
                EditText et2=(EditText)findViewById(R.id.personalinfo11);
                String cn = et.getText().toString();//获取edittext中填写的内容
                String cn1=et1.getText().toString();
                String cn2=et2.getText().toString();

                new SettingToServer().SendDataToServer(userName_now,cn,cn1,cn2);///
                // TODO Auto-generated method stub
                Intent intent = new Intent(action);
                intent.putExtra("data", cn);
                intent.putExtra("data1",cn1);
                intent.putExtra("data2",cn2);
                sendBroadcast(intent);
                finish();
            }
        });

        /*cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(settings_activity.this,MainActivity.class);
                startActivity(intent);
            }
        });*/
    }
}
