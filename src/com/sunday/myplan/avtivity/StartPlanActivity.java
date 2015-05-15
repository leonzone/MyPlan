package com.sunday.myplan.avtivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.litepal.crud.DataSupport;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sunday.myplan.BaseActivty;
import com.sunday.myplan.R;
import com.sunday.myplan.bean.Plan;
import com.sunday.myplan.view.HoloCircularProgressBar;
import com.sunday.myplan.view.TickPlusDrawable;
import com.sunday.myplan.view.TickPlusDrawable.ToggleStateListener;

public class StartPlanActivity extends BaseActivty {
	private List<Plan>mPlanList = new ArrayList<Plan>();
	private HoloCircularProgressBar mHoloCircularProgressBar;
	private ObjectAnimator mProgressBarAnimator;
	private TextView mTVName;
	private int i=0,times=0,Retime;
	private int FLAG=0;
	private ImageView mIVPlan;
	private CountDownTimer timer;
	private TickPlusDrawable mTickPlusDrawable;
	private View view;
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.activity_start_plan);


		initView();
		initAnimi();
		initlistener();
	}
	
	protected void restarttTick() {
		mTVName.setText("暂停");
		startTick(i,Retime);
		//		mProgressBarAnimator.resume();
	}
	protected void pausetTick() {
		mTVName.setText("继续");
		timer.cancel();
		//mProgressBarAnimator.cancel();;

	}
	private void initdata() {
		Intent intent=getIntent();
		int id=intent.getIntExtra("plan_name_id", 0);
		if(id>0)
		{
			mPlanList=selectFromDAO(id);		
		}
	}
	private void initView() {
		mHoloCircularProgressBar=(HoloCircularProgressBar)findViewById(R.id.holoCircularProgressBar);
		mTVName=(TextView)findViewById(R.id.tvPlanName);
		mIVPlan=(ImageView) findViewById(R.id.iv_plan);
		view=findViewById(R.id.start_view);

		Random r = new Random();
		int randomColor = getResources().getColor(R.color.color_out);
		mHoloCircularProgressBar.setProgressColor(randomColor);
		randomColor =  getResources().getColor(R.color.color_in);
		mHoloCircularProgressBar.setProgressBackgroundColor(randomColor);


	}
	@SuppressLint("NewApi")
	private void initAnimi() {
		mTickPlusDrawable = new TickPlusDrawable(getResources().getDimensionPixelSize(R.dimen.stroke_width), getResources().getColor(R.color.color_in),getResources().getColor(R.color.color_out));

		if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
			view.setBackgroundDrawable(mTickPlusDrawable);
		} else {
			view.setBackground(mTickPlusDrawable);
		}

	}
	private void initlistener() {
		view.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				mTickPlusDrawable.toggle();				
			}
		});
		mTickPlusDrawable.setToggleStateLinstener(new ToggleStateListener() {

			@Override
			public void changeState(boolean b) {
				if(FLAG==0)
				{
					startTick(0,0);
					FLAG=1;
				}else if(FLAG==1)
				{
					pausetTick();
					FLAG=2;
				}else if(FLAG==2)
				{
					restarttTick();
					FLAG=1;
				}

			}
		});
	}
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		//mPlanList=selectFromDAO(1);
		initdata();
	}


	protected void startTick(int location,int reStartTime) {
		mTVName.setText("暂停任务");
		List<Plan> list=mPlanList;
		i=location;//现在已到位置

		if(location<list.size())
		{
			if(reStartTime==0)
			{
				times=list.get(i).getTime()*1000;
			}
			else {
				times=reStartTime;
			}
			animate(mHoloCircularProgressBar, null, 1, times);
			mHoloCircularProgressBar.setMarkerProgress(1);
			mHoloCircularProgressBar.setProgress(0.5f);
			mTVName.setText(list.get(i).getPlaname());
			int src=Integer.parseInt(list.get(i).getSrc());
			mIVPlan.setImageResource(src);
			timer=new CountDownTimer(times, 1000) {
				@Override
				public void onTick(long millisUntilFinished) {
					//每秒改变进度条
					int resTime=Integer.parseInt(String.valueOf(millisUntilFinished/1000));
					Retime=resTime*1000;
					Log.d("my", resTime+"s");
					mTVName.setText(resTime+"s");
					if(resTime<4)
					{
						playSoundToWalm();
					}
				}

				@Override
				public void onFinish() {
					animate(mHoloCircularProgressBar, null, 0, 0);
					startTick(i+1,0);
				}
			}.start();

		}
		else
		{
			Toast.makeText(StartPlanActivity.this, "plan is finish", Toast.LENGTH_SHORT).show();
			mTVName.setText("重新开始");
			mTVName.setText("0s");
			FLAG=0;
		}
	}
	protected void playSoundToWalm() {
		Vibrator vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE); 
		//震动一次 
		vibrator.vibrate(200); 

	}
	private void animate(final HoloCircularProgressBar progressBar, final AnimatorListener listener,
			final float progress, final int duration) {

		mProgressBarAnimator = ObjectAnimator.ofFloat(progressBar, "progress", progress);
		mProgressBarAnimator.setDuration(duration);

		mProgressBarAnimator.addListener(new AnimatorListener() {

			@Override
			public void onAnimationCancel(final Animator animation) {
			}

			@Override
			public void onAnimationEnd(final Animator animation) {
				progressBar.setProgress(progress);
			}

			@Override
			public void onAnimationRepeat(final Animator animation) {
			}

			@Override
			public void onAnimationStart(final Animator animation) {
			}
		});
		if (listener != null) {
			mProgressBarAnimator.addListener(listener);
		}
		mProgressBarAnimator.reverse();
		mProgressBarAnimator.addUpdateListener(new AnimatorUpdateListener() {

			@Override
			public void onAnimationUpdate(final ValueAnimator animation) {
				progressBar.setProgress((Float) animation.getAnimatedValue());
			}
		});
		progressBar.setMarkerProgress(progress);
		mProgressBarAnimator.start();
	}
	private List<Plan> selectFromDAO(int flag) {
		int f=flag;
		List<Plan> list = DataSupport .where("planname_id = ?", f+"").find(Plan.class); 
		if(list.size()==0)
		{
			list.add(new Plan("mjump",30));
		}
		return list;


	}
	@Override
	protected void onPause() {
		super.onPause();
	}
	protected void onStop() {
		super.onStop();
		cancelTask();
	}
	private void cancelTask() {
		Log.d("my", "task is cancel");
		timer.cancel();
		mProgressBarAnimator.cancel();

	}
	
}

