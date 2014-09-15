package com.jike.shanglv.Models;

public class TrainInfo  implements Cloneable {
	String WZ,// 无座票价
			RW,// 软卧票价
			YW,// 硬卧票价
			YZ,// 硬座票价
			RZ,// 软座票价
			Distance,// 距离
			RunTime,// 运行时间
			ETime,// 到达时间
			GoTime,// 出发时间
			StationS,// 出发城市
			StationE,// 到达城市
			SFType,// 始-终
			TrainType,// 列车类型
			TrainID,// 车次
			WZ_Y,// 无座剩余
			RW_Y,// 软卧剩余
			YW_Y,// 硬卧剩余
			YZ_Y,// 硬座剩余
			RZ_Y,// 软座剩余
			
			//以下三个字段为根据上面的数据生成的，以便在列表中显示
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
