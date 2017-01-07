package com.jb.uieffect.effect.module;

import com.jb.Ui.R;
import com.jb.uieffect.animation.UItools;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.PopupWindow;

/**
 * Ч����
 * @author denghuihan
 *
 */

public class EffectView extends View{
	//ʹ��˫����
	private static final boolean bUseDoubleBuffer = false;
	private static final boolean bUseMultiThread = true;
	private static final int PARENTPOPUP_LOCATION_OFFSET_X = 0;
	private static float TRANSLATEY_EFFECT_VIEW_ON_DRAW = 0.0f;
	private static final String TAG = "EffectView";
	//˫����߶ȺͿ��
	private int mDoubleBuffHeight = 0;
	private int mDoubleBuffWidth = 0;
	private Bitmap  mSrcBitmap = null;
	private Canvas  mSrcCanvas = null;
	private AlphaAnimation mAlphaAnimation = null;
	private ShowEffect mShowEffect = null;
	private int mTargetOldX = 0;
	private int mTargetOldY = 0;
	private Paint mPaint = null;
	private boolean mShowMoved = false;
	private Runnable mShowThread = null;
	private Runnable mDismissThread = null;
	
	/** �Ƿ��ӳ�����Ч��  **/
	private boolean mDismissLazy = false;
	private View mMainView = null;
	/** ��ť����Ļλ�� **/
	private int mX = -1;
	private int mY = -1;
	View mTriggerView = null;
	Point mTriggerTopLeftPoint = new Point();
	//��������
	private PopupWindow parentPopup = null;
	
	public EffectView(View MainView) {
		super(MainView.getContext());
		// TODO Auto-generated constructor stub
        mMainView = MainView;
        mPaint = new Paint();
        //ʹ�ö��߳�
        if(bUseMultiThread)
        {
        	Log.d( TAG, "new Runnable");
			mShowThread = new Runnable() {
				public void run() {
					Log.d( TAG, "Runnable show");
					show(mTargetOldX, mTargetOldY);
				}
			};
			
			mDismissThread = new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					Log.d( TAG, "Runnable mDismiss");
					//Ч����ʧ
					dismiss(false);
				}
			};
        }// end of if(bUseMultiThread)
        
        //ʹ��˫����
        if(bUseDoubleBuffer)
        {
			mDoubleBuffHeight = mMainView.getResources().getDimensionPixelSize(R.dimen.eff_double_buf_height);
			mDoubleBuffWidth = mMainView.getResources().getDimensionPixelSize(R.dimen.eff_double_buf_width);
			mSrcBitmap = Bitmap.createBitmap(mDoubleBuffWidth, mDoubleBuffHeight, Config.ARGB_8888);
            mSrcCanvas = new Canvas();
            mSrcCanvas.setBitmap(mSrcBitmap);
        }
        
	}//end of EffectView()

	/**
	 * �ӳ�
	 * @param x
	 * @param y
	 * @param delayMillis The delay (in milliseconds) until the Runnable will be executed.
	 */
	public void show_lazy(float x, float y , long delayMillis)
	{
		if(bUseMultiThread)
		{
			Log.d( TAG, "postDelayed mShower");
			mMainView.postDelayed(mShowThread, delayMillis);
			mTargetOldX =(int)x;
			mTargetOldY =(int)y;
			//����Ϣ�������Ƴ�ָ���߳�
			removeCallbacks(mDismissThread);
		}
		else 
		{
			show(x,y);
		}
	}
	/**
	 * ������Ļƫ��
	 */
	public void setScreenOffset(int offsetX, int offsetY, int absPosX, int absPosY)
	{
		mX = offsetX + absPosX;
		mY = offsetY + absPosY;
	}
	//��ʱTemp
	private int mTempX = -1;
	private int mTempY = -1;
	
	/**
	 * ת����ʵ���� 
	 */
	private void translateRealPos(float x, float y)
	{
		mTempX = (int) (x - mX+mTriggerTopLeftPoint.x);
		mTempY = (int) (y - mY+mTriggerTopLeftPoint.y);
	}
	
	public void moveTo(float x,float y)
	{
		if(!isShowing())
			return;

		translateRealPos(x, y);
		if(null != mShowEffect)
		{
			if(mShowEffect.onMove(mTempX, mTempY))
			{
				//�ػ�
				mAlphaAnimation = new AlphaAnimation(0.1f, 1.0f);
				//�ӳ�һ��
				mAlphaAnimation.setDuration(1000);
				this.startAnimation(mAlphaAnimation);
				Log.d( TAG, "startAnimation");
				mShowMoved = true ;
			}
		}
		
	}//end of moveTo
	
	//����Ļ����ʾ����
	public void drawScreenUnit(float x, float y)
	{
		if(null==mShowEffect)
			return;
	    mShowEffect.setOriPos(mTriggerTopLeftPoint.x+(int)x, mTriggerTopLeftPoint.y+(int)y);
	    Log.d( TAG, "DrawUnit =(	"+x+",	"+y+")");
	}
	
	@Override
	public boolean  onKeyDown  (int keyCode, KeyEvent event)
	{
		Log.d( TAG, "keyCode="+keyCode);
		return false;
	}
	
	@Override
	protected void onDraw(Canvas canvas)
	{
		if(!isShowing())
		{
			return;
		}
		super.onDraw(canvas);
		Log.d(TAG, "onDraw");
		if(null!=mShowEffect)
		{
			canvas.translate(mShowEffect.getGlobalTranslateX(), 
					TRANSLATEY_EFFECT_VIEW_ON_DRAW+mShowEffect.getGlobalTranslateY());
			//�Ƿ�ʹ��˫����
			if(bUseDoubleBuffer)
			{
				mSrcBitmap=Bitmap.createBitmap(mDoubleBuffWidth, mDoubleBuffHeight, 
						Config.ARGB_8888);
				mSrcCanvas.setBitmap(mSrcBitmap);
				mShowEffect.setCanvas( mSrcCanvas );
			}else
			{
				mShowEffect.setCanvas(canvas);
			}
			if(!mShowMoved)
			{
				//��ʾĬ��Ч��
				mShowEffect.showDefaultEff();
			}else
			{
				//��ʾ�ƶ�
				mShowEffect.showMove();
			}
			
			if(bUseDoubleBuffer)
			{
				canvas.drawBitmap(mSrcBitmap, 0, 0, mPaint);
			}
		}
	}
	
	public boolean onTouch(View v, MotionEvent event)
	{
		if(!isShowing())
		{
			return false;
		}
		switch(event.getAction())
		{
		case MotionEvent.ACTION_DOWN:
			break;
		case MotionEvent.ACTION_UP:
			finish();
			break;
		default:
			moveTo(event.getX(), event.getY());
			break;
		}
		return false;
	}
	
	//���õ������ڷ��
	public void setParentPopup(PopupWindow parentPopup)
	{
		this.parentPopup = parentPopup;
		parentPopup.setAnimationStyle(android.R.style.Animation_Toast);
	}
	
	public void setShowEffect(ShowEffect showEffect)
	{
		mShowMoved = false;// ��ʾĬ��״̬
		mShowEffect = showEffect;
		mShowEffect.setEffectView(this);
		mShowEffect.setResources(getResources());
	}
	
	//�ڴ�����
	public void finish()
	{
		if(null==mShowEffect)
		{
			return;
		}
		if(bUseMultiThread)
		{
			mMainView.removeCallbacks(mShowThread);
			if(isShowing())
			{
				mShowEffect.finish();
				this.dismiss(mDismissLazy);
			}
		}else
		{
			mShowEffect.finish( );
			this.dismiss( false );
		}
	}
	//��ʾЧ��
	public void show(float oldX, float oldY)
	{
		// TODO:TRANSLATEY_EFFECT_VIEW_ON_DRAW ̫����
		TRANSLATEY_EFFECT_VIEW_ON_DRAW = getDimension(R.dimen.translateY_effect_view_on_draw);
		if(null != mShowEffect)
			parentPopup.setHeight( (int) (TRANSLATEY_EFFECT_VIEW_ON_DRAW/2.0f )+
					/**/ mShowEffect.getViewZoneHeight());
		parentPopup.setWidth((int) UItools.getScreenWidth());
		
		int showX = PARENTPOPUP_LOCATION_OFFSET_X;
		int [] location = new int[2];
		mMainView.getLocationOnScreen(location);
		int showY = -(location[1]-UItools.getStatusBarHeight());// ��ȥ״̬���߶�
		Log.d(TAG,"showY="+showY+" mMainView.location[1]="+location[1]+",UItools.getStatusBarHeight()="+UItools.getStatusBarHeight());
		int gravity = Gravity.LEFT | Gravity.TOP;
		if(!isShowing())
		{
			parentPopup.showAtLocation(mMainView, gravity, showX, showY);			
		}
		//������Ļƫ��
		this.setScreenOffset(location[0], location[1], showX, showY);
		Log.d( TAG, "mMainView.localtion:(	"+location[0]+",	"+location[1]+"	)");
		drawScreenUnit(oldX-mX, oldY-mY);
		mShowEffect.playSoundOnShow();
		//ˢ��
		invalidate();
	}
	//Ч����ʧ
	private void dismiss(boolean bLazy)
	{
		if(bLazy)
		{
			postDelayed(mDismissThread, 300);
		}
		else
			parentPopup.dismiss();
	}
	/**
	 * 
	 * @return true if the view is showing, false otherwise 
	 */
	public boolean isShowing ()
	{
		return parentPopup.isShowing();
	}
	
//	/**
//	 * ȡ����λ��
//	 * @return
//	 */
//	public Point getKeyBoardPos()
//	{
//		Point point = new Point();
//		point.x = 0;
//		point.y = parentPopup.getHeight() - (int)UItools.getKeyboardHeight( mMainView );
//		Log.d(TAG, "point.y= "+point.y+" ,parentPopup.getHeight()="+parentPopup.getHeight()+
//				"(int)UItools.getKeyboardHeight( mMainView )="+(int)UItools.getKeyboardHeight( mMainView ));
//		return point;
//	}
	
//	/**
//	 * ȡ���̿��
//	 * @return
//	 */
//	public float getKeyBoardWidth()
//	{
//		return UItools.getQwertyKeyboardWidth(mMainView);
//	}
	
	// ȡ��������
	public float getDimension(int id)
	{
		return mMainView.getResources().getDimension(id);
	}

	//���ô�����view
	public void setTriggerView(View v)
	{
		mTriggerView = v;
		int location[] = new int[2];
		mTriggerView.getLocationOnScreen(location);
		mTriggerTopLeftPoint.set(location[0], location[1]);
		Log.d(TAG,"mTriggerTopLeftPoint="+mTriggerTopLeftPoint.x+"," +
				"mTriggerTopLeftPoint.y="+mTriggerTopLeftPoint.y);

	}
	public void setDismissLazy(boolean bDismissLazy)
	{
		this.mDismissLazy = bDismissLazy;		
	}
}
