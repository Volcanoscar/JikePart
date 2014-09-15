//找回密码
package com.jike.shanglv;

import com.jike.shanglv.Common.CommonFunc;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

public class Activity_RetrievePassword extends Activity {

	private EditText phone_input_et;
	private Button retrieve_btn;
	private ImageButton back_imgbtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_retrieve_password);
		
		retrieve_btn=(Button) findViewById(R.id.retrieve_btn);
		phone_input_et=(EditText) findViewById(R.id.phone_input_et);
		back_imgbtn=(ImageButton) findViewById(R.id.back_imgbtn);
		back_imgbtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		retrieve_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!CommonFunc.isMobileNO(phone_input_et.getText()
						.toString().trim())) {
					new AlertDialog.Builder(Activity_RetrievePassword.this).setTitle("手机号码格式不正确")
							.setMessage("请输入合法的手机号码！")
							.setPositiveButton("确定", null).show();
					return;
				}
			}
		});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}
	
}
