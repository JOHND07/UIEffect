package com.jb.uieffect.effect.module;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import com.jb.uieffect.effect.module.EffectView;

/**
 * 可以应用效果的按钮
 * @author denghuihan
 *
 */

public class EffectButton extends Button implements OnTouchListener {
    private final static String TAG ="EffectButton";
    //Btn 动作监听接口
    public interface OnButtonActionListener{
    	/** 长按下 **/
		public static final short ACTION_TYPE_LONG_PRESS		= 1;
		/** 短按完成 **/
		public static final short ACTION_TYPE_SHORT_PRESSED		= 2;
		/** 按下 **/
		public static final short ACTION_TYPE_PRESS_DOWN		= 3;
		/** 弹起 **/
		public static final short ACTION_TYPE_PRESS_UP			= 4;
		/** 横向左长拖完成 **/
		public static final short ACTION_SCROLL_LEFT_LONGED		= 5;
		/** 横向右长拖完成 **/
		public static final short ACTION_SCROLL_RIGHT_LONGED	= 6;
		/** 纵向下拖完成 **/
		public static final short ACTION_SCROLL_DOWN_LONGED		= 7;
		/** 纵向上拖完成 **/
		public static final short ACTION_SCROLL_UP_LONGED		= 8;
		
		/** 手势方向 **/
		public static final short GESTURE_DETECTION				= 10;
		
		public int onButtonActionListener( EffectButton btn, int action_type, int key_code, int acc, int [] state_array, int x, int y);

    }
    private EffectView mEffectView = null;
    private OnButtonActionListener m_OnButtonActionListener = null;
	public EffectButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		this.setOnTouchListener(this);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		int location[] = new int [2];
		this.getLocationOnScreen(location);
		Log.d(TAG,"mTriggerTopLeftPoint="+location[0]+",mTriggerTopLeftPoint.y="+location[1]);
		
		if(null != mEffectView)
		{
			mEffectView.onTouch(v,event);
		}
	
		switch(event.getAction())
		{
		case MotionEvent.ACTION_DOWN:
			if(null != m_OnButtonActionListener)
			{
				m_OnButtonActionListener.onButtonActionListener(this, OnButtonActionListener.ACTION_TYPE_PRESS_DOWN, 
						0, 0, null,(int)event.getX(), (int)event.getY());
			}
			break;
		case MotionEvent.ACTION_UP:
			if(null != mEffectView)
			{
				//关闭View
				mEffectView.finish();
			}
			break;
		}
		return false;
	}
	//设置效果View
	public void setEffectView(EffectView effectView)
	{
		this.mEffectView = effectView;
		mEffectView.setTriggerView(this);
	}
	//设置监听Button
	public void setOnButtonActionListener(OnButtonActionListener onButtonActionListener)
	{
		m_OnButtonActionListener = onButtonActionListener;
	}

}
