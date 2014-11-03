package com.jike.shanglv;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import com.jike.shanglv.Common.MyRolateAnimImageView;
import com.jike.shanglv.Enums.PackageKeys;

public class HomeActivity extends Activity {

	public static HomeActivity instance = null;
	private SharedPreferences sp;
	// private ImageButton btn_gnjp, btn_gjjp, btn_hbdt, btn_jd, btn_tg,
	// btn_hfcz,
	// btn_jdmp, btn_zhcz, btn_hcp;

	private MyRolateAnimImageView btn_gnjp, btn_gjjp, btn_hbdt, btn_jd, btn_tg,
			btn_hfcz, btn_jdmp, btn_zhcz, btn_hcp;
	private Context context;

	// AppUpdate appUpdate;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_home2);
			context = this;
			((MyApplication) getApplication()).addActivity(this);
			sp = this.getSharedPreferences("mySPData", Context.MODE_PRIVATE);

			// UpdateChecker.checkForDialog(MainActivity.this,"jike");
			/*
			 * btn_gnjp = (ImageButton) findViewById(R.id.imgBtn_gnjp); btn_gjjp
			 * = (ImageButton) findViewById(R.id.imgBtn_gjjp); btn_hbdt =
			 * (ImageButton) findViewById(R.id.imgBtn_hbdt); btn_hfcz =
			 * (ImageButton) findViewById(R.id.imgBtn_hfcz); btn_zhcz =
			 * (ImageButton) findViewById(R.id.imgBtn_zhcz); btn_hcp =
			 * (ImageButton) findViewById(R.id.imgBtn_hcp); btn_jd =
			 * (ImageButton) findViewById(R.id.imgBtn_jd);
			 * 
			 * // btn_tg = (ImageButton) findViewById(R.id.imgBtn_tg); //
			 * btn_jdmp = (ImageButton) findViewById(R.id.imgBtn_jdmp); //
			 * btn_tg.setOnClickListener(btnClickListner); //
			 * btn_jdmp.setOnClickListener(btnClickListner);
			 */
			btn_gnjp = (MyRolateAnimImageView) findViewById(R.id.imgBtn_gnjp);
			btn_gjjp = (MyRolateAnimImageView) findViewById(R.id.imgBtn_gjjp);
			btn_hbdt = (MyRolateAnimImageView) findViewById(R.id.imgBtn_hbdt);
			btn_hfcz = (MyRolateAnimImageView) findViewById(R.id.imgBtn_hfcz);
			btn_zhcz = (MyRolateAnimImageView) findViewById(R.id.imgBtn_zhcz);
			btn_hcp = (MyRolateAnimImageView) findViewById(R.id.imgBtn_hcp);
			btn_jd = (MyRolateAnimImageView) findViewById(R.id.imgBtn_jd);
			btn_gnjp.setOnClickListener(btnClickListner);
			btn_gjjp.setOnClickListener(btnClickListner);
			btn_hbdt.setOnClickListener(btnClickListner);
			btn_jd.setOnClickListener(btnClickListner);
			btn_hfcz.setOnClickListener(btnClickListner);
			btn_zhcz.setOnClickListener(btnClickListner);
			btn_hcp.setOnClickListener(btnClickListner);

			MyApp mApp = new MyApp(getApplicationContext());
			((ImageView) findViewById(R.id.menu_logo))
					.setBackgroundResource((Integer) mApp.getHm().get(
							PackageKeys.MENU_LOGO_DRAWABLE.getString()));

			// 检查更新
			// UpdateChecker.checkForDialog(HomeActivity.this,"jike");
			// appUpdate = AppUpdateService.getAppUpdate(this);
			// appUpdate.checkLatestVersion("UPDATE_URL",
			// new SimpleJSONParser());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
		SharedPreferences.Editor edit = sp.edit();
		edit.putLong("adjustFontSize", adjustFontSize());
		edit.commit();// 将按屏幕分辨率计算好的合适文字大小存起来供后面的自定义控件使用
	}

	View.OnClickListener btnClickListner = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			try {
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
				// case R.id.imgBtn_tg:
				// startActivity(new Intent(HomeActivity.this,
				// Guojijipiao_Search.class));
				// break;
				case R.id.imgBtn_hfcz:
					startActivity(new Intent(HomeActivity.this,
							ActivityHuafeichongzhi.class));
					break;
				// case R.id.imgBtn_jdmp:
				// startActivity(new Intent(HomeActivity.this,
				// Guojijipiao_Search.class));
				// break;
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
			} catch (Exception e) {
				e.printStackTrace();
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
	 * 根据屏幕分辨率获取字体大小(例如城市列表右侧的字母搜索条)
	 */
	public int adjustFontSize() {
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenWidth = dm.widthPixels; // 获取分辨率宽度
		int screenHeight = dm.heightPixels;
		screenWidth = screenWidth > screenHeight ? screenWidth : screenHeight;
		int rate = (int) (6 * (float) screenWidth / 320);
		return rate < 16 ? 16 : rate; // 字体不要太小
	}
}
