package com.jb.uieffect.effect.module;

import com.jb.Ui.R;
import com.jb.uieffect.animation.UItools;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint.FontMetrics;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.Path.Direction;
import android.graphics.Region.Op;
import android.graphics.drawable.Drawable;

/**
 * ����
 * @author denghuihan
 *
 */
public class SectorEff extends ShowEffect {

	private final static String TAG = "SectorEff"; 
	//�����ھ�
	private int mRin = 30;
	
	//����ֱ�������ο�� = ֱ��-�ھ�
	private int mR = 80;
	
	//��������չ
	private int mHighLightExpand = 90;
	
	//����Բ��x,y����
	private int mCx;
	private int mCy;
	
	//ÿ��ѡ��Ƕ�
	private float mSweepAngle = 20.0f;
	
	//��ʼ�������νǶ�
	private float mStartAngle;
	private float mBgOffsetX =0.0f;
	private float mBgTextOffsetX = 0.0f;
	private float mHighLightOffsetX = 0.0f;
	private float mGlobalTranslateX = 0.0f;
	
	private Region mExcludeRegion = null;
	private Path   mExcludePath = null;
	
	private Drawable[] mSectorDrawableList = null;
	private boolean  mUseSectorList = false;
	private Bitmap  mSectorBitmap = null;
	private int mFontColor;
	//�Ǹ�������
	private int mOLFontSize;
	
	//��������
	private int mHLFontSize;
	
	public SectorEff(Context c) {
		super(c);
		// TODO Auto-generated constructor stub
	}

	//��������ͼƬ
	public void setSectorDrawable(Drawable sectorDrawable)
	{
		mSectorBitmap = UItools.drawable2Bitmap(sectorDrawable);
	}
	//����ֱ��ʹ�������б�
	public void setUseSectorDrawableList(Drawable[] sectorDrawableList)
	{
		mUseSectorList = true;
		this.mSectorDrawableList = sectorDrawableList;
	}
	
	@Override
	public void setResources(Resources r)
	{
		super.setResources(r);
		 mRin = (int) r.getDimension( R.dimen.eff_sector_r_in);
		 mR = (int) r.getDimension( R.dimen.eff_sector_r);
		 mHighLightExpand = (int) r.getDimension( R.dimen.eff_sector_highlight_expand);
		 mBgOffsetX = r.getDimensionPixelOffset(R.dimen.eff_sector_bg_offset_x);
		 mHighLightOffsetX = r.getDimensionPixelOffset(R.dimen.eff_sector_highlight_offset_x);
		 mBgTextOffsetX = r.getDimensionPixelOffset(R.dimen.eff_sector_bg_text_x);
		 mGlobalTranslateX = r.getDimension(R.dimen.eff_sector_global_translate_x);
		 mFontColor = r.getColor(R.color.eff_sector_font_color);
		 mOLFontSize = r.getDimensionPixelSize(R.dimen.eff_sector_offlight_font_size);
		 mHLFontSize = r.getDimensionPixelSize(R.dimen.eff_sector_highlight_font_size);		 
		 mViewZoneHeight = (int) ((mR+mHighLightExpand+Math.abs(mHighLightOffsetX))*2)+30;
		 lastInZoneIdx = 2;
	}
	
	//��ʾĬ�ϵ�Ч�������ʼ��Ч��
	@Override
	public void showDefaultEff()
	{
		showBackGround();
		showEff(mAccArry.length/2);
	}
	
	@Override
	public void setCanvas(Canvas canvas)
	{
		super.setCanvas(canvas);
	}
	
	private void genExcludeRegion()
	{
		mExcludeRegion = new Region();
		Region tmpRegion = new Region();
		RectF rectf = new RectF(mCx-mRin, mCy-mRin, mCx+mRin, mCy+mRin);
		Path path = new Path();
		path.moveTo(mCx, mCy);
		path.addOval(rectf, Direction.CW);
		path.close();
		tmpRegion.set((int)rectf.left, (int)rectf.top, 
				(int)rectf.right, (int)rectf.bottom);
		mExcludeRegion.setPath(path, tmpRegion);
		mExcludePath = path;
	}
	
	//��ʾ����
	@Override
	protected void showBackGround() {
		// TODO Auto-generated method stub
        final float nBgLeft = (mCx-mR+mBgOffsetX);
        final float nBgTop =(mCy-mBGBitmap.getHeight()/2);
        
        if(bEnableShadow)
        {
        	UItools.drawRightTopBitmap(mCanvas, mPaint, mBG_shadow_Bitmap, 
        			nBgLeft+mBGBitmap.getWidth(), nBgTop);
        }
    	mCanvas.drawBitmap(mBGBitmap, nBgLeft/*mBGBitmap.getWidth() + 24 -3*/, 
				nBgTop /*+2*/, mPaint);
    	
    	for(int i=0;i<mAccArry.length;i++)
    	{
    		if(lastInZoneIdx!=i)
    		{
    		   drawText(i, mFontColor, false);	
    		}
    	}
    	if(bDUMP)
    	{
    		mCanvas.clipPath(mExcludePath, Op.DIFFERENCE);
    		mPaint.setColor(Color.argb(127, 0, 50, 0));
    		for(int i=0;i<mPaths.length;i++)
    		{
    			Path path = mPaths[i];
    			mPaint.setColor(Color.argb(127, i*30, i*30, i*30));
    			mCanvas.drawPath(path, mPaint);
    		}
			float angle = mStartAngle+0.0f-180.0f+mSweepAngle/2.0f;//= mStartAngle+mSweepAngle*1.5f;
//			mCanvas.save();
//			mCanvas.rotate( angle, mCx, mCy);
			for( int i = 0; i < mAccArry.length; i++ )
			{
				drawText(i, Color.GREEN, false );
			}
//			mCanvas.restore();
    	}
	}

	@Override
	protected void showEff(int zoneIndx) {
		// TODO Auto-generated method stub
        if(zoneIndx>=0 && zoneIndx<mAccArry.length)
        {
        	drawText(zoneIndx,mFontColor,true);
        }
	}
	protected void drawText(int i, int color, boolean bHighLight)
	{
		mPaint.setTextSize(mOLFontSize);
		mPaint.setColor(color);
		FontMetrics fm = mPaint.getFontMetrics();
		float TextX =(float)((mCx - mR) + ((mR - mRin - mPaint.measureText(mAccArry[i])) / 2.0));
	    float TextY = mCy - fm.ascent/2;
	    mCanvas.save();
	    float angle = mStartAngle+0.0f-180.0f+mSweepAngle/2.0f;
	    
	    //������ת
	    mCanvas.rotate(angle, mCx, mCy);
//	    mCanvas.rotate(-mSweepAngle, mCx, mCy);
	    mCanvas.rotate(-mSweepAngle*i, mCx, mCy);	
	    if(bHighLight)
	    {
	    	if(bDUMP)
	    	{
				RectF rectF = new RectF(mTouchRectF);
				rectF.top-= mHighLightExpand;
				rectF.left-= mHighLightExpand;
				rectF.bottom+= mHighLightExpand;
				rectF.right+= mHighLightExpand;
//				float angle = ;//mStartAngle+0.0f+mSweepAngle/2.0f;
				Path path = new Path();
				path.moveTo(mCx, mCy);
				path.arcTo( rectF, 
				//		mStartAngle-mSweepAngle*(i+((float)mAccArry.length)/2.0f-0.5f)
						mStartAngle-mSweepAngle*(((float)mAccArry.length)/2.0f-0.5f)
						, mSweepAngle);
				path.close();
				mCanvas.drawPath(path, mPaint);
	    	}
	    	// ���Ƹ�����
			//			mCanvas.drawArc(	rectF, (float)( mStartAngle-mSweepAngle*i), mSweepAngle, true, mPaint);
	    }
	    if(bHighLight)
	    {
	    	mPaint.setTextSize(mHLFontSize);
	    	final float nLeft = mCx - mR -mRin+mHighLightOffsetX;
			final float nTop = mCy - mSectorBitmap.getHeight()/2;
			if(bEnableShadow)
				UItools.drawRightTopBitmap(mCanvas, mPaint, mHL_shadow_Bitmap, 
					nLeft+mSectorBitmap.getWidth(), nTop);
			mCanvas.drawBitmap( mSectorBitmap, 
					nLeft/*- mHighLightExpand*/, 
					nTop, mPaint);
			TextX=(float) (mCx - mR-(mHighLightExpand)/2.0);
	    }
	    else
	    {
	    	//���ǻ��Ƹ��������ǻ��Ʊ���
	    	TextX += mBgTextOffsetX;
	    }
	    mCanvas.drawText(mAccArry[i], TextX, TextY, mPaint);
	    mCanvas.restore();
	}

	@Override
	public void onSelectChanged() {
		// TODO Auto-generated method stub

	}
	
	/**
	 * ȫ��ˮƽƫ��
	 * @return
	 */
	public float getGlobalTranslateX()
	{
		return mGlobalTranslateX;
	}
	

	@Override
	public void getResult(EffResUnit res) {
		// TODO Auto-generated method stub
        if(null==mAccArry)
        {
        	return;
        }
        if(lastInZoneIdx<0)
        {
        	return;
        }
        res.resStr = mAccArry[lastInZoneIdx];
    	res.resInt = lastInZoneIdx;
		res.resType = EFFECT_TYPE.SECTOR_EFF;
	}
	@Override
	protected int isInZone(float x, float y)
	{
		if(mExcludeRegion.contains((int)x, (int)y))
		{
			return -1;
		}
		return super.isInZone(x, y);
	}
	
	//���ô�����
	@Override
	public void setOriPos(int oriX, int oriY)
	{
		super.setOriPos(oriX, oriY);
		//�����������ڵ������
		mCx = oriX+(mR-mRin)/2+mRin;
		mCy = oriY;
		
		//���崥����Χ
		mTouchRectF.set(mCx-mR, mCy-mR, mCx+mR, mCy+mR);
		//����Բ���Ŀ�ʼ���ƽǶ�
		mStartAngle = 180.0f+ mSweepAngle*((float)mAccArry.length)/2.0f-mSweepAngle;
		mPaths = new Path[mAccArry.length];
		
		//���ɿɸ�֪�Ŀռ�
		float startAngle = 0;
		for(int i=0;i<mPaths.length;i++)
		{
			Path path = new Path();
			path.moveTo(mCx, mCy);
			path.arcTo(mTouchRectF, mStartAngle+startAngle, mSweepAngle);
			path.close();
			mPaths[i] = path;
			startAngle-=mSweepAngle;
		}
		genExcludeRegion();
	}

}
