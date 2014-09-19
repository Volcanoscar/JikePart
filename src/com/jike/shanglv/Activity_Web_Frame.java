//ʹ��WebView�������ݣ�ֻ��Ҫ�ṩ����ͼ��ص�url����
package com.jike.shanglv;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.TextView;

public class Activity_Web_Frame extends Activity {

	public static final String URL = "redire_url";
	public static final String TITLE = "activity_title";
	private WebView webView;

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_web_frame);

		((ImageButton) findViewById(R.id.back)).setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
		((ImageButton) findViewById(R.id.home)).setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				startActivity(new Intent(Activity_Web_Frame.this, MainActivity.class));
			}
		});

		webView = new WebView(this);
		webView = (WebView) findViewById(R.id.webView);
		WebSettings webSettings = webView.getSettings();
		webSettings.setJavaScriptEnabled(true);// ��WebView��ʹ��JavaScript����ҳ��������JavaScript������ΪWebViewʹ��JavaScript
		String url = getIntent().getExtras().getString(URL);
		String title = getIntent().getExtras().getString(TITLE);
		((TextView)findViewById(R.id.title)).setText(title);

		webView.setWebViewClient(new WebViewClient() {/// ����д�Ļ����������ֻ��������

			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) { // Handle the error
			}

			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}
		});
		webView.loadUrl(url);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
		}

		return super.onKeyDown(keyCode, event);
	}

}