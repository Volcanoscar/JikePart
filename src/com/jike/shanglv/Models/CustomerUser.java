package com.jike.shanglv.Models;

public class CustomerUser {
	String UserName,// StoneMK, //�û���
			DealerLevel,// ��ʯ��,//�û�����
			RealName,// ��ΰ,//��ʵ��Ϣ
			Phone,// 18502193643,//�绰����
			RegDate,// 2014-06-26 10:58,//ע������
			Status;// ����,//״̬

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
