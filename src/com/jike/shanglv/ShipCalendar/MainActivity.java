package com.jike.shanglv.ShipCalendar;

import static android.widget.Toast.LENGTH_SHORT;
import java.util.Calendar;
import java.util.Date;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;
import com.jike.shanglv.R;
import com.squareup.timessquare.CalendarPickerView;
import com.squareup.timessquare.CalendarPickerView.SelectionMode;

public class MainActivity extends Activity implements OnClickListener{
	private static final String TAG = "SampleTimesSquareActivity";
	private CalendarPickerView calendar;
	private String selectedDay = "jjj";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_calendar_picker);

		final Calendar nextYear = Calendar.getInstance();
		nextYear.add(Calendar.YEAR, 1);

		final Calendar lastYear = Calendar.getInstance();
		lastYear.add(Calendar.YEAR, -1);

		final Calendar thisMonth = Calendar.getInstance();
		thisMonth.add(Calendar.MONTH, 0);
		final Calendar lastMonth = Calendar.getInstance();
		lastMonth.add(Calendar.MONTH, 6);

		calendar = (CalendarPickerView) findViewById(R.id.calendar_view);
		// calendar.init(lastYear.getTime(), nextYear.getTime()) //
		// .inMode(SelectionMode.SINGLE) //
		// .withSelectedDate(new Date());
		calendar.init(thisMonth.getTime(), lastMonth.getTime()) //
				.inMode(SelectionMode.SINGLE) //
				.withSelectedDate(new Date());
		calendar.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				Toast.makeText(MainActivity.this,
						calendar.getSelectedDate().getTime() + "aaa",
						LENGTH_SHORT).show();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				Toast.makeText(MainActivity.this,
						calendar.getSelectedDate().getTime() + "sss",
						LENGTH_SHORT).show();
			}
		});
		calendar.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Toast.makeText(MainActivity.this,
						calendar.getSelectedDate().getTime() + "kkk",
						LENGTH_SHORT).show();
			}
		});

		findViewById(R.id.finish_tv).setOnClickListener(
				selected_finishClickListener);
		findViewById(R.id.back_imgbtn).setOnClickListener(
				selected_finishClickListener);
	}

	OnClickListener selected_finishClickListener = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			// Log.d(TAG, "Selected time in millis: " +
			// calendar.getSelectedDate().getTime());
			String month = "";
			if (calendar.getSelectedDate().getMonth() < 9)
				month = "0" + (calendar.getSelectedDate().getMonth() + 1);
			else
				month = String.valueOf(calendar.getSelectedDate().getMonth() + 1);
			String date = (calendar.getSelectedDate().getYear() + 1900) + "-"+ month+ "-"
					+ calendar.getSelectedDate().getDate();
			// Toast.makeText(MainActivity.this, date, LENGTH_SHORT).show();
			setResult(0, getIntent().putExtra("pickedDate", date));
			finish();
		}
	};
	
	@Override
	public void onClick(View paramView) {
//		if (paramView.getTag() != null) 
		{
			String date = (calendar.getSelectedDate().getYear() + 1900) + "-"
					+ (calendar.getSelectedDate().getMonth()+1) + "-"
					+ calendar.getSelectedDate().getDate();
			setResult(0, getIntent().putExtra("pickedDate", date));
			finish();
		}
	}
}
