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
    //����ͼƬ�Ŀ�ȣ��߶�,�Ƕ�
	private int mBgWidth = 126;
	private int mBgHeight = 95;
	private float mStartAngle = 270;
	private float mDrawStartAngle = 0.0f;
	//�ȷֽ�
	private float mQuarterAngle =0.0f;		
	//�ȷְ��
	private float mQuarterHalfAngle = 0.0f;
	//�л��Ƕ�
	private float mSweepAngle ;
	private float mTextOffset = 40.0f;
	//����Ĭ������뾶
	private int mCenterR =25;	
	//ѡ����������ʱ���Ĭ��ֵ
	private int mDefCenterVal = -1;	
	//ѡ����������ʱ���Ĭ����ʾ
	private String mDefCenterStr="";	
	//����ֱ��ʹ��Բ���б�
	private boolean mbUsePlateList = false;
	//��ͼƬ��һ�����鴢��
	private Bitmap[] mPlateHLBitmapList = null;	
	//�û���������
	private int mDrawOriOffsetY =0;
	//�û�������������
	private int mTouchExpand = 0;	
	//�Ƿ��ڵ��λ������ʾ
	private boolean mDrawOnTouchPos = false;

	public AccurateSelEff(Context c) {
		super(c);
		// TODO Auto-generated constructor stub
	}
	//����Ĭ��ֵ
	public void setDefVal(int defval)
	{
		mDefCenterVal = defval;
	}
	//����Ĭ���ַ���
	public void setDefStr(String defStr)
	{
		mDefCenterStr = defStr;
	}

	//����ֱ��ʹ��Բ��ͼ���б� , ע�⽫���һ����Ϊѡ��Ĭ�ϸ���
	public void setUseSectorDrawableList(Drawable[] plateDrawableList)
	{
		mbUsePlateList = true;
		mPlateHLBitmapList = new Bitmap[mPlateHLBitmapList.length];
		for(int i = 0 ; i < plateDrawableList.length; i++)
		{
			mPlateHLBitmapList[i]=UItools.drawable2Bitmap(plateDrawableList[i]);
		}
	}
	
	//����ֱ��ʹ��Բ��ͼ���б� , ע�⽫���һ����Ϊѡ��Ĭ�ϸ���
	public void setUseSectorDrawableList(Bitmap[] plateBitmap)
	{
		mbUsePlateList = true;
		mPlateHLBitmapList = plateBitmap;
	}
	//������Դ,�õ������С֮��
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
	//����ÿ��Բ���ĽǶ�,�����setOriPosǰ���ã����򲻿�Ԥ��
	@Override
	public void setAccArry(int[] accArry)
	{
		super.setAccArry(accArry);
		//����ÿ��Բ���Ƕ�
		mSweepAngle = 360 / mAccArry.length; 
		//����·�������һ����������
		mPaths = new Path[mAccArry.length+1];
	}
	
	//�����Ƿ��ڵ��λ����ʾ
	public void setDrawOnTouchPos(boolean flag)
	{
		mDrawOnTouchPos = flag;
	}
	
	//���ô������
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
		// ���һ������path
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
        //�����м������
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
	    //��X��Y����
	    double tx = mTextOffset*Math.sin(Math.toRadians(txt_angle));
	    double ty = mTextOffset * Math.cos( Math.toRadians(txt_angle) );
	    
	    tx = orix - tx;
	    ty = oriy - ty;
	    //�����м�����
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
        		//ֱ��ʹ��Բ���б���ͼ
        		if(zoneIndx<mPaths.length)
        		{
        			mCanvas.drawBitmap(mPlateHLBitmapList[zoneIndx], nLeft, nTop, mPaint);
        		}
        	}
        	//��������
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

	//�õ���ͼ����߶�
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
		//��ʾ������ɫ
		showBackGround();
		//��ʾЧ��
		showEff(mAccArry.length);
	}
	/**
	 * ����res.isDefValue == true�������ϼ�ֵΪres.resInt
	 * �������ϼ�ֵΪ(byte)res.resStr.charAt(0)
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
        //���ﶨ�����һ��pathΪ��������
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
