package com.sunday.myplan.avtivity;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.litepal.tablemanager.Connector;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Menu;
import android.view.ViewConfiguration;
import android.view.Window;

import com.sunday.myplan.R;
import com.sunday.myplan.fragment.ChoosePlanFragment;
import com.sunday.myplan.fragment.StartPlanFragment;
import com.sunday.myplan.view.PagerSlidingTabStrip;


public class MainActivity extends FragmentActivity {

	private ChoosePlanFragment choosePlanFragment;
	private StartPlanFragment mStartPlanFragment;
	private  ViewPager pager;
	/**
	 * PagerSlidingTabStrip的实例
	 */
	private PagerSlidingTabStrip tabs;

	/**
	 * 获取当前屏幕的密度
	 */
	private DisplayMetrics dm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setOverflowShowingAlways();
		//checkFristOpen();
		dm = getResources().getDisplayMetrics();
		pager = (ViewPager) findViewById(R.id.pager);
		tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
		pager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
		tabs.setViewPager(pager);
		setTabsValue();
		initData();
	}

	private void checkFristOpen() {
		SharedPreferences sp = getSharedPreferences("isFirstIn", Activity.MODE_PRIVATE);
		boolean isFirstIn = sp.getBoolean("isFirstIn", true);
		if(isFirstIn) {
			SharedPreferences.Editor editor = sp.edit();
			editor.putBoolean("isFirstIn", false);
			editor.commit();
			
			new AlertDialog.Builder(this).setMessage("这是第一次打开").show();
		} else {
			new AlertDialog.Builder(this).setMessage("你打开了n次了").show();
		}
	}

	private void initData() {
		SQLiteDatabase db = Connector.getDatabase();  		
	}

	/**
	 * 对PagerSlidingTabStrip的各项属性进行赋值。
	 */
	private void setTabsValue() {
		// 设置Tab是自动填充满屏幕的
		tabs.setShouldExpand(true);
		// 设置Tab的分割线是透明的
		tabs.setDividerColor(Color.TRANSPARENT);
		// 设置Tab底部线的高度
		tabs.setUnderlineHeight((int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 1, dm));
		// 设置Tab Indicator的高度
		tabs.setIndicatorHeight((int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 4, dm));
		// 设置Tab标题文字的大小
		tabs.setTextSize((int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_SP, 16, dm));
		// 设置Tab Indicator的颜色
		tabs.setIndicatorColor( getResources().getColor(R.color.color_in));
		// 设置选中Tab文字的颜色 (这是我自定义的一个方法)
		tabs.setSelectedTextColor(getResources().getColor(R.color.color_in));
		// 取消点击Tab时的背景色
		tabs.setTabBackground(0);
	}

	public class MyPagerAdapter extends FragmentPagerAdapter {

		public MyPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		private final String[] titles = { "开始", "模式"};

		@Override
		public CharSequence getPageTitle(int position) {
			return titles[position];
		}

		@Override
		public int getCount() {
			return titles.length;
		}

		@Override
		public Fragment getItem(int position) {
			switch (position) {
			case 0:
				if (mStartPlanFragment == null) {
					mStartPlanFragment = new StartPlanFragment();
				}
				return mStartPlanFragment;
			case 1:
				if (choosePlanFragment == null) {
					choosePlanFragment = new ChoosePlanFragment();
				}
				return choosePlanFragment;
			default:
				return null;
			}
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onMenuOpened(int featureId, Menu menu) {
		if (featureId == Window.FEATURE_ACTION_BAR && menu != null) {
			if (menu.getClass().getSimpleName().equals("MenuBuilder")) {
				try {
					Method m = menu.getClass().getDeclaredMethod(
							"setOptionalIconsVisible", Boolean.TYPE);
					m.setAccessible(true);
					m.invoke(menu, true);
				} catch (Exception e) {
				}
			}
		}
		return super.onMenuOpened(featureId, menu);
	}

	private void setOverflowShowingAlways() {
		try {
			ViewConfiguration config = ViewConfiguration.get(this);
			Field menuKeyField = ViewConfiguration.class
					.getDeclaredField("sHasPermanentMenuKey");
			menuKeyField.setAccessible(true);
			menuKeyField.setBoolean(config, false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public  void choosePlanResult(){
		pager.setCurrentItem(0);//跳转至开始页面1
		StartPlanFragment fragement = (StartPlanFragment) getSupportFragmentManager(). 
				findFragmentByTag("android:switcher:"+R.id.pager+":" + 0);
		        fragement.initData();


	}


}