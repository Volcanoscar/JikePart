package com.jike.shanglv;

import com.jike.shanglv.SeclectCity.ClearEditText;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.RelativeLayout;

public class ActivityClientManage extends Activity {
	
	private RelativeLayout add_client_rl,client_grad_set_rl;
	private com.jike.shanglv.SeclectCity.ClearEditText filter_edit;
	private ListView listview;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_client_manage);
		
		listview=(ListView) findViewById(R.id.listview);
		add_client_rl=(RelativeLayout) findViewById(R.id.add_client_rl);
		client_grad_set_rl=(RelativeLayout) findViewById(R.id.client_grad_set_rl);
		filter_edit=(ClearEditText) findViewById(R.id.filter_edit);
	}

}
