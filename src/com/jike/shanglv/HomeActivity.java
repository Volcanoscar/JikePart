package com.jike.shanglv;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ViewFlipper;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.view.WindowManager;
import android.app.Activity;
import android.app.ActivityGroup;
import android.app.LocalActivityManager;
import android.view.Display;

@SuppressWarnings({ "deprecation", "unused" })
public class HomeActivity extends Activity {

	public static HomeActivity instance = null;
	private SharedPreferences sp;
	private ImageButton btn_gnjp, btn_gjjp, btn_hbdt, btn_jd, btn_tg, btn_hfcz,
			btn_jdmp, btn_zhcz, btn_hcp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);

		sp=this.getSharedPreferences("mySPData",Activity.MODE_PRIVATE);
		btn_gnjp = (ImageButton) findViewById(R.id.imgBtn_gnjp);
		btn_gjjp = (ImageButton) findViewById(R.id.imgBtn_gjjp);
		btn_hbdt = (ImageButton) findViewById(R.id.imgBtn_hbdt);
		btn_jd = (ImageButton) findViewById(R.id.imgBtn_jd);
		btn_tg = (ImageButton) findViewById(R.id.imgBtn_tg);
		btn_hfcz = (ImageButton) findViewById(R.id.imgBtn_hfcz);
		btn_jdmp = (ImageButton) findViewById(R.id.imgBtn_jdmp);
		btn_zhcz = (ImageButton) findViewById(R.id.imgBtn_zhcz);
		btn_hcp = (ImageButton) findViewById(R.id.imgBtn_hcp);
		btn_gnjp.setOnClickListener(btnClickListner);
		btn_gjjp.setOnClickListener(btnClickListner);
		btn_hbdt.setOnClickListener(btnClickListner);
		btn_jd.setOnClickListener(btnClickListner);
		btn_tg.setOnClickListener(btnClickListner);
		btn_hfcz.setOnClickListener(btnClickListner);
		btn_jdmp.setOnClickListener(btnClickListner);
		btn_zhcz.setOnClickListener(btnClickListner);
		btn_hcp.setOnClickListener(btnClickListner);
	}

	@Override
	protected void onStart() {
		super.onStart();
		SharedPreferences.Editor edit = sp.edit();  
		edit.putLong("adjustFontSize", adjustFontSize());  
		edit.commit();//������Ļ�ֱ��ʼ���õĺ������ִ�С��������������Զ���ؼ�ʹ��
	}

	View.OnClickListener btnClickListner = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.imgBtn_gnjp:
				startActivity(new Intent(HomeActivity.this,
					ActivityInlandAirlineticket.class));
				break;
			case R.id.imgBtn_gjjp:
				startActivity(new Intent(HomeActivity.this,
						ActivityInternationalAirlineticket.class));
				break;
			case R.id.imgBtn_hbdt:
				startActivity(new Intent(HomeActivity.this,
						ActivityHangbandongtai.class));
				break;
			case R.id.imgBtn_jd:
				startActivity(new Intent(HomeActivity.this,
						ActivityHotel.class));
				break;
//			case R.id.imgBtn_tg:
//				startActivity(new Intent(HomeActivity.this,
//						Guojijipiao_Search.class));
//				break;
			case R.id.imgBtn_hfcz:
				startActivity(new Intent(HomeActivity.this,
						ActivityHuafeichongzhi.class));
				break;
//			case R.id.imgBtn_jdmp:
//				startActivity(new Intent(HomeActivity.this,
//						Guojijipiao_Search.class));
//				break;
			case R.id.imgBtn_zhcz:
				startActivity(new Intent(HomeActivity.this,
						ActivityZhanghuchongzhi.class));
				break;
			case R.id.imgBtn_hcp:
				startActivity(new Intent(HomeActivity.this,
						ActivityTrain.class));
				break;
			default:
				break;
			}
		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}
	
	/* 
	 * ������Ļ�ֱ��ʻ�ȡ�����С(��������б��Ҳ����ĸ������)
	*/
	public int adjustFontSize() {
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenWidth = dm.widthPixels; // ��ȡ�ֱ��ʿ���
		int screenHeight = dm.heightPixels;
		screenWidth = screenWidth > screenHeight ? screenWidth : screenHeight;
		int rate = (int) (6 * (float) screenWidth / 320);
		return rate < 16 ? 16 : rate; // ���岻Ҫ̫С
	}
}