package com.jike.shanglv;

import com.jike.shanglv.Enums.SPkeys;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MineActivity extends Activity {
	private ImageButton back_iv,user_login_imgbtn;
	private RelativeLayout my_account_rl, all_order_rl,noLogin_rl,hasLogin_rl;
	private TextView welcome_tv,username_tv,zhanghuyue_tv,chongzhi_tv;
	private SharedPreferences sp;
	private Boolean loginState=false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mine);

		sp = getSharedPreferences(SPkeys.SPNAME.getString(), 0);
		welcome_tv=(TextView) findViewById(R.id.welcome_tv);
		username_tv=(TextView) findViewById(R.id.username_tv);
		zhanghuyue_tv=(TextView) findViewById(R.id.zhanghuyue_tv);
		chongzhi_tv=(TextView) findViewById(R.id.chongzhi_tv);
		back_iv = (ImageButton) findViewById(R.id.back_imgbtn);
		user_login_imgbtn=(ImageButton) findViewById(R.id.user_login_imgbtn);
		user_login_imgbtn.setOnClickListener(btnClickListner);
		back_iv.setOnClickListener(btnClickListner);
		my_account_rl = (RelativeLayout) findViewById(R.id.my_account_rl);
		all_order_rl = (RelativeLayout) findViewById(R.id.all_order_rl);
		all_order_rl.setOnClickListener(btnClickListner);
		my_account_rl.setOnClickListener(btnClickListner);
		noLogin_rl = (RelativeLayout) findViewById(R.id.noLogin_rl);
		hasLogin_rl = (RelativeLayout) findViewById(R.id.hasLogin_rl);
		
		loginState=sp.getBoolean(SPkeys.loginState.getString(), false);
		if (!loginState) {
			hasLogin_rl.setVisibility(View.GONE);
			noLogin_rl.setVisibility(View.VISIBLE);
		}else {
			hasLogin_rl.setVisibility(View.VISIBLE);
			noLogin_rl.setVisibility(View.GONE);
			username_tv.setText(sp.getString(SPkeys.username.getString(), ""));
			zhanghuyue_tv.setText(sp.getString(SPkeys.amount.getString(), ""));
			chongzhi_tv.setOnClickListener(btnClickListner);
		}
	}

	View.OnClickListener btnClickListner = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.back_imgbtn:
				startActivity(new Intent(MineActivity.this, MainActivity.class));
				break;
			case R.id.all_order_rl:
				startActivity(new Intent(MineActivity.this, OrderActivity.class));
				break;
			case R.id.user_login_imgbtn:
				startActivity(new Intent(MineActivity.this, Activity_Login.class));
				break;
			case R.id.my_account_rl:
				if (!loginState) {
					Toast.makeText(getApplicationContext(), "ÇëÏÈµÇÂ¼£¡", 0).show();
					break;
				}
				startActivity(new Intent(MineActivity.this, ActivityMyAccout.class));
				break;
			case R.id.chongzhi_tv:
				startActivity(new Intent(MineActivity.this, ActivityZhanghuchongzhi.class));
				break;
			default:
				break;
			}
		}
	};

	@Override
	protected void onResume() {
		super.onResume();
		loginState=sp.getBoolean(SPkeys.loginState.getString(), false);
		if (!loginState) {
			hasLogin_rl.setVisibility(View.GONE);
			noLogin_rl.setVisibility(View.VISIBLE);
		}else {
			hasLogin_rl.setVisibility(View.VISIBLE);
			noLogin_rl.setVisibility(View.GONE);
		}
	}

}
