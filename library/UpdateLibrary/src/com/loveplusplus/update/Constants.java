package com.loveplusplus.update;

public class Constants {

	protected static final String APP_UPDATE_SERVER_URL =
			"http://dn-rocky.qbox.me/shanglvguanjiaversion.xml";
//			"http://dn-rocky.qbox.me/update_test_json.json";
	
	// json {"url":"http://192.168.1.115:8080/xxx.apk","versionCode":2,"updateMessage":"�汾������Ϣ"}
	//��������������ص�json�����������ģ����Ը���ʵ������޸��������������
	public static final String APK_DOWNLOAD_URL = "url";
	public static final String APK_UPDATE_CONTENT = "updateMessage";
	public static final String APK_VERSION_CODE = "versionCode";
}
