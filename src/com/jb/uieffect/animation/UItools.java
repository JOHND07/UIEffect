package com.jb.uieffect.animation;

import com.jb.Ui.R;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.Window;

/**
 * 
 * @author denghuihan
 *
 */
public class UItools {
//	private static InputMethodService mBaseActivity = null;
	private static Window m_window ; 
//	static public void init(InputMethodService a)
//	{
//		mBaseActivity = a;
//	}
	static public void init( Window w)
	{
		m_window = w;
	}
	static public Drawable drawable_getPartOf(Drawable d, int x, int y, int width, int height )
	{
		Bitmap.Config config = d.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
				: Bitmap.Config.RGB_565; // ȡdrawable����ɫ��ʽ
		Bitmap bitmap = Bitmap.createBitmap(width, height, config); // ������Ӧbitmap
		Canvas canvas = new Canvas(bitmap); // ������Ӧbitmap�Ļ���
		d.setBounds(x, y, x+width, y+height);
		d.draw(canvas); // ��drawable���ݻ���������
		return new BitmapDrawable(bitmap);
	}

	static public Drawable getDrawableOf(View context, int id )
	{
		return context.getResources().getDrawable( id );
	}
	static public Drawable getDrawableOf(View context, String strIdName )
	{
		// Log.d( " getDrawableOf = " , strIdName);
		return context.getResources().getDrawable(
				context.getResources().getIdentifier(     
				strIdName, "drawable", R.class.getPackage().getName()));
	}
	static public Bitmap drawable2Bitmap( Drawable drawable )
	{
		if( null == drawable)
			return null;
		Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
				drawable.getIntrinsicHeight(),
				drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888: Bitmap.Config.RGB_565);
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
		drawable.draw(canvas);
		return bitmap;
	}
	
	static public void drawCenterText( Canvas canvas, Paint paint, String text, float center_x, float center_y)
	{
		FontMetrics fm = paint.getFontMetrics();
		float tcenx =  paint.measureText(text)/2;
		float tceny = fm.ascent / 2.0f;
		canvas.drawText(text, (float)center_x-tcenx, (float)center_y-tceny, paint);
	}
	
	/**
	 * draw a bitmap by right top point, �Ҷ�����right_x��top_y
	 * @param canvas
	 * @param paint
	 * @param bitmap
	 * @param right_x
	 * @param top_y
	 */
	static public void drawRightTopBitmap( Canvas canvas, Paint paint, Bitmap bitmap, float right_x, float top_y )
	{
		if( null == bitmap)
			return;
		canvas.drawBitmap(bitmap, right_x-bitmap.getWidth(), top_y, paint);
	}
	
	/**
	 * ����Ŀ�����ϼ���ֻ��ת��Ӣ����ĸ���������ԣ�
	 * @param tarLowercaseAccs Ŀ���д���ϼ�
	 * @param tarUppercaseAccs Ŀ��Сд���ϼ�
	 * @param srcAccs Դ���ϼ�
	 */
	static public void genLUcase( int[] tarLowercaseAccs, int[] tarUppercaseAccs, String srcAccs )
	{
		srcAccs = srcAccs.toLowerCase();
		for( int i=0; i<srcAccs.length(); i++ )
		{
			tarLowercaseAccs[i] = srcAccs.charAt(i);
			// �Զ����ɴ�д�����ϼ�
			if( tarLowercaseAccs[i] >= 'a' && tarLowercaseAccs[i] <= 'z' )
			{
				tarUppercaseAccs[i] = tarLowercaseAccs[i]-32;
			}
			else
			{
				tarUppercaseAccs[i] = tarLowercaseAccs[i];
			}
		}
	}
	static public int char2Int( char c)
	{
		return c;
	}
	
	/**
	 * ȡָ��view�Ľ�ͼ
	 * @param v
	 * @return ��view��ͼת��Ϊbitmap
	 */
	static public Bitmap getViewShot2Bitmap( View v)
	{
		Bitmap bit = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_8888);
		Canvas c = new Canvas(bit);
		v.draw(c);
		return bit;
	}
	/**
	 * ȡָ��view�Ľ�ͼ
	 * @param v
	 * @return ��view��ͼת��ΪDrawable
	 */
	static public Drawable getViewShot2Drawable( View v)
	{
		return new BitmapDrawable(getViewShot2Bitmap(v));
	}
	/**
	 * ȡ״̬���߶�
	 * @return
	 */
	static public int getStatusBarHeight()
	{
		 Rect frame = new Rect();  
		 m_window.getDecorView().getWindowVisibleDisplayFrame(frame);  
		 return frame.top;   
	}
	/**
	 * ȡ��Ļ���
	 * @return
	 */
	static public int getScreenWidth()
	{
		return m_window.getWindowManager().getDefaultDisplay().getWidth();
	}
	
}
