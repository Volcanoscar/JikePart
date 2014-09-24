package com.jike.shanglv;

import com.jike.shanglv.Enums.SPkeys;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

public class MoreActivity extends Activity {
	private ImageButton back_iv;
	private SharedPreferences sp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_more);
		sp=getSharedPreferences(SPkeys.SPNAME.getString(), 0);
		
		 back_iv = (ImageButton) findViewById(R.id.back_imgbtn);
		 back_iv.setOnClickListener(btnClickListner);
		 ((RelativeLayout)findViewById(R.id.yjfh_rl)).setOnClickListener(btnClickListner);
		 ((RelativeLayout)findViewById(R.id.gwpf_rl)).setOnClickListener(btnClickListner);
		 ((RelativeLayout)findViewById(R.id.jcgx_rl)).setOnClickListener(btnClickListner);
		 ((RelativeLayout)findViewById(R.id.gyslgj_rl)).setOnClickListener(btnClickListner);
		 ((RelativeLayout)findViewById(R.id.xbjs_rl)).setOnClickListener(btnClickListner);
	}
	
	View.OnClickListener btnClickListner = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent=new Intent(MoreActivity.this,Activity_Web_Frame.class);
			switch (v.getId()) {
			case R.id.back_imgbtn:
				startActivity(new Intent(MoreActivity.this, MainActivity.class));
				break;
			case R.id.yjfh_rl:
				intent.putExtra(Activity_Web_Frame.TITLE, "意见反馈");
				intent.putExtra(Activity_Web_Frame.URL, "http://www.baidu.com");
				startActivity(intent);
				break;
			case R.id.gwpf_rl:
				intent.putExtra(Activity_Web_Frame.TITLE, "给我评分");
				intent.putExtra(Activity_Web_Frame.URL, "http://www.163.com");
				startActivity(intent);
				break;
			case R.id.xbjs_rl:
				startActivity(new Intent(MoreActivity.this,GuideActivity.class));
				break;
			case R.id.jcgx_rl:
				break;
			case R.id.gyslgj_rl:
				intent.putExtra(Activity_Web_Frame.TITLE, "关  于");
				MyApp ma=new MyApp(getApplicationContext());
//				http://m.51jp.cn/About/?siteid=65&userid=6266&os=android
				intent.putExtra(Activity_Web_Frame.URL, 
						String.format(ma.getAbout(),
						sp.getString(SPkeys.userid.getString(), ""),
						sp.getString(SPkeys.siteid.getString(), "")
						));
				startActivity(intent);
				break;
			default:
				break;
			}
		}
	};
}
