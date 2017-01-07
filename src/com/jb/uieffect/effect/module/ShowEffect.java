package com.jb.uieffect.effect.module;


import com.jb.Ui.R;
import com.jb.uieffect.animation.UItools;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.drawable.Drawable;
import android.util.Log;

/**
 * 效果基类
 * @author denghuihan
 *
 */
public abstract class ShowEffect {
	
	//选择监听效果  接口
	public interface EffectSelectListener{
		// finish的时候会被调用 
		public void onEffectSelected(ShowEffect eff,EffResUnit res);
	}
	
	private final static String TAG = "ShowEffect";

	//效果类型接口
    public interface EFFECT_TYPE{
		public final static int  SECTOR_EFF		=	1;
		public final static int  ACCURATE_SEL_EFF		=	2;
    }
    
	// 一般为原始点，即第一次点击的位置 
	protected int mOriX = -1;
	protected int mOriY = -1;
	protected int mTranX = 0;
	protected int mTranY = 0;
	
	// 绘图的偏移位置 
	protected int mOffsetX = 0;
	protected int mOffsetY = 0;
	
	// 触摸区域 
	protected RectF mTouchRectF = null;
	
	// 默认画笔 
	protected Paint mPaint = null;
	public static final boolean bDUMP = false;
	public static final boolean bEnableShadow = true;
	
	// 效果需要绘制的目标画布 
	protected Canvas mCanvas;
	
	// 上次坐标所在的区域 
	protected int lastInZoneIdx = -1;

	// 可感知的区域列表 
	protected Region[] mRegionArray = null;

	// 可感知的路径列表 
	protected Path[] mPaths = null;
	
	// 需要显示的列表（跟区域列表有关） 
	String [] mAccArry = null ;
	
	// 效果背景
	protected Bitmap mBGBitmap = null;
	// 效果背景阴影 
	protected Bitmap mBG_shadow_Bitmap = null ;
	
	// 效果高亮阴影 
	protected Bitmap mHL_shadow_Bitmap = null ;
	protected EffectView mEffectView = null;
	protected int mDefFontSize = 20;
	// 默认高亮字体 
	protected int mDefHLFontSize = 20;
	protected int mDefFontColor = 0xff000000 ;
	// 默认高亮字体颜色 
	protected int mDefHLfontColor ;
	// 默认非高亮字体颜色 
	protected int mDefOLfontColor ;
	// view的可视区域高度 
	protected int mViewZoneHeight = 800;
	protected Point mDrawOri = new Point();
	
	private EffectSelectListener mEffectSelectListner = null;
	
	public void setEffectView(EffectView effectView)
	{
		this.mEffectView = effectView;
	}
	
	public void setBGDrawable(Drawable BGDrawable)
	{
		mBGBitmap = UItools.drawable2Bitmap( BGDrawable );
	}
	/**
	 * 构造函数 
	 */
	public ShowEffect(Context c)
	{
		//触屏区域
		mTouchRectF = new RectF();
		mPaint = new Paint();
		mPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
		mPaint.setAntiAlias(true);
		mPaint.setDither(true);
	}
	/**
	 * 完成后  
	 */
	public void finish()
	{
		//把效果返回给一个集合
		EffResUnit tmpRes = new EffResUnit();
		this.getResult(tmpRes);
		if( null != mEffectSelectListner )
		{
			mEffectSelectListner.onEffectSelected(this,tmpRes);
		}
		mPaths = null;
		mRegionArray = null;
		lastInZoneIdx = -1;
	}
	/**
	 * 显示背景
	 * @param canvas
	 */
	protected abstract void showBackGround();
	
	/**
	 * 显示当前效果
	 * @param canvas zoneIndx当前区域序号，-1表示没有被选中
	 */
	protected abstract void showEff(int zoneIndx);
	
	/**
	 * 判断当前坐标是否落在指定的区域
	 * @param x
	 * @param y
	 * @return 返回区域的标识index,小于0，表示没有被在区域中
	 */
	protected int isInZone(float x,float y)
	{
		if(null==mPaint)
			return -1;
		if(null==mRegionArray)
		{
			mRegionArray = new Region[mPaths.length];
			for(int i=0;i<mPaths.length;i++)
			{
				if(null==mPaths[i])
				{
					continue;
				}
				mRegionArray[i] = new Region();
				Region region =  new Region();
				region.set((int)mTouchRectF.left, (int)mTouchRectF.top, (int)mTouchRectF.right, (int)mTouchRectF.bottom);
				mRegionArray[i].setPath(mPaths[i], region);
			}
		}
		int xInt =(int)x;
		int yInt =(int)y;
		//令最后一个市默认的区域，先检查这个区域
		for(int i = mRegionArray.length-1;i>=0;i--)
		{
			if(null==mRegionArray[i])
			{
				continue;
			}
			if(mRegionArray[i].contains(xInt, yInt))
			{
				return i;
			}
		}
		return -1;
	}
	
	/**
	 * 当选择改变时被调用
	 */
	public abstract void onSelectChanged();
	
	/**
	 * 如果效果返回后有结果，可从这里取得
	 * @param res :结果集
	 */
	public abstract void getResult(EffResUnit res);
	
	/**
	 * 坐标位置移动时的响应
	 * @param x
	 * @param y、
	 * @return 返回true，表示必须重绘
	 */
	public boolean onMove(float x,float y)
	{
		Log.d(TAG,"onMove x="+x+",y="+y);
		int idx = isInZone(x,y);
		//如果坐标所在区域跟上次的一样，则什么也不做
		if(lastInZoneIdx != idx)
		{
			lastInZoneIdx = idx;
			Log.d( TAG, "lastInZoneIdx="+lastInZoneIdx);
			onSelectChanged();
			return true;
		}
		return false;
	}
	/**
	 * 显示移动
	 */
	public void showMove()
	{
		mCanvas.save();
		mCanvas.translate(mTranX, mTranY);
		showBackGround();
		showEff(lastInZoneIdx);
		mCanvas.restore();
	}
	
	/**
	 * 显示默认的效果，如初始效果
	 */
	public void showDefaultEff()
	{
		showBackGround();
		showEff(-1);
	}
	public void setAccArry(String[] accArry)
	{
		this.mAccArry = accArry;
	}
	/**
	 * 字符串转换
	 * ansii array to string arr
	 * @param accArry
	 */
	public void setAccArry(int[] accArry)
	{
		mAccArry = new String[accArry.length];
		for(int i=0;i<accArry.length;i++)
		{
			char c=(char)accArry[i];
			mAccArry[i] = String.valueOf(c);
		}
	}
	
	/**
	 * 设置画布
	 * @param canvas
	 */
	public void setCanvas(Canvas canvas)	
	{
		this.mCanvas = canvas;
	}
	
	/**
	 * 设置触摸起点
	 * @param oriX
	 * @param oriY
	 */
	public void setOriPos(int oriX,int oriY)
	{
		this.mOriX = oriX;
		this.mOriY = oriY;
		Log.d(TAG,"setOriPos="+oriX+","+oriY);
	}
	
	/**设置偏移*/
	public void setOffsetX(int offsetX) {
		this.mOffsetX = offsetX;
	}
	public void setOffsetY(int offsetY) {
		this.mOffsetY = offsetY;
	}
	
	/**
	 * 取资源，如xml中的像素资源或坐标资源，每次效果的时候将会调用
	 * @param r
	 */
	public void setResources(Resources res)
	{
		mDefFontSize = res.getDimensionPixelSize(R.dimen.eff_def_font_size);
		mDefFontColor = res.getColor(R.color.eff_def_font_color);
		mDefHLfontColor = res.getColor(R.color.eff_def_highlight_font_color);
		mDefOLfontColor = res.getColor(R.color.eff_def_offlight_font_color);
		mDefHLFontSize = res.getDimensionPixelSize(R.dimen.eff_def_highlight_font_size);
		mViewZoneHeight = res.getDimensionPixelSize(R.dimen.eff_double_buf_height);
	}
	
	
	/**
	 * 得到视图区域高度
	 */
	public int getViewZoneHeight()
	{
		return mViewZoneHeight;
	}
	
	public void setBGBitmap(Bitmap bitmap)
	{
		this.mBGBitmap = bitmap;
	}
	
	/**
	 * 全局水平偏移
	 * @return
	 */
	public float getGlobalTranslateX()
	{
		return 0.0f;
	}
		
	/**
	 * 全局垂直偏移
	 * @return
	 */
	public float getGlobalTranslateY()
	{
		return 0.0f;
	}
	
	/**
	 * 弹出时声效
	 */
	public void playSoundOnShow()
	{
		
	}
	public void setViewZoneHeight(int viewZoneHeight)
	{
		this.mViewZoneHeight = viewZoneHeight;
	}
	public void setEffectSelectListener(EffectSelectListener effectSelectListener)
	{
		this.mEffectSelectListner = effectSelectListener;
	}
	
	public void setBG_shadow_Bitmap(Bitmap BGShadowBitmap) {
		mBG_shadow_Bitmap = BGShadowBitmap;
	}
	public void setHL_shadow_Bitmap(Bitmap HLShadowBitmap) {
		mHL_shadow_Bitmap = HLShadowBitmap;
	}


	
}
