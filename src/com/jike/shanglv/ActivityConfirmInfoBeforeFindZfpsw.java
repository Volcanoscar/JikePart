package com.jike.shanglv;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
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
import com.jike.shanglv.Enums.SPkeys;
import com.jike.shanglv.NetAndJson.HttpUtils;

public class ActivityConfirmInfoBeforeFindZfpsw extends Activity {

	private Context context;
	private ImageButton back_iv;
	private Button nextstep_button;
	private SharedPreferences sp;
	private TextView username_tv,phone_tv;
	private com.jike.shanglv.Common.ClearEditText yanzhengma_cet;
	private String yanzhengmaReturnJson = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_confirminfo_before_findzfpsw);

		sp = getSharedPreferences(SPkeys.SPNAME.getString(), 0);
		context = this;
		back_iv = (ImageButton) findViewById(R.id.back_imgbtn);
		back_iv.setOnClickListener(btnClickListner);
		nextstep_button = (Button) findViewById(R.id.nextstep_button);
		nextstep_button.setOnClickListener(btnClickListner);
		username_tv=(TextView) findViewById(R.id.username_tv); 
		phone_tv=(TextView) findViewById(R.id.phone_tv);
		nextstep_button.setEnabled(false);
		((TextView)findViewById(R.id.get_yanzhengma_tv)).setOnClickListener(btnClickListner);
	}

	View.OnClickListener btnClickListner = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.back_imgbtn:
				startActivity(new Intent(context,ActivityMyAccout.class));
				break;
			case R.id.nextstep_button:

				break;
			case R.id.get_yanzhengma_tv:
				startGetYanzhengma();
				break;
			default:
				break;
			}
		}
	};

	private void startGetYanzhengma() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				MyApp ma = new MyApp(getApplicationContext());
				String str = "{\"userID\":\""
						+ sp.getString(SPkeys.userid.getString(), "")
						+ "\",\"siteID\":\""
						+ sp.getString(SPkeys.siteid.getString(), "")
						+ "\",\"phone\":\"" +sp.getString(SPkeys.userphone.getString(), "")
						+ "\"}";
				String param = "action=restcode&str=" + str + "&userkey="
						+ MyApp.userkey + "&sign="
						+ CommonFunc.MD5(MyApp.userkey + "restcode" + str)
						+ "&sitekey=" + MyApp.sitekey;
				yanzhengmaReturnJson = HttpUtils.getJsonContent(
						ma.getServeUrl(), param);
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
				jsonParser = new JSONTokener(yanzhengmaReturnJson);
				try {
					if (yanzhengmaReturnJson.length() == 0) {
						new AlertDialog.Builder(context)
								.setTitle("验证码发送失败")
								.setPositiveButton("确认", null).show();
						break;
					}
					JSONObject jsonObject = (JSONObject) jsonParser.nextValue();
					String state = jsonObject.getString("c");
					String message = jsonObject.getString("d");
					if (state.equals("0000")) {
						nextstep_button.setEnabled(true);
						new AlertDialog.Builder(context).setTitle("验证码发送成功")
								.setMessage(message)
								.setPositiveButton("确认", null).show();
					} else {
						new AlertDialog.Builder(context).setTitle("验证码发送失败")
								.setMessage(message)
								.setPositiveButton("确认", null).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				break;
			}
		}
	};

}
