package com.sunday.myplan.avtivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.sunday.myplan.BaseActivty;
import com.sunday.myplan.R;
import com.sunday.myplan.bean.Plan;

public class PlanItemActivity extends BaseActivty{
	ImageView mIVPlanItem;
	EditText mItemName,mItemTime,mItemPic;
	Button mBtnSaveItem;
	Plan mPlan;
int [] picNumber=new int[]{R.drawable.rest,R.drawable.e1,R.drawable.e2,R.drawable.e3,R.drawable.e4,R.drawable.e5,R.drawable.e6,R.drawable.e7,R.drawable.e8,R.drawable.e9,R.drawable.e10,R.drawable.e11,R.drawable.e12,};
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.activity_plan_item);
		intView();
		intdata();
		initLisenser();
	}
	private void intdata() {
		Intent intent= getIntent();
		Plan plan=(Plan) intent.getSerializableExtra("plan_item");
		if(plan!=null)
		{
			mItemName.setText(plan.getPlaname());
			mItemTime.setText(Integer.toString(plan.getTime()));
			mPlan=plan;
		}
	}
	private void initLisenser() {
		mBtnSaveItem.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String itemName=mItemName.getText().toString().trim();
				String s=mItemTime.getText().toString().trim();
				int itemTime=Integer.parseInt(s);
				if(mPlan==null)
				{
					int i=Integer.parseInt(mItemPic.getText().toString());
					String pic=Integer.toString(picNumber[i]);
					mPlan=new Plan(itemName, itemTime,pic);
					Log.d("my", "item已生成");
				}
				else 
				{
					mPlan.setPlaname(itemName);
					mPlan.setTime(itemTime);
					Log.d("my", "item已更新");
				}

				setBack();
				//				Toast.makeText(PlanItemActivity.this, "item已生成", Toast.LENGTH_SHORT).show();

			}
		});		
	}
	protected void setBack() {
		if(mPlan!=null)
		{
			Intent intent = new Intent();
			setResult(RESULT_OK, intent);
			intent.putExtra("plan_return", mPlan);
			finish();
		}
		else if(mPlan.equals(null))
		{
			Intent intent = new Intent();
			setResult(RESULT_CANCELED, intent);
			finish();
		}

	}
	private void intView() {
		mIVPlanItem=(ImageView) findViewById(R.id.iv_plan_item);
		mItemName=(EditText) findViewById(R.id.ed_plan_item_name);
		mItemTime=(EditText) findViewById(R.id.ed_plan_item_time);
		mItemPic=(EditText) findViewById(R.id.editnumber);
		mBtnSaveItem=(Button) findViewById(R.id.btn_save);
	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();


	}
}
