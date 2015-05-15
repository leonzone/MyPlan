package com.sunday.myplan.fragment;

import java.util.ArrayList;
import java.util.List;

import org.litepal.crud.DataSupport;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.sunday.myplan.BaseFragment;
import com.sunday.myplan.R;
import com.sunday.myplan.adpter.PlanNameList;
import com.sunday.myplan.avtivity.EditPlanActivity;
import com.sunday.myplan.avtivity.MainActivity;
import com.sunday.myplan.bean.Plan;
import com.sunday.myplan.bean.PlanName;

public class ChoosePlanFragment extends BaseFragment {
	private ListView mLSChoosePlan;
	private List<PlanName>mPlanNameList = new ArrayList<PlanName>();
	private PlanNameList mPlanAdpter;
	private Button mBtnAddPlan;
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ViewGroup rootView=(ViewGroup) inflater.inflate(R.layout.fragment_choose_plan, null);	
		return rootView;
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initViwe();
		
		initLisenser();

	}

	private void initViwe() {
		mLSChoosePlan=(ListView) getView().findViewById(R.id.lv_choose_plan);
		mBtnAddPlan=(Button) getView().findViewById(R.id.btn_add_plan);

	}
	private void initData() {
		mPlanNameList=selectFromDAO();
		mPlanAdpter =new PlanNameList(getActivity(), R.layout.cell_list_plan, mPlanNameList);
		mLSChoosePlan.setAdapter(mPlanAdpter);
	}
	private void initLisenser() {
		mLSChoosePlan.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				PlanName planname=mPlanNameList.get(position);
                addSharedPreferences(mPlanNameList.get(position).getId());
				((MainActivity) getActivity()).choosePlanResult();
				Toast.makeText(getActivity(), planname.getPlanname(), Toast.LENGTH_SHORT).show();

			}
		});	
		mBtnAddPlan.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				checkPlanDetil(0);
			}
		});
	}
	@SuppressLint("CommitPrefEdits")
	protected void addSharedPreferences(int planId) {
		SharedPreferences.Editor editor =getActivity().getSharedPreferences("plan",getActivity().MODE_PRIVATE
				).edit();
		editor.putInt("planId", planId);
		editor.commit();

	}
	protected void checkPlanDetil(int planId) {
		int id=planId;
		Intent intent=new Intent(getActivity(),EditPlanActivity.class );
		intent.putExtra("planid", id);
		startActivity(intent);
		//TODO 启动plan详情页

	}
	private List<PlanName> selectFromDAO() {
		List<PlanName> list = DataSupport.findAll(PlanName.class);
		return list;
	}
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		initData();
	}
	public void deletPlan(int planNameId,int position){//删除计划
		DataSupport.delete(PlanName.class, planNameId);
		DataSupport.deleteAll(Plan.class, "planname_id = ?", planNameId+"");
		mPlanNameList.remove(position);
		mPlanAdpter.notifyDataSetChanged();
		
	}
}
