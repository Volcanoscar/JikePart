package com.jike.shanglv.Contact;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import com.jike.shanglv.MainActivity;
import com.jike.shanglv.R;
import com.jike.shanglv.Common.DateUtil;
import com.jike.shanglv.SeclectCity.SideBar.OnTouchingLetterChangedListener;

public class ContactActivity extends Activity {
	
	private ImageButton back_imgbtn, home_imgbtn;
	private ListView sortListView;
	private SortAdapter adapter;
	private ClearEditText mClearEditText;
	private Context context;
	
	/**
	 * ����ת����ƴ������
	 */
	private CharacterParser characterParser;
	private List<ContactModel> SourceDateList;
	
	/**
	 * ����ƴ��������ListView�����������
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contact_select_activity);
		context=this;
		initViews();
	}

	private void initViews() {
		back_imgbtn = (ImageButton) findViewById(R.id.back_imgbtn);
		home_imgbtn = (ImageButton) findViewById(R.id.home_imgbtn);
		back_imgbtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		home_imgbtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(context, MainActivity.class));
			}
		});
		
		//ʵ��������תƴ����
		characterParser = CharacterParser.getInstance();
		
//		//�����Ҳഥ������
//		sideBar.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {
//			
//			@Override
//			public void onTouchingLetterChanged(String s) {
//				//����ĸ�״γ��ֵ�λ��
//				int position = adapter.getPositionForSection(s.charAt(0));
//				if(position != -1){
//					sortListView.setSelection(position);
//				}
//			}
//		});
		
		sortListView = (ListView) findViewById(R.id.country_lvcountry);
		sortListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				//����Ҫ����adapter.getItem(position)����ȡ��ǰposition����Ӧ�Ķ���

				//Toast.makeText(getApplication(), ((ContactModel)adapter.getItem(position)).getCityName(), Toast.LENGTH_SHORT).show();
				
				//���س�����ϵ�˼��绰
				setResult(
						0,
						getIntent().putExtra("pickedContact",
								((ContactModel)adapter.getItem(position)).getName()+
								"#"+
								((ContactModel)adapter.getItem(position)).getPhoneNumber()));
				finish();
			}
		});
		
		SourceDateList = getAllContacts() ;
				//filledData(getResources().getStringArray(R.array.date));
		
		// ����a-z��������Դ����
		//Collections.sort(SourceDateList, pinyinComparator);
//		sortCities();
		adapter = new SortAdapter(context, SourceDateList);
		sortListView.setAdapter(adapter);
		mClearEditText = (ClearEditText) findViewById(R.id.filter_edit);
		
		//�������������ֵ�ĸı�����������
		mClearEditText.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				//������������ֵΪ�գ�����Ϊԭ�����б�����Ϊ���������б�
				filterData(s.toString());
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			@Override
			public void afterTextChanged(Editable s) {
			}
		});
	}
	
	/*
	 *����
	 */
	private void sortCities() {
		Comparator<ContactModel> comparator = new Comparator<ContactModel>() {
			public int compare(ContactModel s1, ContactModel s2) {
				if (s1.nameSort.compareTo(s2.nameSort) != 0) {
					return s1.nameSort.compareTo(s2.nameSort);
				} else {
					return s1.name.compareTo(s2.name);
				}
			}
		};
		Collections.sort(SourceDateList, comparator);
	}
	
	/*
	 * ��ȡ��ϵ�˵���Ϣ
	 */
	public ArrayList<ContactModel> getAllContacts() {
		ArrayList<ContactModel> names = new ArrayList<ContactModel>();
		Cursor cursor = context.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, 
				 null, null, null, null);
		int contactIdIndex = 0;
		int nameIndex = 0;
		
		if(cursor.getCount() > 0) {
			contactIdIndex = cursor.getColumnIndex(ContactsContract.Contacts._ID);
			nameIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
		}
		while(cursor.moveToNext()) {
			ContactModel acm = new ContactModel();
			String contactId = cursor.getString(contactIdIndex);
			String name = cursor.getString(nameIndex);
			acm.setContactId(contactId);
			acm.setName(name);

			/*
			 * ���Ҹ���ϵ�˵�phone��Ϣ
			 */
			Cursor phones = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, 
					null, 
					ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + contactId, 
					null, null);
			int phoneIndex = 0;
			if(phones.getCount() > 0) {
				phoneIndex = phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
			}
			while(phones.moveToNext()) {
		    	String phoneNumber = phones.getString(phoneIndex);
		    	acm.setPhoneNumber(phoneNumber);
			}
			names.add(acm);
			phones.close();
		}
		cursor.close();
		return names;
	}


	/**
	 * ΪListView�������
	 * @param date
	 * @return
	 */
	private List<ContactModel> filledData(String [] date){
		List<ContactModel> mSortList = new ArrayList<ContactModel>();
		
		for(int i=0; i<date.length; i++){
			ContactModel ContactModel = new ContactModel();
			ContactModel.setName(date[i]);
			//����ת����ƴ��
			String pinyin = characterParser.getSelling(date[i]);
			String sortString = pinyin.substring(0, 1).toUpperCase();
			
			// ������ʽ���ж�����ĸ�Ƿ���Ӣ����ĸ
			if(sortString.matches("[A-Z]")){
				ContactModel.setNameSort(sortString.toUpperCase());
			}else{
				ContactModel.setNameSort("#");
			}
			mSortList.add(ContactModel);
		}
		return mSortList;
	}
	
	/**
	 * ����������е�ֵ���������ݲ�����ListView
	 * @param filterStr
	 */
	private void filterData(String filterStr){
		List<ContactModel> filterDateList = new ArrayList<ContactModel>();
		
		if(TextUtils.isEmpty(filterStr)){
			filterDateList = SourceDateList;
		}else{
			filterDateList.clear();
			for(ContactModel ContactModel : SourceDateList){
				String name = ContactModel.getName();
				if(name.indexOf(filterStr.toString()) != -1 || characterParser.getSelling(name).startsWith(filterStr.toString())){
					filterDateList.add(ContactModel);
				}
				
				if(name.indexOf(filterStr.toString()) != -1 || ContactModel.getName().startsWith(filterStr.toString())){
					filterDateList.add(ContactModel);
				}
				if(name.indexOf(filterStr.toString()) != -1 || ContactModel.getPhoneNumber().startsWith(filterStr.toString())){
					filterDateList.add(ContactModel);
				}
				if(name.indexOf(filterStr.toString()) != -1 || ContactModel.getNameSort().startsWith(filterStr.toString())){
					filterDateList.add(ContactModel);
				}
			}
		}
		filterDateList=DateUtil.removeDuplicateWithOrder(filterDateList);
		// ����a-z��������
		sortCities();
		adapter.updateListView(filterDateList);
	}
}
