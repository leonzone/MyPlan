package com.sunday.myplan.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.sunday.myplan.BaseFragment;
import com.sunday.myplan.R;
import com.sunday.myplan.avtivity.StartPlanActivity;
import com.sunday.myplan.view.MenuDrawable;
import com.sunday.myplan.view.MenuDrawable.ToggleStateListener;
import com.sunday.myplan.view.TickPlusDrawable;

public class StartPlanFragment extends BaseFragment {
	Button btnStart;
	TextView tvStart;
	int mPlanId;
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ViewGroup rootView = (ViewGroup) inflater.inflate( R.layout.fragment_to_start_plan, container, false);
		return rootView;

	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initView();

		btnStart.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent= new Intent(getActivity(), StartPlanActivity.class);
				intent.putExtra("plan_name_id", mPlanId);
				startActivity(intent);
			}
		});
	}
	public void onResume() {
		super.onResume();
		initData();
	}
	public void initData() {
		int planId=getSharedPreferences();
		tvStart.setText("你选择了计划"+planId);
		mPlanId=planId;
		if(planId>-1)
		{
			//查询计划
		}
	}
	private void initView() {
		btnStart=(Button) getView().findViewById(R.id.btn_to_start_plan);
		tvStart=(TextView) getView().findViewById(R.id.tv_to_start_plan);

	}
	private int getSharedPreferences() {
		int id=-1;
		SharedPreferences pref =getActivity().getSharedPreferences("plan",getActivity().MODE_PRIVATE);
		if(pref!=null)
		{
			id=pref.getInt("planId", -1);
		}
		return id;

	}
	

}
