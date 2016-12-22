package com.example.lshare;

/**
 * Created by m5033 on 2016/12/12.
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Services.AddFriendToServer;
import com.example.Services.QueryFriend;

public class addFriendsActivity extends Activity {
    private ImageView ivDeleteText;
    private EditText etSearch;
    private TextView tvFnSerrch; //Textview FriendsName搜索结果
    private Button btnAddFriend;
    private Button btnSearch;

    Intent intent=null;
    String userName_now=null;
    String freindName=null;

    final Handler handler=new Handler(){
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case QueryFriend.SEND_SUCCESS:
                    Toast.makeText(addFriendsActivity.this, "用户存在", Toast.LENGTH_SHORT).show();
                  /*  System.out.println("------"+freindName);
                    System.out.println("----"+etSearch.getText());*/
                    tvFnSerrch.setText("Friend： "+etSearch.getText().toString());
                    break;
                case QueryFriend.SEND_FAIL:
                    Toast.makeText(addFriendsActivity.this, "该用户不存在", Toast.LENGTH_SHORT).show();
                   /* System.out.print("_______________________不存在");*/
                    tvFnSerrch.setText("");
                    break;

                default:
                    break;
            }
        };
    };
    final Handler handler2=new Handler(){
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case QueryFriend.SEND_SUCCESS:
                    Toast.makeText(addFriendsActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(addFriendsActivity.this,MainActivity.class);
                    intent.putExtra("addFriendName",freindName);
                    intent.putExtra("userName",userName_now);
                    finish();
        //            startActivity(intent);
                    break;
                case QueryFriend.SEND_FAIL:
                    Toast.makeText(addFriendsActivity.this, "没有添加成功", Toast.LENGTH_SHORT).show();
                    break;

                default:
                    break;
            }
        };
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addfriends);
        ivDeleteText = (ImageView) findViewById(R.id.ivDeleteText);
        etSearch = (EditText) findViewById(R.id.etSearch);
        tvFnSerrch=(TextView)findViewById(R.id.tv_FnSearch);
        btnAddFriend=(Button)findViewById(R.id.btn_addFriend);
        btnSearch=(Button)findViewById(R.id.btnSearch);

        intent=this.getIntent();
        userName_now=intent.getStringExtra("userName");


        ivDeleteText.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                etSearch.setText("");
            }
        });

        etSearch.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub

            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    ivDeleteText.setVisibility(View.GONE);
                } else {
                    ivDeleteText.setVisibility(View.VISIBLE);
                }
            }
        });

        btnSearch.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                String result=etSearch.getText().toString().trim();
                new QueryFriend(handler).queryFriendToServer(userName_now,result);
            }
        });
        //TODO
        /*if(etSearch.getText().toString().trim()!=""){
           btnAddFriend.setEnabled(true);
        }else{
            btnAddFriend.setEnabled(false);
        }*/
        btnAddFriend.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tvFnSerrch.getText().toString()!=""&&(tvFnSerrch.getText().toString()!="Friend： ")){
                    new AddFriendToServer(handler2).addFriendToServer(userName_now,etSearch.getText().toString());
                }
            }
        });






    }


}
