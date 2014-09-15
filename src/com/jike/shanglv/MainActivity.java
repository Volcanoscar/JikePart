package com.jike.shanglv;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
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
public class MainActivity extends ActivityGroup  implements OnCheckedChangeListener{

	public static MainActivity instance = null;
	private RadioGroup radio_group;
	private Intent mIntent;
	private ViewFlipper container;
	private RadioButton radio_order,radio_home,radio_mine,radio_more; 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initView();
		initHomePage();
		radio_group.setOnCheckedChangeListener(this);
	}
	
	private void switchPage(int positoon) {
		switch (positoon) {
		case 0:
			mIntent = new Intent(this, OrderActivity.class);
			break;
		case 1:
			mIntent = new Intent(this, HomeActivity.class);
			break;
		case 2:
			mIntent = new Intent(this, MineActivity.class);
			break;
		case 3:
			mIntent = new Intent(this, MoreActivity.class);
			break;

		default:
			break;
		}
		
		container.removeAllViews();
		    mIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		    Window subActivity = getLocalActivityManager().startActivity("subActivity", mIntent);
		container.addView(subActivity.getDecorView(), new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,
		        LayoutParams.FILL_PARENT));
	}
	/**
	 * 根据checkedId来判断选定的Radio，从而进行页面的换
	 */
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.radio_order:
			switchPage(0);
			break;
		case R.id.radio_home:
			switchPage(1);
			break;
		case R.id.radio_mine:
			switchPage(2);
			break;
		case R.id.radio_more:
			switchPage(3);
			break;
		default:
			break;
		}
	}
	
	private void initHomePage()
	{
		container.removeAllViews();
		mIntent = new Intent(this, HomeActivity.class);
		    mIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		    Window subActivity = getLocalActivityManager().startActivity("subActivity", mIntent);
		container.addView(subActivity.getDecorView(), new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,
		        LayoutParams.FILL_PARENT));
	}

	/**
	 * 初始化各种控件
	 */
	private void initView() {
		container = (ViewFlipper) findViewById(R.id.container);
		radio_group = (RadioGroup) findViewById(R.id.radio_group);
		radio_order=(RadioButton)findViewById(R.id.radio_order);
		radio_home=(RadioButton)findViewById(R.id.radio_home);
		radio_mine=(RadioButton)findViewById(R.id.radio_mine);
		radio_more=(RadioButton)findViewById(R.id.radio_more);
	}

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
}
