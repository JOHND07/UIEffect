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
 * Ч������
 * @author denghuihan
 *
 */
public abstract class ShowEffect {
	
	//ѡ�����Ч��  �ӿ�
	public interface EffectSelectListener{
		// finish��ʱ��ᱻ���� 
		public void onEffectSelected(ShowEffect eff,EffResUnit res);
	}
	
	private final static String TAG = "ShowEffect";

	//Ч�����ͽӿ�
    public interface EFFECT_TYPE{
		public final static int  SECTOR_EFF		=	1;
		public final static int  ACCURATE_SEL_EFF		=	2;
    }
    
	// һ��Ϊԭʼ�㣬����һ�ε����λ�� 
	protected int mOriX = -1;
	protected int mOriY = -1;
	protected int mTranX = 0;
	protected int mTranY = 0;
	
	// ��ͼ��ƫ��λ�� 
	protected int mOffsetX = 0;
	protected int mOffsetY = 0;
	
	// �������� 
	protected RectF mTouchRectF = null;
	
	// Ĭ�ϻ��� 
	protected Paint mPaint = null;
	public static final boolean bDUMP = false;
	public static final boolean bEnableShadow = true;
	
	// Ч����Ҫ���Ƶ�Ŀ�껭�� 
	protected Canvas mCanvas;
	
	// �ϴ��������ڵ����� 
	protected int lastInZoneIdx = -1;

	// �ɸ�֪�������б� 
	protected Region[] mRegionArray = null;

	// �ɸ�֪��·���б� 
	protected Path[] mPaths = null;
	
	// ��Ҫ��ʾ���б��������б��йأ� 
	String [] mAccArry = null ;
	
	// Ч������
	protected Bitmap mBGBitmap = null;
	// Ч��������Ӱ 
	protected Bitmap mBG_shadow_Bitmap = null ;
	
	// Ч��������Ӱ 
	protected Bitmap mHL_shadow_Bitmap = null ;
	protected EffectView mEffectView = null;
	protected int mDefFontSize = 20;
	// Ĭ�ϸ������� 
	protected int mDefHLFontSize = 20;
	protected int mDefFontColor = 0xff000000 ;
	// Ĭ�ϸ���������ɫ 
	protected int mDefHLfontColor ;
	// Ĭ�ϷǸ���������ɫ 
	protected int mDefOLfontColor ;
	// view�Ŀ�������߶� 
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
	 * ���캯�� 
	 */
	public ShowEffect(Context c)
	{
		//��������
		mTouchRectF = new RectF();
		mPaint = new Paint();
		mPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
		mPaint.setAntiAlias(true);
		mPaint.setDither(true);
	}
	/**
	 * ��ɺ�  
	 */
	public void finish()
	{
		//��Ч�����ظ�һ������
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
	 * ��ʾ����
	 * @param canvas
	 */
	protected abstract void showBackGround();
	
	/**
	 * ��ʾ��ǰЧ��
	 * @param canvas zoneIndx��ǰ������ţ�-1��ʾû�б�ѡ��
	 */
	protected abstract void showEff(int zoneIndx);
	
	/**
	 * �жϵ�ǰ�����Ƿ�����ָ��������
	 * @param x
	 * @param y
	 * @return ��������ı�ʶindex,С��0����ʾû�б���������
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
		//�����һ����Ĭ�ϵ������ȼ���������
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
	 * ��ѡ��ı�ʱ������
	 */
	public abstract void onSelectChanged();
	
	/**
	 * ���Ч�����غ��н�����ɴ�����ȡ��
	 * @param res :�����
	 */
	public abstract void getResult(EffResUnit res);
	
	/**
	 * ����λ���ƶ�ʱ����Ӧ
	 * @param x
	 * @param y��
	 * @return ����true����ʾ�����ػ�
	 */
	public boolean onMove(float x,float y)
	{
		Log.d(TAG,"onMove x="+x+",y="+y);
		int idx = isInZone(x,y);
		//�����������������ϴε�һ������ʲôҲ����
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
	 * ��ʾ�ƶ�
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
	 * ��ʾĬ�ϵ�Ч�������ʼЧ��
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
	 * �ַ���ת��
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
	 * ���û���
	 * @param canvas
	 */
	public void setCanvas(Canvas canvas)	
	{
		this.mCanvas = canvas;
	}
	
	/**
	 * ���ô������
	 * @param oriX
	 * @param oriY
	 */
	public void setOriPos(int oriX,int oriY)
	{
		this.mOriX = oriX;
		this.mOriY = oriY;
		Log.d(TAG,"setOriPos="+oriX+","+oriY);
	}
	
	/**����ƫ��*/
	public void setOffsetX(int offsetX) {
		this.mOffsetX = offsetX;
	}
	public void setOffsetY(int offsetY) {
		this.mOffsetY = offsetY;
	}
	
	/**
	 * ȡ��Դ����xml�е�������Դ��������Դ��ÿ��Ч����ʱ�򽫻����
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
	 * �õ���ͼ����߶�
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
	 * ȫ��ˮƽƫ��
	 * @return
	 */
	public float getGlobalTranslateX()
	{
		return 0.0f;
	}
		
	/**
	 * ȫ�ִ�ֱƫ��
	 * @return
	 */
	public float getGlobalTranslateY()
	{
		return 0.0f;
	}
	
	/**
	 * ����ʱ��Ч
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
