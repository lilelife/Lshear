package com.example.login;

/**
 * Created by m5033 on 2016/12/10.
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.Services.SendDateToLogin;
import com.example.Services.SendDateToSign;
import com.example.lshare.R;

public class Register extends Activity {
    private EditText mAccount;                        //�û����༭
    private EditText mPwd;                            //����༭
    private EditText mPwdCheck;                       //����༭
    private Button mSureButton;                       //ȷ����ť
    private Button mCancelButton;                     //ȡ����ť
    private UserDataManager mUserDataManager;         //�û����ݹ�����

    final Handler handler=new Handler(){
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SendDateToLogin.SEND_SUCCESS:
                    Toast.makeText(Register.this, "ע��ɹ�", Toast.LENGTH_SHORT).show();
                    break;
                case SendDateToLogin.SEND_FAIL:
                    Toast.makeText(Register.this, "ע��ʧ��", Toast.LENGTH_SHORT).show();
                    break;

                default:
                    break;
            }
        };
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        mAccount = (EditText) findViewById(R.id.resetpwd_edit_name);
        mPwd = (EditText) findViewById(R.id.resetpwd_edit_pwd_old);
        mPwdCheck = (EditText) findViewById(R.id.resetpwd_edit_pwd_new);

        mSureButton = (Button) findViewById(R.id.register_btn_sure);
        mCancelButton = (Button) findViewById(R.id.register_btn_cancel);



        mSureButton.setOnClickListener(m_register_Listener);      //ע�����������ť�ļ����¼�
        mCancelButton.setOnClickListener(m_register_Listener);

        if (mUserDataManager == null) {
            mUserDataManager = new UserDataManager(this);
            mUserDataManager.openDataBase();                              //�����������ݿ�
        }

    }
    View.OnClickListener m_register_Listener = new View.OnClickListener() {    //��ͬ��ť���µļ����¼�ѡ��
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.register_btn_sure:                       //ȷ�ϰ�ť�ļ����¼�
                    register_check();

                    break;
                case R.id.register_btn_cancel:                     //ȡ����ť�ļ����¼�,��ע����淵�ص�¼����
                    Intent intent_Register_to_Login = new Intent(Register.this,LoginActivity.class) ;    //�л�User Activity��Login Activity
                    startActivity(intent_Register_to_Login);
                    finish();
                    break;
            }
        }
    };
    public void register_check() {                                //ȷ�ϰ�ť�ļ����¼�
        if (isUserNameAndPwdValid()) {
            String userName = mAccount.getText().toString().trim();
            String userPwd = mPwd.getText().toString().trim();
            String userPwdCheck = mPwdCheck.getText().toString().trim();
            //����û��Ƿ����
            int count=mUserDataManager.findUserByName(userName);
            //�û��Ѿ�����ʱ���أ�������ʾ����
            if(count>0){
                Toast.makeText(this, getString(R.string.name_already_exist, userName),Toast.LENGTH_SHORT).show();
                return ;
            }
            if(userPwd.equals(userPwdCheck)==false){     //�����������벻һ��
                Toast.makeText(this, getString(R.string.pwd_not_the_same),Toast.LENGTH_SHORT).show();
                return ;
            } else {
                UserData mUser = new UserData(userName, userPwd);
                mUserDataManager.openDataBase();
                long flag = mUserDataManager.insertUserData(mUser); //�½��û���Ϣ
                if (flag == -1) {
                    Toast.makeText(this, getString(R.string.register_fail),Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(this, getString(R.string.register_success),Toast.LENGTH_SHORT).show();

                    //�����ݷ��͵���������
                    new SendDateToSign(handler).SendDataToServer(userName,userPwd);

                    Intent intent_Register_to_Login = new Intent(Register.this,LoginActivity.class) ;    //�л�User Activity��Login Activity
                    startActivity(intent_Register_to_Login);
                    finish();
                }
            }
        }
    }
    public boolean isUserNameAndPwdValid() {
        if (mAccount.getText().toString().trim().equals("")) {
            Toast.makeText(this, getString(R.string.account_empty),
                    Toast.LENGTH_SHORT).show();
            return false;
        } else if (mPwd.getText().toString().trim().equals("")) {
            Toast.makeText(this, getString(R.string.pwd_empty),
                    Toast.LENGTH_SHORT).show();
            return false;
        }else if(mPwdCheck.getText().toString().trim().equals("")) {
            Toast.makeText(this, getString(R.string.pwd_check_empty),
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
