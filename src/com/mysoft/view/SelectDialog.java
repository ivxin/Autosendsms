package com.mysoft.view;

import com.mysoft.autosendsms.R;
import com.mysoft.entity.Contactor;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SelectDialog extends Dialog {
	private Context context;
	private TextView tv_name;
	private LinearLayout ll_numbers;
	private LinearLayout.LayoutParams layoutParams;

	public SelectDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
		initView(context);
	}

	public SelectDialog(Context context, int themeResId) {
		super(context, themeResId);
		initView(context);
	}

	public SelectDialog(Context context) {
		super(context);
		initView(context);
	}

	private void initView(Context context) {
		this.context = context;
		layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		layoutParams.bottomMargin = 20;
		View view = View.inflate(context, R.layout.layout_select_dialog, null);
		tv_name = (TextView) view.findViewById(R.id.tv_name);
		ll_numbers = (LinearLayout) view.findViewById(R.id.ll_numbers);
		setContentView(view);
	}

	public SelectDialog setData(Contactor contactor) {
		tv_name.setText("选择号码:" + contactor.getName());
		ll_numbers.removeAllViews();
		for (String number : contactor.getNumberList()) {
			TextView tv_number = new TextView(context);
			tv_number.setTextColor(Color.BLACK);
			tv_number.setBackgroundResource(R.drawable.item_button_background);
			tv_number.setTextSize(16f);
			tv_number.setText(number);
			tv_number.setTag(contactor);
			tv_number.setGravity(Gravity.CENTER);
			tv_number.setPadding(0, 20, 0, 20);
			tv_number.setOnClickListener(new MyOnClickListener());
			ll_numbers.addView(tv_number, layoutParams);
		}
		return this;
	}

	public interface OnNumberClickListener {
		void onNumberClick(String number, Contactor contactor);
	}

	private OnNumberClickListener onNumberClickListener;

	public void setOnNumberClickListener(OnNumberClickListener onNumberClickListener) {
		this.onNumberClickListener = onNumberClickListener;
	}

	class MyOnClickListener implements android.view.View.OnClickListener {

		@Override
		public void onClick(View v) {
			if (v instanceof TextView) {
				TextView tv = (TextView) v;
				String number = tv.getText().toString();
				Contactor contactor = (Contactor) tv.getTag();
				if (onNumberClickListener != null) {
					onNumberClickListener.onNumberClick(number, contactor);
				}
			}

		}

	}

}
