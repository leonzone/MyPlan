package com.sunday.myplan.avtivity;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.litepal.crud.DataSupport;
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
import android.widget.Toast;

import com.sunday.myplan.R;
import com.sunday.myplan.bean.Plan;
import com.sunday.myplan.bean.PlanName;
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
		checkFristOpen();
		iniView();
		initData();
	}

	private void iniView() {
		setOverflowShowingAlways();
		//checkFristOpen();
		dm = getResources().getDisplayMetrics();
		pager = (ViewPager) findViewById(R.id.pager);
		tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
		pager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
		tabs.setViewPager(pager);
	}

	private void checkFristOpen() {
		SharedPreferences sp = getSharedPreferences("isFirstIn", Activity.MODE_PRIVATE);
		boolean isFirstIn = sp.getBoolean("isFirstIn", true);
		if(isFirstIn) {
			SharedPreferences.Editor editor = sp.edit();
			editor.putBoolean("isFirstIn", false);
			editor.commit();
			 initMyDate();
		} else {
			
		}
	}

	
	private void initData() {
		setTabsValue();
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
	private void initMyDate() {
		
		int time=0;
		int time2=0;
		

		String planname="7分钟锻炼";
		String src=Integer.toString(R.drawable.ic_launcher);
		Date date =getTime();
		List<Plan> planList=new ArrayList<Plan>();
		planList=addlist();
		for(int i=0;i<planList.size();i++)
		{
			time+=planList.get(i).getTime();
		}
		DataSupport.saveAll(planList);
		PlanName name=new PlanName( time, planname, src, date,planList);
		name.save();
		
		String planname2="番茄时间";
		String src2=Integer.toString(R.drawable.tomato);
		Date date2 =getTime();
		List<Plan> planList2=new ArrayList<Plan>();
		planList2=addlist2();
		for(int i=0;i<planList2.size();i++)
		{
			time2+=planList2.get(i).getTime();
		}
		
		DataSupport.saveAll(planList2);
		PlanName name2=new PlanName( time2, planname2, src2, date2,planList2);
		name2.save();
	}
	private List<Plan> addlist2() {
		 List<Plan>mPlanList = new ArrayList<Plan>();
		 mPlanList.add(new Plan("开始", 1500, Integer.toString(R.drawable.tomato)));
		 mPlanList.add(new Plan("休息", 300, Integer.toString(R.drawable.rest)));
		return mPlanList;
	}

	private List<Plan> addlist() {
		 List<Plan>mPlanList = new ArrayList<Plan>();
		 mPlanList.add(new Plan("起跳", 30, Integer.toString(R.drawable.e1)));
		 mPlanList.add(new Plan("休息", 10, Integer.toString(R.drawable.rest)));
		 mPlanList.add(new Plan("蹲墙", 30, Integer.toString(R.drawable.e2)));
		 mPlanList.add(new Plan("休息", 10, Integer.toString(R.drawable.rest)));
		 mPlanList.add(new Plan("俯卧撑", 30, Integer.toString(R.drawable.e3)));
		 mPlanList.add(new Plan("休息", 10, Integer.toString(R.drawable.rest)));
		 mPlanList.add(new Plan("仰卧起坐", 30, Integer.toString(R.drawable.e4)));
		 mPlanList.add(new Plan("休息", 10, Integer.toString(R.drawable.rest)));
		 mPlanList.add(new Plan("起立", 30, Integer.toString(R.drawable.e5)));
		 mPlanList.add(new Plan("休息", 10, Integer.toString(R.drawable.rest)));
		 mPlanList.add(new Plan("下蹲", 30, Integer.toString(R.drawable.e6)));
		 mPlanList.add(new Plan("休息", 10, Integer.toString(R.drawable.rest)));
		 mPlanList.add(new Plan("反飞", 30, Integer.toString(R.drawable.e7)));
		 mPlanList.add(new Plan("休息", 10, Integer.toString(R.drawable.rest)));
		 mPlanList.add(new Plan("平板", 30, Integer.toString(R.drawable.e8)));
		 mPlanList.add(new Plan("休息", 10, Integer.toString(R.drawable.rest)));
		 mPlanList.add(new Plan("飞膝", 30, Integer.toString(R.drawable.e9)));
		 mPlanList.add(new Plan("休息", 10, Integer.toString(R.drawable.rest)));
		 mPlanList.add(new Plan("王子", 30, Integer.toString(R.drawable.e10)));
		 mPlanList.add(new Plan("休息", 10, Integer.toString(R.drawable.rest)));
		 mPlanList.add(new Plan("俯卧侧飞", 30, Integer.toString(R.drawable.e11)));
		 mPlanList.add(new Plan("休息", 10, Integer.toString(R.drawable.rest)));
		 mPlanList.add(new Plan("侧飞", 30, Integer.toString(R.drawable.e12)));
		return mPlanList;
	}

	private Date getTime() {
		Date  curDat =new Date(System.currentTimeMillis());//获取当前时间       
		return curDat;
	}


}