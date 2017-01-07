package com.jb.uieffect.effect.module;

import com.jb.Ui.R;
import com.jb.uieffect.animation.UItools;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

public class HiteEff extends ShowEffect {
    
	private final static String TAG ="HiteEff";
	private int mHLidx = -1;
	
	public HiteEff(Context c) {
		super(c);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void showBackGround() {
		// TODO Auto-generated method stub
        int left = mDrawOri.x - mBGBitmap.getWidth()/2;
        mCanvas.drawBitmap(mBGBitmap, 
				left, mDrawOri.y-mBGBitmap.getHeight()/2, mPaint);
        Log.d(TAG,"Draw center y="+mDrawOri.y);
        int stepX =  mBGBitmap.getWidth() / (this.mAccArry.length+1);
        for(int i=0;i<mAccArry.length;i++)
        {
        	if(i!=mHLidx)
        	{
        		mPaint.setTextSize(mDefFontSize);
        		mPaint.setColor(mDefFontColor);
        	}
        	else
        	{
				mPaint.setTextSize( mDefHLFontSize );
				mPaint.setColor( mDefHLfontColor);
        	}
        	int centerX = left+stepX*(i+1);
        	UItools.drawCenterText(mCanvas, mPaint, mAccArry[i], 
        			centerX, mDrawOri.y);
        }
        Log.d(TAG,"HiteEff mAccArry[mHLidx]="+mAccArry[mHLidx]);
	}

	@Override
	protected void showEff(int zoneIndx) {
		// TODO Auto-generated method stub

	}
	@Override
	public void setResources(Resources res)
	{
		super.setResources(res);
		mDrawOri.y = (int)res.getDimension(R.dimen.eff_hite_draw_ori_offset_y);
		mDrawOri.x =(int)(UItools.getScreenWidth()/2);
		mViewZoneHeight =(int)res.getDimension(R.dimen.eff_hite_view_zone_height);
	}

	public void setHLidx(int nHLidx)
	{
		this.mHLidx = nHLidx;
	}
	
	@Override
	public void onSelectChanged() {
		// TODO Auto-generated method stub

	}

	@Override
	public void getResult(EffResUnit res) {
		// TODO Auto-generated method stub

	}

}
