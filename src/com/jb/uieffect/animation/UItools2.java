package com.jb.uieffect.animation;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Paint.FontMetrics;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

/**
 * UI工具资源类型定义
 * @author denghuihan
 *
 */
public class UItools2 {

	public interface Res
	{
		final static int XML =0;
		final static int DRAWABLE 	= 1;
		final static int STRING 	= 2;
		final static int COLOR 		= 3;
		final static int DIMEN 		= 4;
		static final int FRACTION 	= 5;
		static final int INTEGER 	= 6;
		static final int ARRAY	 	= 7;
		
		final static int NOT_FOUND	= 0;
	}
	private static final String[] RES_DEF_TYPE_NAME = new String[]{"xml", "drawable", "string", "color", "dimen", "fraction", "integer", "array"};
	
	static public Drawable getDrawable( Context outerContext,Context defaultContext, String resName )
	{
		return (Drawable) getRes(outerContext, defaultContext, resName, Res.DRAWABLE);
	}
	static public String getString( Context outerContext,Context defaultContext, String resName )
	{
		return (String) getRes(outerContext, defaultContext, resName, Res.STRING);
	}
	static public String getString( Context c, String resName )
	{
		int resId = getResId( c, resName, Res.STRING );
		return c.getString(resId);
	}
	static protected int getColor( Context outerContext,Context defaultContext, String resName )
	{
		return (Integer) getRes(outerContext, defaultContext, resName, Res.COLOR);
	}
	static public float getDimen( Context outerContext,Context defaultContext, String resName )
	{
		return (Float) getRes(outerContext, defaultContext, resName, Res.DIMEN);
	}
	static public float getFraction( Context outerContext,Context defaultContext, String resName )
	{
		return (Float) getRes(outerContext, defaultContext, resName, Res.FRACTION);
	}
	static public int getInteger( Context outerContext,Context defaultContext, String resName )
	{
		return (Integer) getRes(outerContext, defaultContext, resName, Res.INTEGER);
	}
	static public XmlResourceParser getXml( Context outerContext,Context defaultContext, String resName )
	{
		return (XmlResourceParser) getRes(outerContext, defaultContext, resName, Res.XML);
	}
	/**
	 * 在两个上下文中选择资源，优先使用 outerContext里面的资源，如果没有 outerContext里面没有相应的资源，则使用默认defaultContext上下文的资源
	 * @param outerContext
	 * @param defaultContext
	 * @param resName
	 * @param defType
	 * @return
	 */
    private static Object getRes( Context outerContext,Context defaultContext, String resName,int defType )
    {
    	ContextResult cr;
    	cr = getOuterResId( outerContext, defaultContext, resName, RES_DEF_TYPE_NAME[defType] );
    	switch (defType) {
		case Res.XML:
			return cr.getContext().getResources().getXml(cr.getResid());
		case Res.DRAWABLE:
			return cr.getContext().getResources().getDrawable(cr.getResid());
		case Res.STRING:
			return cr.getContext().getResources().getString(cr.getResid());
		case Res.COLOR:
			return cr.getContext().getResources().getColor(cr.getResid());
		case Res.DIMEN:
			return cr.getContext().getResources().getDimension(cr.getResid());
		case Res.FRACTION:
			return cr.getContext().getResources().getFraction(cr.getResid(), 1, 1);
		case Res.INTEGER:
			return cr.getContext().getResources().getInteger(cr.getResid());
		default:
			return null;
		}
    	
    }
    /**
     * 根据上下文和资源的名称取资源id，如果返回0，则表明id不存在
     * @param context
     * @param name
     * @param defType
     * @return
     */
    private static int getResId( Context context, String name, String defType )
    {
    	if( null == context )
    		return 0;
    	return context.getResources().getIdentifier( name, defType, context.getPackageName());
    }
    /**
     * 根据上下文和资源的名称取资源id，如果返回0，则表明id不存在
     * @param context
     * @param name
     * @param defType {@link UITools.Res}
     * @return
     */
    public static int getResId( Context context, String name, int defType )
    {
    	return context.getResources().getIdentifier( name, RES_DEF_TYPE_NAME[defType], context.getPackageName());
    }
    
    /**
     * 查找指定图片名对应的资源
     * @param context
     * @param specResName 指定的资源名
     * @param defResName 如果指定的资源名不存在，则找默认资源名
     * @return
     */
	static public Drawable getSpecDrawable( Context outerContext, Context defContext, String specResName ,String defResName)
	{
		ContextResult cr = getSpecRes(outerContext, defContext, specResName, defResName, Res.DRAWABLE);
		return cr.context.getResources().getDrawable(cr.resid);
	}
    /**
     * 查找指定颜色名对应的资源
     * @param context
     * @param specResName 指定的资源名
     * @param defResName 如果指定的资源名不存在，则找默认资源名
     * @return
     */
	static public int getSpecColor( Context outerContext, Context defContext, String specResName ,String defResName)
	{
		ContextResult cr = getSpecRes(outerContext, defContext, specResName, defResName, Res.COLOR);
		return cr.context.getResources().getColor(cr.resid);
	}
    /**
     * 查找指定字符串名对应的资源
     * @param context
     * @param specResName 指定的资源名
     * @param defResName 如果指定的资源名不存在，则找默认资源名
     * @return
     */
	static public String getSpecString( Context outerContext, Context defContext, String specResName ,String defResName)
	{
		ContextResult cr = getSpecRes(outerContext, defContext, specResName, defResName, Res.STRING);
		return cr.context.getResources().getString(cr.resid);
	}
    /**
     * 查找指定像素名对应的资源
     * @param context
     * @param specResName 指定的资源名
     * @param defResName 如果指定的资源名不存在，则找默认资源名
     * @return
     */
	static public float getSpecDimen( Context outerContext, Context defContext, String specResName ,String defResName)
	{
		ContextResult cr = getSpecRes(outerContext, defContext, specResName, defResName, Res.DIMEN);
		try {
		return cr.context.getResources().getDimension(cr.resid);
		} catch (Resources.NotFoundException e) {
			return getDimen(defContext, defContext, defResName) ;
		}
	}
    /**
     * 查找指定百分比名对应的资源
     * @param context
     * @param specResName 指定的资源名
     * @param defResName 如果指定的资源名不存在，则找默认资源名
     * @return
     */
	static public float getSpecFraction( Context outerContext, Context defContext, String specResName ,String defResName)
	{
		ContextResult cr = getSpecRes(outerContext, defContext, specResName, defResName, Res.FRACTION);
		try {			
			return cr.context.getResources().getFraction(cr.resid, 1, 1);
		} catch (Resources.NotFoundException e) {
			return getFraction(defContext, defContext, defResName);
		}
	}
    /**
     * 查找指定整形名对应的资源
     * @param context
     * @param specResName 指定的资源名
     * @param defResName 如果指定的资源名不存在，则找默认资源名
     * @return
     */
	static public int getSpecInteger( Context outerContext, Context defContext, String specResName ,String defResName)
	{
		ContextResult cr = getSpecRes(outerContext, defContext, specResName, defResName, Res.INTEGER);
		return cr.context.getResources().getInteger(cr.resid);
	}

	/**
	 * 根据主题包取得指定资源
	 * @param outerContext
	 * @param defContext
	 * @param specResName
	 * @param defResName
	 * @param defType
	 * @return
	 */
	public static ContextResult getSpecRes( Context outerContext, Context defContext, String specResName ,String defResName , int defType )
	{
		ContextResult cr;
		int resId = getResId(outerContext, specResName, defType);
		if( resId != 0 )
		{
			cr = new ContextResult(outerContext, resId);
		}
		else
		{
			cr = getOuterResId( outerContext, defContext, defResName, RES_DEF_TYPE_NAME[defType] ); 
		}
		return cr;
	}
	
//    /**
//     * 查找指定资源名对应的资源ID
//     * @param context
//     * @param specResName 指定的资源名
//     * @param defResName 如果指定的资源名不存在，则找默认资源名
//     * @param defType
//     * @return
//     */
//    public static int getSpecResId( Context context, String specResName, String defResName, int defType )
//    {
//    	int specId = context.getResources().getIdentifier( specResName, RES_DEF_TYPE_NAME[defType], context.getPackageName());
//    	if( 0 != specId )
//    		return specId;
//    	// 如果指定资源名找不到，则找默认资源名
//    	return context.getResources().getIdentifier( defResName, RES_DEF_TYPE_NAME[defType], context.getPackageName());
//    }
    
    /**
     * 传入两个上下文，则优先查找outerContext中的对应资源，如果resid等于零，则使用默认defaultContext
     * @param outerContext
     * @param defaultContext
     * @param name
     * @param defType
     * @return
     */
    public static ContextResult getOuterResId( Context outerContext,Context defaultContext,  String name, String defType )
    {
    	int resId = getResId( outerContext, name, defType );
    	if ( 0 != resId ) 
    	{	
    		return new ContextResult(  outerContext, resId);
    	}
    	return new ContextResult( 
    			defaultContext,
    			getResId(  defaultContext, name, defType ) );
    }
    static public class ContextResult
    {
		int resid;
    	Context context;
    	public ContextResult( Context c, int id)
    	{
    		context = c;
    		resid = id;
    	}
    	public int getResid() {
			return resid;
		}
		public Context getContext() {
			return context;
		}
    }
	static public Drawable getDrawableOf(View context, int id )
	{
		return context.getResources().getDrawable( id );
	}
	static public Drawable getDrawableOf(Context context, int id )
	{
		return context.getResources().getDrawable( id );
	}
	static public Drawable getDrawableOf( Context context, String fileName )
	{
		return getDrawableOf( context, getResId(context, fileName, RES_DEF_TYPE_NAME[Res.DRAWABLE]));
	}
	/**
	 * 取屏幕宽度
	 * @return
	 */
	static public int getScreenWidth( Context context )
	{
		Resources r = context.getResources();
        DisplayMetrics dm = r.getDisplayMetrics();
        if( Configuration.ORIENTATION_PORTRAIT == r.getConfiguration().orientation  )
        {// 竖屏，h>w
        	return dm.heightPixels > dm.widthPixels ?  dm.widthPixels : dm.heightPixels;
        }
        else if( Configuration.ORIENTATION_LANDSCAPE == r.getConfiguration().orientation  )
        {// 横屏，w>h
        	return dm.widthPixels > dm.heightPixels ?  dm.widthPixels : dm.heightPixels;
        }
        return dm.widthPixels;
	}
	/**
	 * 取屏幕宽度
	 * @return
	 */
	static public int getScreenWidth( Context context, int targetOrientation )
	{
		Resources r = context.getResources();
		Configuration bakConfiguration = new Configuration(r.getConfiguration());
		Configuration tmpConfiguration = new Configuration(r.getConfiguration());
		tmpConfiguration.orientation = targetOrientation;
		r.updateConfiguration( tmpConfiguration, r.getDisplayMetrics() );
		
		int w = getScreenWidth(context);
		
		r.updateConfiguration( bakConfiguration, r.getDisplayMetrics() );
        return w;
	}
	/**
	 * 取屏幕高度
	 * @return
	 */
	static public int getScreenHeight( Context context )
	{
		Resources r = context.getResources();
        DisplayMetrics dm = r.getDisplayMetrics();
        if( Configuration.ORIENTATION_PORTRAIT == r.getConfiguration().orientation  )
        {// 竖屏，h>w
        	return dm.heightPixels > dm.widthPixels ?  dm.heightPixels : dm.widthPixels;
        }
        else if( Configuration.ORIENTATION_LANDSCAPE == r.getConfiguration().orientation  )
        {// 横屏，w>h
        	return dm.widthPixels > dm.heightPixels ?  dm.heightPixels : dm.widthPixels;
        }
        return dm.heightPixels;
	}
	static public Drawable drawable_getPartOf(Drawable src, int x, int y, int width, int height )
	{
		return drawable_getPartOf(src, src.getIntrinsicWidth(), src.getIntrinsicHeight(), x, y, width, height);
	}
	static public Drawable drawable_getPartOf(Drawable src, int specialWholeBoundWidth,int specialWholeBoundHeight, int x, int y, int width, int height )
	{
		src.setBounds(0, 0, specialWholeBoundWidth, specialWholeBoundHeight);
		Bitmap.Config config = src.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
				: Bitmap.Config.RGB_565; // 取drawable的颜色格式
		Bitmap bitmap = Bitmap.createBitmap(width, height, config); // 建立对应bitmap
		Canvas canvas = new Canvas(bitmap); // 建立对应bitmap的画布
		canvas.translate(-x, -y);
		src.draw(canvas);
		return new BitmapDrawable(bitmap);
	}
	/**
	 * 绘制指定位置的drawable
	 * @param canvas
	 * @param drawable
	 * @param x
	 * @param y
	 * @param boundsWidth
	 * @param boundsHeight
	 */
	static public void drawXYDrawable( Canvas canvas, Drawable drawable, int x, int y, int boundsWidth, int boundsHeight )
	{
		drawable.setBounds(0, 0, boundsWidth+1, boundsHeight+1);
		canvas.translate(x, y);
		drawable.draw(canvas);
		canvas.translate(-x, -y);
	}
	/**
	 * 删除子view的父view引用
	 * @param childView
	 */
	static public void removeViewParent( View childView)
	{
		if( null == childView )
			return;
		ViewParent p = childView.getParent();
        if (p != null && p instanceof ViewGroup) {
            ((ViewGroup)childView.getParent()).removeView(childView);
        }
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
	 * 缩放图像
	 * @param drawable
	 * @param widthScale
	 * @param heightScale
	 * @return
	 */
	public static Drawable zoomDrawable(Drawable drawable, float widthScale, float heightScale)
    {
		  int width = drawable.getIntrinsicWidth();
		  int height= drawable.getIntrinsicHeight();
		  Bitmap oldbmp = ((BitmapDrawable)drawable).getBitmap(); // drawable 转换成 bitmap
		  Matrix matrix = new Matrix();   // 创建操作图片用的 Matrix 对象
		  matrix.postScale(widthScale, heightScale);         // 设置缩放比例
		  Bitmap newbmp = Bitmap.createBitmap(oldbmp, 0, 0, (int)(width*widthScale), (int)(height*heightScale), matrix, true);       // 建立新的 bitmap ，其内容是对原 bitmap 的缩放后的图
		  return new BitmapDrawable(newbmp);       // 把 bitmap 转换成 drawable 并返回
    }
	public static String getStringSetting( Context c, String key, int defValId )
    {
    	SharedPreferences sp = PreferenceManager
		.getDefaultSharedPreferences(c);
    	return sp.getString(key, c.getResources().getString(defValId));
    }
    public static int getRadioSetting( Context c, String key, int arrayId, int defValId )
    {
    	Resources r = c.getResources();
    	String[] array = r.getStringArray(arrayId);
    	String selectVal = getStringSetting(c, key, defValId  );
    	for ( int i = 0; i <array.length; i++)
    	{
    		if( array[i].equals(selectVal) )
    		{
    			return i;
    		}
    	}
    	return -1;
    }
    public static boolean getBoolSetting( Context c, String key, int defValId )
    {
    	SharedPreferences sp = PreferenceManager
		.getDefaultSharedPreferences(c);
    	return sp.getBoolean(key,	c.getResources().getBoolean(defValId));
    }
    public static int getIntegerSetting( Context c, String key, int defValId )
    {
    	SharedPreferences sp = PreferenceManager
		.getDefaultSharedPreferences(c);
    	return sp.getInt(key,	c.getResources().getInteger(defValId));
    }
    public static int getIntegerSetting( Context c, String key, Integer defVal )
    {
    	SharedPreferences sp = PreferenceManager
		.getDefaultSharedPreferences(c);
    	return sp.getInt(key,	defVal);
    }
	static public void drawCenterText( Canvas canvas, Paint paint, String text, float center_x, float center_y)
	{
		FontMetrics fm = paint.getFontMetrics();
		float tcenx =  paint.measureText(text)/2;
		float tceny = fm.ascent / 2.0f;
		canvas.drawText(text, (float)center_x-tcenx, (float)center_y-tceny, paint);
	}
	/**
	 * 从资源路径中取得图片文件名
	 * @param nude
	 * @return
	 */
	static public String getDrawableFileNameForPathStr( String nude)
	{
		if( null == nude )
			return nude;
		nude = nude.substring(nude.lastIndexOf('/')+1, nude.length());
    	if( nude.contains(".") )
    		nude = nude.substring(0, nude.lastIndexOf('.'));
    	return nude;
	}
	/**
	 * 适应大小
	 * @param srcTextSize
	 * @param maxWidth
	 * @param text
	 * @return
	 */
	public static int adjustTextSize( Paint paint, int srcTextSize, int maxWidth, String text )
	{
		if( maxWidth <= 0 )
			return srcTextSize;
		paint.setTextSize(srcTextSize);
		while( paint.measureText(text) >= maxWidth )
		{
			srcTextSize --;
			paint.setTextSize(srcTextSize);
		}
		return srcTextSize;
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
}
