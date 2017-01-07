package com.jb.Ui;

import com.jb.uieffect.animation.AnimationMan;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.OnGestureListener;
import android.view.View.OnTouchListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.ViewFlipper;

public class TestRotate3d extends Activity implements OnTouchListener,
		OnGestureListener, OnCheckedChangeListener, OnItemSelectedListener {

    private static final String[] INTERPOLATORS = {
    	"Linear", "Decelerate", "Accelerate/Decelerate",
        "Anticipate", "Overshoot", "Anticipate/Overshoot",
        "Bounce","Accelerate"
    };
    
    //功能键横向长拖最少距离
    private static float DISTANCE_FUNC_SCROLL_LONG_X	=	70;
    //纵向长拖最少距离 
    private final static float DISTANCE_SCROLL_LONG_Y	=	20;
	private static final String TAG = "Flipper";
	
	private GestureDetector mGestureDetector;
	private ViewFlipper mFlipperView = null;
	private ImageView m_eff_image_flipper_image1 = null;
	private ImageView m_eff_image_flipper_image2 = null;
	private CheckBox  m_eff_image_flipper_3d_cb = null;
	private int m_interpolator = android.R.anim.linear_interpolator;
	
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.effect_flipper);
	
        mGestureDetector = new GestureDetector(this) ;
        mFlipperView = (ViewFlipper) findViewById(R.id.eff_image_flipper);
        m_eff_image_flipper_image1 = (ImageView) findViewById(R.id.eff_image_flipper_image1);
        m_eff_image_flipper_image2 = (ImageView) findViewById(R.id.eff_image_flipper_image2);
        m_eff_image_flipper_3d_cb = (CheckBox) findViewById(R.id.eff_image_flipper_3d_cb);
        m_eff_image_flipper_3d_cb.setOnCheckedChangeListener(this);
        //very import
        mFlipperView.setLongClickable(true);
        mFlipperView.setOnTouchListener(this);
        AnimationMan.initAnimations(mFlipperView.getContext());
        
        Spinner s = (Spinner) findViewById(R.id.eff_image_flipper_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, INTERPOLATORS);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(adapter);
        s.setOnItemSelectedListener(this);
	}
	
	@Override
	public void onItemSelected(AdapterView<?> parent, View v, int position,
			long id) {
		// TODO Auto-generated method stub
        switch(position)
        {
        case 0:
        	m_interpolator = android.R.anim.linear_interpolator;
        	break;
        case 1:
        	m_interpolator = android.R.anim.decelerate_interpolator;
        	break;
        case 2:
        	m_interpolator = android.R.anim.accelerate_decelerate_interpolator;
        	break;
        case 3:
        	m_interpolator = android.R.anim.anticipate_interpolator;
        	break;
        case 4:
        	m_interpolator = android.R.anim.overshoot_interpolator;
        	break;
        case 5:
        	m_interpolator = android.R.anim.anticipate_overshoot_interpolator;
        	break;
        case 6:
        	m_interpolator = android.R.anim.bounce_interpolator;
        	break;
        case 7:
        	m_interpolator = android.R.anim.accelerate_interpolator;
        	break;
        }
        Log.d(TAG,"m_interpolator="+m_interpolator);
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		// TODO Auto-generated method stub
        AnimationMan.bUse3D = isChecked;
        AnimationMan.initAnimations(m_eff_image_flipper_3d_cb.getContext());
	}

	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		//右
		if(distanceX<-DISTANCE_FUNC_SCROLL_LONG_X)
		{
			Log.d( TAG, "emit ACTION_SCROLL_RIGHT_LONGED");
			mFlipperView.setInAnimation(AnimationMan.getAnim(R.anim.push_right_in));
			mFlipperView.setOutAnimation(AnimationMan.getAnim(R.anim.push_right_out));			
	        mFlipperView.showPrevious();
		}
		//左
		else if(distanceX > DISTANCE_FUNC_SCROLL_LONG_X)
		{
			Log.d( TAG, "emit ACTION_SCROLL_LEFT_LONGED");
            mFlipperView.setInAnimation(AnimationMan.getAnim(R.anim.push_left_in));
            mFlipperView.setOutAnimation(AnimationMan.getAnim(R.anim.push_left_out));
            mFlipperView.showNext();
		}
		//上
		if( distanceY < -DISTANCE_SCROLL_LONG_Y)
		{
			Log.d( TAG, "emit ACTION_SCROLL_DOWN_LONGED");
            mFlipperView.setInAnimation(AnimationMan.getAnim(R.anim.keyboard_down_in));
            mFlipperView.setOutAnimation(AnimationMan.getAnim(R.anim.keyboard_down_out));
            mFlipperView.showPrevious();
		}
		//下 
		else if(distanceY > DISTANCE_SCROLL_LONG_Y)
		{
			Log.d( TAG, "emit ACTION_SCROLL_UP_LONGED");
            mFlipperView.setInAnimation(AnimationMan.getAnim(R.anim.keyboard_up_in));
            mFlipperView.setOutAnimation(AnimationMan.getAnim(R.anim.keyboard_up_out ));
            mFlipperView.showNext();
		}
		
		if(null != mFlipperView.getInAnimation())
		{
			mFlipperView.getInAnimation().setInterpolator(AnimationUtils.
					loadInterpolator(this, m_interpolator));
		}
		if(null != mFlipperView.getOutAnimation())
		{
        	mFlipperView.getOutAnimation().setInterpolator(AnimationUtils.loadInterpolator(this,
                    m_interpolator));
		}
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		if(mGestureDetector.onTouchEvent(event))
		{
			return true;
		}
		return false;
	}

}
