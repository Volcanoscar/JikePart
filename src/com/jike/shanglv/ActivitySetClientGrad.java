package com.jike.shanglv;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.jike.shanglv.Common.CommonFunc;
import com.jike.shanglv.Common.CustomerAlertDialog;
import com.jike.shanglv.Enums.PackageKeys;
import com.jike.shanglv.Enums.SPkeys;
import com.jike.shanglv.Models.DealerLevel;
import com.jike.shanglv.NetAndJson.HttpUtils;
import com.jike.shanglv.NetAndJson.JSONHelper;

public class ActivitySetClientGrad extends Activity {
	protected static final int DEALERLEVELMSGCODE=0;
	protected static final int ADDLEVELMSGCODE = 1;
	public static final String DISPLAY_TYPENAME_STRING="DISPLAY_TYPENAME_STRING";
	public static final String CUSTOMER_DISPLAYNAME="客户";
	public static final String DEALER_DISPLAYNAME="分销商";
	
	private RelativeLayout set_default_rl,add_grad_rl;
	private LinearLayout loading_ll;
	private ImageView frame_ani_iv;
	private TextView query_status_tv,default_grad_tv,title_tv;
	private ListView listview;
	private Context context;
	private SharedPreferences sp;
	private String dealerlevallistReturnJson="",addcustomerlevalReturnJson="",displayName="",levellistActionName="",addlevelActionName="";
	private ArrayList<DealerLevel> customerlever_List;//客户级别、经商上级别列表使用同一个Model
	private ListAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_set_client_grad);
		
		context=this;
		sp = getSharedPreferences(SPkeys.SPNAME.getString(), 0);
		customerlever_List=new ArrayList<DealerLevel>();
		listview=(ListView) findViewById(R.id.listview);
		loading_ll=(LinearLayout) findViewById(R.id.loading_ll);
		frame_ani_iv=(ImageView) findViewById(R.id.frame_ani_iv);
		query_status_tv=(TextView) findViewById(R.id.query_status_tv);
		set_default_rl=(RelativeLayout) findViewById(R.id.set_default_rl);
		add_grad_rl=(RelativeLayout) findViewById(R.id.add_grad_rl);
		default_grad_tv=(TextView) findViewById(R.id.default_grad_tv);
		title_tv=(TextView) findViewById(R.id.title_tv);
		((ImageButton)findViewById(R.id.back_imgbtn)).setOnClickListener(clickListener);
		set_default_rl.setOnClickListener(clickListener);
		add_grad_rl.setOnClickListener(clickListener);
		Bundle bundle=new Bundle();
		bundle=getIntent().getExtras();
		if (bundle!=null) {
			displayName=bundle.containsKey(DISPLAY_TYPENAME_STRING)?bundle.getString(DISPLAY_TYPENAME_STRING):"";
			title_tv.setText(displayName+"级别设置");
		}
		if (displayName.equals(CUSTOMER_DISPLAYNAME)) {
			levellistActionName="customerlevallist";
			addlevelActionName="addcustomerleval";
		}else if (displayName.equals(DEALER_DISPLAYNAME)) {
			levellistActionName="dealerlevallist";
			addlevelActionName="adddealerleval";
		}
		startQueryCustomer();
	}
	
	View.OnClickListener clickListener=new View.OnClickListener() {

		@Override
		public void onClick(View arg0) {
			switch (arg0.getId()) {
			case R.id.back_imgbtn:
				finish();
				break;
			case R.id.set_default_rl:
				
				break;
			case R.id.add_grad_rl:
				 final EditText inputServer = new EditText(context);
			        inputServer.setFocusable(true);

			        AlertDialog.Builder builder = new AlertDialog.Builder(context);
			        builder.setTitle("新增"+displayName+"级别").setView(inputServer).setNegativeButton(
			                "取消", null);
			        builder.setPositiveButton("确定",
			                new DialogInterface.OnClickListener() {
			                    public void onClick(DialogInterface dialog, int which) {
			                        String inputName = inputServer.getText().toString();
			                        startAddGrad(inputName);
			                    }
			                });
			        builder.show();
				break;
			default:
				break;
			}
		}
	};
	
	
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		frame_ani_iv.setBackgroundResource(R.anim.frame_rotate_ani);
		AnimationDrawable anim = (AnimationDrawable) frame_ani_iv
				.getBackground();
		anim.setOneShot(false);
		anim.start();
	}
	
	private void startQueryCustomer() {
		if (HttpUtils.showNetCannotUse(context)) {
			loading_ll.setVisibility(View.GONE);
			return;
		}
		new Thread(new Runnable() {
			@Override
			public void run() {
				MyApp ma = new MyApp(context);
				String str = "{\"userID\":\""
						+ sp.getString(SPkeys.userid.getString(), "") + "\"}";
				String param = "action="+levellistActionName+"&str=" + str + "&userkey="
						+ ma.getHm().get(PackageKeys.USERKEY.getString()).toString() + "&sitekey=" + MyApp.sitekey
						+ "&sign="
						+ CommonFunc.MD5(ma.getHm().get(PackageKeys.USERKEY.getString()).toString() + levellistActionName + str);
				dealerlevallistReturnJson = HttpUtils.getJsonContent(ma.getServeUrl(),
						param);
				Message msg = new Message();
				msg.what = DEALERLEVELMSGCODE;
				handler.sendMessage(msg);
			}
		}).start();
	}
	
	private void startAddGrad(final String newName) {
		if (HttpUtils.showNetCannotUse(context)) {
			loading_ll.setVisibility(View.GONE);
			return;
		}
		new Thread(new Runnable() {
			@Override
			public void run() {
				MyApp ma = new MyApp(context);
				String str="";
				str = "{\"userID\":\""+ sp.getString(SPkeys.userid.getString(), "") 
						+"\",\"lname\":\""+ newName+ "\"}";
				String param="";
				try {
					param = "action="+addlevelActionName+"&str=" + URLEncoder.encode(str, "utf-8") + "&userkey="
							+ ma.getHm().get(PackageKeys.USERKEY.getString()).toString() + "&sitekey=" + MyApp.sitekey
							+ "&sign="
							+ CommonFunc.MD5(ma.getHm().get(PackageKeys.USERKEY.getString()).toString() + addlevelActionName+ str);
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				addcustomerlevalReturnJson = HttpUtils.getJsonContent(ma.getServeUrl(),
						param);
				Message msg = new Message();
				msg.what = ADDLEVELMSGCODE;
				handler.sendMessage(msg);
			}
		}).start();
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			JSONTokener jsonParser;
			switch (msg.what) {
			case ADDLEVELMSGCODE:
				jsonParser = new JSONTokener(addcustomerlevalReturnJson);
				try {
					JSONObject jsonObject = (JSONObject) jsonParser.nextValue();
					String state = jsonObject.getString("c");
					String msgString=jsonObject.getJSONObject("d").getString("msg");
					if (state.equals("0000")) {
						final CustomerAlertDialog cad=new CustomerAlertDialog(context,true);
						cad.setTitle("添加用户级别信息成功");
						cad.setPositiveButton("确定", new OnClickListener(){
							@Override
							public void onClick(View arg0) {
								cad.dismiss();
							}});
						startQueryCustomer();
					} else {
						final CustomerAlertDialog cad=new CustomerAlertDialog(context,true);
						cad.setTitle(msgString);
						cad.setPositiveButton("确定", new OnClickListener(){
							@Override
							public void onClick(View arg0) {
								cad.dismiss();
							}});
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			case DEALERLEVELMSGCODE:
				jsonParser = new JSONTokener(dealerlevallistReturnJson);
				try {
					JSONObject jsonObject = (JSONObject) jsonParser.nextValue();
					String state = jsonObject.getString("c");

					if (state.equals("0000")) {
						JSONArray cArray= jsonObject.getJSONArray("d");
						customerlever_List.clear();
						for (int i = 0; i < cArray.length(); i++) {
							DealerLevel cUser=JSONHelper.parseObject(cArray.getJSONObject(i), DealerLevel.class);
							customerlever_List.add(cUser);
							if (cUser.getIsDefault().equals("1")) {
								default_grad_tv.setText(cUser.getLevalName());
							}
						}
						adapter=new ListAdapter(context, customerlever_List);
						listview.setAdapter(adapter);
						if (customerlever_List.size() == 0){
							query_status_tv.setText("未查询到客户级别信息");
							frame_ani_iv.setVisibility(View.INVISIBLE);
							break;
						}
						loading_ll.setVisibility(View.GONE);
						listview.setVisibility(View.VISIBLE);
						listview.setOnItemClickListener(new OnItemClickListener() {
							@Override
							public void onItemClick(AdapterView<?> parent,
									View view, int position, long id) {
								DealerLevel cu = customerlever_List
										.get(position);
							}
						});

					} else {
						query_status_tv.setText("查询级别信息失败");
						frame_ani_iv.setVisibility(View.INVISIBLE);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			}
		}
	};

	private class ListAdapter extends BaseAdapter {
		private LayoutInflater inflater;
		private List<DealerLevel> str;

		public ListAdapter(Context context, List<DealerLevel> list1) {
			this.inflater = LayoutInflater.from(context);
			this.str = list1;
		}

		public void refreshData(List<DealerLevel> data) {
			this.str = data;
			notifyDataSetChanged();
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

		@SuppressLint("ResourceAsColor")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = inflater.inflate(
						R.layout.item_dealerlevel, null);
			}
			TextView dealerLevelName_tv = (TextView) convertView
					.findViewById(R.id.dealerLevelName_tv);

			if(str.get(position).getLevalName()!=null&&str.get(position).getLevalName().length()>0)
				dealerLevelName_tv.setText(str.get(position).getLevalName());
			return convertView;
		}
	}
	
}
