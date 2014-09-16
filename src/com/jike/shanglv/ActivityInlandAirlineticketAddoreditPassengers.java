package com.jike.shanglv;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import org.json.JSONException;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.jike.shanglv.Common.ClearEditText;
import com.jike.shanglv.Common.CommonFunc;
import com.jike.shanglv.Common.IDCard;
import com.jike.shanglv.Common.IdType;
import com.jike.shanglv.Models.Passenger;
import com.jike.shanglv.NetAndJson.JSONHelper;

public class ActivityInlandAirlineticketAddoreditPassengers extends Activity {

	protected static final int EDIT_PASSENGER_CODE = 2;
	protected static final String TOKEN_NAME = "PASSENGER";

	private TextView cancel_tv,finish_tv, passengerType_tv, identificationType_tv;
	private ClearEditText passengerName_et, identificationNum_et, phoneNum_et;
	private Button shenfenzheng_btn, huzhao_btn, gangaotongxingzheng_btn,
			taobaozheng_btn, qita_btn, chengren_btn, ertongpiao_btn,
			yingerpiao_btn;
	private PopupWindow popupWindow_idtype, popupWindow_ptype;
	private View popupWindowView_idtype, popupWindowView_ptype;
	private Context context;
	InputMethodManager imm;
	private Passenger editPassenger;
	private ArrayList<Passenger> passengerList;// 所有联系人的列表

	// 选择乘机人页面点击编辑后传过来的乘机人信息
	private String passengerString = "";// 所有联系人信息反序列化后传过来
	private int index = 0;// 当前编辑联系人在所有联系人列表中的序号
	private String systype = "0";// "systype":"0国内 1国际 2火车票"

	// 国际机票乘机人编辑
	private TextView IDdeadline_et, nation_et, gender_et, birthDay_et;
	private RelativeLayout IDdeadline_rl, nation_rl, gender_rl, birthDay_rl;
	private String IDdeadline = "", nation = "", gender = "", birthDay = "",
			issueAt = "";
	String genderValue = "男", genderKey = "1";// 性别选择，用户不选择默认时的值
	private EditText issueAt_et;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		if (intent != null) {
			if (intent
					.hasExtra(ActivityInlandAirlineticketSelectPassengers.SYSTYPE)) {
				systype = intent
						.getStringExtra(ActivityInlandAirlineticketSelectPassengers.SYSTYPE);
			}
		}
		if (systype.equals("0")||systype.equals("2")) {//国内机票或火车票
			setContentView(R.layout.activity_inland_airlineticket_addoredit_passengers);
		} else if (systype.equals("1")) {//国际需求单
			setContentView(R.layout.activity_international_airlineticket_addoredit_passengers);
			initInternationalView();
		}
		initView();
	}

	private void initView() {
		context = this;
		passengerList = new ArrayList<Passenger>();
		imm = (InputMethodManager) getSystemService(ActivityInlandAirlineticketAddoreditPassengers.this.INPUT_METHOD_SERVICE);
		cancel_tv = (TextView)findViewById(R.id.cancel_tv);
		finish_tv = (TextView)findViewById(R.id.finish_tv);
		passengerType_tv = (TextView) findViewById(R.id.passengerType_tv);
		identificationType_tv = (TextView) findViewById(R.id.identificationType_tv);
		passengerName_et = (ClearEditText) findViewById(R.id.passengerName_et);
		identificationNum_et = (ClearEditText) findViewById(R.id.identificationNum_et);
		phoneNum_et = (ClearEditText) findViewById(R.id.phoneNum_et);

		cancel_tv.setOnClickListener(clickListener);
		finish_tv.setOnClickListener(clickListener);
		identificationType_tv.setOnClickListener(clickListener);
		passengerType_tv.setOnClickListener(clickListener);

		LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		popupWindowView_idtype = inflater.inflate(
				R.layout.popupwindow_identification_type, null);
		popupWindow_idtype = new PopupWindow(popupWindowView_idtype,
				LayoutParams.FILL_PARENT, 420, true);
		popupWindow_idtype.setBackgroundDrawable(new BitmapDrawable());
		// 设置PopupWindow的弹出和消失效果
		popupWindow_idtype.setAnimationStyle(R.style.AnimBottomPopup);
		shenfenzheng_btn = (Button) popupWindowView_idtype
				.findViewById(R.id.shenfenzheng_btn);
		huzhao_btn = (Button) popupWindowView_idtype
				.findViewById(R.id.huzhao_btn);
		gangaotongxingzheng_btn = (Button) popupWindowView_idtype
				.findViewById(R.id.gangaotongxingzheng_btn);
		taobaozheng_btn = (Button) popupWindowView_idtype
				.findViewById(R.id.taobaozheng_btn);
		qita_btn = (Button) popupWindowView_idtype.findViewById(R.id.qita_btn);
		shenfenzheng_btn.setOnClickListener(popupClickListener);
		huzhao_btn.setOnClickListener(popupClickListener);
		gangaotongxingzheng_btn.setOnClickListener(popupClickListener);
		taobaozheng_btn.setOnClickListener(popupClickListener);
		qita_btn.setOnClickListener(popupClickListener);

		popupWindowView_ptype = inflater.inflate(
				R.layout.popupwindow_passenger_type, null);
		popupWindow_ptype = new PopupWindow(popupWindowView_ptype,
				LayoutParams.FILL_PARENT, 280, true);
		popupWindow_ptype.setBackgroundDrawable(new BitmapDrawable());
		// 设置PopupWindow的弹出和消失效果
		popupWindow_ptype.setAnimationStyle(R.style.AnimBottomPopup);
		chengren_btn = (Button) popupWindowView_ptype
				.findViewById(R.id.chengren_btn);
		ertongpiao_btn = (Button) popupWindowView_ptype
				.findViewById(R.id.ertongpiao_btn);
		yingerpiao_btn = (Button) popupWindowView_ptype
				.findViewById(R.id.yingerpiao_btn);
		chengren_btn.setOnClickListener(popupClickListener_ptype);
		ertongpiao_btn.setOnClickListener(popupClickListener_ptype);
		yingerpiao_btn.setOnClickListener(popupClickListener_ptype);

		Intent intent = getIntent();
		if (intent != null) {
			if (intent.hasExtra("passengerList") && intent.hasExtra("index")) {
				passengerString = intent.getStringExtra("passengerList");
				index = intent.getIntExtra("index", 0);
			} else {
				return;
			}
			try {
				passengerList = (ArrayList<Passenger>) JSONHelper
						.parseCollection(passengerString, List.class,
								Passenger.class);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			editPassenger = passengerList.get(index);
			if (editPassenger.getPassengerType() != null
					&& !editPassenger.getPassengerType().equals("null"))
				passengerType_tv.setText(editPassenger.getPassengerType());
			if (editPassenger.getPassengerName() != null
					&& !editPassenger.getPassengerName().equals("null"))
				passengerName_et.setText(editPassenger.getPassengerName());
			if (editPassenger.getIdentificationNum() != null
					&& !editPassenger.getIdentificationNum().equals("null"))
				identificationNum_et.setText(editPassenger
						.getIdentificationNum());
			if (editPassenger.getIdentificationType() != null
					&& !editPassenger.getIdentificationType().equals("null"))
				identificationType_tv.setText(editPassenger
						.getIdentificationType());
			if (editPassenger.getMobie() != null
					&& !editPassenger.getMobie().equals("null"))
				phoneNum_et.setText(editPassenger.getMobie());
			if (systype.equals("1")) {// 若为国际机票
				shenfenzheng_btn.setBackgroundColor(getResources().getColor(R.color.gray));
				shenfenzheng_btn.setEnabled(false);//国际不能使用身份证
				// issueAt_et,IDdeadline_et, nation_et, gender_et, birthDay_et
				if (editPassenger.getIssueAt() != null
						&& !editPassenger.getIssueAt().equals("null"))
					issueAt_et.setText(editPassenger.getIssueAt());
				if (editPassenger.getIDdeadline() != null
						&& !editPassenger.getIDdeadline().equals("null"))
					IDdeadline_et.setText(editPassenger.getIDdeadline());
				if (editPassenger.getNation() != null
						&& !editPassenger.getNation().equals("null"))
					nation_et.setText(editPassenger.getNation());
				if (editPassenger.getGender() != null
						&& !editPassenger.getGender().equals("null"))
					gender_et.setText(editPassenger.getGender().trim().equals("1")?"男":"女");
				if (editPassenger.getBirthDay() != null
						&& !editPassenger.getBirthDay().equals("null"))
					birthDay_et.setText(editPassenger.getBirthDay());
			}
		}
	}

	private void initInternationalView() {
		IDdeadline_et = (TextView) findViewById(R.id.IDdeadline_et);
		nation_et = (TextView) findViewById(R.id.nation_et);
		gender_et = (TextView) findViewById(R.id.gender_et);
		birthDay_et = (TextView) findViewById(R.id.birthDay_et);

		issueAt_et = (EditText) findViewById(R.id.issueAt_et);

		IDdeadline_rl = (RelativeLayout) findViewById(R.id.IDdeadline_rl);
		nation_rl = (RelativeLayout) findViewById(R.id.nation_rl);
		gender_rl = (RelativeLayout) findViewById(R.id.gender_rl);
		birthDay_rl = (RelativeLayout) findViewById(R.id.birthDay_rl);
		IDdeadline_rl.setOnClickListener(clickListener);
		nation_rl.setOnClickListener(clickListener);
		gender_rl.setOnClickListener(clickListener);
		birthDay_rl.setOnClickListener(clickListener);
	}

	/**
	 * 点击空白处隐藏键盘
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			System.out.println("down");
			if (ActivityInlandAirlineticketAddoreditPassengers.this
					.getCurrentFocus() != null) {
				if (ActivityInlandAirlineticketAddoreditPassengers.this
						.getCurrentFocus().getWindowToken() != null) {
					imm.hideSoftInputFromWindow(
							ActivityInlandAirlineticketAddoreditPassengers.this
									.getCurrentFocus().getWindowToken(),
							InputMethodManager.HIDE_NOT_ALWAYS);
				}
			}
		}
		return super.onTouchEvent(event);
	}

	OnClickListener clickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.cancel_tv://
				 finish();
				 break;
			case R.id.finish_tv:
				if (!validInput())
					break;
				Passenger passenger = new Passenger();
				passenger.setPassengerName(passengerName_et.getText()
						.toString().trim());
				passenger.setIdentificationNum(identificationNum_et.getText()
						.toString().trim());
				passenger.setMobie(phoneNum_et.getText().toString().trim());
				passenger.setPassengerType(passengerType_tv.getText()
						.toString().trim());
				passenger.setIdentificationType(identificationType_tv.getText()
						.toString().trim());
				passenger.setNation(nation);
				passenger.setGender(gender);
				passenger.setBirthDay(birthDay);
				passenger.setIDdeadline(IDdeadline);
				if (systype.equals("1"))
					issueAt = issueAt_et.getText().toString().trim();
				passenger.setIssueAt(issueAt);
				if (passenger.getPassengerName().equals(null)
						|| passenger.getPassengerName().trim().equals("")) {
					passengerList.remove(index);
				} else {
					passengerList.set(index, passenger);// 用新的编辑后的联系人替换联系人列表中原来的联系人
				}
				String passengerJsonString = JSONHelper.toJSON(passengerList);
				setResult(
						EDIT_PASSENGER_CODE,
						getIntent()
								.putExtra(
										ActivityInlandAirlineticketBooking.ALLPASSENGERSLIST,
										passengerJsonString));
				finish();// 返回所有联系人的列表
				break;
			case R.id.identificationType_tv:
				imm.hideSoftInputFromWindow(
						ActivityInlandAirlineticketAddoreditPassengers.this
								.getCurrentFocus().getWindowToken(),
						InputMethodManager.HIDE_NOT_ALWAYS);
				popupWindow_idtype.showAtLocation(shenfenzheng_btn,
						Gravity.BOTTOM, 0, 0);
				break;
			case R.id.passengerType_tv:
				imm.hideSoftInputFromWindow(
						ActivityInlandAirlineticketAddoreditPassengers.this
								.getCurrentFocus().getWindowToken(),
						InputMethodManager.HIDE_NOT_ALWAYS);
				popupWindow_ptype.showAtLocation(chengren_btn, Gravity.BOTTOM,
						0, 0);
				break;
			case R.id.IDdeadline_rl:
				Calendar c = Calendar.getInstance();
				new DatePickerDialog(context,
						new DatePickerDialog.OnDateSetListener() {
							@Override
							public void onDateSet(DatePicker view, int year,
									int monthOfYear, int dayOfMonth) {
								// TODO Auto-generated method stub
								IDdeadline_et.setText(year + "/"
										+ (monthOfYear + 1) + "/" + dayOfMonth);
								IDdeadline = year + "/" + (monthOfYear + 1)
										+ "/" + dayOfMonth;
							}
						}, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c
								.get(Calendar.DAY_OF_MONTH)).show();
				break;
			case R.id.birthDay_rl:
				Calendar c1 = Calendar.getInstance();
				new DatePickerDialog(context,
						new DatePickerDialog.OnDateSetListener() {
							@Override
							public void onDateSet(DatePicker view, int year,
									int monthOfYear, int dayOfMonth) {
								// TODO Auto-generated method stub
								birthDay_et.setText(year + "/"
										+ (monthOfYear + 1) + "/" + dayOfMonth);
								birthDay = year + "/" + (monthOfYear + 1) + "/"
										+ dayOfMonth;
							}
						}, c1.get(Calendar.YEAR), c1.get(Calendar.MONTH), c1
								.get(Calendar.DAY_OF_MONTH)).show();
				break;
			case R.id.gender_rl:
				AlertDialog.Builder builder = new AlertDialog.Builder(context);
				builder.setTitle("请选择性别");
				final String[] sex = { "女", "男" };
				// 设置一个单项选择下拉框
				/**
				 * 第一个参数指定我们要显示的一组下拉单选框的数据集合 第二个参数代表索引，指定默认哪一个单选框被勾选上，1表示默认'女'
				 * 会被勾选上 第三个参数给每一个单选项绑定一个监听器
				 */
				builder.setSingleChoiceItems(sex, 1,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// Toast.makeText(context, "性别为：" + sex[which],
								// Toast.LENGTH_SHORT).show();
								genderValue = (sex[which]);
								genderKey = which + "";
								gender_et.setText(genderValue);
								gender = genderKey;
							}
						});
				builder.setPositiveButton("确定",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								gender_et.setText(genderValue);
								gender = genderKey;
							}
						});
				builder.setNegativeButton("取消",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {

							}
						});
				builder.show();
				break;
			case R.id.nation_rl:
				AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
				builder1.setTitle("请选择国籍");
				// 指定下拉列表的显示数据
				final String[] cities = { "中国", "台湾", "香港", "澳门", "新加坡", "日本",
						"韩国", "美国", "加拿大", "法国", "英国", "其他" };
				// 设置一个下拉的列表选择项
				builder1.setItems(cities,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// Toast.makeText(context, "选择的国籍为：" +
								// cities[which], Toast.LENGTH_SHORT).show();
								nation_et.setText(cities[which]);
								nation = cities[which];
							}
						});
				builder1.show();
				break;
			default:
				break;
			}
		}
	};

	OnClickListener popupClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Button btn = (Button) v;
			identificationType_tv.setText(btn.getText());
			popupWindow_idtype.dismiss();
		}
	};

	OnClickListener popupClickListener_ptype = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Button btn = (Button) v;
			passengerType_tv.setText(btn.getText());
			popupWindow_ptype.dismiss();
		}
	};

	// 输入合法性判断
	private Boolean validInput() {
		if (passengerName_et.getText().toString().trim().length() == 0) {
			new AlertDialog.Builder(context).setTitle("姓名不能为空")
					.setMessage("请输入乘客姓名！").setPositiveButton("确定", null)
					.show();
			return false;
		}
		if (!systype.equals("1")&&phoneNum_et.getText().toString().trim().length()>0&&(CommonFunc.isMobileNO(phoneNum_et.getText()
				.toString().trim())==false&&CommonFunc.isPhone(phoneNum_et.getText()
						.toString().trim())==false)) {
			new AlertDialog.Builder(context).setTitle("电话号码格式不正确")
					.setMessage("请输入合法的手机号码或座机号码！")
					.setPositiveButton("确定", null).show();
			return false;
		}
		if (identificationNum_et.getText().toString().trim().length() == 0) {
			new AlertDialog.Builder(context).setTitle("证件号码不能为空")
					.setMessage("请输入证件号码！").setPositiveButton("确定", null)
					.show();
			return false;
		}
		if ((identificationType_tv.getText().toString().trim())
				.equals(IdType.IdType.get(0))) {
			if (!(new IDCard().verify(identificationNum_et.getText().toString()
					.trim()))) {
				new AlertDialog.Builder(context).setTitle("身份证号不合法")
						.setMessage("请输入合法的身份证号码！")
						.setPositiveButton("确定", null).show();
				return false;
			}
		}
		if (systype.equals("1")) {
			if (!CommonFunc.isEnglishName(passengerName_et.getText()
					.toString().trim())) {
				new AlertDialog.Builder(context).setTitle("姓名格式不正确")
						.setMessage("请输入英文名，姓氏和名字之间以斜杠分割，格式为\"zhang/san\"")
						.setPositiveButton("确定", null).show();
				return false;
			} 
			if (IDdeadline_et.getText().toString().trim().length() == 0) {
				new AlertDialog.Builder(context).setTitle("请选择证件有效期")
						.setMessage("请选择证件有效期！").setPositiveButton("确定", null)
						.show();
				return false;
			}
			if (nation_et.getText().toString().trim().length() == 0) {
				new AlertDialog.Builder(context).setTitle("请选择国籍")
						.setMessage("请选择国籍！").setPositiveButton("确定", null)
						.show();
				return false;
			}
			if (issueAt_et.getText().toString().trim().length() == 0) {
				new AlertDialog.Builder(context).setTitle("请输入证件签发地")
						.setMessage("请输入证件签发地！").setPositiveButton("确定", null)
						.show();
				return false;
			}
			if (gender_et.getText().toString().trim().length() == 0) {
				new AlertDialog.Builder(context).setTitle("请选择性别")
						.setMessage("请选择性别！").setPositiveButton("确定", null)
						.show();
				return false;
			}
			if (birthDay_et.getText().toString().trim().length() == 0) {
				new AlertDialog.Builder(context).setTitle("请选择出生年月日")
						.setMessage("请选择出生年月日！").setPositiveButton("确定", null)
						.show();
				return false;
			}
		}
		return true;
	}
}
