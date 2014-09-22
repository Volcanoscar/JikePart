package com.jike.shanglv;

import com.jike.shanglv.Enums.SPkeys;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

/**
 *     class desc: �������� (1)�ж��Ƿ����״μ���Ӧ��--��ȡ��ȡSharedPreferences�ķ���
 *     (2)�ǣ������GuideActivity���������MainActivity (3)3s��ִ��(2)����
 */
public class WelcomeActivity extends Activity {
	boolean isFirstIn = false;
	SharedPreferences preferences;
	private static final int GO_HOME = 1000;
	private static final int GO_GUIDE = 1001;
	// �ӳ�3��
	private static final long SPLASH_DELAY_MILLIS = 3000;

	/**
	 * Handler:��ת����ͬ����
	 */
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case GO_HOME:
				goHome();
				break;
			case GO_GUIDE:
				goGuide();
				break;
			}
			super.handleMessage(msg);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome);

		init();
	}

	private void init() {
		// ��ȡSharedPreferences����Ҫ������
		// ʹ��SharedPreferences����¼�����ʹ�ô���
		preferences = getSharedPreferences(
				SPkeys.SPNAME.getString(), MODE_PRIVATE);
		
		isFirstIn = preferences.getBoolean(SPkeys.isFirstIn.getString(), false);

		// �жϳ�����ڼ������У�����ǵ�һ����������ת���������棬������ת��������
		if (isFirstIn) {
			// ʹ��Handler��postDelayed������3���ִ����ת��MainActivity
			mHandler.sendEmptyMessageDelayed(GO_HOME, SPLASH_DELAY_MILLIS);
		} else {
			//			mHandler.sendEmptyMessageDelayed(GO_GUIDE, SPLASH_DELAY_MILLIS);
			Message msg = new Message();
			msg.what=GO_GUIDE;
			mHandler.sendMessage(msg);
			preferences.edit().putBoolean(SPkeys.isFirstIn.getString(), true).commit();
		}

	}

	private void goHome() {
		Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
		WelcomeActivity.this.startActivity(intent);
		WelcomeActivity.this.finish();
	}

	private void goGuide() {
		Intent intent = new Intent(WelcomeActivity.this, GuideActivity.class);
		WelcomeActivity.this.startActivity(intent);
		WelcomeActivity.this.finish();
	}
}
