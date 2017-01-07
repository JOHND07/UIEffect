package com.jb.uieffect.effect.module;

/**
 * 效果返回的结果集合
 * @author denghuihan
 *
 */

public class EffResUnit {
	// 是否为默认值 
	public boolean isDefValue = false;
	public int resInt;
	public String resStr;
	public int resType;
	
	public EffResUnit()
	{
		resInt = -1;
		resStr = "";
		isDefValue = false;
	}
	public EffResUnit(int resint,String resstr)
	{
		resInt = resint;
		resStr = resstr;
		isDefValue = false;
	}
}
