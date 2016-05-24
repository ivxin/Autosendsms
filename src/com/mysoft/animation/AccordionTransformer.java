package com.mysoft.animation;

import android.support.v4.view.ViewPager.PageTransformer;
import android.view.View;

public class AccordionTransformer implements PageTransformer {

	@Override
	public void transformPage(View view, float position) {
		if (position < -1) {
			view.setPivotX(view.getMeasuredWidth() * 0.5f);
			view.setPivotY(view.getMeasuredHeight() * 0.5f);
			view.setScaleX(1);
		} else if (position <= 0) {
			view.setPivotX(view.getMeasuredWidth());
			view.setPivotY(0);
			view.setScaleX(1 + position);
		} else if (position <= 1) {
			view.setPivotX(0);
			view.setPivotY(0);
			view.setScaleX(1 - position);
		} else {
			view.setPivotX(view.getMeasuredWidth() * 0.5f);
			view.setPivotY(view.getMeasuredHeight() * 0.5f);
			view.setScaleX(1);
		}
	}
}
