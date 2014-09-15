package com.jike.shanglv.Models;

public class TrainInfo  implements Cloneable {
	String WZ,// ����Ʊ��
			RW,// ����Ʊ��
			YW,// Ӳ��Ʊ��
			YZ,// Ӳ��Ʊ��
			RZ,// ����Ʊ��
			Distance,// ����
			RunTime,// ����ʱ��
			ETime,// ����ʱ��
			GoTime,// ����ʱ��
			StationS,// ��������
			StationE,// �������
			SFType,// ʼ-��
			TrainType,// �г�����
			TrainID,// ����
			WZ_Y,// ����ʣ��
			RW_Y,// ����ʣ��
			YW_Y,// Ӳ��ʣ��
			YZ_Y,// Ӳ��ʣ��
			RZ_Y,// ����ʣ��
			
			//���������ֶ�Ϊ����������������ɵģ��Ա����б�����ʾ
			Seat_Type,
			Price,
			Remain_Count;
	
	public Object clone() {  
		TrainInfo o = null;  
        try {  
            o = (TrainInfo) super.clone();  
        } catch (CloneNotSupportedException e) {  
            e.printStackTrace();  
        }  
        return o;  
    }  

	public String getRemain_Count() {
		return Remain_Count;
	}

	public void setRemain_Count(String remain_Count) {
		Remain_Count = remain_Count;
	}

	public String getSeat_Type() {
		return Seat_Type;
	}

	public void setSeat_Type(String seat_Type) {
		Seat_Type = seat_Type;
	}

	public String getPrice() {
		return Price;
	}

	public void setPrice(String price) {
		Price = price;
	}

	public String getWZ() {
		return WZ;
	}

	public void setWZ(String wZ) {
		WZ = wZ;
	}

	public String getRW() {
		return RW;
	}

	public void setRW(String rW) {
		RW = rW;
	}

	public String getYW() {
		return YW;
	}

	public void setYW(String yW) {
		YW = yW;
	}

	public String getYZ() {
		return YZ;
	}

	public void setYZ(String yZ) {
		YZ = yZ;
	}

	public String getRZ() {
		return RZ;
	}

	public void setRZ(String rZ) {
		RZ = rZ;
	}

	public String getDistance() {
		return Distance;
	}

	public void setDistance(String distance) {
		Distance = distance;
	}

	public String getRunTime() {
		return RunTime;
	}

	public void setRunTime(String runTime) {
		RunTime = runTime;
	}

	public String getETime() {
		return ETime;
	}

	public void setETime(String eTime) {
		ETime = eTime;
	}

	public String getGoTime() {
		return GoTime;
	}

	public void setGoTime(String goTime) {
		GoTime = goTime;
	}

	public String getStationS() {
		return StationS;
	}

	public void setStationS(String stationS) {
		StationS = stationS;
	}

	public String getStationE() {
		return StationE;
	}

	public void setStationE(String stationE) {
		StationE = stationE;
	}

	public String getSFType() {
		return SFType;
	}

	public void setSFType(String sFType) {
		SFType = sFType;
	}

	public String getTrainType() {
		return TrainType;
	}

	public void setTrainType(String trainType) {
		TrainType = trainType;
	}

	public String getTrainID() {
		return TrainID;
	}

	public void setTrainID(String trainID) {
		TrainID = trainID;
	}

	public String getWZ_Y() {
		return WZ_Y;
	}

	public void setWZ_Y(String wZ_Y) {
		WZ_Y = wZ_Y;
	}

	public String getRW_Y() {
		return RW_Y;
	}

	public void setRW_Y(String rW_Y) {
		RW_Y = rW_Y;
	}

	public String getYW_Y() {
		return YW_Y;
	}

	public void setYW_Y(String yW_Y) {
		YW_Y = yW_Y;
	}

	public String getYZ_Y() {
		return YZ_Y;
	}

	public void setYZ_Y(String yZ_Y) {
		YZ_Y = yZ_Y;
	}

	public String getRZ_Y() {
		return RZ_Y;
	}

	public void setRZ_Y(String rZ_Y) {
		RZ_Y = rZ_Y;
	}

}
