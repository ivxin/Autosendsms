package com.mysoft.view;

import com.mysoft.autosendsms.R;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class TitleView extends RelativeLayout{
	private Context mContext;
	public TextView tv_left, tv_center, tv_right;

	public TitleView(Context context) {
		super(context);
		init(context);
	}

	public TitleView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public TitleView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context);
	}

	public TitleView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		init(context);
	}

	private void init(Context context) {
		mContext = context;
		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.layout_title, this, true);
		tv_left = (TextView) findViewById(R.id.tv_left);
		tv_center = (TextView) findViewById(R.id.tv_center);
		tv_right = (TextView) findViewById(R.id.tv_right);

		tv_left.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mContext instanceof Activity) {
					((Activity) mContext).onBackPressed();
				}
			}
		});

	}

}
