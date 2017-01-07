package com.jb.uieffect.animation;

import java.util.HashMap;
import java.util.Map;

import com.jb.Ui.R;

import android.content.Context;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

/**
 *  动画管理累
 *  @author denghuihan
 *
 */
public class AnimationMan {
    
	//是否启用3D效果
	public static boolean bUse3D = false;
	
	private static Map<Integer,Animation> mAnmiMap = new HashMap<Integer, Animation>();
	private static Context mContext = null;
	
	private static Animation m3dUpOutAnimation = null;
	private static Animation m3dUpInAnimation = null;
	private static Animation m3dDownOutAnimation = null;
	private static Animation m3dDownInAnimation = null;
	
	private static Animation m3dLeftOutAnimation = null;
	private static Animation m3dLeftinAnimation = null;
	private static Animation m3dRightOutAnimation = null;
	private static Animation m3dRightInAnimation = null;
	
	//从资源中得到动画
	public static Animation getAnimFromRes(int id)
	{
		return AnimationUtils.loadAnimation(mContext, id);
	}
		
	//从资源中得到动画
	public static Animation getAnimFromRes(Animation anim, int id)
	{
		if(null != anim)
		{
			return anim;
		}
		return AnimationUtils.loadAnimation(mContext, id);
	}
	
	//把资源与ID关联起来
	private static void putAnimRes2Map(int res_id)
	{
		mAnmiMap.put(res_id, getAnimFromRes(res_id));
	}
	//初始化动画
	public static void initAnimations(Context c)
	{
		mContext = c;
		
		putAnimRes2Map(R.anim.push_left_in);
		putAnimRes2Map(R.anim.push_right_in);
		putAnimRes2Map(R.anim.push_up_in);
		putAnimRes2Map(R.anim.push_down_in);
		
		putAnimRes2Map(R.anim.push_left_out);
		putAnimRes2Map(R.anim.push_right_out);
		putAnimRes2Map(R.anim.push_up_out);
		putAnimRes2Map(R.anim.push_down_out);
		
		putAnimRes2Map(R.anim.alpha_appearance);
		
		putAnimRes2Map(R.anim.keyboard_up_in);
		putAnimRes2Map(R.anim.keyboard_down_in);
		putAnimRes2Map(R.anim.keyboard_up_out);
		putAnimRes2Map(R.anim.keyboard_down_out);

		if( bUse3D )
		{
			m3dUpOutAnimation 	= Rotate3d.getUpOutAnim() ;
			m3dUpInAnimation 	= Rotate3d.getUpInAnim();
			m3dDownOutAnimation = Rotate3d.getDownOutAnim();
			m3dDownInAnimation 	= Rotate3d.getDownInAnim();
			
			m3dLeftOutAnimation = Rotate3d.getLeftOutAnim();
			m3dLeftinAnimation 	= Rotate3d.getLeftInAnim();
			m3dRightOutAnimation= Rotate3d.getRightOutAnim();
			m3dRightInAnimation = Rotate3d.getRightInAnim();
		}
	}
	
	public static Animation get2DAnim(int nAnimId)
	{
		return mAnmiMap.get(nAnimId);
	}
	
	public static Animation getAnim(int nAnimId)
	{
		if(bUse3D)
		{
			Animation a = get3DAnim(nAnimId);
			if(null!=a)
				return a;
		}
		return get2DAnim(nAnimId);
	}
	
	public static Animation get3DAnim(int nAnimId)
	{
		switch(nAnimId)
		{
		case R.anim.keyboard_up_in:
//			case R.anim.push_up_in:
				return m3dUpInAnimation;
			case R.anim.keyboard_down_in:
//			case R.anim.push_down_in:
				return m3dDownInAnimation;
			case R.anim.push_left_in:
				return m3dLeftinAnimation;
			case R.anim.push_right_in:
				return m3dRightInAnimation;
				
			case R.anim.keyboard_up_out:
//			case R.anim.push_up_out:
				return m3dUpOutAnimation;
			case R.anim.keyboard_down_out:
//			case R.anim.push_down_out:
				return m3dDownOutAnimation;
			case R.anim.push_left_out:
				return m3dLeftOutAnimation;
			case R.anim.push_right_out:
				return m3dRightOutAnimation;
			default:
				break;
		}
		return null;
	}
	
}
