package com.jike.shanglv.Enums;

import com.jike.shanglv.NetAndJson.UserInfo;

/**��¼SharedPreferences�еļ�ֵ
 * */
public enum SPkeys {
	SPNAME("mySP"),//SharedPreferences������
	
	userid("userid"),
	username("username"),
	amount("amount"),//�����˺Ž��
	siteid("siteid"),//ϵͳid
	
	UserInfoJson("UserInfoJson"),
	lastUsername("lastUsername"),
	lastPassword("lastPassword"),
	autoLogin("autoLogin"),
	loginState("loginState"),
	gnjpContactPhone("gnjpContactPhone"),	//���ڻ�Ʊ�µ����ϴε���ϵ���ֻ�����
	gjjpContactPhone("gjjpContactPhone"),	//���ʻ�Ʊ���󵥣��ϴε���ϵ���ֻ�����
	trainContactPhone("trainContactPhone")	//��Ʊ���ϴε���ϵ���ֻ�����
	
	;

	private String key;

	private SPkeys(String s) {
		key = s;
	}

	public String getString() {
		return key;
	}
};
