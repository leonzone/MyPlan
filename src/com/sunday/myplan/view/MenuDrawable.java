package com.sunday.myplan.view;


import static android.graphics.Paint.ANTI_ALIAS_FLAG;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.Property;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

public class MenuDrawable extends Drawable {

	private MyLine mTopLine, mCenterLine ;
	private Paint mLinePaint, mBackgroundPaint,mBackSmallRingPaint,mBackBigRingPaint,mRingPaint;
	private int mPadding,mStrokeWidth;
	private int mLineColor, mCrossColor;
	private Rect mb;
	private RectF mbf;
	public interface ToggleStateListener{
		void changeState(boolean b);
	}
	private ToggleStateListener toggleStateListener;

	public void setToggleStateLinstener(ToggleStateListener toggleStateLinstener) {
		this.toggleStateListener = toggleStateLinstener;
	}

	public MenuDrawable(int strokeWidth, int openColor, int closeColor, int padding) {

		mPadding = padding;
		mCrossColor = closeColor;
		mLineColor = openColor;
		mStrokeWidth=strokeWidth;

		mLinePaint = new Paint();
		mLinePaint.setAntiAlias(true);
		mLinePaint.setStrokeWidth(strokeWidth);
		mLinePaint.setStrokeCap(Paint.Cap.ROUND);

		mBackgroundPaint = new Paint(ANTI_ALIAS_FLAG);
		mBackgroundPaint.setStyle(Paint.Style.FILL);
		mBackgroundPaint.setColor(closeColor);

		mBackSmallRingPaint = new Paint(ANTI_ALIAS_FLAG);
		mBackSmallRingPaint.setStyle(Paint.Style.FILL);
		mBackSmallRingPaint.setColor(openColor);


		mBackBigRingPaint = new Paint(ANTI_ALIAS_FLAG);
		mBackBigRingPaint.setStyle(Paint.Style.FILL);
		mBackBigRingPaint.setColor(closeColor);


		mRingPaint=new Paint();
		mRingPaint.setStrokeWidth(strokeWidth/160);
		mRingPaint.setStyle(Style.STROKE);
		mRingPaint.setAntiAlias(true);



	}

	private Rect mBounds;

	@Override
	protected void onBoundsChange(Rect bounds) {
		super.onBoundsChange(bounds);
		mBounds = bounds;
		mb = new Rect();
		mb.left = bounds.left + mPadding;
		mb.right = bounds.right - mPadding;
		mb.top = bounds.top + mPadding;
		mb.bottom = bounds.bottom - mPadding;
		mbf=new RectF();
		mbf.left = bounds.left+mStrokeWidth/160;
		mbf.right = bounds.right-mStrokeWidth/160 ;
		mbf.top = bounds.top+mStrokeWidth/160;
		mbf.bottom = bounds.bottom-mStrokeWidth/160 ;
		mTopLine = new MyLine(mb.centerX(), mb.top, mb.centerX(), mb.bottom, mLineColor);
		mCenterLine = new MyLine(mb.left, mb.centerY(), mb.right, mb.centerY(), mLineColor);


		mRingPaint.setShader(new LinearGradient(0, bounds.exactCenterY(), bounds.exactCenterX(),bounds.exactCenterY(), new int[] {  
			Color.WHITE,Color.parseColor("#b5b5b5"),Color.parseColor("#b5b5b5"),Color.parseColor("#b5b5b5"),Color.parseColor("#b5b5b5"),Color.parseColor("#b5b5b5") ,Color.parseColor("#b5b5b5"),Color.parseColor("#b5b5b5"),Color.parseColor("#b5b5b5")}, null, 
			Shader.TileMode.MIRROR));
	}

	public float mDegrees = 0;
	@Override
	public void draw(Canvas canvas) {
		canvas.save();
		canvas.rotate(mDegrees,mBounds.right/2,mBounds.bottom/2);
		canvas.drawCircle(mBounds.exactCenterX(), mBounds.exactCenterY(), mBounds.exactCenterX(), mBackBigRingPaint);
		canvas.drawCircle(mBounds.exactCenterX(), mBounds.exactCenterY(), mBounds.exactCenterX()-mPadding*0.5f, mBackSmallRingPaint);
		canvas.drawCircle(mBounds.exactCenterX(), mBounds.exactCenterY(), mBounds.exactCenterX()-mPadding*0.6f, mBackgroundPaint);
		mCenterLine.draw(canvas, mLinePaint);
		mTopLine.draw(canvas, mLinePaint);
		canvas.restore();
		canvas.drawArc(mbf,180, 180, false, mRingPaint);
	}

	@Override
	public void setAlpha(int alpha) {
		System.out.println("HelloDrawable setAlpha");
	}

	@Override
	public void setColorFilter(ColorFilter cf) {
		System.out.println("HelloDrawable setColorFilter");
	}

	@Override
	public int getOpacity() {
		System.out.println("HelloDrawable getOpacity");
		return 50;
	}

	private boolean isOpen = false;
	public void toggle(boolean b) {
		if(toggleStateListener!=null){
			if(b==false)
			{   
				if(isOpen)
				{
				endAnima();
				toggleStateListener.changeState(false);
				}
				isOpen=false;
			}
			else
			{

				if(!isOpen){
					startAnima();
					toggleStateListener.changeState(true);
				}else{
					endAnima();
					toggleStateListener.changeState(false);
				}
				isOpen = !isOpen;
			}
		}
	}

	private MyBgProperty mProperty = new MyBgProperty();
	private ArgbEvaluator mArgbEvaluator = new ArgbEvaluator();

	public void startAnima() {
		AnimatorSet set = new AnimatorSet();
		set.playTogether(

				ObjectAnimator.ofFloat(this,new MyBgProperty1(), 45f),
				ObjectAnimator.ofObject(mCenterLine, "color", mArgbEvaluator, mCrossColor),
				ObjectAnimator.ofObject(mTopLine, "color", mArgbEvaluator, mCrossColor),
				ObjectAnimator.ofObject(this, mProperty, mArgbEvaluator, mLineColor)
				);
		set.setDuration(500);
		set.setInterpolator(new DecelerateInterpolator());
		set.start();
	}

	private void endAnima(){
		AnimatorSet set = new AnimatorSet();
		set.playTogether(
				ObjectAnimator.ofFloat(this,new MyBgProperty1(), 0f),
				ObjectAnimator.ofObject(mCenterLine, "color", mArgbEvaluator, mLineColor),
				ObjectAnimator.ofObject(mTopLine, "color", mArgbEvaluator, mLineColor),
				ObjectAnimator.ofObject(this, mProperty, mArgbEvaluator, mCrossColor)

				);
		set.setDuration(500);
		set.setInterpolator(new DecelerateInterpolator());
		set.start();
	}

	class MyBgProperty1 extends Property<MenuDrawable, Float> {
		public MyBgProperty1() {
			super(Float.class, "");
		}

		@Override
		public Float get(MenuDrawable object) {
			return object.mDegrees;
		}

		@Override
		public void set(MenuDrawable object, Float value) {
			object.mDegrees = value;
		}

	};

	class MyBgProperty extends Property<MenuDrawable, Integer> {
		public MyBgProperty() {
			super(Integer.class, "");
		}

		@Override
		public Integer get(MenuDrawable object) {
			return object.mBackgroundPaint.getColor();
		}

		@Override
		public void set(MenuDrawable object, Integer value) {
			object.mBackgroundPaint.setColor(value);
			object.invalidateSelf();
		}

	};

	class MyLine {

		public String mProperty;
		private float startX;
		private float StartY;
		private float EndX;
		private float EndY;
		private int color;

		public MyLine(float startX, float startY, float endX, float endY, int color) {
			super();
			this.startX = startX;
			StartY = startY;
			EndX = endX;
			EndY = endY;
			this.color = color;
		}

		public void draw(Canvas canvas, Paint paint) {
			paint.setColor(color);
			canvas.drawLine(startX, StartY, EndX, EndY, paint);
		}

		public int getColor() {
			return color;
		}

		public void setColor(int color) {
			this.color = color;
		}

		public float getStartX() {
			return startX;
		}

		public void setStartX(float startX) {
			this.startX = startX;
		}

		public float getStartY() {
			return StartY;
		}

		public void setStartY(float startY) {
			StartY = startY;
		}

		public float getEndX() {
			return EndX;
		}

		public void setEndX(float endX) {
			EndX = endX;
		}

		public float getEndY() {
			return EndY;
		}

		public void setEndY(float endY) {
			EndY = endY;
		}

	}
}
