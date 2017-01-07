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
				: Bitmap.Config.RGB_565; // 取drawable的颜色格式
		Bitmap bitmap = Bitmap.createBitmap(width, height, config); // 建立对应bitmap
		Canvas canvas = new Canvas(bitmap); // 建立对应bitmap的画布
		d.setBounds(x, y, x+width, y+height);
		d.draw(canvas); // 把drawable内容画到画布中
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
	 * draw a bitmap by right top point, 右对齐于right_x和top_y
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
	 * 生成目标联合键（只会转换英文字母，其他忽略）
	 * @param tarLowercaseAccs 目标大写联合键
	 * @param tarUppercaseAccs 目标小写联合键
	 * @param srcAccs 源联合键
	 */
	static public void genLUcase( int[] tarLowercaseAccs, int[] tarUppercaseAccs, String srcAccs )
	{
		srcAccs = srcAccs.toLowerCase();
		for( int i=0; i<srcAccs.length(); i++ )
		{
			tarLowercaseAccs[i] = srcAccs.charAt(i);
			// 自动生成大写的联合键
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
	 * 取指定view的截图
	 * @param v
	 * @return 将view截图转换为bitmap
	 */
	static public Bitmap getViewShot2Bitmap( View v)
	{
		Bitmap bit = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_8888);
		Canvas c = new Canvas(bit);
		v.draw(c);
		return bit;
	}
	/**
	 * 取指定view的截图
	 * @param v
	 * @return 将view截图转换为Drawable
	 */
	static public Drawable getViewShot2Drawable( View v)
	{
		return new BitmapDrawable(getViewShot2Bitmap(v));
	}
	/**
	 * 取状态栏高度
	 * @return
	 */
	static public int getStatusBarHeight()
	{
		 Rect frame = new Rect();  
		 m_window.getDecorView().getWindowVisibleDisplayFrame(frame);  
		 return frame.top;   
	}
	/**
	 * 取屏幕宽度
	 * @return
	 */
	static public int getScreenWidth()
	{
		return m_window.getWindowManager().getDefaultDisplay().getWidth();
	}
	
}
