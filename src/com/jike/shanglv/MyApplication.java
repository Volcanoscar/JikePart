package com.jike.shanglv;

import android.app.Application;

public class MyApplication extends Application {
	private Boolean hasCheckedUpdate=false;

	public Boolean getHasCheckedUpdate() {
		return hasCheckedUpdate;
	}

	public void setHasCheckedUpdate(Boolean hasCheckedUpdate) {
		this.hasCheckedUpdate = hasCheckedUpdate;
	}
	
}
