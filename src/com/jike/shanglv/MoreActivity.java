package com.jike.shanglv;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class MoreActivity extends Activity {
	private ImageView back_iv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_more);
		
		 back_iv = (ImageView) findViewById(R.id.back_iv);
		 back_iv.setOnClickListener(btnClickListner);
	}
	
	View.OnClickListener btnClickListner = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.back_iv:
				finish();
				break;
			default:
				break;
			}
		}
	};
}
