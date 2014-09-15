package com.jike.shanglv;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jike.shanglv.Common.ClearEditText;
import com.jike.shanglv.Common.CommonFunc;
import com.jike.shanglv.Common.CustomProgressDialog;
import com.jike.shanglv.Common.DateUtil;
import com.jike.shanglv.Common.IdType;
import com.jike.shanglv.Enums.Platform;
import com.jike.shanglv.Enums.SPkeys;
import com.jike.shanglv.Enums.SingleOrDouble;
import com.jike.shanglv.LazyList.ImageLoader;
import com.jike.shanglv.Models.CabList;
import com.jike.shanglv.Models.CuandanFlight;
import com.jike.shanglv.Models.CuandanPassenger;
import com.jike.shanglv.Models.InlandAirlineInfo;
import com.jike.shanglv.Models.Passenger;
import com.jike.shanglv.NetAndJson.HttpUtils;
import com.jike.shanglv.NetAndJson.JSONHelper;

public class ActivityInlandAirlineticketBooking extends Activity {

	protected static final String ALLPASSENGERSLIST = "ALL_PASSENGERS_LIST";
	protected static final String SELECTEDPASSENGERSLIST = "SELECTED_PASSENGERS_LIST";
	protected static final String SELECTED_CABIN_INDEX = "selectCabinListIndex";// ����ʱ���û�ѡ��Ĳ�λ
	protected static final String SELECTED_CABIN_INDEX1 = "selectCabinListIndex1"; // ���̻�����ȥ�̲�λ���
	protected static final String SELECTED_CABIN_INDEX2 = "selectCabinListIndex2"; // �������̲�λ���
	protected static final String ORDERWAYTYPE = "ORDER_WAY_TYPE";

	protected static final int ADD_PASSENGERS_FORRESULET_CODE = 13;
	protected static final int BAOXIAN_MSG_CODE = 5;
	protected static final int COMMIT_ORDER_MSG_CODE = 6;
	protected static final int NEW_ORDER_DETAIL_CODE = 7;

	private Context context;
	private ImageButton back_imgbtn, home_imgbtn, lianxiren_icon_imgbtn,
			baoxian_check_imgbtn;
	private TextView total_price_tv, baoxian_price_and_count_tv,
			order_totalmoney_tv, add_passager_tv,

			startoff_date_tv, CarrinerName_tv, FlightNo_tv, canbin_grade_tv,
			startoff_time_tv, arrive_time_tv, start_port_tv, arrive_port_tv,
			start_port_tv2, arrive_port_tv2, jipiaojia_tv, jijian_price_tv,
			ranyou_price_tv, fanMoney_tv, tuiGaiQian_state_tv, runtime_tv,

			startoff_date_tv3, CarrinerName_tv3, FlightNo_tv3,
			canbin_grade_tv3, startoff_time_tv3, arrive_time_tv3,
			start_port_tv3, arrive_port_tv3, start_port_tv23, arrive_port_tv23,
			jipiaojia_tv3, jijian_price_tv3, ranyou_price_tv3, fanMoney_tv3,
			tuiGaiQian_state_tv3, runtime_tv3,

			total_jipiaojia_tv, total_jijian_price_tv, total_ranyou_price_tv,
			total_fanMoney_tv;
	private ClearEditText contact_person_phone_et;
	private Button order_now_btn;
	private RelativeLayout add_passager_rl, tuiGaiQian_rl, baoxian_rl;
	private ImageView up_down_arrow_iv,flight_company_logo;
	private ListView passenger_listview;
	private View passenger_head_divid_line;
	private SharedPreferences sp;
	private float totalPrice = 0, baoxian_unitPrice = 0;

	private JSONObject jsonObject;
	private int selectCabinListIndex = 0;
	private float jipiaojia = 0, youhui = 0, jijianfei = 0, ranyoufei = 0;

	private JSONObject jsonObject3;
	private int selectCabinListIndex3 = 0;
	private float jipiaojia3 = 0, youhui3 = 0, jijianfei3 = 0, ranyoufei3 = 0;
	private RelativeLayout tuiGaiQian_rl3;
	private ImageView up_down_arrow_iv3;

	private Boolean needBaoxianBoolean = true;// �Ƿ�����
	private String baoxianReturnJson = "", commitReturnJson = "";
	private ArrayList<Passenger> passengerList;// ѡ��ĳ˻����б�
	private ArrayList<Passenger> allPassengerList;// ��ǰ���г˻��˵��б�������˺��û������ģ�
	private CustomProgressDialog progressdialog;
	private ImageLoader imageLoader;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getOrderWayType() == SingleOrDouble.singleWay) {
			setContentView(R.layout.activity_inland_airlineticket_booking);
		} else {
			setContentView(R.layout.activity_inland_airlineticket_booking_doubleway);
		}
		try {// ���̡�����ȥ����Ϣ
			initView();
			if (getOrderWayType() == SingleOrDouble.doubleWayBack) {
				initView3();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void initView() {
		context = this;
		sp = getSharedPreferences(SPkeys.SPNAME.getString(), 0);
		imageLoader=new ImageLoader(context.getApplicationContext());
		passengerList = new ArrayList<Passenger>();
		allPassengerList = new ArrayList<Passenger>();

		total_jipiaojia_tv = (TextView) findViewById(R.id.total_jipiaojia_tv);
		total_jijian_price_tv = (TextView) findViewById(R.id.total_jijian_price_tv);
		total_ranyou_price_tv = (TextView) findViewById(R.id.total_ranyou_price_tv);
		total_fanMoney_tv = (TextView) findViewById(R.id.total_fanMoney_tv);

		back_imgbtn = (ImageButton) findViewById(R.id.back_imgbtn);
		home_imgbtn = (ImageButton) findViewById(R.id.home_imgbtn);
		back_imgbtn.setOnClickListener(btnClickListner);
		home_imgbtn.setOnClickListener(btnClickListner);
		passenger_head_divid_line = findViewById(R.id.passenger_head_divid_line);

		lianxiren_icon_imgbtn = (ImageButton) findViewById(R.id.lianxiren_icon_imgbtn);
		lianxiren_icon_imgbtn.setOnClickListener(btnClickListner);
		startoff_date_tv = (TextView) findViewById(R.id.startoff_date_tv);
		CarrinerName_tv = (TextView) findViewById(R.id.CarrinerName_tv);
		FlightNo_tv = (TextView) findViewById(R.id.FlightNo_tv);
		canbin_grade_tv = (TextView) findViewById(R.id.canbin_grade_tv);
		startoff_time_tv = (TextView) findViewById(R.id.startoff_time_tv);
		arrive_time_tv = (TextView) findViewById(R.id.arrive_time_tv);
		start_port_tv2 = (TextView) findViewById(R.id.start_port_tv2);
		arrive_port_tv2 = (TextView) findViewById(R.id.arrive_port_tv2);
		jipiaojia_tv = (TextView) findViewById(R.id.jipiaojia_tv);
		jijian_price_tv = (TextView) findViewById(R.id.jijian_price_tv);
		ranyou_price_tv = (TextView) findViewById(R.id.ranyou_price_tv);
		fanMoney_tv = (TextView) findViewById(R.id.fanMoney_tv);
		total_price_tv = (TextView) findViewById(R.id.total_price_tv);
		tuiGaiQian_state_tv = (TextView) findViewById(R.id.tuiGaiQian_state_tv);
		start_port_tv = (TextView) findViewById(R.id.start_port_tv);
		arrive_port_tv = (TextView) findViewById(R.id.arrive_port_tv);
		runtime_tv = (TextView) findViewById(R.id.runtime_tv);
		order_totalmoney_tv = (TextView) findViewById(R.id.order_totalmoney_tv);
		baoxian_price_and_count_tv = (TextView) findViewById(R.id.baoxian_price_and_count_tv);
		add_passager_tv = (TextView) findViewById(R.id.add_passager_tv);
		flight_company_logo=(ImageView) findViewById(R.id.flight_company_logo);

		contact_person_phone_et = (ClearEditText) findViewById(R.id.contact_person_phone_et);
		order_now_btn = (Button) findViewById(R.id.order_now_btn);
		order_now_btn.setOnClickListener(btnClickListner);

		add_passager_rl = (RelativeLayout) findViewById(R.id.add_passager_rl);
		add_passager_rl.setOnClickListener(btnClickListner);

		tuiGaiQian_rl = (RelativeLayout) findViewById(R.id.tuiGaiQian_rl);
		tuiGaiQian_rl.setOnClickListener(btnClickListner);
		passenger_listview = (ListView) findViewById(R.id.passenger_listview);
		up_down_arrow_iv = (ImageView) findViewById(R.id.up_down_arrow_iv);

		// B2C���չ̶�Ϊ20��B2B���ռ۸�ӷ�������ȡ
		if (MyApp.platform == Platform.B2C) {
			baoxian_unitPrice = 20;
		} else if (MyApp.platform == Platform.B2B) {
			startQueryBaoxian();
		}
		// ���ڳ�����ϵ�ˣ�ֱ�ӷ����ϴζ�Ʊʱ����ϵ���ֻ��ţ����������򷵻ر����ֻ�����
		if (sp.getString(SPkeys.gnjpContactPhone.getString(), "").equals(""))
			contact_person_phone_et.setText(CommonFunc.getPhoneNumber(context));
		else
			contact_person_phone_et.setText(sp.getString(
					SPkeys.gnjpContactPhone.getString(), ""));

		InlandAirlineInfo ia = new InlandAirlineInfo();
		// if (getOrderWayType()==SingleOrDouble.singleWay) {
		ia = getIntentFlightInfo(SingleOrDouble.singleWay);
		// }else if (getOrderWayType()==SingleOrDouble.doubleWayBack) {
		// ia =getIntentFlightInfo(SingleOrDouble.doubleWayBack);
		// }
		try {
			startoff_date_tv.setText(DateUtil.getDate(ia.getOffTime()));
			startoff_time_tv.setText(DateUtil.getTime(ia.getOffTime()));
			arrive_time_tv.setText(DateUtil.getTime(ia.getArriveTime()));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		start_port_tv.setText(ia.getStartPortName() + ia.getStartT());
		arrive_port_tv.setText(ia.getEndPortName() + ia.getEndT());
		start_port_tv2.setText(ia.getStartPortName());
		arrive_port_tv2.setText(ia.getEndPortName());
		runtime_tv.setText(ia.getRunTime());
		jijianfei = Float.valueOf(ia.getTax());
		ranyoufei = Float.valueOf(ia.getOil());
		jijian_price_tv.setText("��" + String.valueOf(jijianfei));
		ranyou_price_tv.setText("��" + String.valueOf(ranyoufei));
		CarrinerName_tv.setText(ia.getCarrinerName());
		FlightNo_tv.setText(ia.getFlightNo());
		try {
			jipiaojia = Float.parseFloat(jsonObject.getJSONArray("CabList")
					.getJSONObject(selectCabinListIndex).getString("Fare"));
			youhui = Float.parseFloat(jsonObject.getJSONArray("CabList")
					.getJSONObject(selectCabinListIndex).getString("YouHui"));
			canbin_grade_tv.setText(jsonObject.getJSONArray("CabList")
					.getJSONObject(selectCabinListIndex).getString("CabinName")
					+ jsonObject.getJSONArray("CabList")
							.getJSONObject(selectCabinListIndex)
							.getString("Cabin"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		jipiaojia_tv.setText("��" + String.valueOf(jipiaojia));
		fanMoney_tv.setText("����" + youhui);
		
		String picN=ia.getFlightNo().substring(0, 2);
		MyApp ma = new MyApp(context);
		String imgUrl=String.format(ma.getFlightCompanyLogo(),picN);
		imageLoader.DisplayImage(imgUrl, flight_company_logo);

		baoxian_rl = (RelativeLayout) findViewById(R.id.baoxian_rl);
		baoxian_rl.setOnClickListener(btnClickListner);
		baoxian_check_imgbtn = (ImageButton) findViewById(R.id.baoxian_check_imgbtn);
		baoxian_check_imgbtn.setOnClickListener(btnClickListner);
		baoxian_price_and_count_tv.setOnClickListener(btnClickListner);

		caculateBaoxian(baoxian_unitPrice);
		baoxian_check_imgbtn.setSelected(true);
		baoxian_price_and_count_tv.setTextColor(getResources().getColor(
				R.color.price_yellow));
	}

	private void initView3() {
		startoff_date_tv3 = (TextView) findViewById(R.id.startoff_date_tv3);
		CarrinerName_tv3 = (TextView) findViewById(R.id.CarrinerName_tv3);
		FlightNo_tv3 = (TextView) findViewById(R.id.FlightNo_tv3);
		canbin_grade_tv3 = (TextView) findViewById(R.id.canbin_grade_tv3);
		startoff_time_tv3 = (TextView) findViewById(R.id.startoff_time_tv3);
		arrive_time_tv3 = (TextView) findViewById(R.id.arrive_time_tv3);
		start_port_tv3 = (TextView) findViewById(R.id.start_port_tv3);
		arrive_port_tv3 = (TextView) findViewById(R.id.arrive_port_tv3);
		start_port_tv23 = (TextView) findViewById(R.id.start_port_tv23);
		arrive_port_tv23 = (TextView) findViewById(R.id.arrive_port_tv23);
		jipiaojia_tv3 = (TextView) findViewById(R.id.jipiaojia_tv3);
		jijian_price_tv3 = (TextView) findViewById(R.id.jijian_price_tv3);
		ranyou_price_tv3 = (TextView) findViewById(R.id.ranyou_price_tv3);
		fanMoney_tv3 = (TextView) findViewById(R.id.fanMoney_tv3);
		tuiGaiQian_state_tv3 = (TextView) findViewById(R.id.tuiGaiQian_state_tv3);
		runtime_tv3 = (TextView) findViewById(R.id.runtime_tv3);
		up_down_arrow_iv3 = (ImageView) findViewById(R.id.up_down_arrow_iv3);

		InlandAirlineInfo ia = new InlandAirlineInfo();
		// if (getOrderWayType()==SingleOrDouble.singleWay) {
		// ia =getIntentFlightInfo(SingleOrDouble.singleWay);
		// }else if (getOrderWayType()==SingleOrDouble.doubleWayBack)
		{
			ia = getIntentFlightInfo(SingleOrDouble.doubleWayBack);
		}
		try {
			startoff_date_tv3.setText(DateUtil.getDate(ia.getOffTime()));
			startoff_time_tv3.setText(DateUtil.getTime(ia.getOffTime()));
			arrive_time_tv3.setText(DateUtil.getTime(ia.getArriveTime()));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		start_port_tv3.setText(ia.getStartPortName() + ia.getStartT());
		arrive_port_tv3.setText(ia.getEndPortName() + ia.getEndT());
		start_port_tv23.setText(ia.getStartPortName());
		arrive_port_tv23.setText(ia.getEndPortName());
		runtime_tv3.setText(ia.getRunTime());
		jijianfei3 = Float.valueOf(ia.getTax());
		ranyoufei3 = Float.valueOf(ia.getOil());
		jijian_price_tv3.setText("��" + String.valueOf(jijianfei3));
		ranyou_price_tv3.setText("��" + String.valueOf(ranyoufei3));
		CarrinerName_tv3.setText(ia.getCarrinerName());
		FlightNo_tv3.setText(ia.getFlightNo());
		try {
			jipiaojia3 = Float.parseFloat(jsonObject3.getJSONArray("CabList")
					.getJSONObject(selectCabinListIndex3).getString("Fare"));
			youhui3 = Float.parseFloat(jsonObject3.getJSONArray("CabList")
					.getJSONObject(selectCabinListIndex3).getString("YouHui"));
			canbin_grade_tv3.setText(jsonObject3.getJSONArray("CabList")
					.getJSONObject(selectCabinListIndex3)
					.getString("CabinName")
					+ jsonObject3.getJSONArray("CabList")
							.getJSONObject(selectCabinListIndex3)
							.getString("Cabin"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		jipiaojia_tv3.setText("��" + String.valueOf(jipiaojia3));
		fanMoney_tv3.setText("����" + youhui3);

		tuiGaiQian_rl3 = (RelativeLayout) findViewById(R.id.tuiGaiQian_rl3);
		tuiGaiQian_rl3.setOnClickListener(btnClickListner);
		
		caculateBaoxian(baoxian_unitPrice);
	}

	private SingleOrDouble getOrderWayType() {
		SingleOrDouble sd = SingleOrDouble.singleWay;
		Bundle bundle = this.getIntent().getExtras();
		try {
			if (bundle != null) {
				sd = (SingleOrDouble) bundle.get(ORDERWAYTYPE);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sd;
	}

	/**
	 * SingleOrDouble.doubleWayGo����ѡ�񷵳̺󱻴���ΪSingleOrDouble.doubleWayBack
	 * ���Բ���ֻ����ΪSingleOrDouble.singleWay��SingleOrDouble.doubleWayBack������������Ʊ��
	 * @param sd
	 * @return
	 */
	private InlandAirlineInfo getIntentFlightInfo(SingleOrDouble sd) {
		Bundle bundle = this.getIntent().getExtras();
		InlandAirlineInfo ia = null;
		try {
			if (sd == SingleOrDouble.singleWay) {
				selectCabinListIndex = Integer.parseInt(bundle.get(
						SELECTED_CABIN_INDEX1).toString());
				jsonObject = new JSONObject(bundle.get(
						ActivityInlandAirlineticketSelectCabin.TOKEN_NAME1)
						.toString());
				ia = new InlandAirlineInfo(jsonObject);
			} else if (sd == SingleOrDouble.doubleWayBack) {
				selectCabinListIndex3 = Integer.parseInt(bundle.get(
						SELECTED_CABIN_INDEX2).toString());
				jsonObject3 = new JSONObject(bundle.get(
						ActivityInlandAirlineticketSelectCabin.TOKEN_NAME2)
						.toString());
				ia = new InlandAirlineInfo(jsonObject3);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return ia;
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			JSONTokener jsonParser;
			switch (msg.what) {
			case BAOXIAN_MSG_CODE:
				jsonParser = new JSONTokener(baoxianReturnJson);
				try {
					JSONObject jsonObject = (JSONObject) jsonParser.nextValue();
					String state = jsonObject.getString("c");

					if (state.equals("0000")) {
						JSONObject baoxianObject = jsonObject
								.getJSONObject("d");
						baoxian_unitPrice = Float.parseFloat(baoxianObject
								.getString("sale"));
						caculateBaoxian(baoxian_unitPrice);
					} else {
						Toast.makeText(context, "�����쳣����ȡ���ռ۸�ʧ�ܣ�", 0).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				break;
			case COMMIT_ORDER_MSG_CODE:
				jsonParser = new JSONTokener(commitReturnJson);
				try {
					JSONObject jsonObject = (JSONObject) jsonParser.nextValue();
					String state = jsonObject.getString("c");

					if (state.equals("0000")) {
						String orderID = jsonObject.getJSONObject("d")
								.getString("orderid");
						Intent intent = new Intent(context,
								ActivityInlandAirlineticketOrderDetail.class);
						intent.putExtra(
								ActivityInlandAirlineticketOrderDetail.ORDERRECEIPT,
								orderID);
						startActivityForResult(intent, NEW_ORDER_DETAIL_CODE);
					} else {
						Toast.makeText(context, "�����쳣���ύ����ʧ�ܣ�", 0).show();
					}
					progressdialog.dismiss();
				} catch (JSONException e) {
					progressdialog.dismiss();
					e.printStackTrace();
				}
				break;
			}
		}
	};

	private void startQueryBaoxian() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				// url?action=flightisu&sign=1232432&userkey=2bfc0c48923cf89de19f6113c127ce81&str={"userid":"","siteid":""}&sitekey=defage
				MyApp ma = new MyApp(context);
				String str = "{\"userid\":\""
						+ sp.getString(SPkeys.userid.getString(), "")
						+ "\",\"siteid\":\"65\"}";
				String param = "action=flightisu&str=" + str + "&userkey="
						+ MyApp.userkey + "&sign="
						+ CommonFunc.MD5(MyApp.userkey + "flightisu" + str);
				baoxianReturnJson = HttpUtils.getJsonContent(ma.getServeUrl(),
						param);
				Message msg = new Message();
				msg.what = BAOXIAN_MSG_CODE;
				handler.sendMessage(msg);
			}
		}).start();
	}

	private void commitOrder() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				// url?action=forder& "userid": "649","amount": "682","origin":
				// "6","orderremark": "������ע",
				MyApp ma = new MyApp(context);
				String flightString = "";
				if (getOrderWayType() == SingleOrDouble.singleWay) {
					flightString = getFlightsJsonString(SingleOrDouble.singleWay);
				} else if (getOrderWayType() == SingleOrDouble.doubleWayBack) {
					flightString = getFlightsJsonString(SingleOrDouble.singleWay)
							+ ","
							+ getFlightsJsonString(SingleOrDouble.doubleWayBack);
				}
				String str = "{\"userid\":\""
						+ sp.getString(SPkeys.userid.getString(), "")
						+ "\",\"amount\": \"" + totalPrice
						+ "\",\"origin\":\"2\",\"orderremark\":\"Android�ͻ���\""
						+ ",\"flights\":[" + flightString + "],\"passenger\":"
						+ getPassengerJsonString() + ",\"content\":"
						+ getContentJsonString() + ",\"itinerary\":{}"
						+ ",\"siteid\":\"65\"}";
				str=str.replace("null", "\"\"");
				String param = "?action=forder&userkey=" + MyApp.userkey
						+ "&sign="
						+ CommonFunc.MD5(MyApp.userkey + "forder" + str);
				commitReturnJson = HttpUtils.myPost(ma.getServeUrl() + param,
						"&str=" + str);
				Message msg = new Message();
				msg.what = COMMIT_ORDER_MSG_CODE;
				handler.sendMessage(msg);
			}
		}).start();
		progressdialog = CustomProgressDialog.createDialog(context);
		progressdialog.setMessage("�����ύ���������Ժ�...");
		progressdialog.setCancelable(true);
		progressdialog.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
			}
		});
		progressdialog.show();
	}

	/*
	 * ���ݳ˻����������յ��ۼ��㱣�ռ۸���ʾ��ҳ�� B2C�۸�̶�Ϊ��20/��
	 */
	private void caculateBaoxian(Float unitPrice) {
		int personCount = 0;
		if (passengerList == null) {
			personCount = 0;
		} else {
			personCount = passengerList.size();
		}

		if (getOrderWayType() == SingleOrDouble.singleWay) {
			baoxian_price_and_count_tv.setText("��" + baoxian_unitPrice + "x"
					+ personCount + "��");
			if (needBaoxianBoolean)
				totalPrice = personCount
						* (jipiaojia + jijianfei + ranyoufei
								+ baoxian_unitPrice - youhui);
			else
				totalPrice = personCount
						* (jipiaojia + jijianfei + ranyoufei - youhui);
		} else {
			baoxian_price_and_count_tv.setText("��" + baoxian_unitPrice + "x"
					+ personCount + "��x2(����)");
			if (needBaoxianBoolean)
				totalPrice = personCount
						* (jipiaojia + jijianfei + ranyoufei
								+ baoxian_unitPrice - youhui+jipiaojia3 + jijianfei3 + ranyoufei3
								+ baoxian_unitPrice - youhui3);
			else
				totalPrice = personCount
						* (jipiaojia + jijianfei + ranyoufei - youhui+jipiaojia3 + jijianfei3 + ranyoufei3 - youhui3);
			
			total_jipiaojia_tv.setText("��" + (jipiaojia + jipiaojia3));
			total_jijian_price_tv.setText("��" + (jijianfei + jijianfei));
			total_ranyou_price_tv.setText("��" + (ranyoufei + ranyoufei3));
			total_fanMoney_tv.setText("��" + (youhui + youhui3));
		}
		
		total_price_tv.setText("��" + String.valueOf(totalPrice));
		order_totalmoney_tv.setText("��" + String.valueOf(totalPrice));
	}

	// ���� ������Ϣ
	private String getFlightsJsonString(SingleOrDouble sd) {
		String str = "";
		InlandAirlineInfo flight = new InlandAirlineInfo();
		if (sd == SingleOrDouble.singleWay) {
			flight = getIntentFlightInfo(SingleOrDouble.singleWay);
		} else if (sd == SingleOrDouble.doubleWayBack) {
			flight = getIntentFlightInfo(SingleOrDouble.doubleWayBack);
		}
		CabList cabin = null;
		try {
			JSONObject object = flight.getCablist().getJSONObject(
					selectCabinListIndex);
			cabin = new CabList(object);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		CuandanFlight cf = new CuandanFlight();
		cf.setCabin(cabin.getCabin());
		cf.setCabinname(cabin.getCabinName());
		cf.setCarrname(flight.getCarrinerName());
		cf.setCrrier(cabin.getFareEx());
		cf.setDiscount(cabin.getDiscount());
		cf.setEcname(flight.getEndPortName());
		cf.setEcode(flight.getEndPort());
		try {
			cf.setEdate(DateUtil.getDate(flight.getArriveTime()));
			cf.setEtime(DateUtil.getTime(flight.getArriveTime()));
			cf.setSdate(DateUtil.getDate(flight.getOffTime()));
			cf.setStime(DateUtil.getTime(flight.getOffTime()));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		cf.setEt(flight.getEndT());
		cf.setFare(cabin.getFare());
		cf.setFlightno(flight.getFlightNo());
		cf.setIsspe(cabin.getIsSpe());
		cf.setOil(flight.getOil());
		cf.setPlane(flight.getPlaneModel());
		cf.setPolicyid(cabin.getPolicyID());
		cf.setPolicytype(cabin.getIsSpePolicy() == "true" ? "1" : "0");
		cf.setRate(cabin.getRate());
		cf.setRateinfo(cabin.getRateInfo().replace("\n", "").replace("\r", "")
				.replace(" ", ""));
		cf.setRebate(cabin.getRate());
		cf.setRemark("");
		cf.setRt("");// ��Ӧ����Ʊʱ��
		cf.setRuntime(flight.getRunTime());
		cf.setScname(flight.getStartPortName());
		cf.setScode(flight.getStartPort());
		cf.setStafare(flight.getStaFare());
		cf.setSupplier(cabin.getSupplier());
		cf.setSt(flight.getStartT());
		cf.setTax(flight.getTax());
		cf.setType("1");// ����
		cf.setUserrebate(cabin.getUserRate());
		cf.setWt("");// "��Ӧ�����°�ʱ��"

		str = JSONHelper.toJSON(cf);
		return str;
	}

	// ���� �˻�����Ϣ
	private String getPassengerJsonString() {
		String str = "";
		ArrayList<CuandanPassenger> cpList = new ArrayList<CuandanPassenger>();
		for (int i = 0; i < passengerList.size(); i++) {
			CuandanPassenger cp = new CuandanPassenger();
			cp.setAddto("1");
			cp.setIdno(passengerList.get(i).getIdentificationNum());
			String idtypeString = passengerList.get(i).getIdentificationType();
			cp.setIdtype(String.valueOf(IdType.IdTypeReverse.get(idtypeString)));
			cp.setIsunum(needBaoxianBoolean ? "1" : "0");
			cp.setMobile(passengerList.get(i).getMobie());
			try {
				cp.setPname(URLEncoder.encode((passengerList.get(i).getPassengerName()), "utf-8"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			cp.setPtype("1");
			cpList.add(cp);
		}
		str = JSONHelper.toJSON(cpList);
		return str;
	}

	// ���� ��ϵ����Ϣ
	private String getContentJsonString() {
		String str = "";
		str = "{\"name\": \"" + sp.getString(SPkeys.username.getString(), "")
				+ "\",\"mobile\": \""
				+ contact_person_phone_et.getText().toString().trim()
				+ "\",\"tel\": \"\",\"email\": \"\"}";
		return str;
	}

	View.OnClickListener btnClickListner = new View.OnClickListener() {
		@SuppressLint("ResourceAsColor")
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.back_imgbtn:
				finish();
				break;
			case R.id.home_imgbtn:
				startActivity(new Intent(context, MainActivity.class));
				break;
			case R.id.lianxiren_icon_imgbtn:
				startActivityForResult(
						new Intent(
								context,
								com.jike.shanglv.SeclectCity.ContactListActivity.class),
						13);
				break;
			case R.id.tuiGaiQian_rl:
				up_down_arrow_iv.setSelected(!up_down_arrow_iv.isSelected());
				if (tuiGaiQian_state_tv.getVisibility() == View.VISIBLE) {
					tuiGaiQian_state_tv.setVisibility(View.GONE);
				} else if (tuiGaiQian_state_tv.getVisibility() == View.GONE) {
					tuiGaiQian_state_tv.setVisibility(View.VISIBLE);
				}
				break;
			case R.id.tuiGaiQian_rl3:
				up_down_arrow_iv3.setSelected(!up_down_arrow_iv.isSelected());
				if (tuiGaiQian_state_tv3.getVisibility() == View.VISIBLE) {
					tuiGaiQian_state_tv3.setVisibility(View.GONE);
				} else if (tuiGaiQian_state_tv3.getVisibility() == View.GONE) {
					tuiGaiQian_state_tv3.setVisibility(View.VISIBLE);
				}
				break;
			case R.id.add_passager_rl:
				Intent intent = new Intent(context,
						ActivityInlandAirlineticketSelectPassengers.class);
				intent.putExtra(ActivityInlandAirlineticketSelectPassengers.SYSTYPE,
						"0");
				intent.putExtra(ALLPASSENGERSLIST,
						JSONHelper.toJSON(allPassengerList));
				intent.putExtra(SELECTEDPASSENGERSLIST,
						JSONHelper.toJSON(passengerList));
				startActivityForResult(intent, ADD_PASSENGERS_FORRESULET_CODE);
				break;
			case R.id.baoxian_price_and_count_tv:
			case R.id.baoxian_check_imgbtn:
			case R.id.baoxian_rl:
				if (!baoxian_check_imgbtn.isSelected()) {
					baoxian_check_imgbtn.setSelected(true);
					needBaoxianBoolean = true;
					baoxian_price_and_count_tv.setTextColor(getResources()
							.getColor(R.color.price_yellow));
				} else if (baoxian_check_imgbtn.isSelected()) {
					baoxian_check_imgbtn.setSelected(false);
					needBaoxianBoolean = false;
					baoxian_price_and_count_tv.setTextColor(getResources()
							.getColor(R.color.danhuise));
				}
				caculateBaoxian(baoxian_unitPrice);
				break;
			case R.id.order_now_btn:
				if (passengerList.size() == 0) {
					new AlertDialog.Builder(context).setTitle("�˻��˲���Ϊ��")
							.setMessage("�����ӳ˻��ˣ�")
							.setPositiveButton("ȷ��", null).show();
					break;
				}
				if (!CommonFunc.isMobileNO(contact_person_phone_et.getText()
						.toString().trim())) {
					new AlertDialog.Builder(context).setTitle("�ֻ������ʽ����ȷ")
							.setMessage("������Ϸ����ֻ����룡")
							.setPositiveButton("ȷ��", null).show();
					break;
				} else {
					sp.edit()
							.putString(
									SPkeys.gnjpContactPhone.getString(),
									contact_person_phone_et.getText()
											.toString()).commit();
				}
				commitOrder();
				break;
			default:
				break;
			}
		}
	};

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (resultCode) {
		case ActivityInlandAirlineticketSelectPassengers.SELECTED_FINISH:
			Bundle b = null;
			if (data != null) {
				b = data.getExtras();
			} else
				break;
			String passengerListString = "",
			allPassengerListString = "";
			if (b != null && b.containsKey(SELECTEDPASSENGERSLIST)) {
				passengerListString = b.getString(SELECTEDPASSENGERSLIST);
			}
			if (b != null && b.containsKey(ALLPASSENGERSLIST)) {
				allPassengerListString = b.getString(ALLPASSENGERSLIST);
			} else
				break;
			try {
				passengerList.clear();
				passengerList = (ArrayList<Passenger>) JSONHelper
						.parseCollection(passengerListString, List.class,
								Passenger.class);
				allPassengerList = (ArrayList<Passenger>) JSONHelper
						.parseCollection(allPassengerListString, List.class,
								Passenger.class);
				passengerList = removeDuplictePassengers(passengerList);
				if (passengerList.size() > 0) {
					add_passager_tv.setText(getResources().getString(
							R.string.modify_passenger));
					passenger_head_divid_line.setVisibility(View.VISIBLE);
				} else if (passengerList.size() == 0) {
					add_passager_tv.setText(getResources().getString(
							R.string.add_passenger));
					passenger_head_divid_line.setVisibility(View.GONE);
				}
				caculateBaoxian(baoxian_unitPrice);
				ListAdapter adapter = new PassengerListAdapter(context,
						passengerList);
				passenger_listview.setAdapter(adapter);
				setListViewHeightBasedOnChildren(passenger_listview);
			} catch (JSONException e) {
				e.printStackTrace();
				break;
			}
			break;
		default:
			break;
		}
		
		if (requestCode == 13) {
			if (data == null)
				return;
			Bundle b = data.getExtras();
			if (b != null && b.containsKey("pickedPhoneNum")) {
				String myNum = b.getString("pickedPhoneNum");
				if (myNum.startsWith("17951")) {
					myNum=myNum.substring(5);
				}else if (myNum.startsWith("+86")) {
					myNum=myNum.substring(3);
				}
				contact_person_phone_et.setText(myNum);
			}
		}
	}

	private class PassengerListAdapter extends BaseAdapter {
		private LayoutInflater inflater;
		private List<Passenger> str;

		public PassengerListAdapter(Context context, List<Passenger> list1) {
			this.inflater = LayoutInflater.from(context);
			this.str = list1;
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
								R.layout.item_inland_airlineticket_passenger_list,
								null);
			}
			View divid_line = convertView.findViewById(R.id.divid_line);
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
			if (position == passengerList.size() - 1) {
				divid_line.setVisibility(View.GONE);
			}

			ImageButton delete_imgbtn = (ImageButton) convertView
					.findViewById(R.id.delete_imgbtn);
			delete_imgbtn.setTag(position + "");// ��Item�е�button����tag������tag�ж��û�����˵ڼ���
			delete_imgbtn.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					int index = Integer.parseInt(v.getTag().toString());
					passengerList.remove(index);
					notifyDataSetChanged();
					setListViewHeightBasedOnChildren(passenger_listview);
					if (passengerList.size() == 0) {
						add_passager_tv.setText(getResources().getString(
								R.string.add_passenger));
						passenger_head_divid_line.setVisibility(View.GONE);
					}
					caculateBaoxian(baoxian_unitPrice);
				}
			});

			return convertView;
		}
	}

	// ȥ���ظ��ĳ˻���
	public static ArrayList<Passenger> removeDuplictePassengers(
			ArrayList<Passenger> userList) {
		Set<Passenger> s = new TreeSet<Passenger>(new Comparator<Passenger>() {

			@Override
			public int compare(Passenger o1, Passenger o2) {
				return o1.getPassengerName().compareTo(o2.getPassengerName());
			}
		});
		s.addAll(userList);
		return new ArrayList<Passenger>(s);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString("passengerList", JSONHelper.toJSON(passengerList));
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		if (savedInstanceState != null) {
			String pl = (String) savedInstanceState.getString("passengerList");
			try {
				passengerList.clear();
				passengerList = (ArrayList<Passenger>) JSONHelper
						.parseCollection(pl, List.class, Passenger.class);
				PassengerListAdapter adapter = new PassengerListAdapter(
						context, passengerList);
				passenger_listview.setAdapter(adapter);
				setListViewHeightBasedOnChildren(passenger_listview);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * ���ScrollViewǶ��ListViewֻ��ʾһ�е�����
	 * ��������ListView��Adapter�󣬸���ListView������Ŀ���¼���ListView�ĸ߶�
	 * @param listView
	 */
	public static void setListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			return;
		}

		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
	}
}