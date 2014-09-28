package com.jike.shanglv;

import java.util.HashMap;
import android.content.Context;
import com.jike.shanglv.Enums.PackageKeys;
import com.jike.shanglv.Enums.Platform;

//�ٶȵ�ͼ SHA1 0D:3C:EC:2E:C2:01:A0:E6:C7:AE:44:B4:05:17:9D:F8:BE:A9:70:9E
public class MyApp {
	private Context context;
	
	HashMap<String,Object> self_hm=new HashMap<String,Object>();//����B2C
	HashMap<String,Object> self_b_hm=new HashMap<String,Object>();//����B2B
	HashMap<String,Object> nanbei_hm=new HashMap<String,Object>();
	HashMap<String,Object> nanbei_b_hm=new HashMap<String,Object>();
	HashMap<String,Object> menghang_hm=new HashMap<String,Object>();
	HashMap<String,Object> menghang_b_hm=new HashMap<String,Object>();
	
//	HashMap��ʹ��
//	put(K key, V value) 
//	hm.put(a,b); //����ֵΪb,keyֵΪa
//	hm.get(key); //����ֵΪvalue
	/**�����ͬ����ʱ���Ĵ˴�   ������ʱ��Ҫ���İٶȵ�ͼ��key
	 * ������ֻ�������������ֵ��RELEASE��hm��platform
	 */
	public static boolean RELEASE = false;//����  or �������ӿ�
	private HashMap<String,Object> hm=self_hm;
	public static Platform platform=Platform.B2C;
	
	public static String userkey="5b13658a9fc945e34893f806027d467a";//5b13658a9fc945e34893f806027d467a��Ч�ڵ�2014.09.10
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
	
	/**��ȡ�ӿڵĵ�ַ
	 */
	public String getServeUrl() {
		if(RELEASE)
			return context.getResources().getString(R.string.formal_server_url);
		else return context.getResources().getString(R.string.test_server_url);
	}
	/**��ȡ֧���ӿڵĵ�ַ
	 */
	public String getPayServeUrl() {
		if(RELEASE)
			return context.getResources().getString(R.string.formal_pay_server_url);
		else return context.getResources().getString(R.string.test_pay_server_url);
	}
	/**��ȡ��Ʊ��֤��ӿڵĵ�ַ
	 */
	public String getValidcodeServeUrl() {
		if(RELEASE)
			return context.getResources().getString(R.string.formal_train_validcode);
		else return context.getResources().getString(R.string.test_train_validcode);
	}
	/**��ȡ���չ�˾logo
	 */
	public String getFlightCompanyLogo() {
		if(RELEASE)
			return context.getResources().getString(R.string.formal_flight_company_logo);
		else return context.getResources().getString(R.string.test_flight_company_logo);
	}
	/**�������˵��Url
	 */
	public String getAbout() {
		if(RELEASE)
			return context.getResources().getString(R.string.formal_about);
		else return context.getResources().getString(R.string.test_about);
	}
	/**��ȡupdate�ӿڵĵ�ַ
	 */
	public String getUpdateServeUrl() {
		if(RELEASE)
			return context.getResources().getString(R.string.formal_update_url);
		else return context.getResources().getString(R.string.test_update_url);
	}

	/**������ͬ���ҵĴ������
	 */
	public void createValue(){
		self_hm.put(PackageKeys.WELCOME_DRAWABLE.getString(), R.drawable.welcome);
		self_hm.put(PackageKeys.APP_NAME.getString(), R.string.app_name);
		self_hm.put(PackageKeys.MENU_LOGO_DRAWABLE.getString(), R.drawable.menu_logo);
		self_hm.put(PackageKeys.UPDATE_NOTE.getString(), "jike");
		self_hm.put(PackageKeys.PLATFORM.getString(), Platform.B2C);
	}
}
