package com.sunday.myplan.avtivity;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.litepal.crud.DataSupport;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.sunday.myplan.BaseActivty;
import com.sunday.myplan.R;
import com.sunday.myplan.adpter.PlanList;
import com.sunday.myplan.bean.Plan;
import com.sunday.myplan.bean.PlanName;

public class EditPlanActivity extends BaseActivty implements OnClickListener {
	private static final int EDIT_ITEM = 1;
	private static final int ITEM_ADD = 0;
	private static final int CHOOSE_PIC = 2;
	
	private Button mBtnAddPlan,mBtnAddItem;
	private EditText mEDName,mEDTime;
	private ListView mLvPlan;
	private List<Plan>mPlanList = new ArrayList<Plan>();
	private List<Plan>mPlanListCache = new ArrayList<Plan>();
	private PlanList mPlanAdpter;
	private ImageView mIVplan;

	private int mListLocation,mCacheLocation;
	private int SIZE=0;
	private Plan mPlan;
	private String mPlaName;
	private String mUserName;
	private int mPlanTime;
	private String mSrc;
	private Date mDate;
	private int mFlag;
	private int mEditPlanId=0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_plan);
		initview();
		inidata();
		initListener();

	}


	private void initListener() {
		mBtnAddItem.setOnClickListener(this);
		mBtnAddPlan.setOnClickListener(this);
		mLvPlan.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				mListLocation=position;
				mCacheLocation=position-SIZE;
				Intent intent=new Intent(EditPlanActivity.this, PlanItemActivity.class);
				intent.putExtra("plan_item", mPlanList.get(position));
				startActivityForResult(intent,EDIT_ITEM);//编辑item

			}
		});
		mIVplan.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();  
                /* 开启Pictures画面Type设定为image */  
                intent.setType("image/*");  
                /* 使用Intent.ACTION_GET_CONTENT这个Action */  
                intent.setAction(Intent.ACTION_GET_CONTENT);   
                /* 取得相片后返回本画面 */  
                startActivityForResult(intent, CHOOSE_PIC);
			}
		});
	}


	//	protected void refresh() {
	//		mPlanList=selectFromDAO(mEditPlanId);
	//		if(mPlanList!=null){
	//			mPlanAdpter.notifyDataSetChanged();
	//		}
	//	}
	protected void addPlanToDao(String planName, int time) {




	}
	private Date getTime() {
		Date  curDat =new Date(System.currentTimeMillis());//获取当前时间       
		return curDat;
	}
	private void initview() {
		mLvPlan=(ListView)findViewById(R.id.ls_edit_plan_list);
		mEDName=(EditText)findViewById(R.id.ed_plan_name);
		mEDTime=(EditText)findViewById(R.id.ed_plan_time);
		mBtnAddPlan=(Button)findViewById(R.id.btn_save_plan);
		mBtnAddItem=(Button) findViewById(R.id.btn_add_plan_item);
		mIVplan=(ImageView) findViewById(R.id.iv_plan);
	}
	private void inidata() {
		Intent intent = getIntent();
		mEditPlanId = intent.getIntExtra("plan_id", 0);
		if(mEditPlanId>0)
		{
			mPlanList=selectFromDAO(mEditPlanId);
			SIZE=mPlanList.size();

		}
		mPlanAdpter =new PlanList(EditPlanActivity.this, R.layout.cell_list_plan, mPlanList);
		mLvPlan.setAdapter(mPlanAdpter);
	}
	private List<Plan> selectFromDAO(int flag) {
		int f=flag;
		List<Plan> list = DataSupport .where("planname_id = ?", f+"").find(Plan.class);
		if(list.size()==0)
		{
		}
		return list;


	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_save_plan:
			addPlan();//TODO 需要对短时间多次保存做处理
			break;
		case R.id.btn_add_plan_item:
			addItem();
			break;
		default:
			break;
		}		
	}


	private void changeThePlanImage() {
		// TODO 设置计划的图片
		Toast.makeText(EditPlanActivity.this, "请设置计划图片", Toast.LENGTH_SHORT).show();

	}


	private void addItem() {
		if(mEditPlanId==0)//为0时说明是新建计划
		{
			//  判断是否有计划名，并保存
			if("".equals(mEDName.getText().toString()))
			{
				Toast.makeText(this, "请先输入计划名", Toast.LENGTH_SHORT).show();
			}
			else
			{
				if(mEditPlanId==0)
				{
					mEditPlanId=addPlan();
					if(mEditPlanId>-1){
						Intent intent=new Intent(this, PlanItemActivity.class);
						startActivityForResult(intent,ITEM_ADD);
					}
				}

			}
		}
		else if(mEditPlanId>0)
		{
			Intent intent=new Intent(this, PlanItemActivity.class);
			startActivityForResult(intent,ITEM_ADD);
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data); 
		switch (requestCode) {
		case ITEM_ADD:
			if (resultCode ==RESULT_OK) {
				addItem(data);
			}
			break;
		case EDIT_ITEM:
			//TODO 编辑item，需要判断item是否在数据库中（即不在listcache中）
			if (resultCode ==RESULT_OK) {
				editItem(data);
			}
			break;
		case CHOOSE_PIC:
			  if (resultCode == RESULT_OK) {  
		            choosePic(data);  
		        }  
		break;
		default:
		}
	}


	private void choosePic(Intent data) {
		Uri uri = data.getData();  
		Log.e("uri", uri.toString());  
		ContentResolver cr = this.getContentResolver();  
		try {  
		    Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));  
		    /* 将Bitmap设定到ImageView */  
		    mIVplan.setImageBitmap(bitmap);  
		} catch (FileNotFoundException e) {  
		    Log.d("my", e.getMessage(),e);  
		}
	}


	private void editItem(Intent data) {
		Plan returnedPlan = (Plan) data.getSerializableExtra("plan_return");
		if(returnedPlan!=null)
		{
			if(returnedPlan.getId()==0)//id等于0说明不在数据库中
			{
				uploadNewItem(returnedPlan);
			}
			else
			{
				uploadOldItem();
			}
		}
	}


	private void addItem(Intent data) {
		Plan returnedPlan = (Plan) data.getSerializableExtra("plan_return");
		if(returnedPlan!=null)
		{
			mPlanList.add(returnedPlan);//放入list中以方便显示
			mPlanListCache.add(returnedPlan);//保存在缓存中以更新
			mPlanAdpter.notifyDataSetChanged();
			Log.d("my", "getback");
		}
	}



	private void uploadOldItem() {
		Log.d("my", "这条数据在数据库中");	
	}


	private void uploadNewItem(Plan returnedPlan) {
		Log.d("my", "这条数据在cache中");
		Plan plan=returnedPlan;
		mPlanList.set(mListLocation, plan);
		mPlanListCache.set(mCacheLocation, plan);
		mPlanAdpter.notifyDataSetChanged();

	}


	private int addPlan() {
		String planName =mEDName.getText().toString();
		int id=-1;
		int time=0;//时间需要计算：将planList的时间相加
		if(!planName.isEmpty())
		{
			if(mPlanList.size()==0)
			{
				//判断使用相同的间隔时间10s
				id=addNewPlan(planName);
			}
			else 
			{
				saveOldPlan(planName,mPlanListCache);
			}
			//TODO 添加计划到数据库  
			//addPlanToDao(planName,time);

		}
		else
		{
			Toast.makeText(this, "请先输入计划名", Toast.LENGTH_SHORT).show();
		}
		return id;

	}
	private void saveOldPlan(String planName,List<Plan> list) {
		int time=0;
		for(int i=0;i<mPlanList.size();i++)
		{
			time+=mPlanList.get(i).getTime();
		}

		String planname=planName;
		String src="scr://";
		Date date =getTime();
		List<Plan> planList=list;
		DataSupport.delete(PlanName.class, mEditPlanId);
		DataSupport.deleteAll(Plan.class, "planname_id = ?", mEditPlanId+""); 
		DataSupport.saveAll(list);
		PlanName name=new PlanName( time, planname, src, date,planList);
		name.save();
		Toast.makeText(this, "计划保存成功", Toast.LENGTH_SHORT).show();


	}


	private int addNewPlan(String planName) {
		int time=0;
		String planname=planName;
		String src="scr://";
		Date date =getTime();
		PlanName name=new PlanName( time, planname, src, date);
		name.save();
		Toast.makeText(this, "添加成功", Toast.LENGTH_SHORT).show();
		PlanName name2=DataSupport.findLast(PlanName.class);
		return name2.getId();//返回新添加的计划的ID

	}
	public void deletItem(int planId,int position)//删除计划项目
	{
		DataSupport.delete(Plan.class,planId);
		mPlanList.remove(position);
		mPlanAdpter.notifyDataSetChanged();

	}
}	

