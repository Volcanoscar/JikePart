package com.jike.shanglv.ShipCalendar;import android.app.Application;import android.content.Context;import android.view.WindowManager;public class MyApplication extends Application {	@SuppressWarnings("unused")	private final String TAG = MyApplication.class.getSimpleName();	private static MyApplication instance;	private static int screenWidth, screenHeight;	public static final String S_MSG_TAG = "msgHandler";	public static MyApplication getInstance() {		return instance;	}	@SuppressWarnings({ "static-access", "deprecation" })	@Override	public void onCreate() {		super.onCreate();		instance = this;		WindowManager wm = (WindowManager) this				.getSystemService(Context.WINDOW_SERVICE);		this.setScreenWidth(wm.getDefaultDisplay().getWidth());// ��Ļ����		this.setScreenHeight(wm.getDefaultDisplay().getHeight());// ��Ļ�߶�	}	public static int getScreenWidth() {		return screenWidth;	}	public static void setScreenWidth(int screenWidth) {		MyApplication.screenWidth = screenWidth;	}	public static int getScreenHeight() {		return screenHeight;	}	public static void setScreenHeight(int screenHeight) {		MyApplication.screenHeight = screenHeight;	}}