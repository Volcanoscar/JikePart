package com.jike.shanglv;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class MoreActivity extends Activity {
	private ImageButton back_iv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_more);
		
		 back_iv = (ImageButton) findViewById(R.id.back_imgbtn);
		 back_iv.setOnClickListener(btnClickListner);
	}
	
	View.OnClickListener btnClickListner = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.back_imgbtn:
				startActivity(new Intent(MoreActivity.this, MainActivity.class));
				break;
			default:
				break;
			}
		}
	};
}
