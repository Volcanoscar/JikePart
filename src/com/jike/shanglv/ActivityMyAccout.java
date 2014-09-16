package com.jike.shanglv;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.jike.shanglv.Enums.SPkeys;

public class ActivityMyAccout extends Activity {
	private ImageButton back_iv;
	private Button logout_button;
	private TextView username_tv,zhanghuyue_tv,chongzhi_tv,phone_tv,email_tv;
	private SharedPreferences sp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_myaccount);

		sp = getSharedPreferences(SPkeys.SPNAME.getString(), 0);
		back_iv = (ImageButton) findViewById(R.id.back_imgbtn);
		back_iv.setOnClickListener(btnClickListner);
		logout_button=(Button) findViewById(R.id.logout_button);
		logout_button.setOnClickListener(btnClickListner);
		
		username_tv=(TextView) findViewById(R.id.username_tv);
		zhanghuyue_tv=(TextView) findViewById(R.id.zhanghuyue_tv);
		chongzhi_tv=(TextView) findViewById(R.id.chongzhi_tv);
		phone_tv=(TextView) findViewById(R.id.phone_tv);
		email_tv=(TextView) findViewById(R.id.email_tv);
		
		username_tv.setText(sp.getString(SPkeys.username.getString(), ""));
		zhanghuyue_tv.setText(sp.getString(SPkeys.amount.getString(), ""));
		phone_tv.setText(sp.getString(SPkeys.userphone.getString(), ""));
		email_tv.setText(sp.getString(SPkeys.useremail.getString(), ""));
		chongzhi_tv.setOnClickListener(btnClickListner);
		((RelativeLayout) findViewById(R.id.changePsw_rl)).setOnClickListener(btnClickListner);
		((RelativeLayout) findViewById(R.id.findZfPsw_rl)).setOnClickListener(btnClickListner);
	}

	View.OnClickListener btnClickListner = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.back_imgbtn:
				startActivity(new Intent(ActivityMyAccout.this, MineActivity.class));
				break;
			case R.id.logout_button:
				sp.edit().putString(SPkeys.userid.getString(),"").commit();
				sp.edit().putString(SPkeys.username.getString(),"").commit();
				sp.edit().putBoolean(SPkeys.loginState.getString(), false).commit();
				finish();
				break;
			case R.id.chongzhi_tv:
				startActivity(new Intent(ActivityMyAccout.this, ActivityZhanghuchongzhi.class));
				break;
			case R.id.changePsw_rl:
				startActivity(new Intent(ActivityMyAccout.this, ActivityChangePsw.class));
				break;
			case R.id.findZfPsw_rl:
				startActivity(new Intent(ActivityMyAccout.this, ActivityConfirmInfoBeforeFindZfpsw.class));
				break;
			default:
				break;
			}
		}
	};

}
