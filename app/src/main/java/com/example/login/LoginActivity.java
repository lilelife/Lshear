package com.example.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * ��¼ҳ��Activity
 */
import com.example.Services.SendDateToLogin;
import com.example.lshare.MainActivity;
import com.example.lshare.R;

public class LoginActivity extends Activity{
	EditText et_name;
	EditText et_passWord;
	Button btn_load;//��¼ ע�� ��������
	Button btn_regist;
	Button btn_forgetPwd;
	UserDataManager ud=null;
	private boolean is;
	String name;
	String pwd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		final Handler handler=new Handler(){
			public void handleMessage(Message msg) {
				//super.handleMessage(msg); //
				System.out.println(msg.arg1);

				switch (msg.what) {
					case SendDateToLogin.SEND_SUCCESS:
						Toast.makeText(LoginActivity.this, "��¼�ɹ�", Toast.LENGTH_SHORT).show();

						is=true;
						System.out.print("iisssisisis"+is);
						break;
					case SendDateToLogin.SEND_FAIL:
						Toast.makeText(LoginActivity.this, "��¼ʧ��", Toast.LENGTH_SHORT).show();

						is=false;
						System.out.print("iisssisisis"+is);
						break;
					default:
						break;
				}
				if(is==false){
					Toast.makeText(getApplicationContext(), "�û������벻ƥ�䣬����������..", Toast.LENGTH_SHORT).show();
				}
				else {
					Intent intent =new Intent(LoginActivity.this,MainActivity.class);
					intent.putExtra("username",name);
					intent.putExtra("pwd",pwd);
					startActivity(intent);
					finish();
				}
			};
		};


		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		et_name=(EditText)findViewById(R.id.name);
		et_passWord=(EditText)findViewById(R.id.passWord);
		btn_load=(Button) findViewById(R.id.load);
		btn_regist=(Button) findViewById(R.id.regist);
		btn_forgetPwd=(Button) findViewById(R.id.resetPassword);
		if(ud==null){
			ud=new UserDataManager(this);// �����������ݿ�
			ud.openDataBase();
		}

		btn_regist.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO ע��activity
				Intent intent_Login_to_Register = new Intent(LoginActivity.this,Register.class) ;    //�л�Login Activity��User Activity
				startActivity(intent_Login_to_Register);
			}
		});



		//��¼���ٶȵ�ͼ����  ������ȡ��username�� ���͵�MainActivity
		btn_load.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				name=et_name.getText().toString();
				pwd=et_passWord.getText().toString();
				/*User user=new User(name,pwd);
				ArrayList<User> users=GPUsers.getUerList(getApplicationContext());*/
				new SendDateToLogin(handler).SendDataToLogin(name,pwd);

			}
		});

		//��������
		btn_forgetPwd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent_Login_to_reset = new Intent(LoginActivity.this,Resetpwd.class) ;    //�л�Login Activity��User Activity
				startActivity(intent_Login_to_reset);
			}
		});
	}
}
