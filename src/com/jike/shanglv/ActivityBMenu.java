package com.jike.shanglv;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.jike.shanglv.Common.CommonFunc;
import com.jike.shanglv.Enums.PackageKeys;
import com.jike.shanglv.Enums.Platform;
import com.jike.shanglv.Enums.SPkeys;
import com.jike.shanglv.NetAndJson.HttpUtils;
import com.jike.shanglv.NetAndJson.JSONHelper;
import com.jike.shanglv.NetAndJson.UserInfo;
import com.jike.shanglv.Update.UpdateManager;

public class ActivityBMenu extends Activity {

	public static ActivityBMenu instance = null;
	private LinearLayout gnjp_ll, gjjp_ll, jdyd_ll, hcp_ll, jdmp_ll, hfcz_ll,
			hbdt_ll, wpt_ll, sxy_ll, fxgl_ll, khgl_ll, zhgl_ll, ddgl_ll, wd_ll;
	private ImageButton imgBtn_gnjp, imgBtn_gjjp, imgBtn_jdyd, imgBtn_hcp,
		imgBtn_jdmp, imgBtn_hfcz, imgBtn_hbdt, imgBtn_wpt, imgBtn_sxy,
		imgBtn_fxgl, imgBtn_khgl, imgBtn_zhgl, imgBtn_ddgl, imgBtn_wd;
	private Context context;
	private SharedPreferences sp;
	private String loginReturnJson="";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_b_menu);
		context = this;
		((MyApplication) getApplication()).addActivity(this);

		MyApp mApp = new MyApp(getApplicationContext());
		((ImageView) findViewById(R.id.menu_logo))
				.setBackgroundResource((Integer) mApp.getHm().get(
						PackageKeys.MENU_LOGO_DRAWABLE.getString()));

		if (!((MyApplication) getApplication()).getHasCheckedUpdate()) {
			MyApp ma = new MyApp(context);
			UpdateManager manager = new UpdateManager(context, ma.getHm()
					.get(PackageKeys.UPDATE_NOTE.getString()).toString());
			manager.checkForUpdates(false);
			((MyApplication) getApplication()).setHasCheckedUpdate(true);
		}

		imgBtn_gnjp = (ImageButton) findViewById(R.id.imgBtn_gnjp);
		imgBtn_gjjp = (ImageButton) findViewById(R.id.imgBtn_gjjp);
		imgBtn_jdyd = (ImageButton) findViewById(R.id.imgBtn_jdyd);
		imgBtn_hcp = (ImageButton) findViewById(R.id.imgBtn_hcp);
		imgBtn_jdmp = (ImageButton) findViewById(R.id.imgBtn_jdmp);
		imgBtn_hfcz = (ImageButton) findViewById(R.id.imgBtn_hfcz);
		imgBtn_hbdt = (ImageButton) findViewById(R.id.imgBtn_hbdt);
		imgBtn_wpt = (ImageButton) findViewById(R.id.imgBtn_wpt);
		imgBtn_sxy = (ImageButton) findViewById(R.id.imgBtn_sxy);
		imgBtn_fxgl = (ImageButton) findViewById(R.id.imgBtn_fxgl);
		imgBtn_khgl = (ImageButton) findViewById(R.id.imgBtn_khgl);
		imgBtn_zhgl = (ImageButton) findViewById(R.id.imgBtn_zhgl);
		imgBtn_ddgl = (ImageButton) findViewById(R.id.imgBtn_ddgl);
		imgBtn_wd = (ImageButton) findViewById(R.id.imgBtn_wd);
		imgBtn_gnjp.setOnClickListener(btnClickListner);
		imgBtn_gjjp.setOnClickListener(btnClickListner); 
		imgBtn_jdyd.setOnClickListener(btnClickListner); 
		imgBtn_hcp.setOnClickListener(btnClickListner);
		imgBtn_jdmp.setOnClickListener(btnClickListner);
		imgBtn_hfcz.setOnClickListener(btnClickListner);
		imgBtn_hbdt.setOnClickListener(btnClickListner);
		imgBtn_wpt.setOnClickListener(btnClickListner); 
		imgBtn_sxy.setOnClickListener(btnClickListner);
		imgBtn_fxgl.setOnClickListener(btnClickListner);
		imgBtn_khgl.setOnClickListener(btnClickListner);
		imgBtn_zhgl.setOnClickListener(btnClickListner); 
		imgBtn_ddgl.setOnClickListener(btnClickListner);
		imgBtn_wd.setOnClickListener(btnClickListner);
		gnjp_ll = (LinearLayout) findViewById(R.id.gnjp_ll);
		gjjp_ll = (LinearLayout) findViewById(R.id.gjjp_ll);
		jdyd_ll = (LinearLayout) findViewById(R.id.jdyd_ll);
		hcp_ll = (LinearLayout) findViewById(R.id.hcp_ll);
		jdmp_ll = (LinearLayout) findViewById(R.id.jdmp_ll);
		hfcz_ll = (LinearLayout) findViewById(R.id.hfcz_ll);
		hbdt_ll = (LinearLayout) findViewById(R.id.hbdt_ll);
		wpt_ll = (LinearLayout) findViewById(R.id.wpt_ll);
		sxy_ll = (LinearLayout) findViewById(R.id.sxy_ll);
		fxgl_ll = (LinearLayout) findViewById(R.id.fxgl_ll);
		khgl_ll = (LinearLayout) findViewById(R.id.khgl_ll);
		zhgl_ll = (LinearLayout) findViewById(R.id.zhgl_ll);
		ddgl_ll = (LinearLayout) findViewById(R.id.ddgl_ll);
		wd_ll = (LinearLayout) findViewById(R.id.wd_ll);
		gnjp_ll.setOnClickListener(btnClickListner);
		gjjp_ll.setOnClickListener(btnClickListner);
		jdyd_ll.setOnClickListener(btnClickListner);
		hcp_ll.setOnClickListener(btnClickListner);
		jdmp_ll.setOnClickListener(btnClickListner);
		hfcz_ll.setOnClickListener(btnClickListner);
		hbdt_ll.setOnClickListener(btnClickListner);
		wpt_ll.setOnClickListener(btnClickListner);
		sxy_ll.setOnClickListener(btnClickListner);
		fxgl_ll.setOnClickListener(btnClickListner);
		khgl_ll.setOnClickListener(btnClickListner);
		zhgl_ll.setOnClickListener(btnClickListner);
		ddgl_ll.setOnClickListener(btnClickListner);
		wd_ll.setOnClickListener(btnClickListner);
		
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	View.OnClickListener btnClickListner = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.imgBtn_gnjp:
			case R.id.gnjp_ll:
				startActivity(new Intent(context,
						ActivityInlandAirlineticket.class));
				break;
			case R.id.imgBtn_gjjp:
			case R.id.gjjp_ll:
				startActivity(new Intent(context,
						ActivityInternationalAirlineticket.class));
				break;
			case R.id.imgBtn_hbdt:
			case R.id.hbdt_ll:
				startActivity(new Intent(context, ActivityHangbandongtai.class));
				break;
			case R.id.imgBtn_jdyd:
			case R.id.jdyd_ll:
				startActivity(new Intent(context, ActivityHotel.class));
				break;
			case R.id.imgBtn_wpt:
			case R.id.wpt_ll:
//				startActivity(new Intent(context, Guojijipiao_Search.class));
				break;
			case R.id.imgBtn_hfcz:
			case R.id.hfcz_ll:
				startActivity(new Intent(context, ActivityHuafeichongzhi.class));
				break;
			case R.id.imgBtn_jdmp:
			case R.id.jdmp_ll:
//				startActivity(new Intent(context, Guojijipiao_Search.class));
				break;
			case R.id.imgBtn_sxy:
			case R.id.sxy_ll:
//				startActivity(new Intent(context, ActivityZhanghuchongzhi.class));
				break;
			case R.id.imgBtn_hcp:
			case R.id.hcp_ll:
				startActivity(new Intent(context, ActivityTrain.class));
				break;
			case R.id.imgBtn_fxgl:
			case R.id.fxgl_ll:

				break;
			case R.id.imgBtn_khgl:
			case R.id.khgl_ll:

				break;
			case R.id.imgBtn_zhgl:
			case R.id.zhgl_ll:

				break;
			case R.id.imgBtn_ddgl:
			case R.id.ddgl_ll:
				startActivity(new Intent(context, OrderActivity.class));
				break;
			case R.id.imgBtn_wd:
			case R.id.wd_ll:
				startActivity(new Intent(context, MineActivity.class));
				break;
			default:
				break;
			}
		}
	};
	
	private void queryUserInfo(){
		new Thread(new Runnable() {
			@Override
			public void run() {
				int utype = 0;
				MyApp ma = new MyApp(context);
				Platform pf = (Platform) ma.getHm().get(PackageKeys.PLATFORM.getString());
				if (pf == Platform.B2B)
					utype = 1;
				else if (pf == Platform.B2C)
					utype = 2;
				String str = "{\"uname\":\""
						+ sp.getString(SPkeys.lastUsername.getString(), "")
						+ "\",\"upwd\":\""
						+ sp.getString(SPkeys.lastPassword.getString(), "")
						+ "\",\"utype\":\"" + utype + "\"}";
				String param = "action=userlogin&sitekey=&userkey="
						+ ma.getHm()
								.get(PackageKeys.USERKEY.getString())
								.toString()
						+ "&str="
						+ str
						+ "&sign="
						+ CommonFunc.MD5(ma.getHm()
								.get(PackageKeys.USERKEY.getString())
								.toString()
								+ "userlogin" + str);
				loginReturnJson = HttpUtils.getJsonContent(
						ma.getServeUrl(), param);
				Log.v("loginReturnJson", loginReturnJson);
				Message msg = new Message();
				msg.what = 1;
				handler.sendMessage(msg);
			}
		}).start();
	}
	
	private Handler handler = new Handler() {//在主界面判断用户名密码是否失效
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:// 获取登录返回的数据

				JSONTokener jsonParser;
				jsonParser = new JSONTokener(loginReturnJson);
				try {
					JSONObject jsonObject = (JSONObject) jsonParser.nextValue();
					String state = jsonObject.getString("c");

					if (state.equals("0000")) {
						String content = jsonObject.getString("d");
						sp.edit()
								.putString(SPkeys.UserInfoJson.getString(),
										content).commit();

						// 以下代码将用户信息反序列化到SharedPreferences中
						UserInfo user = JSONHelper.parseObject(content,
								UserInfo.class);
						sp.edit()
								.putString(SPkeys.userid.getString(),
										user.getUserid()).commit();
						sp.edit()
								.putString(SPkeys.username.getString(),
										user.getUsername()).commit();
						sp.edit()
								.putString(SPkeys.amount.getString(),
										user.getAmmount()).commit();
						sp.edit()
								.putString(SPkeys.siteid.getString(),
										user.getSiteid()).commit();
						sp.edit()
								.putString(SPkeys.userphone.getString(),
										user.getMobile()).commit();
						sp.edit()
								.putString(SPkeys.useremail.getString(),
										user.getEmail()).commit();
						sp.edit()
								.putBoolean(SPkeys.loginState.getString(), true)
								.commit();
					} else if (state.equals("1003")){
						sp.edit().putString(SPkeys.userid.getString(),"").commit();
						sp.edit().putString(SPkeys.username.getString(),"").commit();
						sp.edit().putBoolean(SPkeys.loginState.getString(), false).commit();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
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
	
	private long mExitTime;
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if ((System.currentTimeMillis() - mExitTime) > 2000) {
				Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
				mExitTime = System.currentTimeMillis();
			} else {
				 ((MyApplication)getApplication()).exit();
				  android.os.Process.killProcess(android.os.Process.myPid());
				  finish();
				  System.exit(0);
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
