package com.jike.shanglv;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.jike.shanglv.Common.CommonFunc;
import com.jike.shanglv.Enums.SPkeys;
import com.jike.shanglv.Models.Passenger;
import com.jike.shanglv.NetAndJson.HttpUtils;
import com.jike.shanglv.NetAndJson.JSONHelper;

public class ActivityInlandAirlineticketSelectPassengers extends Activity {

	protected static final int SELECTED_FINISH = 12;
	protected static final String SYSTYPE="Inland0_International1_Train2";
	private ImageButton back_imgbtn;
	private TextView finish_tv;
	private RelativeLayout add_new_passager_rl;
	private ListView history_passenger_listview;
	private Context context;
	private SharedPreferences sp;
	private ArrayList<Passenger> passengerList;//������ϵ���б�
	private ArrayList<Passenger> selectedPassengerList;//��ѡ�ĳ˻����б�
	private ArrayList<Passenger> bookingPassengerList;//����ҳ���û���ѡ��ĳ˻���list
	private String passengersReturnJson;// ����˷��صĳ�����ϵ���б�json
	private JSONArray plist;// ��ѯ���ĳ�����ϵ���б�
	private ListAdapter adapter;
	private Boolean isHasBookingPassenger=false;
	private Boolean isVisitNetwork=true;
	private String systype="0";//"systype":"0���� 1���� 2��Ʊ"

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_inland_airlineticket_select_passengers);
		initView();
		isHasBookingPassenger=getBookingPassengerList();
		if(!isVisitNetwork){//���û�з������磬��ֱ�Ӱ�intent�е����ݵ���ϵ���б��listview��
			adapter = new ListAdapter(
					context,
					ActivityInlandAirlineticketSelectPassengers.this,
					passengerList);
			history_passenger_listview.setAdapter(adapter);

		}
	}

	private void initView() {
		context = this;
		sp = getSharedPreferences(SPkeys.SPNAME.getString(), 0);
		passengerList = new ArrayList<Passenger>();
		selectedPassengerList = new ArrayList<Passenger>();
		bookingPassengerList = new ArrayList<Passenger>();
		back_imgbtn = (ImageButton) findViewById(R.id.back_imgbtn);
		finish_tv = (TextView) findViewById(R.id.finish_tv);
		add_new_passager_rl = (RelativeLayout) findViewById(R.id.add_new_passager_rl);
		history_passenger_listview = (ListView) findViewById(R.id.history_passenger_listview);

		back_imgbtn.setOnClickListener(clickListener);
		finish_tv.setOnClickListener(clickListener);
		add_new_passager_rl.setOnClickListener(clickListener);
	}
	
	/**�������ҳ������ѡ��˻����򷵻�true����ȡ�˻����б�
	 * @return 
	 */
	private Boolean getBookingPassengerList() {
		Intent intent = getIntent();
		if (intent!=null) {
			String passengerString="",allPassengersListString="";
			if (intent.hasExtra(SYSTYPE)) {
				systype=intent.getStringExtra(SYSTYPE);
			}
			if (intent.hasExtra(ActivityInlandAirlineticketBooking.ALLPASSENGERSLIST)) {
				allPassengersListString=intent.getStringExtra(ActivityInlandAirlineticketBooking.ALLPASSENGERSLIST);
			}
			if (intent.hasExtra(ActivityInlandAirlineticketBooking.SELECTEDPASSENGERSLIST)) {
				passengerString=intent.getStringExtra(ActivityInlandAirlineticketBooking.SELECTEDPASSENGERSLIST);
			}
			try {
				bookingPassengerList=(ArrayList<Passenger>)JSONHelper.parseCollection(passengerString, List.class, Passenger.class);
				passengerList=(ArrayList<Passenger>)JSONHelper.parseCollection(allPassengersListString, List.class, Passenger.class);
				if (passengerList==null||passengerList.size()==0) {//������ϵ�˵��б�Ϊ�գ�˵���ǵ�һ����ӣ������·�л�ȡ����
					startQueryCommonPassengers();
				}
				if (passengerList!=null&&passengerList.size()>0) {
					isVisitNetwork=false;//intent�������ݣ�����Ҫ�������л�ȡ������
				}
				if (bookingPassengerList!=null&&bookingPassengerList.size()>0) {
					return true;
				}
			} catch (JSONException e) {
				if (allPassengersListString.equals("")) {
					startQueryCommonPassengers();	
				}
				e.printStackTrace();
				return false;
			}
		}
		return false;
	}

	OnClickListener clickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.back_imgbtn:
//				finish();
//				break;
			case R.id.finish_tv:
				setResult(
						SELECTED_FINISH,
						getIntent()
								.putExtra(
										ActivityInlandAirlineticketBooking.SELECTEDPASSENGERSLIST,
										JSONHelper
												.toJSON(selectedPassengerList)));
				setResult(
						SELECTED_FINISH,
						getIntent()
								.putExtra(
										ActivityInlandAirlineticketBooking.ALLPASSENGERSLIST,
										JSONHelper
												.toJSON(passengerList)));
				finish();
				break;
			case R.id.add_new_passager_rl:
				/**Ϊ�˱���ͳһ�������༭ҳ��ļ򻯣�������ϵ��ʱҲ�Ὣ��ϵ���б�����һ��ҳ�棬
				 * �����б�����һ��λ������һ�����б�indexΪlastindex
				 */
				int index = passengerList.size();
				Intent intent = new Intent(
						context,
						ActivityInlandAirlineticketAddoreditPassengers.class);
				intent.putExtra(SYSTYPE, systype);
				intent.putExtra("index", index);
				passengerList.add(new Passenger());
				intent.putExtra("passengerList",
						JSONHelper.toJSON(passengerList));
				startActivityForResult(intent, 20);
//				startActivity(new Intent(context,
//						ActivityInlandAirlineticketAddoreditPassengers.class));
				break;
			default:
				break;
			}
		}
	};

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				JSONTokener jsonParser;
				jsonParser = new JSONTokener(passengersReturnJson);
				try {
					JSONObject jsonObject = (JSONObject) jsonParser.nextValue();
					String state = jsonObject.getString("c");

					if (state.equals("0000")) {
						plist = jsonObject.getJSONArray("d");
						createList(plist);
						adapter = new ListAdapter(
								context,
								ActivityInlandAirlineticketSelectPassengers.this,
								passengerList);
						history_passenger_listview.setAdapter(adapter);

					} else {// "��ѯʧ��"
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				break;
			}
		}
	};

	private void createList(JSONArray flist_list) {
		passengerList.clear();
		for (int i = 0; i < flist_list.length(); i++) {
			try {
				Passenger ia = new Passenger(flist_list.getJSONObject(i)
						.toString(),systype);
				passengerList.add(ia);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void startQueryCommonPassengers() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				// url?action=passenger&sign=1232432&userkey=2bfc0c48923cf89de19f6113c127ce81&str={"userid":"","siteid":"","systype":"0���� 1���� 2��Ʊ","psgtype":"1���� 2��ͯ 3Ӥ��"}&sitekey=defage
				MyApp ma = new MyApp(context);
				String str = "{\"systype\":\"" + systype + "\",\"psgtype\":\"" + "1"
						+ "\",\"userid\":\""
						+ sp.getString(SPkeys.userid.getString(), "")
						+ "\",\"siteid\":\"65\"}";
				String param = "action=passenger&str=" + str + "&userkey="
						+ MyApp.userkey + "&sign="
						+ CommonFunc.MD5(MyApp.userkey + "passenger" + str);
				passengersReturnJson = HttpUtils.getJsonContent(
						ma.getServeUrl(), param);
				Message msg = new Message();
				msg.what = 1;
				handler.sendMessage(msg);
			}
		}).start();
	}

	private class ListAdapter extends BaseAdapter {
		private LayoutInflater inflater;
		private List<Passenger> str;
		private Activity activity;

		public ListAdapter(Context context, Activity activity,
				List<Passenger> list1) {
			this.inflater = LayoutInflater.from(context);
			this.str = list1;
			this.activity = activity;
		}

		@Override
		public int getCount() {
			return str.size();
		}

		@Override
		public Object getItem(int position) {
			return str.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = inflater
						.inflate(
								R.layout.item_inland_airlineticket_select_passenger_list,
								null);
			}
			TextView passengerName_tv = (TextView) convertView
					.findViewById(R.id.passengerName_tv);
			TextView identificationType_tv = (TextView) convertView
					.findViewById(R.id.identificationType_tv);
			TextView identificationNum_tv = (TextView) convertView
					.findViewById(R.id.identificationNum_tv);
			TextView passengerType_tv = (TextView) convertView
					.findViewById(R.id.passengerType_tv);

			passengerName_tv.setText(str.get(position).getPassengerName());
			identificationType_tv.setText(str.get(position)
					.getIdentificationType());
			identificationNum_tv.setText(str.get(position)
					.getIdentificationNum());
			passengerType_tv.setText("(" + str.get(position).getPassengerType()
					+ ")");
			RelativeLayout passenger_rl = (RelativeLayout) convertView
					.findViewById(R.id.passenger_rl);
			passenger_rl.setTag(position + "");

			ImageButton edit_imgbtn = (ImageButton) convertView
					.findViewById(R.id.edit_imgbtn);
			edit_imgbtn.setTag(position + "");// ��Item�е�button����tag������tag�ж��û�����˵ڼ���
			edit_imgbtn.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					int index = Integer.parseInt(v.getTag().toString());
					Intent intent = new Intent(
							context,
							ActivityInlandAirlineticketAddoreditPassengers.class);
					intent.putExtra(SYSTYPE, systype);
					intent.putExtra("index", index);
					intent.putExtra("passengerList",
							JSONHelper.toJSON(passengerList));
					activity.startActivityForResult(intent, 10);
				}
			});

			final ImageButton select_imgbtn = (ImageButton) convertView
					.findViewById(R.id.select_imgbtn);
			select_imgbtn.setTag(position + "");
//			if (!isHasBookingPassenger&&position == 0) {//��һ����ӳ˻��ˣ�Ĭ��ѡ�е�һ��
//				select_imgbtn.setSelected(true);
//				if (!selectedPassengerList.contains(passengerList.get(0)))
//					selectedPassengerList.add(passengerList.get(0));
//			}else 
			if(isHasBookingPassenger){//����ҳ�����г˻��ˣ���ѡ����Щ�˻���(����������֤�����ж�)
				for (int i = 0; i < bookingPassengerList.size(); i++) {
					if (bookingPassengerList.get(i).getPassengerName().equals(passengerList.get(position).getPassengerName())&&
							bookingPassengerList.get(i).getIdentificationNum().equals(passengerList.get(position).getIdentificationNum())) {
						select_imgbtn.setSelected(true);
						if (!selectedPassengerList.contains(passengerList.get(position))) {
							 selectedPassengerList.add(passengerList.get(position));
						 }
					}
				}
			}
			passenger_rl.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					select_imgbtn.performClick();
				}
			});
			select_imgbtn.setOnClickListener(selectedPassengerClickListener);
			return convertView;
		}
		
		View.OnClickListener selectedPassengerClickListener=new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				int index = Integer.parseInt(v.getTag().toString());
				ImageButton the_select_imgbtn = (ImageButton) v;
//				the_select_imgbtn.setSelected(!the_select_imgbtn.isSelected());
				 if (the_select_imgbtn.isSelected()) {
					 the_select_imgbtn.setSelected(false);
					 if (selectedPassengerList.contains(passengerList.get(index))) {
						 selectedPassengerList.remove(passengerList.get(index));
					 }
				 }
				 else{
					 the_select_imgbtn.setSelected(true);
					 if (!selectedPassengerList.contains(passengerList.get(index))) {
						 selectedPassengerList.add(passengerList.get(index));
					 }
				 }
			}
		};
	}

	//����������༭�˻���ҳ��
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (resultCode) {
		case ActivityInlandAirlineticketAddoreditPassengers.EDIT_PASSENGER_CODE:
			Bundle b = null;
			if (data != null) {
				b = data.getExtras();
			} else
				break;
			String modifiedpassengerList = "";
			if (b != null
					&& b.containsKey(ActivityInlandAirlineticketBooking.ALLPASSENGERSLIST)) {
				modifiedpassengerList = b
						.getString(ActivityInlandAirlineticketBooking.ALLPASSENGERSLIST);
			} else
				break;
			try {
				passengerList = (ArrayList<Passenger>) JSONHelper
						.parseCollection(modifiedpassengerList, List.class,
								Passenger.class);
				ListAdapter adapter = new ListAdapter(context,
						ActivityInlandAirlineticketSelectPassengers.this,
						passengerList);
				history_passenger_listview.setAdapter(adapter);
			} catch (JSONException e) {
				e.printStackTrace();
				break;
			}
			break;
		default:
			break;
		}
	}
}
