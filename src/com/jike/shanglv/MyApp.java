package com.jike.shanglv;

import java.util.HashMap;

import android.content.Context;

import com.jike.shanglv.Enums.PackageKeys;
import com.jike.shanglv.Enums.Platform;
//百度地图 SHA1 0D:3C:EC:2E:C2:01:A0:E6:C7:AE:44:B4:05:17:9D:F8:BE:A9:70:9E
public class MyApp {
	private Context context;
	
	HashMap<String,Object> self_hm=new HashMap<String,Object>();//际珂B2C
	HashMap<String,Object> self_b_hm=new HashMap<String,Object>();//际珂B2B
	HashMap<String,Object> nanbei_hm=new HashMap<String,Object>();
	HashMap<String,Object> nanbei_b_hm=new HashMap<String,Object>();
	HashMap<String,Object> menghang_hm=new HashMap<String,Object>();
	HashMap<String,Object> menghang_b_hm=new HashMap<String,Object>();
	
//	HashMap的使用
//	put(K key, V value) 
//	hm.put(a,b); //插入值为b,key值为a
//	hm.get(key); //返回值为value
	/**打包不同程序时更改此处
	 * 此类中只需更改以下三个值：RELEASE、hm、platform
	 */
	public static boolean RELEASE = false;//测试  or 发布，接口
	private HashMap<String,Object> hm=self_hm;
	public static Platform platform=Platform.B2C;
	
	public static String userkey="5b13658a9fc945e34893f806027d467a";//5b13658a9fc945e34893f806027d467a有效期到2014.09.10
	public static String sitekey="";
	
	public MyApp(Context context){
		this.context=context;
		createValue();
	}
	
	public boolean isRELEASE() {
		return RELEASE;
	}
	
	public HashMap<String, Object> getHm() {
		return hm;
	}
	
	public Platform getPlatform() {
		return platform;
	}
	
	/**获取接口的地址
	 */
	public String getServeUrl() {
		if(RELEASE)
			return context.getResources().getString(R.string.formal_server_url);
		else return context.getResources().getString(R.string.test_server_url);
	}
	/**获取支付接口的地址
	 */
	public String getPayServeUrl() {
		if(RELEASE)
			return context.getResources().getString(R.string.formal_pay_server_url);
		else return context.getResources().getString(R.string.test_pay_server_url);
	}
	/**获取火车票验证码接口的地址
	 */
	public String getValidcodeServeUrl() {
		if(RELEASE)
			return context.getResources().getString(R.string.formal_train_validcode);
		else return context.getResources().getString(R.string.test_train_validcode);
	}
	/**获取航空公司logo
	 */
	public String getFlightCompanyLogo() {
		if(RELEASE)
			return context.getResources().getString(R.string.formal_flight_company_logo);
		else return context.getResources().getString(R.string.test_flight_company_logo);
	}
	/**关于软件说明Url
	 */
	public String getAbout() {
		if(RELEASE)
			return context.getResources().getString(R.string.formal_about);
		else return context.getResources().getString(R.string.test_about);
	}	

	/**构建不同厂家的打包数据
	 */
	public void createValue(){
		self_hm.put(PackageKeys.WELCOME_DRAWABLE.getString(), R.drawable.welcome);
		self_hm.put(PackageKeys.APP_NAME.getString(), R.string.app_name);
		self_hm.put(PackageKeys.MENU_LOGO_DRAWABLE.getString(), R.drawable.menu_logo);
		self_hm.put(PackageKeys.UPDATE_NOTE.getString(), "jikeb2c");
		self_hm.put(PackageKeys.PLATFORM.getString(), Platform.B2C);
	}
}
