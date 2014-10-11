package com.jike.shanglv.Models;

public class CustomerUser {
	String UserName,// StoneMK, //用户名
			DealerLevel,// 钻石卡,//用户级别
			RealName,// 李伟,//真实信息
			Phone,// 18502193643,//电话号码
			RegDate,// 2014-06-26 10:58,//注册日期
			Status;// 正常,//状态

	public String getUserName() {
		return UserName;
	}

	public void setUserName(String userName) {
		UserName = userName;
	}

	public String getDealerLevel() {
		return DealerLevel;
	}

	public void setDealerLevel(String dealerLevel) {
		DealerLevel = dealerLevel;
	}

	public String getRealName() {
		return RealName;
	}

	public void setRealName(String realName) {
		RealName = realName;
	}

	public String getPhone() {
		return Phone;
	}

	public void setPhone(String phone) {
		Phone = phone;
	}

	public String getRegDate() {
		return RegDate;
	}

	public void setRegDate(String regDate) {
		RegDate = regDate;
	}

	public String getStatus() {
		return Status;
	}

	public void setStatus(String status) {
		Status = status;
	}
	
}
