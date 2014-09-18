package com.jike.shanglv;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import com.jike.shanglv.Common.ClearEditText;
import com.jike.shanglv.Common.CommonFunc;
import com.jike.shanglv.Common.CustomProgressDialog;
import com.jike.shanglv.Common.DateUtil;
import com.jike.shanglv.Enums.SPkeys;
import com.jike.shanglv.NetAndJson.HttpUtils;

public class ActivityZhanghuchongzhi extends Activity {

	private String phoneproReturnJson,commitReturnJson;
	private ImageButton back_imgbtn, home_imgbtn;
	private TextView dangqianyue_tv, chongzhijilu_tv;
	private com.jike.shanglv.Common.ClearEditText chongzhijine_et;
	private Button chongzhi_button;
	private Context context;
	private SharedPreferences sp;
	private CustomProgressDialog progressdialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_zhanghuchongzhi);
		initView();
	}

	private void initView() {
		context = this;
		sp = getSharedPreferences(SPkeys.SPNAME.getString(), 0);

		back_imgbtn = (ImageButton) findViewById(R.id.back_imgbtn);
		home_imgbtn = (ImageButton) findViewById(R.id.home_imgbtn);
		back_imgbtn.setOnClickListener(clickListener);
		home_imgbtn.setOnClickListener(clickListener);
		chongzhi_button = (Button) findViewById(R.id.chongzhi_button);
		chongzhi_button.setOnClickListener(clickListener);
		dangqianyue_tv=(TextView) findViewById(R.id.dangqianyue_tv);
		chongzhijilu_tv=(TextView) findViewById(R.id.chongzhijilu_tv);
		chongzhijine_et=(ClearEditText) findViewById(R.id.chongzhijine_et);
		chongzhijilu_tv.setOnClickListener(clickListener);
		dangqianyue_tv.setText("￥"+sp.getString(SPkeys.amount.getString(), "0"));
		
		if (!sp.getBoolean(SPkeys.loginState.getString(), false)) {
			startActivity(new Intent(context, Activity_Login.class));
		}
	}

	View.OnClickListener clickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.back_imgbtn:
				finish();
				break;
			case R.id.home_imgbtn:
				startActivity(new Intent(context, MainActivity.class));
				break;
			case R.id.chongzhijilu_tv:
				
				break;
			case R.id.chongzhi_button:
				if (HttpUtils.showNetCannotUse(context)) {
					break;
				}
				if (chongzhijine_et.getText().toString().trim().length()==0) {
					new AlertDialog.Builder(context).setTitle("请输入充值金额")
					.setPositiveButton("确认", null).show();
					break;
				}
				if (!CommonFunc.isNumber(chongzhijine_et.getText().toString().trim())) {
					new AlertDialog.Builder(context).setTitle("请输入合法的充值金额")
					.setPositiveButton("确认", null).show();
					break;
				}
				
				String userid=sp.getString(SPkeys.userid.getString(), "");
				int paysystype=15;
				String siteid=sp.getString(SPkeys.siteid.getString(), "");
				String sign=CommonFunc. MD5(chongzhijine_et.getText().toString().trim() + userid + paysystype + siteid);
				MyApp ma = new MyApp(context);
				// <string name="test_pay_server_url">http://gatewayceshi.51jp.cn/PayMent/BeginPay.aspx?orderID=%1$s&amp;amount=%2$s&amp;userid=%3$s&amp;paysystype=%4$s&amp;siteid=%5$s&amp;sign=%6$s</string>
				String url=String.format(ma.getPayServeUrl(),"", chongzhijine_et.getText().toString().trim(),userid,paysystype,siteid,sign);
				Intent intent=new Intent(context,Activity_Web_Pay.class);
				intent.putExtra(Activity_Web_Pay.URL, url);
				intent.putExtra(Activity_Web_Pay.TITLE, "账户充值支付");
				startActivity(intent);
				break;
			default:

				break;
			}
		}
	};
	
	private void startQueryPhoneOrderList() {
		if (HttpUtils.showNetCannotUse(context)) {
			return;
		}
		new Thread(new Runnable() {
			@Override
			public void run() {
				// url?action=phonepro&sign=1232432&userkey=2bfc0c48923cf89de19f6113c127ce81&sitekey=defage
				// &str={"phone":"","value":"","userid":"","siteid":""}
				MyApp ma = new MyApp(context);
				String str = "";
				try {
					str = "{\"orderID\":\""	+ ""
							+ "\",\"tm1\":\""+DateUtil.GetDateAfterToday(-30)
							+ "\",\"tm2\":\""+DateUtil.GetTodayDate()
							+ "\",\"pageSize\":\""+50
							+ "\",\"pageIndex\":\""+1
							+ "\",\"userid\":\""
							+ sp.getString(SPkeys.userid.getString(), "")
							+ "\",\"siteid\":\"65\"}";
				} catch (Exception e) {
					e.printStackTrace();
				}
				String param = "action=phoneprov2&str=" + str + "&userkey="
						+ MyApp.userkey + "&sitekey=" + MyApp.sitekey
						+ "&sign="
						+ CommonFunc.MD5(MyApp.userkey + "phoneprov2" + str);
				phoneproReturnJson = HttpUtils.getJsonContent(ma.getServeUrl(),
						param);
				Message msg = new Message();
				msg.what = 1;
				handler.sendMessage(msg);
			}
		}).start();
	}
	

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				JSONTokener jsonParser;
				jsonParser = new JSONTokener(commitReturnJson);
				try {
					JSONObject jsonObject = (JSONObject) jsonParser.nextValue();
					String state = jsonObject.getString("c");
					if (state.equals("0000")) {
						jsonObject=jsonObject.getJSONObject("d");
						
						String userid=sp.getString(SPkeys.userid.getString(), "");
						int paysystype=15;
						String siteid=sp.getString(SPkeys.siteid.getString(), "");
						String sign=CommonFunc. MD5("" + chongzhijine_et.getText().toString().trim().substring(1) + userid + paysystype + siteid);
						MyApp ma = new MyApp(context);
						// <string name="test_pay_server_url">http://gatewayceshi.51jp.cn/PayMent/BeginPay.aspx?orderID=%1$s&amp;amount=%2$s&amp;userid=%3$s&amp;paysystype=%4$s&amp;siteid=%5$s&amp;sign=%6$s</string>
						String url=String.format(ma.getPayServeUrl(),"", chongzhijine_et.getText().toString().trim().substring(1),userid,paysystype,siteid,sign);
						Intent intent=new Intent(context,Activity_Web_Pay.class);
						intent.putExtra(Activity_Web_Pay.URL, url);
						intent.putExtra(Activity_Web_Pay.TITLE, "话费充值支付");
						startActivity(intent);
					} else {
						String message = jsonObject.getString("msg");
						new AlertDialog.Builder(context).setTitle("订单提交失败")
								.setMessage(message)
								.setPositiveButton("确认", null).show();
						chongzhi_button.setBackgroundColor(getResources()
								.getColor(R.color.gray));
						chongzhi_button.setEnabled(false);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				progressdialog.dismiss();
				break;
			}
		}
	};
}
