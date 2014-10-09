package com.jike.shanglv;

import org.json.JSONTokener;

import com.jike.shanglv.Common.CommonFunc;
import com.jike.shanglv.Enums.PackageKeys;
import com.jike.shanglv.Enums.SPkeys;
import com.jike.shanglv.NetAndJson.HttpUtils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class SelectZhengceActivity extends Activity {
	public static final String PLICYLISTSTR="policylist_str";
	
	private ImageButton back_imgbtn;
	private ListView sortListView;
	private Context context;
	private Boolean isInlandCity=true;
	private String str="",policyReturnJson="";
	
	private ImageView scrollbar_iv;
	private TextView putongzhengce_tv, gaofanzhengce_tv;
	private float screenWidth;// �ֻ���Ļ���
	private int bmpW;// ����ͼƬ���
	private int offset = 0;// ����ͼƬƫ����
	private Animation animation;
	private int one ;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_zhengce_activity);
		context=this;
		initViews();
		((MyApplication)getApplication()).addActivity(this);
	}

	private void initViews() {
		Bundle bundle=getIntent().getExtras();
		if (bundle!=null) {
			if (bundle.containsKey(PLICYLISTSTR)) {
				str=bundle.getString(PLICYLISTSTR);
			}
		}
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		screenWidth = dm.widthPixels; // ��ȡ�ֱ��ʿ��
		scrollbar_iv = (ImageView) findViewById(R.id.scrollbar_iv);
		bmpW = BitmapFactory
				.decodeResource(getResources(), R.drawable.typeline).getWidth();// ��ȡͼƬ���
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		offset = (int) ((screenWidth / 2 - bmpW) / 2);// ����ƫ����
		one = (int) ((screenWidth / 2) + 50);
		Matrix matrix = new Matrix();
		matrix.postTranslate(0, 0);
		scrollbar_iv.setImageMatrix(matrix);// ���ö�����ʼλ��
		putongzhengce_tv = (TextView) findViewById(R.id.singleline_tv);
		gaofanzhengce_tv = (TextView) findViewById(R.id.doubleline_tv);
		putongzhengce_tv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				putongzhengce_tv.setTextColor(context.getResources().getColor(
						R.color.blue_title_color));
				gaofanzhengce_tv.setTextColor(context.getResources().getColor(
						R.color.black_txt_color));
				isInlandCity=true;

				animation = new TranslateAnimation(one, 0, 0, 0);
				animation.setFillAfter(true);// True:ͼƬͣ�ڶ�������λ��
				animation.setDuration(300);
				scrollbar_iv.startAnimation(animation);
				
			}
		});
		gaofanzhengce_tv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				putongzhengce_tv.setTextColor(context.getResources().getColor(
						R.color.black_txt_color));
				gaofanzhengce_tv.setTextColor(context.getResources().getColor(
						R.color.blue_title_color));
				isInlandCity=false;

				animation = new TranslateAnimation(offset, one, 0, 0);
				animation.setFillAfter(true);// True:ͼƬͣ�ڶ�������λ��
				animation.setDuration(300);
				scrollbar_iv.startAnimation(animation);
				
//				SourceDateList = getAirportCityModel() ;
//				sortCities();
//				adapter = new SortAdapter(context, SourceDateList);
//				sortListView.setAdapter(adapter);
			}
		});
		
		back_imgbtn = (ImageButton) findViewById(R.id.back_imgbtn);
		back_imgbtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		sortListView = (ListView) findViewById(R.id.country_lvcountry);
		sortListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				//����Ҫ����adapter.getItem(position)����ȡ��ǰposition����Ӧ�Ķ���
				//Toast.makeText(getApplication(), ((ContactModel)adapter.getItem(position)).getCityName(), Toast.LENGTH_SHORT).show();
				//���س������Ƽ�������
//				setResult(
//						0,
//						getIntent().putExtra("pickedCity",
//								((AirportCityModel)adapter.getItem(position)).getCityName()+
//								"#"+
//								((AirportCityModel)adapter.getItem(position)).getAirportcode()));
				finish();
			}
		});
	}
	
	private void startQueryBaoxian() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				MyApp ma = new MyApp(context);
				String param = "action=policylist&str=" + str + "&userkey="
						+ ma.getHm().get(PackageKeys.USERKEY.getString()).toString() + "&sign="
						+ CommonFunc.MD5(ma.getHm().get(PackageKeys.USERKEY.getString()).toString() + "policylist" + str);
				policyReturnJson = HttpUtils.getJsonContent(ma.getServeUrl(),
						param);
				Message msg = new Message();
				msg.what = 1;
				handler.sendMessage(msg);
			}
		}).start();
	}
	
	private Handler handler=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what==1) {
				JSONTokener jsonParser;
				jsonParser = new JSONTokener(policyReturnJson);
			}
		}
		
	};
}
