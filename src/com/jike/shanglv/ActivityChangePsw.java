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
import com.jike.shanglv.Common.ClearEditText;
import com.jike.shanglv.Common.CommonFunc;
import com.jike.shanglv.Common.CustomProgressDialog;
import com.jike.shanglv.Enums.SPkeys;
import com.jike.shanglv.NetAndJson.HttpUtils;

public class ActivityChangePsw extends Activity {

	private Context context;
	private ImageButton back_iv;
	private Button ok_button;
	private SharedPreferences sp;
	private CustomProgressDialog progressdialog;
	private com.jike.shanglv.Common.ClearEditText oldpsw_cet, newpsw_cet,
			confirmpsw_cet;
	private String changePswReturnJson = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_changepsw);

		sp = getSharedPreferences(SPkeys.SPNAME.getString(), 0);
		context = this;
		back_iv = (ImageButton) findViewById(R.id.back_imgbtn);
		back_iv.setOnClickListener(btnClickListner);
		ok_button = (Button) findViewById(R.id.ok_button);
		ok_button.setOnClickListener(btnClickListner);
		oldpsw_cet = (ClearEditText) findViewById(R.id.oldpsw_cet);
		newpsw_cet = (ClearEditText) findViewById(R.id.newpsw_cet);
		confirmpsw_cet = (ClearEditText) findViewById(R.id.confirmpsw_cet);
	}

	View.OnClickListener btnClickListner = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.back_imgbtn:
				startActivity(new Intent(context,ActivityMyAccout.class));
				break;
			case R.id.ok_button:
				if (!newpsw_cet.getText().toString()
						.equals(confirmpsw_cet.getText().toString())) {
					new AlertDialog.Builder(context).setTitle("��������������벻һ��")
							.setPositiveButton("ȷ��", null).show();
					break;
				}
				if (!CommonFunc.isValidPassword(newpsw_cet.getText().toString()
						.trim())) {
					new AlertDialog.Builder(context).setTitle("�����ʽ����ȷ")
							.setMessage("Ϊ��֤����İ�ȫ�ԣ�������6-20λ�����ֻ���ĸ����ϣ�")
							.setPositiveButton("ȷ��", null).show();
					break;
				}
				startChange();
				break;
			default:
				break;
			}
		}
	};

	private void startChange() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				// uid:�û�ID sid:��վID oldpass:�ϵ�¼���� newpass:�µ�¼����
				MyApp ma = new MyApp(getApplicationContext());
				String str = "{\"uid\":\""
						+ sp.getString(SPkeys.userid.getString(), "")
						+ "\",\"sid\":\""
						+ sp.getString(SPkeys.siteid.getString(), "")
						+ "\",\"oldpass\":\"" + oldpsw_cet.getText().toString()
						+ "\",\"newpass\":\"" + newpsw_cet.getText().toString()
						+ "\"}";
				String param = "action=updatepass&str=" + str + "&userkey="
						+ MyApp.userkey + "&sign="
						+ CommonFunc.MD5(MyApp.userkey + "updatepass" + str)
						+ "&sitekey=" + MyApp.sitekey;
				changePswReturnJson = HttpUtils.getJsonContent(
						ma.getServeUrl(), param);
				Message msg = new Message();
				msg.what = 1;
				handler.sendMessage(msg);
			}
		}).start();
		progressdialog = CustomProgressDialog.createDialog(context);
		progressdialog.setMessage("�����ύ�����޸��������Ժ�...");
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
			switch (msg.what) {
			case 1:
				JSONTokener jsonParser;
				jsonParser = new JSONTokener(changePswReturnJson);
				try {
					if (changePswReturnJson.length() == 0) {
						new AlertDialog.Builder(context)
								.setTitle("�޸�����ʧ�ܣ����Ժ����ԣ�")
								.setPositiveButton("ȷ��", null).show();
						progressdialog.dismiss();
						break;
					}
					JSONObject jsonObject = (JSONObject) jsonParser.nextValue();
					String state = jsonObject.getString("c");

					jsonObject = jsonObject.getJSONObject("d");
					String message = jsonObject.getString("msg");
					if (state.equals("0000")) {
						new AlertDialog.Builder(context).setTitle("�����޸ĳɹ�")
								.setMessage(message)
								.setPositiveButton("ȷ��", new OnClickListener() {
									@Override
									public void onClick(DialogInterface arg0,
											int arg1) {
										finish();
									}
								}).show();
					} else {
						new AlertDialog.Builder(context).setTitle("�����޸�ʧ��")
								.setMessage(message)
								.setPositiveButton("ȷ��", null).show();
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
