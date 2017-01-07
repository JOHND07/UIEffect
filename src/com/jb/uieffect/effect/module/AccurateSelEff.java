package com.jb.uieffect.effect.module;


import com.jb.Ui.R;
import com.jb.uieffect.animation.UItools;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.drawable.Drawable;
import android.util.Log;

public class AccurateSelEff extends ShowEffect {
	
	private final static String TAG="AccurateSelEff";
    //背景图片的宽度，高度,角度
	private int mBgWidth = 126;
	private int mBgHeight = 95;
	private float mStartAngle = 270;
	private float mDrawStartAngle = 0.0f;
	//等分角
	private float mQuarterAngle =0.0f;		
	//等分半角
	private float mQuarterHalfAngle = 0.0f;
	//切换角度
	private float mSweepAngle ;
	private float mTextOffset = 40.0f;
	//中心默认区域半径
	private int mCenterR =25;	
	//选中中心区域时候的默认值
	private int mDefCenterVal = -1;	
	//选中中心区域时候的默认显示
	private String mDefCenterStr="";	
	//设置直接使用圆盘列表
	private boolean mbUsePlateList = false;
	//把图片用一个数组储存
	private Bitmap[] mPlateHLBitmapList = null;	
	//用户可视中心
	private int mDrawOriOffsetY =0;
	//用户触摸区域扩张
	private int mTouchExpand = 0;	
	//是否在点击位置上显示
	private boolean mDrawOnTouchPos = false;

	public AccurateSelEff(Context c) {
		super(c);
		// TODO Auto-generated constructor stub
	}
	//设置默认值
	public void setDefVal(int defval)
	{
		mDefCenterVal = defval;
	}
	//设置默认字符串
	public void setDefStr(String defStr)
	{
		mDefCenterStr = defStr;
	}

	//设置直接使用圆盘图标列表 , 注意将最后一个作为选中默认高亮
	public void setUseSectorDrawableList(Drawable[] plateDrawableList)
	{
		mbUsePlateList = true;
		mPlateHLBitmapList = new Bitmap[mPlateHLBitmapList.length];
		for(int i = 0 ; i < plateDrawableList.length; i++)
		{
			mPlateHLBitmapList[i]=UItools.drawable2Bitmap(plateDrawableList[i]);
		}
	}
	
	//设置直接使用圆盘图标列表 , 注意将最后一个作为选中默认高亮
	public void setUseSectorDrawableList(Bitmap[] plateBitmap)
	{
		mbUsePlateList = true;
		mPlateHLBitmapList = plateBitmap;
	}
	//设置资源,得到间隔大小之类
	@Override
	public void setResources(Resources res)
	{
		super.setResources(res);
		mDrawOriOffsetY = (int)res.getDimension(R.dimen.eff_accurate_sel_draw_ori_offset_y);
		mTextOffset = res.getDimensionPixelOffset(R.dimen.eff_accurate_sel_text_offset);
		mCenterR = (int) res.getDimension(R.dimen.eff_accurate_sel_center_r);
		mTouchExpand = res.getDimensionPixelOffset(R.dimen.eff_accurate_sel_touch_expand);
		int bghh = -1;
		if( null != mBG_shadow_Bitmap )
		{
			bghh = (int) (mBG_shadow_Bitmap.getHeight()/2.0f);
		}
		else
		{
			bghh = (int) (mBGBitmap.getHeight()/2.0f);
		}
		mViewZoneHeight = (int) (mDrawOriOffsetY + bghh/**/) ;//+ mBGBitmap.getHeight()/2;
		Log.d(TAG, "Draw center y"+mDrawOriOffsetY);
	    
	}
	//设置每个圆弧的角度,必须比setOriPos前调用，否则不可预料
	@Override
	public void setAccArry(int[] accArry)
	{
		super.setAccArry(accArry);
		//设置每个圆弧角度
		mSweepAngle = 360 / mAccArry.length; 
		//设置路径，添加一个中心区域
		mPaths = new Path[mAccArry.length+1];
	}
	
	//设置是否在点击位置显示
	public void setDrawOnTouchPos(boolean flag)
	{
		mDrawOnTouchPos = flag;
	}
	
	//设置触摸起点
	@Override
	public void setOriPos(int oriX,int oriY)
	{
		super.setOriPos(oriX, oriY);
		if(!mDrawOnTouchPos)
		{
			mDrawOri.x = (int)(0+UItools.getScreenWidth()/2.0f);
			Log.d(TAG,"mDrawOri.y="+mDrawOri.y+",mDrawOriOffsetY="+mDrawOriOffsetY+",mBGBitmap.getHeight()/2.0f"+(mBGBitmap.getHeight()/2.0f));
			mDrawOri.y = mDrawOriOffsetY-(int)(mBGBitmap.getHeight()/2.0f);
		}
		else
		{
			mDrawOri.x = oriX;
			mDrawOri.y = oriY;
		}
		Log.d(TAG,"mDrawOri.y="+mDrawOri.y+",mDrawOriOffsetY="+mDrawOriOffsetY+",mBGBitmap.getHeight()/2.0f"+(mBGBitmap.getHeight()/2.0f));
		mBgWidth = mBGBitmap.getWidth();
		mBgHeight = mBGBitmap.getHeight();
		
		mTouchRectF.set(oriX-mBgWidth/2-mTouchExpand, oriY-mBgHeight/2-mTouchExpand,
				 oriX+mBgWidth/2+mTouchExpand, oriY+mBgHeight/2+mTouchExpand);
		
		float startAngle =0;
		mQuarterAngle = (360.0f/mAccArry.length*1.0f);
		mQuarterHalfAngle = (360.0f/mAccArry.length*1.0f)/2.0f;
		mDrawStartAngle = -mQuarterHalfAngle;
		for( int i = 0; i < mAccArry.length; i++)
		{
			Path path = new Path();
			path.moveTo(oriX, oriY);
			path.arcTo( mTouchRectF, mStartAngle+mDrawStartAngle+startAngle, mSweepAngle);
			path.close();
			mPaths[i] = path;
			startAngle-=mSweepAngle;
		}
		// 添加一个中心path
		Path path = new Path();
		path.moveTo(oriX, oriY);
		path.addCircle(mOriX, mOriY, mCenterR, Direction.CW);
		path.close();
		mPaths[mPaths.length-1] = path;
	}
	
	@Override
	protected void showBackGround() {
		// TODO Auto-generated method stub
        if(bDUMP)
        {
        	mPaint.setColor(Color.argb(127, 255, 0, 0));
        	mCanvas.drawOval(mTouchRectF, mPaint);
        	for(int i=0;i<mAccArry.length;i++)
        	{
        		mPaint.setColor(Color.argb(127, i*30, i*30, i*30));
        		mCanvas.drawPath(mPaths[i], mPaint);
        		mPaint.setColor(Color.GREEN);
        		
        		float txt_angle = mDrawStartAngle+mQuarterHalfAngle+mQuarterAngle*(i*1.0f);
        		Log.d( TAG, "mDrawStartAngle="+mDrawStartAngle+",mQuarterHalfAngle = "+mQuarterHalfAngle
						+",mQuarterAngle = " +mQuarterAngle
						+",sin cos = "+ String.valueOf( txt_angle ));
        		mPaint.setColor(Color.GREEN);
        		drawText(i, mOriX, mOriY, false);
        		Log.d(TAG,"mOriY="+mOriY);
        	}
        	mCanvas.drawCircle(mOriX, mOriY, mCenterR, mPaint);        	
        }
        final float nLeft = mDrawOri.x-mBGBitmap.getWidth()/2.0f;
        final float nTop = mDrawOri.y- mBGBitmap.getHeight()/2.0f;
        
        if(bEnableShadow)
        {
        	if(-1!=lastInZoneIdx)
        	{
        		UItools.drawRightTopBitmap(mCanvas, mPaint, mHL_shadow_Bitmap, nLeft+mBGBitmap.getWidth(), nTop);
        	}else
        	{
        		UItools.drawRightTopBitmap(mCanvas, mPaint, mBG_shadow_Bitmap, nLeft+mBGBitmap.getWidth(), nTop);

        	}
        }
        mCanvas.drawBitmap(mBGBitmap, nLeft, nTop, mPaint);
        int i=0;
        for(;i<mAccArry.length;i++)
        {
        	if(i!=lastInZoneIdx)
        	{
        		drawText(i, mDrawOri.x, mDrawOri.y, false);
        		
        	}
        }
        //绘制中间的文字
        UItools.drawCenterText(mCanvas, mPaint, mDefCenterStr, mDrawOri.x, mDrawOri.y);
        
	}

	private void drawText(int i, int orix, int oriy, boolean bHighlight)
	{
	    if(!bHighlight)
	    {
	    	mPaint.setTextSize(mDefFontSize);
	    	mPaint.setColor(mDefOLfontColor);	    	
	    }
	    else
	    {
	    	mPaint.setTextSize(mDefHLFontSize);
	    	mPaint.setColor(mDefHLfontColor);
	    }
	    float txt_angle = mDrawStartAngle+mQuarterHalfAngle+mQuarterAngle*(i*1.0f);
	    //求X，Y坐标
	    double tx = mTextOffset*Math.sin(Math.toRadians(txt_angle));
	    double ty = mTextOffset * Math.cos( Math.toRadians(txt_angle) );
	    
	    tx = orix - tx;
	    ty = oriy - ty;
	    //绘制中间文字
	    UItools.drawCenterText(mCanvas, mPaint, mAccArry[i], (float)tx, (float)ty);
	    
	}
	
	@Override
	protected void showEff(int zoneIndx) {
		// TODO Auto-generated method stub
        if(null==mPaths)
        	return;
        if(bDUMP)
        {
        	if(zoneIndx>=0&&zoneIndx<mAccArry.length)
        	{
        		mPaint.setColor(Color.BLUE);
        		mCanvas.drawTextOnPath(mAccArry[zoneIndx], mPaths[zoneIndx], mBgWidth/4, mBgHeight/4, mPaint);
        	}
        }
        if(zoneIndx >= 0 )
        {
        	if(mbUsePlateList)
        	{
        		final float nLeft = mDrawOri.x - mBGBitmap.getWidth()/2;
        		final float nTop = mDrawOri.y -mBGBitmap.getHeight()/2;
        		//直接使用圆盘列表贴图
        		if(zoneIndx<mPaths.length)
        		{
        			mCanvas.drawBitmap(mPlateHLBitmapList[zoneIndx], nLeft, nTop, mPaint);
        		}
        	}
        	//绘制字体
        	if(zoneIndx<mAccArry.length)
        	{
        		drawText(zoneIndx, mDrawOri.x, mDrawOri.y, true);
        	}
        	else
        	{
        		if(zoneIndx==mPaths.length-1)
        		{
        			mPaint.setColor(mDefHLfontColor);
        			mPaint.setTextSize(mDefFontSize);
					UItools.drawCenterText(mCanvas, mPaint, mDefCenterStr, 
							mDrawOri.x, mDrawOri.y);
					Log.d(TAG, "in center path!");
        		}
        	}
        }
	}

	//得到视图区域高度
	@Override
	public int getViewZoneHeight()
	{
		if(!bDUMP)
		{
			return mViewZoneHeight;			
		}
		else
		    return 1200;
	}
	@Override
	public void onSelectChanged() {
		// TODO Auto-generated method stub

	}

	@Override
	public void showDefaultEff()
	{
		//显示背景颜色
		showBackGround();
		//显示效果
		showEff(mAccArry.length);
	}
	/**
	 * 返回res.isDefValue == true，则联合键值为res.resInt
	 * 否则联合键值为(byte)res.resStr.charAt(0)
	 */
	@Override
	public void getResult(EffResUnit res) {
		// TODO Auto-generated method stub
        if(null==mPaths)
        {
        	return ;
        }
        res.resType = EFFECT_TYPE.ACCURATE_SEL_EFF;
        res.resInt = lastInZoneIdx;
        if(res.resInt >=0 && res.resInt < mAccArry.length)
        {
        	res.resStr = mAccArry[lastInZoneIdx];
        }
        //这里定义最后一个path为中心区域
        if(mPaths.length-1==lastInZoneIdx)
        {
        	res.isDefValue = true;
			res.resInt = mDefCenterVal;
			res.resStr = mDefCenterStr;
        }
        else
        {
        	res.isDefValue = false;
        }
	}

}
