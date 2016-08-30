package com.ivxin.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class MyViewPager extends ViewPager {

	public MyViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MyViewPager(Context context) {
		super(context);
	}

//	@Override
//	protected boolean canScroll(View v, boolean checkV, int dx, int x, int y) {
//		if (v != this && v instanceof ViewPager) {
//			return true;
//		}
//		return super.canScroll(v, checkV, dx, x, y);
//	}
	
//    @Override  
//    public boolean dispatchTouchEvent(MotionEvent ev) {  
//            requestDisallowInterceptTouchEvent(true);  
//            return super.dispatchTouchEvent(ev);  
//    }  
//
//    @Override  
//    public boolean onInterceptTouchEvent(MotionEvent ev) {  
//            requestDisallowInterceptTouchEvent(true);  
//            return super.onInterceptTouchEvent(ev);  
//    }  
//
//    @Override  
//    public boolean onTouchEvent(MotionEvent event) {  
//            requestDisallowInterceptTouchEvent(true);  
//            return super.onTouchEvent(event);  
//    }  

}
