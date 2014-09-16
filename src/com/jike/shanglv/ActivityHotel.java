package com.jike.shanglv;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.jike.shanglv.Common.ClearEditText;
import com.jike.shanglv.Common.DateUtil;
import com.jike.shanglv.Enums.SPkeys;
import com.jike.shanglv.NetAndJson.HttpUtils;

public class ActivityHotel extends Activity {
	
	private final int ruzhudate = 0,lidiandate = 1,ruzhucity=2;
	private ImageButton back_imgbtn,home_imgbtn;
	private TextView city_tv,ruzhu_date_tv,lidian_date_tv,xingji_tv,jiage_tv;
	private com.jike.shanglv.Common.ClearEditText keywords_et;
	private LinearLayout city_ll,my_position_ll,ruzhu_date_ll,lidian_date_ll,xingji_ll,jiage_ll;
	private Button search_button,buxian_xingji_btn,wuxingji_btn,sixingji_btn,sanxingji_btn,erxingji_btn,
			buxian_price_btn,ybw_yixia_btn,ybw_sb_btn,sly_swl_btn,swy_lb_btn,lly_yq_btn,yq_yishang_btn;
	private Context context;
	private PopupWindow popupWindow_xingji, popupWindow_jiage;
	private View popupWindowView_xingji, popupWindowView_jiage;
	InputMethodManager imm;
	private SharedPreferences sp;
	private Boolean isNearby=false;
	private double latitude,longtitude;
	private String myaddress="";
	private int errorCode;//定位结果
	private LocationClient mLocationClient;
	
	int height_xingji=550;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_hotel);
		initView();
		myNear();
	}
	private void myNear(){
		mLocationClient.start();
		mLocationClient.requestLocation();
		city_tv.setText("我附近的酒店");
		isNearby=true;
	}
	private void initView() {
		context=this;
		sp=getSharedPreferences(SPkeys.SPNAME.getString(), 0);
		imm = (InputMethodManager) getSystemService(ActivityHotel.this.INPUT_METHOD_SERVICE);
		
		mLocationClient = new LocationClient(this.getApplicationContext());
		mLocationClient.registerLocationListener(new MyLocationListener());
		InitLocation();
		
		back_imgbtn=(ImageButton) findViewById(R.id.back_imgbtn);
		home_imgbtn=(ImageButton) findViewById(R.id.home_imgbtn);
		city_tv=(TextView) findViewById(R.id.city_tv); 
		ruzhu_date_tv=(TextView) findViewById(R.id.ruzhu_date_tv); 
		lidian_date_tv=(TextView) findViewById(R.id.lidian_date_tv); 
		xingji_tv=(TextView) findViewById(R.id.xingji_tv); 
		jiage_tv=(TextView) findViewById(R.id.jiage_tv); 
		keywords_et=(ClearEditText) findViewById(R.id.keywords_et);
		my_position_ll=(LinearLayout) findViewById(R.id.my_position_ll); 
		ruzhu_date_ll=(LinearLayout) findViewById(R.id.ruzhu_date_ll);
		lidian_date_ll=(LinearLayout) findViewById(R.id.lidian_date_ll);
		xingji_ll=(LinearLayout) findViewById(R.id.xingji_ll);
		jiage_ll=(LinearLayout) findViewById(R.id.jiage_ll);
		city_ll =(LinearLayout) findViewById(R.id.city_ll);
		city_ll.setOnClickListener(clickListener);
		back_imgbtn.setOnClickListener(clickListener);
		home_imgbtn.setOnClickListener(clickListener);
		my_position_ll.setOnClickListener(clickListener);
		ruzhu_date_ll.setOnClickListener(clickListener);
		lidian_date_ll.setOnClickListener(clickListener);
		xingji_ll.setOnClickListener(clickListener);
		jiage_ll.setOnClickListener(clickListener);
		
		ruzhu_date_tv.setText(DateUtil.GetDateAfterToday(1));
		lidian_date_tv.setText(DateUtil.GetDateAfterToday(2));
		
		search_button=(Button) findViewById(R.id.chongzhi_button);
		search_button.setOnClickListener(clickListener);
		LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		popupWindowView_xingji = inflater.inflate(
				R.layout.popupwindow_hotel_startlevel, null);
		
		// 控制popupwindow的宽度和高度自适应
//		popupWindow_xingji = new PopupWindow(popupWindowView_xingji);
//		popupWindow_xingji.setWidth(popupWindowView_xingji.getMeasuredWidth());
//		popupWindow_xingji.setHeight(popupWindowView_xingji.getMeasuredHeight() + 20);
//		popupWindow_xingji.setFocusable(true);
        
	    popupWindow_xingji = new PopupWindow(popupWindowView_xingji,LayoutParams.FILL_PARENT, height_xingji, true);
		popupWindow_xingji.setBackgroundDrawable(new BitmapDrawable());
		// 设置PopupWindow的弹出和消失效果
		popupWindow_xingji.setAnimationStyle(R.style.AnimBottomPopup);

		popupWindowView_jiage = inflater.inflate(
				R.layout.popupwindow_hotel_price, null);
		popupWindow_jiage = new PopupWindow(popupWindowView_jiage,
				LayoutParams.FILL_PARENT, 660, true);
		popupWindow_jiage.setBackgroundDrawable(new BitmapDrawable());
		// 设置PopupWindow的弹出和消失效果
		popupWindow_jiage.setAnimationStyle(R.style.AnimBottomPopup);
	
		
		buxian_xingji_btn=(Button) popupWindowView_xingji.findViewById(R.id.buxian_xingji_btn);
		wuxingji_btn=(Button) popupWindowView_xingji.findViewById(R.id.wuxingji_btn);
		sixingji_btn=(Button) popupWindowView_xingji.findViewById(R.id.sixingji_btn);
		sanxingji_btn=(Button) popupWindowView_xingji.findViewById(R.id.sanxingji_btn);
		erxingji_btn=(Button) popupWindowView_xingji.findViewById(R.id.erxingji_btn);
		buxian_price_btn=(Button) popupWindowView_jiage.findViewById(R.id.buxian_price_btn);
		ybw_yixia_btn=(Button) popupWindowView_jiage.findViewById(R.id.ybw_yixia_btn);
		ybw_sb_btn=(Button) popupWindowView_jiage.findViewById(R.id.ybw_sb_btn);
		sly_swl_btn=(Button) popupWindowView_jiage.findViewById(R.id.sly_swl_btn);
		swy_lb_btn=(Button) popupWindowView_jiage.findViewById(R.id.swy_lb_btn);
		lly_yq_btn=(Button) popupWindowView_jiage.findViewById(R.id.lly_yq_btn);
		yq_yishang_btn=(Button) popupWindowView_jiage.findViewById(R.id.yq_yishang_btn);
		buxian_xingji_btn.setOnClickListener(starLevel_popupClickListener);
		wuxingji_btn.setOnClickListener(starLevel_popupClickListener);
		sixingji_btn.setOnClickListener(starLevel_popupClickListener);
		sanxingji_btn.setOnClickListener(starLevel_popupClickListener);
		erxingji_btn.setOnClickListener(starLevel_popupClickListener);
		buxian_price_btn.setOnClickListener(jiage_tv_popupClickListener);
		ybw_yixia_btn.setOnClickListener(jiage_tv_popupClickListener);
		ybw_sb_btn.setOnClickListener(jiage_tv_popupClickListener);
		sly_swl_btn.setOnClickListener(jiage_tv_popupClickListener);
		swy_lb_btn.setOnClickListener(jiage_tv_popupClickListener);
		lly_yq_btn.setOnClickListener(jiage_tv_popupClickListener);
		yq_yishang_btn.setOnClickListener(jiage_tv_popupClickListener);
	}
	
	private void InitLocation(){
		LocationClientOption option = new LocationClientOption();
//		option.setLocationMode(LocationMode.Hight_Accuracy);//设置定位模式:高精度
		option.setCoorType("gcj02");//返回的定位结果是百度经纬度，默认值gcj02
		int span=1000;
		option.setScanSpan(span);//设置发起定位请求的间隔时间为1000ms
		option.setIsNeedAddress(true);
		mLocationClient.setLocOption(option);
	}
	
	/**
	 * 实现实位回调监听
	 */
	public class MyLocationListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {//Receive Location 
			latitude=location.getLatitude();
			longtitude=location.getLongitude();
			myaddress=location.getAddrStr();
			errorCode=location.getLocType();
//			((TextView)findViewById(R.id.test_tv)).setText(myaddress);
			if (errorCode==63||errorCode==63||errorCode==67||(errorCode>500&&errorCode<701)) {
//				Toast.makeText(context, "网络异常，自动定位失败", 0).show();
				city_tv.setText("上海");
				isNearby=false;
			}
		}
	}
	
	View.OnClickListener clickListener =new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.back_imgbtn:
				finish();
				break;
			case R.id.home_imgbtn:
				startActivity(new Intent(context,MainActivity.class));
				break;
			case R.id.my_position_ll:
				myNear();
				break;
			case R.id.city_ll:
				Intent cityIntent = new Intent();
				cityIntent.setClass(context,
						com.jike.shanglv.SeclectCity.HotelCityActivity.class);
				startActivityForResult(cityIntent, ruzhucity);
				break;
			case R.id.ruzhu_date_ll:
				Intent dateIntent = new Intent();
				dateIntent.setClass(context,
						com.jike.shanglv.ShipCalendar.MainActivity.class);
				startActivityForResult(dateIntent, ruzhudate);
				break;
			case R.id.lidian_date_ll:
				Intent dateIntent1 = new Intent();
				dateIntent1.setClass(context,
						com.jike.shanglv.ShipCalendar.MainActivity.class);
				startActivityForResult(dateIntent1, lidiandate);
				break;
			case R.id.xingji_ll:
				imm.hideSoftInputFromWindow(
						((Activity) context).getCurrentFocus().getWindowToken(),
						InputMethodManager.HIDE_NOT_ALWAYS);
				popupWindow_xingji.showAtLocation(buxian_xingji_btn,
						Gravity.BOTTOM, 0, 0);
//				SelectPicPopupWindow menuWindow = new SelectPicPopupWindow(ActivityHotel.this,getXingjiData(),1,clickListener);
//				menuWindow.setBackgroundDrawable(getApplicationContext().getResources().getDrawable(
//								R.drawable.btn_style_alert_dialog_background));// 设置背景图片，不能在布局中设置，要通过代码来设置
//				menuWindow.setOutsideTouchable(true);// 触摸popupwindow外部，popupwindow消失。这个要求你的popupwindow要有背景图片才可以成功，如上
//				//显示窗口
//				menuWindow.showAtLocation(ActivityHotel.this.findViewById(R.id.main), Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0); //设置layout在PopupWindow中显示的位置
				break;
			case R.id.jiage_ll:
				imm.hideSoftInputFromWindow(
						((Activity) context).getCurrentFocus().getWindowToken(),
						InputMethodManager.HIDE_NOT_ALWAYS);
				popupWindow_jiage.showAtLocation(buxian_price_btn,
						Gravity.BOTTOM, 0, 0);
				break;
			case R.id.chongzhi_button:
				if (!sp.getBoolean(SPkeys.loginState.getString(), false)) {
					startActivity(new Intent(context,Activity_Login.class));
					break;
				}
				if(DateUtil.compareDateIsBefore(ruzhu_date_tv.getText().toString(),lidian_date_tv.getText().toString())){
					new AlertDialog.Builder(context).
					setTitle("入住日期不能大于离店日期")
					.setPositiveButton("知道了", null)
					.show();
					break;
				}
				if (HttpUtils.showNetCannotUse(context)) {
					break;
				}
				if (city_tv.getText().toString().equals("我附近的酒店")) {
					 isNearby=true;
				}
				if (city_tv.getText().toString().equals("我附近的酒店")&&(myaddress==null||myaddress.equals(""))) {//定位失败
					city_tv.setText("上海");
					isNearby=false;
					Toast.makeText(context, "定位失败，请选择城市进行查询", 0).show();
					break;
				}
				Intent intents=new Intent(context,ActivityHotelSearchlist.class);
				intents.putExtra("nearby", isNearby);
				intents.putExtra("latitude", latitude);
				intents.putExtra("longtitude", longtitude);
				intents.putExtra("myaddress", myaddress);
				intents.putExtra("city", city_tv.getText());
				intents.putExtra("ruzhu_date", ruzhu_date_tv.getText().toString());
				intents.putExtra("lidian_date", lidian_date_tv.getText().toString());
				intents.putExtra("starlevel", xingji_tv.getText().toString());
				intents.putExtra("price", jiage_tv.getText().toString());
				intents.putExtra("keywords", keywords_et.getText().toString());
				startActivity(intents);
				
				break;
			default:
				
				break;
			}
		}
	};
	
	public ArrayList<Map<String, Object>> getXingjiData(){
		 ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		  Map<String, Object> map = new HashMap<String, Object>();
		  map.put("title", "No_Baoxian");
		  list.add(map);
		  map = new HashMap<String, Object>();
		  map.put("title", "Baoxian_Five");
		  list.add(map);
		  map = new HashMap<String, Object>();
		  map.put("title", "Baoxian_Ten");
		  list.add(map);
		  return list;
		}
	
	OnClickListener jiage_tv_popupClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Button btn = (Button) v;
			jiage_tv.setText(btn.getText());
			popupWindow_jiage.dismiss();
		}
	};

	OnClickListener starLevel_popupClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Button btn = (Button) v;
			xingji_tv.setText(btn.getText());
			popupWindow_xingji.dismiss();
		}
	};
	
	/*选择城市或日期后结果回显到界面
	 * */
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(data==null)return;
		Bundle b = data.getExtras();
		String myDate = DateUtil.GetTodayDate();// 获取从com.jike.jikepart.ShipCalendar.MainActivity中回传的值
		switch (requestCode) {
		case ruzhudate:
			if (b != null && b.containsKey("pickedDate")) {
				myDate = b.getString("pickedDate");
				ruzhu_date_tv.setText(myDate);
			}
			break;
		case lidiandate:
			if (b != null && b.containsKey("pickedDate")) {
				myDate = b.getString("pickedDate");
				lidian_date_tv.setText(myDate);
			}
			break;
		case ruzhucity:
			if (b != null && b.containsKey("pickedCity")) {
				String myCity = b.getString("pickedCity");
				city_tv.setText(myCity);
				isNearby=false;
			}
			break;
		default:
			break;
		}
	}
}
