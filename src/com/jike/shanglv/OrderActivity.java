package com.jike.shanglv;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

@SuppressWarnings("unused")
public class OrderActivity extends Activity {

	private ImageView back_iv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order);
		
	    back_iv = (ImageView) findViewById(R.id.back_iv);
	    back_iv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
	    ((RelativeLayout)findViewById(R.id.gnjp_rl)).setOnClickListener(btnClickListner);
	    ((RelativeLayout)findViewById(R.id.gjjp_rl)).setOnClickListener(btnClickListner);
	    ((RelativeLayout)findViewById(R.id.jd_rl)).setOnClickListener(btnClickListner);
	    ((RelativeLayout)findViewById(R.id.hcp_rl)).setOnClickListener(btnClickListner);
	    ((RelativeLayout)findViewById(R.id.hfcz_rl)).setOnClickListener(btnClickListner);
	    ((RelativeLayout)findViewById(R.id.gjjp_xqd_rl)).setOnClickListener(btnClickListner);
	    ((RelativeLayout)findViewById(R.id.zhcz_rl)).setOnClickListener(btnClickListner);
	}
	
	View.OnClickListener btnClickListner = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent intent=new Intent(OrderActivity.this,ActivityOrderList.class);
			switch (v.getId()) {
			case R.id.gnjp_rl:
				intent.putExtra(ActivityOrderList.ACTION_TOKENNAME, ActivityOrderList.FLIGHT_ORDERLIST);
				intent.putExtra(ActivityOrderList.TITLE_TOKENNAME,"国内机票订单");
				break;
			case R.id.gjjp_xqd_rl:
				intent.putExtra(ActivityOrderList.ACTION_TOKENNAME, ActivityOrderList.DEMAND_ORDERLIST);
				intent.putExtra(ActivityOrderList.TITLE_TOKENNAME,"国际需求订单");
				break;
			case R.id.gjjp_rl:
				intent.putExtra(ActivityOrderList.ACTION_TOKENNAME, ActivityOrderList.INTFLIGHT_ORDERLIST);
				intent.putExtra(ActivityOrderList.TITLE_TOKENNAME,"国际机票订单");
				break;
			case R.id.jd_rl:
				intent.putExtra(ActivityOrderList.ACTION_TOKENNAME, ActivityOrderList.HOTEL_ORDERLIST);
				intent.putExtra(ActivityOrderList.TITLE_TOKENNAME,"酒店订单");
				break;
			case R.id.hcp_rl:
				intent.putExtra(ActivityOrderList.ACTION_TOKENNAME, ActivityOrderList.TRAIN_ORDERLIST);
				intent.putExtra(ActivityOrderList.TITLE_TOKENNAME,"火车票订单");
				break;
			case R.id.hfcz_rl:
				intent.putExtra(ActivityOrderList.ACTION_TOKENNAME, ActivityOrderList.PHONE_ORDERLIST);
				intent.putExtra(ActivityOrderList.TITLE_TOKENNAME,"话费充值订单");
				break;
			case R.id.zhcz_rl:
				break;
			default:
				break;
			}
			startActivity(intent);
		}
	};
}
