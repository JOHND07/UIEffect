package com.jb.Ui;

import com.jb.uieffect.animation.UItools;
import com.jb.uieffect.effect.module.AccurateSelEff;
import com.jb.uieffect.effect.module.EffResUnit;
import com.jb.uieffect.effect.module.EffectButton;
import com.jb.uieffect.effect.module.EffectButton.OnButtonActionListener;
import com.jb.uieffect.effect.module.EffectView;
import com.jb.uieffect.effect.module.HiteEff;
import com.jb.uieffect.effect.module.SectorEff;
import com.jb.uieffect.effect.module.ShowEffect;
import com.jb.uieffect.effect.module.ShowEffect.EffectSelectListener;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;

/**
 * 按键 特效
 * @author denghuihan
 *
 */
public class TestButton extends Activity implements OnButtonActionListener,EffectSelectListener{

	public View mMainView = null;
	public View effect_main_view = null;
	public TextView mTextView = null;
	private final static String TAG ="TestButton";
	//扇形按钮
	public EffectButton mSectorButton = null;
	//环形按钮
	public EffectButton mAccurateSelEffButton = null;
	
	EffectView   mEffectView = null;
	
	//装载环形菜单位图
	public static Bitmap[] mPlate4HLBitmapList =null;
	//背景图
	public static Bitmap   mEffPlate4BgBitmap = null;
	//阴影图
	public static Bitmap   mEffPlateHLShadowBitmap = null;
	public static Bitmap mEffPlateBGShadowBitmap = null;
	public static Bitmap mHiteEffBgBitmap = null;
    
	protected HiteEff mHiteEff = null;
	protected AccurateSelEff mAccuSelEff = null; 
	//扇形效果
	protected SectorEff mSectorEff = null;	
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
    	super.onCreate(savedInstanceState);
    	LayoutInflater mInflater = getLayoutInflater();
    	mMainView = mInflater.inflate(R.layout.effects, null);
    	setContentView(R.layout.effects);
    	
    	//初始化效果
    	initEffect();
    	effect_main_view = findViewById(R.id.effects_main_view);
    	effect_main_view.setBackgroundDrawable(getResources().getDrawable(R.drawable.effects_bg));
  
        mTextView = (TextView) findViewById(R.id.effectTextView);
        mTextView.setTextSize(30);
    }
    
	//初始化效果
    private void initEffect()
    {
      	//UItools
    	UItools.init(getWindow());
    	
    	mEffectView = new EffectView(mMainView);		
        PopupWindow effectPopup = new PopupWindow(mEffectView, 100 , 100);
        mEffectView.setParentPopup(effectPopup);
		effectPopup.setBackgroundDrawable(null);  
		effectPopup.setOutsideTouchable(false);  
		effectPopup.setClippingEnabled(false);

		mSectorButton = (EffectButton) findViewById(R.id.sectorBtn) ;
        mSectorButton.setOnButtonActionListener(this);
        
        mAccurateSelEffButton = (EffectButton) findViewById(R.id.accurateSelEffBtn);
        mAccurateSelEffButton.setOnButtonActionListener(this);
    }
    //显示扇形菜单
    private void showSector(int x, int y, int delayMillis)
    {
    	mSectorEff = new SectorEff(mMainView.getContext());
    	mSectorEff.setEffectSelectListener(this);
    	
    	mSectorEff.setBGDrawable(getResources().getDrawable(R.drawable.eff_sector_bg));
        mSectorEff.setBG_shadow_Bitmap(BitmapFactory.decodeResource(getResources(), 
        		R.drawable.eff_sector_bg_shadow));
        mSectorEff.setHL_shadow_Bitmap(BitmapFactory.decodeResource(getResources(), R.drawable.eff_sector_part_shadow));
        
        mSectorEff.setSectorDrawable( getResources().getDrawable(R.drawable.eff_sector_part));
		mSectorEff.setAccArry( new String[]{ "选项1", "选项2" , "选项3", "选项4", "选项5" } );
		mEffectView.setShowEffect(mSectorEff);
//		mEffectView.show_lazy( x, y, delayMillis);
		mEffectView.show( x, y);
		
    }
    //显示环形菜单
    private void showAccurater(int x, int y, int delayMillis)
    {
		if( null == mPlate4HLBitmapList)
		{
			mPlate4HLBitmapList = new Bitmap[5];
			mPlate4HLBitmapList[0] = UItools.drawable2Bitmap(getResources().getDrawable(R.drawable.eff_plate_4_highlight_1));
			mPlate4HLBitmapList[1] = UItools.drawable2Bitmap(getResources().getDrawable(R.drawable.eff_plate_4_highlight_2));
			mPlate4HLBitmapList[2] = UItools.drawable2Bitmap(getResources().getDrawable(R.drawable.eff_plate_4_highlight_3));
			mPlate4HLBitmapList[3] = UItools.drawable2Bitmap(getResources().getDrawable(R.drawable.eff_plate_4_highlight_4));
			mPlate4HLBitmapList[4] = UItools.drawable2Bitmap(getResources().getDrawable(R.drawable.eff_plate_4_highlight_0));
		}
		if( null == mEffPlate4BgBitmap )
		{
			mEffPlate4BgBitmap = UItools.drawable2Bitmap(getResources().getDrawable(R.drawable.eff_plate_4_bg));
		}
		if( null == mEffPlateHLShadowBitmap )
		{
			mEffPlateHLShadowBitmap = UItools.drawable2Bitmap(getResources().getDrawable(R.drawable.eff_plate_hl_shadow));
		}
		if( null == mEffPlateBGShadowBitmap )
		{
			mEffPlateBGShadowBitmap = UItools.drawable2Bitmap(getResources().getDrawable(R.drawable.eff_plate_bg_shadow));
		}
		
		mAccuSelEff=new AccurateSelEff(mMainView.getContext());
		mAccuSelEff.setEffectSelectListener(this);
		mAccuSelEff.setAccArry( new int[] {'零','一','二','三'});
		mAccuSelEff.setHL_shadow_Bitmap(mEffPlateHLShadowBitmap);
		mAccuSelEff.setBG_shadow_Bitmap(mEffPlateBGShadowBitmap);
		mAccuSelEff.setBGBitmap(mEffPlate4BgBitmap);
		mAccuSelEff.setUseSectorDrawableList(mPlate4HLBitmapList);
		
		mAccuSelEff.setDefStr( "8" );
		mAccuSelEff.setDefVal( -1 );

		mEffectView.setShowEffect(mAccuSelEff);
		mEffectView.show( x, y);
    }
    
	@Override
	public void onEffectSelected(ShowEffect eff, EffResUnit res) {
		// TODO Auto-generated method stub
		String tmp="";
		if(eff == mAccuSelEff)
		{
			tmp+="精确选择:";
		}else if(eff == mSectorEff)
		{
			tmp+="扇形:";
		}
		if(!res.isDefValue)
			tmp += ""+res.resInt+","+res.resStr;
		else
			tmp += "默认,"+res.resStr;
		mTextView.setText(tmp);
	}

	@Override
	public int onButtonActionListener(EffectButton btn, int action_type,
			int key_code, int acc, int[] state_array, int x, int y) {
		// TODO Auto-generated method stub
		btn.setEffectView(mEffectView);
		switch(action_type)
		{
		case OnButtonActionListener.ACTION_TYPE_PRESS_DOWN:
			if(btn.getId()==R.id.sectorBtn)
			{
				// 显示扇形菜单
				showSector(x,y,0);
			}
			else if(btn.getId()==R.id.accurateSelEffBtn)
			{
				showAccurater(x, y, 0);
			}
			break;
		default:
			break;
		}
		return 0;
	}

}
