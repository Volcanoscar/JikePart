package com.jike.shanglv;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnHoverListener;
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
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;

@SuppressWarnings({ "deprecation", "unused" })
public class HomeActivity extends Activity  implements AnimationListener{

	public static HomeActivity instance = null;
	private SharedPreferences sp;
	private ImageButton btn_gnjp, btn_gjjp, btn_hbdt, btn_jd, btn_tg, btn_hfcz,
			btn_jdmp, btn_zhcz, btn_hcp;
	
	Animation animScal;
    
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
		
		btn_gnjp.setOnHoverListener(new OnHoverListener() {
			@Override
			public boolean onHover(View arg0, MotionEvent arg1) {
				btn_gnjp.startAnimation(animScal);
				return true;
			}
		});
		
		animScal = AnimationUtils.loadAnimation(getApplicationContext(),
	            R.anim.home_drawable_zoom_out);
		animScal.setAnimationListener(new AnimationListener() {
            
            @Override
            public void onAnimationStart(Animation animation) {
                    
            }
            
            @Override
            public void onAnimationRepeat(Animation animation) {
                    
            }
            
            @Override
            public void onAnimationEnd(Animation animation) {
                    //动画执行完后的动作
            }
    });
	}
	
	
	@Override
	public void onAnimationEnd(Animation arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAnimationRepeat(Animation arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAnimationStart(Animation arg0) {
		// TODO Auto-generated method stub
		
	}
	

	@Override
	protected void onStart() {
		super.onStart();
		SharedPreferences.Editor edit = sp.edit();  
		edit.putLong("adjustFontSize", adjustFontSize());  
		edit.commit();//将按屏幕分辨率计算好的合适文字大小存起来供后面的自定义控件使用
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
