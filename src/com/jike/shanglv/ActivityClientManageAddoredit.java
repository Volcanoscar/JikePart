package com.jike.shanglv;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import com.jike.shanglv.Common.ClearEditText;
import com.jike.shanglv.Common.CommonFunc;
import com.jike.shanglv.Common.CustomProgressDialog;
import com.jike.shanglv.Common.CustomerAlertDialog;
import com.jike.shanglv.Common.SelectProvinceCityAlertDialog;
import com.jike.shanglv.Enums.PackageKeys;
import com.jike.shanglv.Enums.SPkeys;
import com.jike.shanglv.NetAndJson.HttpUtils;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ActivityClientManageAddoredit extends Activity {

	protected static final int ADD_CUSTOMER_MSG_CODE = 0;
	private Context context;
	private TextView title_tv, province_city_tv, cancel_tv, finish_tv,
			startValidDay_tv, endValidDay_tv;
	private LinearLayout dealer_extra_info_ll;
	private com.jike.shanglv.Common.ClearEditText username_et, password_et,
			comfirmPassword_et, contactPerson_et, contactPhone_et,
			companyName_et;
	private String addAction = "", startValidDay = "", endValidDay = "",
			addReturnJson = "";
	private SharedPreferences sp;
	private CustomProgressDialog progressdialog;
	MyBroadcastReceiver receiverCity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_clientmanage_addoredit_client);
		initView();
	}

	private void initView() {
		// 注册广播接收器
		receiverCity = new MyBroadcastReceiver();
		IntentFilter filter = new IntentFilter("com.province_city.rocky");
		registerReceiver(receiverCity, filter);
		context = this;
		sp = getSharedPreferences(SPkeys.SPNAME.getString(), 0);
		province_city_tv = (TextView) findViewById(R.id.province_city_tv);
		province_city_tv.setOnClickListener(onClickListener);
		cancel_tv = (TextView) findViewById(R.id.cancel_tv);
		province_city_tv.setOnClickListener(onClickListener);
		finish_tv = (TextView) findViewById(R.id.finish_tv);
		cancel_tv.setOnClickListener(onClickListener);
		finish_tv.setOnClickListener(onClickListener);
		province_city_tv.setOnClickListener(onClickListener);
		username_et = (ClearEditText) findViewById(R.id.username_et);
		password_et = (ClearEditText) findViewById(R.id.password_et);
		comfirmPassword_et = (ClearEditText) findViewById(R.id.comfirmPassword_et);
		contactPerson_et = (ClearEditText) findViewById(R.id.contactPerson_et);
		contactPhone_et = (ClearEditText) findViewById(R.id.contactPhone_et);
		dealer_extra_info_ll = (LinearLayout) findViewById(R.id.dealer_extra_info_ll);
		companyName_et = (ClearEditText) findViewById(R.id.companyName_et);
		startValidDay_tv = (TextView) findViewById(R.id.startValidDay_tv);
		endValidDay_tv = (TextView) findViewById(R.id.endValidDay_tv);
		startValidDay_tv.setOnClickListener(onClickListener);
		endValidDay_tv.setOnClickListener(onClickListener);
		title_tv = (TextView) findViewById(R.id.title_tv);

		Bundle bundle = new Bundle();
		bundle = getIntent().getExtras();
		String displayName = "";
		if (bundle != null) {
			displayName = bundle
					.containsKey(ActivitySetClientGrad.DISPLAY_TYPENAME_STRING) ? bundle
					.getString(ActivitySetClientGrad.DISPLAY_TYPENAME_STRING)
					: "";
			title_tv.setText("添加" + displayName);
		}
		if (displayName.equals(ActivitySetClientGrad.CUSTOMER_DISPLAYNAME)) {
			addAction = "addcustomeruser";
			dealer_extra_info_ll.setVisibility(View.GONE);
		} else if (displayName.equals(ActivitySetClientGrad.DEALER_DISPLAYNAME)) {
			addAction = "adddealeruser";
			dealer_extra_info_ll.setVisibility(View.VISIBLE);
		}
	}
	
	  public void onDestroy() {
	         unregisterReceiver( receiverCity);
	         super.onDestroy();
	     }


	View.OnClickListener onClickListener = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
			Date date = null;
			switch (arg0.getId()) {
			case R.id.province_city_tv:
				SelectProvinceCityAlertDialog sad = new SelectProvinceCityAlertDialog(
						ActivityClientManageAddoredit.this);
				break;
			case R.id.cancel_tv:
				finish();
				break;
			case R.id.finish_tv:
				if(validInput())startAdd();
				break;
			case R.id.startValidDay_tv:
				Calendar c1 = Calendar.getInstance();
				try {
					if (!startValidDay.isEmpty()) {
						date = sdf.parse(startValidDay);
						c1.setTime(date);
					}
				} catch (java.text.ParseException e) {
					e.printStackTrace();
				}
				new DatePickerDialog(context,
						new DatePickerDialog.OnDateSetListener() {
							@Override
							public void onDateSet(DatePicker view, int year,
									int monthOfYear, int dayOfMonth) {
								startValidDay_tv.setText(year + "/"
										+ (monthOfYear + 1) + "/" + dayOfMonth);
								startValidDay = year + "/" + (monthOfYear + 1)
										+ "/" + dayOfMonth;
							}
						}, c1.get(Calendar.YEAR), c1.get(Calendar.MONTH), c1
								.get(Calendar.DAY_OF_MONTH)).show();
				break;
			case R.id.endValidDay_tv:
				Calendar c11 = Calendar.getInstance();
				try {
					if (!startValidDay.isEmpty()) {
						date = sdf.parse(endValidDay);
						c11.setTime(date);
					}
				} catch (java.text.ParseException e) {
					e.printStackTrace();
				}
				new DatePickerDialog(context,
						new DatePickerDialog.OnDateSetListener() {
							@Override
							public void onDateSet(DatePicker view, int year,
									int monthOfYear, int dayOfMonth) {
								endValidDay_tv.setText(year + "/"
										+ (monthOfYear + 1) + "/" + dayOfMonth);
								endValidDay = year + "/" + (monthOfYear + 1)
										+ "/" + dayOfMonth;
							}
						}, c11.get(Calendar.YEAR), c11.get(Calendar.MONTH), c11
								.get(Calendar.DAY_OF_MONTH)).show();
				break;
			default:
				break;
			}
		}
	};

	public class MyBroadcastReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			String content = intent.getStringExtra("msgContent");
			province_city_tv.setText(content);
		}
	}

	private void startAdd() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				MyApp ma = new MyApp(context);
				String provinceCityString = province_city_tv.getText()
						.toString().trim();
				String city = "", province = "";
				if (provinceCityString.length() > 0
						&& provinceCityString.contains("-")) {
					city = provinceCityString.substring(0,
							provinceCityString.indexOf("-") - 1);
					province = provinceCityString.substring(provinceCityString
							.indexOf("-") + 1);
				}
				String str="";
				if(addAction.equals("addcustomeruser"))
					str = "{\"userID\":\""
						+ sp.getString(SPkeys.userid.getString(), "")
						+ "\",\"userName\":\""
						+ username_et.getText().toString().trim()
						+ "\",\"userPass\":\""
						+ password_et.getText().toString().trim()
						+ "\",\"contactName\":\""
						+ contactPerson_et.getText().toString().trim()
						+ "\",\"contactPhone\":\""
						+ contactPhone_et.getText().toString().trim()
						+ "\",\"province\":\"" + province + "\",\"city\":\""
						+ city + "\"}";
				else if (addAction.equals("adddealeruser")) {
					str = "{\"userID\":\""
							+ sp.getString(SPkeys.userid.getString(), "")
							+ "\",\"userName\":\""
							+ username_et.getText().toString().trim()
							+ "\",\"userPass\":\""
							+ password_et.getText().toString().trim()
							+ "\",\"contactName\":\""
							+ contactPerson_et.getText().toString().trim()
							+ "\",\"contactPhone\":\""
							+ contactPhone_et.getText().toString().trim()
							+ "\",\"startDate\":\""
							+ startValidDay
							+ "\",\"endDate\":\""
							+ endValidDay
							+ "\",\"companyName\":\""
							+ companyName_et.getText().toString().trim()
							+ "\",\"province\":\"" + province 
							+ "\",\"city\":\""+ city + "\"}";
				}
				String param = "action="
						+ addAction
						+ "&sitekey="
						+ MyApp.sitekey
						+ "&userkey="
						+ ma.getHm().get(PackageKeys.USERKEY.getString())
								.toString()
						+ "&sign="
						+ CommonFunc.MD5(ma.getHm()
								.get(PackageKeys.USERKEY.getString())
								.toString()
								+ addAction + str);
				try {
					str=URLEncoder.encode(str,"utf-8");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
//				addReturnJson = HttpUtils.myPost(ma.getServeUrl() + param,
//						"&str=" + str);
				addReturnJson = HttpUtils.getJsonContent(
						ma.getServeUrl(), param+"&str="+str);
				Message msg = new Message();
				msg.what = ADD_CUSTOMER_MSG_CODE;
				handler.sendMessage(msg);
			}
		}).start();
		progressdialog = CustomProgressDialog.createDialog(context);
		progressdialog.setMessage("正在注册新用户，请稍候...");
		progressdialog.setCancelable(true);
		progressdialog.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
			}
		});
		progressdialog.show();
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			JSONTokener jsonParser;
			switch (msg.what) {
			case ADD_CUSTOMER_MSG_CODE:
				jsonParser = new JSONTokener(addReturnJson);
				progressdialog.dismiss();
				try {
					JSONObject jsonObject = (JSONObject) jsonParser.nextValue();
					String state = jsonObject.getString("c");
					String message="";
//					if (state.equals("0000")) {
						message=jsonObject.getJSONObject("d").getString("msg");
						final CustomerAlertDialog cad=new CustomerAlertDialog(context,true);
						cad.setTitle(message);
						cad.setPositiveButton("确定", new OnClickListener(){
							@Override
							public void onClick(View arg0) {
								cad.dismiss();
							}});
//					} else {
//						Toast.makeText(context, "发生未知异常，注册失败！", 0).show();
//					}
				} catch (JSONException e) {
					progressdialog.dismiss();
					e.printStackTrace();
					Toast.makeText(context, "发生未知异常，注册失败！", 0).show();
				}
				break;
			}
		}
	};
	
	private Boolean validInput(){
		final CustomerAlertDialog cad=new CustomerAlertDialog(context,true);
//		 , contactPhone_et, ;
		if (!CommonFunc.isValidUserName(username_et.getText()
				.toString().trim())) {
			cad.setTitle("请输入用户名(由字母、数字或下划线组成长度为6-12位)");
			cad.setPositiveButton("确定", new OnClickListener(){
				@Override
				public void onClick(View arg0) {
					cad.dismiss();
				}});
			return false;
		}
		if (!CommonFunc.isValidPassword(password_et.getText()
				.toString().trim())) {
			cad.setTitle("为保证密码的安全性，请输入6-20位的数字或字母的组合！");
			cad.setPositiveButton("确定", new OnClickListener(){
				@Override
				public void onClick(View arg0) {
					cad.dismiss();
				}});
			return false;
		}
		if (contactPerson_et.getText().toString().trim().isEmpty()) {
			cad.setTitle("请输入联系人姓名");
			cad.setPositiveButton("确定", new OnClickListener(){
				@Override
				public void onClick(View arg0) {
					cad.dismiss();
				}});
			return false;
		}
		if (comfirmPassword_et.getText().toString().trim().isEmpty()) {
			cad.setTitle("请再次输入密码");
			cad.setPositiveButton("确定", new OnClickListener(){
				@Override
				public void onClick(View arg0) {
					cad.dismiss();
				}});
			return false;
		}
		if (!CommonFunc.isMobileNO(contactPhone_et.getText().toString().trim())) {
			cad.setTitle("请输入合法的手机号码");
			cad.setPositiveButton("确定", new OnClickListener(){
				@Override
				public void onClick(View arg0) {
					cad.dismiss();
				}});
			return false;
		}
		if (province_city_tv.getText().toString().trim().isEmpty()) {
			cad.setTitle("请选择用户所在城市");
			cad.setPositiveButton("确定", new OnClickListener(){
				@Override
				public void onClick(View arg0) {
					cad.dismiss();
				}});
			return false;
		}
		if (!password_et.getText().toString().trim().equals(comfirmPassword_et.getText().toString().trim())) {
			cad.setTitle("两次输入密码 不一致");
			cad.setPositiveButton("确定", new OnClickListener(){
				@Override
				public void onClick(View arg0) {
					cad.dismiss();
				}});
			return false;
		}
		if (addAction.equals("adddealeruser")&&companyName_et.getText().toString().trim().isEmpty()) {
			cad.setTitle("请输入客户公司名");
			cad.setPositiveButton("确定", new OnClickListener(){
				@Override
				public void onClick(View arg0) {
					cad.dismiss();
				}});
			return false;
		}
		if (addAction.equals("adddealeruser")&&startValidDay.equals("")) {
			cad.setTitle("选择用户有效期开始日期");
			cad.setPositiveButton("确定", new OnClickListener(){
				@Override
				public void onClick(View arg0) {
					cad.dismiss();
				}});
			return false;
		}
		if (addAction.equals("adddealeruser")&&endValidDay.equals("")) {
			cad.setTitle("选择用户有效期结束日期");
			cad.setPositiveButton("确定", new OnClickListener(){
				@Override
				public void onClick(View arg0) {
					cad.dismiss();
				}});
			return false;
		}
		cad.dismiss();
		
		return true;
	}

}
