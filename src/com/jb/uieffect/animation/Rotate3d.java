package com.jb.uieffect.animation;

import android.graphics.Camera;
import android.graphics.Matrix;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * 3D旋转类
 * @author denghuihan
 *
 */
public class Rotate3d extends Animation {

    private float mFromDegree_y;
    private float mToDegree_y;
    private float mFromDegree_x;
    private float mToDegree_x;
    private float mCenterX;
    private float mCenterY;
//    private float mLeft;
//    private float mTop;
    private Camera mCamera;
    private static final String TAG = "Rotate3d";
    
    public Rotate3d(float fromDegree_y, float toDegree_y,
    		float fromDegree_x, float toDegree_x)
    {
        this.mFromDegree_y = fromDegree_y;
        this.mToDegree_y = toDegree_y;
        this.mFromDegree_x = fromDegree_x;
        this.mToDegree_x = toDegree_x;
//        this.mLeft = left;
//        this.mTop = top;
//        this.mCenterX = centerX;
//        this.mCenterY = centerY;
        setFillAfter(false);
        setDuration(1000);
     // setInterpolator( new BounceInterpolator());
    }
    
    @Override
    public void initialize(int width, int height, int parentWidth,
            int parentHeight)
    {
    	super.initialize(width, height, parentWidth, parentHeight);
    	mCamera = new Camera();
    	mCenterX = width/2;
    	mCenterY = height/2;
    }
    
    static boolean equal_floats( float f1, float f2 )
    {
 	   if( Math.abs(f1 - f2) < 0.1f)
 		   return true;
 	   return false;
    }
    
    protected void applyTransformation(float interpolatedTime, Transformation t)
    {
		final float centerX = mCenterX;
		final float centerY = mCenterY;
	       final Matrix matrix = t.getMatrix();
		if( ! equal_floats(mToDegree_y ,  mFromDegree_y) )
		{
			Log.d(TAG,"mToDegree_y !=  mFromDegree_y");
		   // for 横向旋转
	       final float FromDegree_y = mFromDegree_y;
	       float degrees_y = FromDegree_y + (mToDegree_y - mFromDegree_y)
	       * interpolatedTime;
			
	
	       if (degrees_y <= -76.0f) {
	            degrees_y = -90.0f;
	            mCamera.save();
	            mCamera.rotateY(degrees_y);
	            mCamera.getMatrix(matrix);
	            mCamera.restore();
	       } else if(degrees_y >=76.0f){
	           degrees_y = 90.0f;
	           mCamera.save();
	           mCamera.rotateY(degrees_y);
	           mCamera.getMatrix(matrix);
	           mCamera.restore();
	       }else{
	           mCamera.save();
	                      //这里很重要哦。
	           mCamera.translate(0, 0, centerX);
	           mCamera.rotateY(degrees_y);
	           mCamera.translate(0, 0, -centerX);
	           mCamera.getMatrix(matrix);
	           mCamera.restore();
	       }
		}
       // for纵向旋转
       if( ! equal_floats(mToDegree_x , mFromDegree_x) )
       {
			Log.d(TAG,"mToDegree_x !=  mFromDegree_x");
		    final float FromDegree_x = mFromDegree_x;
			float degrees_x = FromDegree_x + (mToDegree_x - mFromDegree_x)
			* interpolatedTime;
	
	       if (degrees_x <= -76.0f) {
	    	   degrees_x = -90.0f;
	            mCamera.save();
	            mCamera.rotateX(degrees_x);
	            mCamera.getMatrix(matrix);
	            mCamera.restore();
	       } else if(degrees_x >=76.0f){
	    	   degrees_x = 90.0f;
	           mCamera.save();
	           mCamera.rotateX(degrees_x);
	           mCamera.getMatrix(matrix);
	           mCamera.restore();
	       }else{
	           mCamera.save();
	                      //这里很重要哦。
	           mCamera.translate(0, 0, centerY);
	           mCamera.rotateX(degrees_x);
	           mCamera.translate(0, 0, -centerY);
	           mCamera.getMatrix(matrix);
	           mCamera.restore();
	       }       
       }
       matrix.preTranslate(-centerX, -centerY);
       matrix.postTranslate(centerX, centerY);
    }
    
    public static Rotate3d getUpOutAnim()
    {
    	Rotate3d upOutAnimation = new Rotate3d(0, 0, 0, 90);
    	return upOutAnimation;
    }
    
    public static Rotate3d getUpInAnim()
    {
    	Rotate3d upInAnimation = new Rotate3d(0, 0, -90f, 0.0f);
    	return upInAnimation;
    }
    
    public static Rotate3d getDownOutAnim()
    {
    	Rotate3d downOutAnimation = new Rotate3d(0, 0, 0, -90);
    	return downOutAnimation;
    }
    
    public static Rotate3d getDownInAnim()
    {
    	Rotate3d downInAnimation = new Rotate3d(0, 0, 90.0f, 0.0f);
    	return downInAnimation;
    }
    
    public static Rotate3d getLeftOutAnim()
    {
 	      Rotate3d leftOutAnimation = new Rotate3d(0, -90, 0, 0);
 	      return leftOutAnimation;
    }
    public static Rotate3d getLeftInAnim()
    {
 	   	Rotate3d leftInAnimation = new Rotate3d(90, 0, 0.0f, 0.0f);
 	    return leftInAnimation;
    }
    public static Rotate3d getRightOutAnim()
    {
 	   Rotate3d rightOutAnimation = new Rotate3d(0, 90, 0, 0);
 	      return rightOutAnimation;
    }
    public static Rotate3d getRightInAnim()
    {
 	   Rotate3d rightInAnimation = new Rotate3d(-90, 0, 0.0f, 0.0f);
 	   return rightInAnimation;
    }
    
   
}
