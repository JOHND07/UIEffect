package com.jb.uieffect.effect.module;

/**
 * Ч�����صĽ������
 * @author denghuihan
 *
 */

public class EffResUnit {
	// �Ƿ�ΪĬ��ֵ 
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
