package com.mysoft.animation;

import android.support.v4.view.ViewPager.PageTransformer;
import android.view.View;

public class RotateTransformer implements PageTransformer {

	/**
	 * page当然值得就是滑动中德那个view，position这里是float，不是平时理解的int位置信息，而是当前滑动状态的一个表示，
	 * 比如当滑动到正全屏时
	 * ，position是0，而向左滑动，使得右边刚好有一部被进入屏幕时，position是1，如果前一夜和下一页基本各在屏幕占一半时
	 * ，前一页的position是-0.5，后一页的posiotn是0.5，所以根据position的值我们就可以自行设置需要的alpha，x/y信息。
	 */
	@Override
	public void transformPage(View view, float position) {
		if (position < -1) {
		} else if (position <= 0) {
			view.setScaleX(1 + position);
			view.setScaleY(1 + position);
			view.setRotation(360 * position);
		} else if (position <= 1) {
			view.setScaleX(1 - position);
			view.setScaleY(1 - position);
			view.setRotation(360 * position);
		} else {
		}
	}

}
