package com.mysoft.view;

import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class SelectDialog extends Dialog {
	private Context context;
	private ListView lv_phone_numbers;

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
		lv_phone_numbers = new ListView(context);
		lv_phone_numbers.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		setContentView(lv_phone_numbers);
	}

	public void setOnItemClickListener(OnItemClickListener listener) {
		lv_phone_numbers.setOnItemClickListener(listener);
	}

	public void setData(List<String> data) {
		lv_phone_numbers
				.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, data));
	}

}
