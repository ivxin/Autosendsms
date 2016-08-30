package com.mysoft.fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.mysoft.autosendsms.MainActivity;
import com.mysoft.autosendsms.R;
import com.mysoft.db.DBserver;
import com.mysoft.entity.SMS;
import com.mysoft.utils.Constant;
import com.mysoft.utils.StringUtils;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import cn.pedant.SweetAlert.SweetAlertDialog;
import tyrantgit.explosionfield.ExplosionField;

@SuppressLint("InflateParams")
public class SMSRecordFragment extends BaseFragment implements OnItemClickListener, OnItemLongClickListener {
	public static final int ALL = 1;
	public static final int TRANSED = 2;

	private MainActivity context;
	private ListView lv_sms_records;
	private TextView tv_fg_name;
	private List<SMS> list;
	private SMSAdapter adapter;
	private int flag;
	private DBserver dbs;
	private int color;
	private View view;
	private Typeface face;
//	private ExplosionField mExplosionField;

	public void setParams(MainActivity context, int flag) {
		this.context = context;
		this.flag = flag;
		dbs = new DBserver(context);
	}

	@Override
	public void onAttach(Context context) {
		if (context instanceof MainActivity) {
			this.context = (MainActivity) context;
		}
		super.onAttach(context);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//		mExplosionField = ExplosionField.attach2Window(context);
		dbs = new DBserver(context);
		face = StringUtils.getTypeface(context);
		view = inflater.inflate(R.layout.fragment_sms_list, null);
		lv_sms_records = (ListView) view.findViewById(R.id.lv_sms_records);
		tv_fg_name = (TextView) view.findViewById(R.id.tv_fg_name);
		tv_fg_name.setTypeface(face);
		tv_fg_name.setText(flag == TRANSED ? "已经转发的短信" : "收到的所有短信");
		list = new ArrayList<SMS>();
		adapter = new SMSAdapter(list, context);
		lv_sms_records.setAdapter(adapter);

		refresh();
		startAnime();

		lv_sms_records.setOnItemClickListener(this);
		lv_sms_records.setOnItemLongClickListener(this);
		getRandomColor();
		view.setBackgroundColor(color);
		return view;
	}

//	@Override// XXX 跟ViewPager冲突,也不需要用
//	public void onViewCreated(View view, Bundle savedInstanceState) {
//		// step 1. create a MenuCreator
//		SwipeMenuCreator creator = new SwipeMenuCreator() {
//
//			@Override
//			public void create(SwipeMenu menu) {
//				// create "open" item
//				SwipeMenuItem openItem = new SwipeMenuItem(context);
//				// set item background
//				openItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9, 0xCE)));
//				// set item width
//				openItem.setWidth(dp2px(90));
//				// set item title
//				openItem.setTitle("Open");
//				// set item title fontsize
//				openItem.setTitleSize(18);
//				// set item title font color
//				openItem.setTitleColor(Color.WHITE);
//				// add to menu
//				menu.addMenuItem(openItem);
//
//				// create "delete" item
//				SwipeMenuItem deleteItem = new SwipeMenuItem(context);
//				// set item background
//				deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9, 0x3F, 0x25)));
//				// set item width
//				deleteItem.setWidth(dp2px(90));
//				// set a icon
//				deleteItem.setIcon(R.drawable.ic_delete);
//				// add to menu
//				menu.addMenuItem(deleteItem);
//			}
//		};
//
//		// set creator
//		lv_sms_records.setMenuCreator(creator);
//
//		// step 2. listener item click event
//		lv_sms_records.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
//			@Override
//			public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
//				SMS item = list.get(position);
//				switch (index) {
//				case 0:
//					// open
//					showSMSDetailDialog(item);
//					break;
//				case 1:
//					// delete
//					showDeleteSMSDialog(item);
//					
//					break;
//				}
//				return false;
//			}
//		});
//		super.onViewCreated(view, savedInstanceState);
//	}

	public void setSlidingAlpha(float alpha) {
		view.setAlpha(alpha);
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		if (view != null)
			view.setBackgroundColor(color);
	}

	private void getRandomColor() {
		Random random = new Random();
		int red = random.nextInt(128 + 50) + 128 - 50;
		int green = random.nextInt(128 + 50) + 128 - 50;
		int blue = random.nextInt(128 + 50) + 128 - 50;
		color = Color.rgb(red, green, blue);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
		SMS sms = list.get(position);
		showSMSDetailDialog(sms);
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, final View view, final int position, long id) {
		SMS item = list.get(position);
		showDeleteSMSDialog(item);
		return true;
	}

	private void showSMSDetailDialog(final SMS sms) {
		final View v = LayoutInflater.from(getContext()).inflate(R.layout.layout_sms_detail, null);
		// final LinearLayout ll_detail = (LinearLayout)
		// v.findViewById(R.id.ll_detail);
		final TextView tv_sms_sender_detail = (TextView) v.findViewById(R.id.tv_sms_sender_detail);
		final TextView tv_sms_time_detail = (TextView) v.findViewById(R.id.tv_sms_time_detail);
		final TextView tv_sms_content_detail = (TextView) v.findViewById(R.id.tv_sms_content_detail);
		tv_sms_sender_detail.setTypeface(face);
		tv_sms_time_detail.setTypeface(face);
		tv_sms_content_detail.setTypeface(face);
		if (sms.isSended()) {
			tv_sms_sender_detail.setText(sms.getAddress() + "-->" + sms.getTarget());
		} else {
			tv_sms_sender_detail.setText("来自:" + sms.getAddress());
		}
		tv_sms_time_detail.setText(StringUtils.getDateFomated(Constant.PATTERN, sms.getDate_time() + ""));
		tv_sms_content_detail.setText(sms.getContent());
		final AlertDialog dialog = new Builder(context).create();
		dialog.show();
		Window window = dialog.getWindow();
		window.setContentView(v);
	}

	private void showDeleteSMSDialog(final SMS item) {
		new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE).setTitleText("删除这条记录?")
				.setContentText("确认删除?删除后无法恢复!").setConfirmText("确定,删除吧!")
				.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
					@Override
					public void onClick(final SweetAlertDialog sDialog) {
						dbs.deleteSMSbyID(item);
						list.remove(item);
						adapter.notifyDataSetChanged();
						sDialog.dismiss();
//						new Handler().postDelayed(new Runnable() {//XXX 在ListView上用会错位,先不用了
//							@Override
//							public void run() {
//								mExplosionField.explode(view, new AnimatorListener() {
//									@Override
//									public void onAnimationStart(Animator animation) {
//
//									}
//
//									@Override
//									public void onAnimationRepeat(Animator animation) {
//									}
//
//									@Override
//									public void onAnimationEnd(Animator animation) {
//										new Handler().postDelayed(new Runnable() {
//											@Override
//											public void run() {
//												view.setScaleX(1.0f);
//												view.setScaleY(1.0f);
//												view.setAlpha(1.0f);
//												refresh();
//											}
//										}, 100);
//									}
//
//									@Override
//									public void onAnimationCancel(Animator animation) {
//									}
//								});
//							}
//						}, 200);

					}
				}).show();
	}

	public void startAnime() {
		AlphaAnimation animation = new AlphaAnimation(1.0f, 0.0f);
		animation.setDuration(3000L);
		animation.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
				tv_fg_name.setVisibility(View.VISIBLE);
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				tv_fg_name.setVisibility(View.GONE);
			}
		});
		tv_fg_name.startAnimation(animation);
	}

	public void refresh() {
		if (dbs == null) {
			dbs = new DBserver(context);
		}
		switch (flag) {
		case ALL:
			list = dbs.selectAllSMS();
			break;
		case TRANSED:
			list = dbs.selectTransedSMS();
			break;
		default:
			break;
		}
		if (adapter != null) {
			adapter.setList(list);
			adapter.notifyDataSetChanged();
		}
	}

	@SuppressLint("InflateParams")
	public class SMSAdapter extends BaseAdapter {
		List<SMS> list;
		LayoutInflater inflater;
		Context context;
		ViewHolder holder;

		public SMSAdapter(List<SMS> list, Context context) {
			super();
			this.list = list;
			this.context = context;
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
				holder.ll_item_sms = (LinearLayout) convertView.findViewById(R.id.ll_item_sms);
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
//			holder.ll_item_sms.setBackgroundColor(position % 2 > 0 ? Color.WHITE : Color.GRAY);
			holder.tv_sms_time.setText(StringUtils.getDateFomated(Constant.PATTERN, sms.getDate_time() + ""));
			holder.tv_sms_content.setText(sms.getContent());
			SMSRecordFragment.this.context.setUpVis(position > 15);
			return convertView;
		}

		class ViewHolder {
			LinearLayout ll_item_sms;
			TextView tv_num_from, tv_sms_time, tv_sms_content;
		}

	}

	public void setTop() {
		lv_sms_records.smoothScrollToPosition(0);
	}

	private int dp2px(int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
	}
}
