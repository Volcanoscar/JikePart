package com.jike.shanglv;

import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 */
public class GuideActivity extends Activity implements OnPageChangeListener {

	private ViewPager vp;
	private ViewPagerAdapter vpAdapter;
	private List<View> views;

	// �ײ�С��ͼƬ
	private ImageView[] dots;

	// ��¼��ǰѡ��λ��
	private int currentIndex;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activityguide);

		// ��ʼ��ҳ��
		initViews();

		// ��ʼ���ײ�С��
		initDots();
	}

	private void initViews() {
		LayoutInflater inflater = LayoutInflater.from(this);

		views = new ArrayList<View>();
		// ��ʼ������ͼƬ�б�
		views.add(inflater.inflate(R.layout.what_new_one, null));
		views.add(inflater.inflate(R.layout.what_new_two, null));
		views.add(inflater.inflate(R.layout.what_new_three, null));
		views.add(inflater.inflate(R.layout.what_new_four, null));

		// ��ʼ��Adapter
		vpAdapter = new ViewPagerAdapter(views, this);
		
		vp = (ViewPager) findViewById(R.id.viewpager);
		vp.setAdapter(vpAdapter);
		// �󶨻ص�
		vp.setOnPageChangeListener(this);
	}

	private void initDots() {
		LinearLayout ll = (LinearLayout) findViewById(R.id.ll);

		dots = new ImageView[views.size()];

		// ѭ��ȡ��С��ͼƬ
		for (int i = 0; i < views.size(); i++) {
			dots[i] = (ImageView) ll.getChildAt(i);
			dots[i].setEnabled(true);// ����Ϊ��ɫ
		}

		currentIndex = 0;
		dots[currentIndex].setEnabled(false);// ����Ϊ��ɫ����ѡ��״̬
	}

	private void setCurrentDot(int position) {
		if (position < 0 || position > views.size() - 1
				|| currentIndex == position) {
			return;
		}

		dots[position].setEnabled(false);
		dots[currentIndex].setEnabled(true);

		currentIndex = position;
	}

	// ������״̬�ı�ʱ����
	@Override
	public void onPageScrollStateChanged(int arg0) {
	}

	// ����ǰҳ�汻����ʱ����
	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
	}

	// ���µ�ҳ�汻ѡ��ʱ����
	@Override
	public void onPageSelected(int arg0) {
		// ���õײ�С��ѡ��״̬
		setCurrentDot(arg0);
	}
	
	
	
	
	public class ViewPagerAdapter extends PagerAdapter {
		// �����б�
		private List<View> views;
		private Activity activity;

		private static final String SHAREDPREFERENCES_NAME = "first_pref";

		public ViewPagerAdapter(List<View> views, Activity activity) {
			this.views = views;
			this.activity = activity;
		}

		// ����arg1λ�õĽ���
		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPager) arg0).removeView(views.get(arg1));
		}

		@Override
		public void finishUpdate(View arg0) {
		}

		// ��õ�ǰ������
		@Override
		public int getCount() {
			if (views != null) {
				return views.size();
			}
			return 0;
		}

		// ��ʼ��arg1λ�õĽ���
		@Override
		public Object instantiateItem(View arg0, int arg1) {
			((ViewPager) arg0).addView(views.get(arg1), 0);
			if (arg1 == views.size() - 1) {
				ImageView mStartWeiboImageButton = (ImageView) arg0
						.findViewById(R.id.iv_start);
				mStartWeiboImageButton.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// �����Ѿ�����
						setGuided();
						goHome();

					}

				});
			}
			return views.get(arg1);
		}

		private void goHome() {
			// ��ת
			Intent intent = new Intent(activity, MainActivity.class);
			activity.startActivity(intent);
			activity.finish();
		}

		/**
		 * 
		 * method desc�������Ѿ��������ˣ��´����������ٴ�����
		 */
		private void setGuided() {
			SharedPreferences preferences = activity.getSharedPreferences(
					SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE);
			Editor editor = preferences.edit();
			// ��������
			editor.putBoolean("isFirstIn", false);
			// �ύ�޸�
			editor.commit();
		}

		// �ж��Ƿ��ɶ������ɽ���
		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return (arg0 == arg1);
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {
		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View arg0) {
		}

	}
}
