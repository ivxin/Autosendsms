package com.mysoft.adapter;

import java.util.List;

import com.mysoft.autosendsms.R;
import com.mysoft.entity.SMS;
import com.mysoft.utils.Constant;
import com.mysoft.utils.StringUtils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

@SuppressLint("InflateParams")
public class SMSAdapter extends BaseAdapter {
	List<SMS> list;
	LayoutInflater inflater;
	Context context;
	ViewHolder holder;
	private Typeface face;

	public SMSAdapter(List<SMS> list, Context context) {
		super();
		face = StringUtils.getTypeface(context);
		this.list = list;
		this.context=context;
		inflater = LayoutInflater.from(context);
	}

	public void setList(List<SMS> list) {
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		holder = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.item_sms_layout, null);
			holder = new ViewHolder();
			holder.tv_num_from = (TextView) convertView.findViewById(R.id.tv_num_from);
			holder.tv_sms_time = (TextView) convertView.findViewById(R.id.tv_sms_time);
			holder.tv_sms_content = (TextView) convertView.findViewById(R.id.tv_sms_content);
			holder.tv_num_from.setTypeface(face);
			holder.tv_sms_time.setTypeface(face);
			holder.tv_sms_content.setTypeface(face);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		SMS sms = list.get(position);
		if (sms.isSended()) {
			holder.tv_num_from.setText(sms.getAddress() + "-->" + sms.getTarget());
		} else {
			holder.tv_num_from.setText(sms.getAddress());
		}
		holder.tv_sms_time.setText(StringUtils.getDateFomated(Constant.PATTERN, sms.getDate_time()+""));
		holder.tv_sms_content.setText(sms.getContent());
		return convertView;
	}

	class ViewHolder {
		TextView tv_num_from, tv_sms_time, tv_sms_content;
	}

}
