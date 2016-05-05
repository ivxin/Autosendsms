package com.mysoft.autosendsms;

import java.util.ArrayList;

import com.mysoft.adapter.MyPagerAdapter;
import com.mysoft.fragment.SMSRecordFragment;
import com.mysoft.utils.Constant;
import com.mysoft.view.MyViewPager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;
import android.widget.RelativeLayout;

@SuppressLint("NewApi")
public class MainActivity extends BaseActivity implements OnClickListener, OnFocusChangeListener, OnPageChangeListener {

	private static final int CONTACTOR = 0;
	private static final int REQUEST_CODE_ASK_PERMISSIONS = 0;
	private EditText et_receive_from;
	private EditText et_rex;
	private EditText et_target;
	private View btn_stop;
	private View btn_start;
	private RelativeLayout rl_records;
	private RelativeLayout rl_rexs;
	// private LinearLayout ll_btns;
	private SharedPreferences sp;

	private long lastPressed = 0;
	private SMSSavedReceiver receiver = new SMSSavedReceiver();
	private MyViewPager viewPager;
	private SMSRecordFragment fgAll;
	private SMSRecordFragment fgTransed;
	private int page = 0;
	private boolean hasPermission = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		if (Build.VERSION.SDK_INT >= 23) {
			requestPermissions(new String[] { Manifest.permission.READ_CONTACTS }, REQUEST_CODE_ASK_PERMISSIONS);
		}
		initView();
		initDataState();
	}

	/**
	 * 初始化数据和状态
	 */
	@SuppressWarnings("deprecation")
	private void initDataState() {
		sp = getSharedPreferences(Constant.SP_FILE_NAME, MODE_PRIVATE);

		et_receive_from.setText(sp.getString(Constant.NUM_REX_KEY, ""));
		et_target.setText(sp.getString(Constant.TARGET_KEY, ""));
		et_rex.setText(sp.getString(Constant.REX_KEY, ""));

		Constant.started = sp.getBoolean(Constant.STARTED_KEY, false);
		if (Constant.started) {
			startedUI();
		} else {
			stopedUI();
		}

		ArrayList<SMSRecordFragment> list = new ArrayList<SMSRecordFragment>();
		fgAll = new SMSRecordFragment();
		fgTransed = new SMSRecordFragment();

		fgAll.setParams(this, SMSRecordFragment.ALL);
		fgTransed.setParams(this, SMSRecordFragment.TRANSED);

		list.add(fgAll);
		list.add(fgTransed);

		MyPagerAdapter adapter = new MyPagerAdapter(getSupportFragmentManager(), list);
		viewPager.setAdapter(adapter);

		viewPager.setOnPageChangeListener(this);

		et_receive_from.setOnFocusChangeListener(this);
		et_target.setOnFocusChangeListener(this);
		et_rex.setOnFocusChangeListener(this);

		btn_start.setOnClickListener(this);
		btn_stop.setOnClickListener(this);
		et_target.setOnClickListener(this);
	}

	/**
	 * 初始化控件
	 */
	private void initView() {
		rl_rexs = (RelativeLayout) findViewById(R.id.rl_rexs);
		et_receive_from = (EditText) findViewById(R.id.et_received_from);
		et_target = (EditText) findViewById(R.id.et_target_num);
		et_rex = (EditText) findViewById(R.id.et_rexstring);
		
		rl_records = (RelativeLayout) findViewById(R.id.rl_records);
		// ll_btns = (LinearLayout) findViewById(R.id.ll_btns);
		btn_stop = findViewById(R.id.btn_stop);
		btn_start = findViewById(R.id.btn_start);

		viewPager = (MyViewPager) findViewById(R.id.viewPager);
		findViewById(R.id.view_up).setOnClickListener(this);
		findViewById(R.id.v_contact_chooser).setOnClickListener(this);
	}

	public void saveDataToSP() {
		Editor editor = sp.edit();
		editor.putString(Constant.NUM_REX_KEY, et_receive_from.getText().toString().trim());
		editor.putString(Constant.TARGET_KEY, et_target.getText().toString().trim());
		editor.putString(Constant.REX_KEY, et_rex.getText().toString().trim());
		editor.putBoolean(Constant.STARTED_KEY, Constant.started);
		editor.commit();
	}

	public void refresh() {
		if (fgAll != null)
			fgAll.refresh();
		if (fgTransed != null)
			fgTransed.refresh();
	}

	private void stopedUI() {
		et_receive_from.setEnabled(true);
		et_target.setEnabled(true);
		et_rex.setEnabled(true);
		et_receive_from.setHint(R.string.et_receive_hint);
		et_rex.setHint(R.string.et_rex_hint);

		btn_start.setEnabled(true);
		btn_stop.setEnabled(false);

		rl_rexs.setVisibility(View.VISIBLE);
	}

	private void startedUI() {
		et_receive_from.setEnabled(false);
		et_target.setEnabled(false);
		et_rex.setEnabled(false);

		et_receive_from.clearFocus();
		et_target.clearFocus();
		et_rex.clearFocus();

		et_receive_from.setHint(R.string.from_no_filter);
		et_rex.setHint(R.string.rex_no_filter);

		et_receive_from.setTextSize(12f);
		et_target.setTextSize(12f);
		et_rex.setTextSize(12f);

		btn_start.setEnabled(false);
		btn_stop.setEnabled(true);

		rl_rexs.setVisibility(View.GONE);
	}

	@Override
	public void onClick(View v) {
		refresh();
		switch (v.getId()) {
		case R.id.btn_start:
			if (et_target.getText().toString().trim().equals("")) {
				shortToast("输入目标号码");
				break;
			}
			showTextDialog("启动成功,退出不影响转发", buildTip());
			Constant.started = true;
			saveDataToSP();
			startedUI();
			break;
		case R.id.btn_stop:
			if (Constant.started) {
				Constant.started = false;
				saveDataToSP();
				stopedUI();
				shortToast("停止成功,不再转发短信");
			}
			break;
		case R.id.view_up:
			switch (page) {
			case 0:
				fgAll.setTop();
				break;
			case 1:
				fgTransed.setTop();
				break;
			default:
				break;
			}
			break;
		case R.id.v_contact_chooser:
			startActivityForResult(new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI), CONTACTOR);
			break;
		default:
			break;
		}
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		switch (requestCode) {
		case REQUEST_CODE_ASK_PERMISSIONS:
			if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
				// Permission Granted
				hasPermission = true;
			} else {
				// Permission Denied
				hasPermission = false;
				shortToast("READ_CONTACTS Denied");
			}
			break;
		default:
			super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case CONTACTOR:
				if (hasPermission) {
					String username = "";
					String usernumber = "";
					ContentResolver reContentResolverol = getContentResolver();
					Uri contactData = data.getData();
					@SuppressWarnings("deprecation")
					Cursor cursor = managedQuery(contactData, null, null, null, null);
					cursor.moveToFirst();
					username = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
					String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
					Cursor phone = reContentResolverol.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
							ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);
					while (phone.moveToNext()) {
						usernumber = phone
								.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
					}
					et_target.setText(usernumber + " (" + username + ")");
				}
				break;
			default:
				break;
			}
		}
	}

	private String buildTip() {
		String tip = "";
		String from = et_receive_from.getText().toString().trim();
		String keyword = et_rex.getText().toString().trim();
		String target = et_target.getText().toString().trim();
		if (TextUtils.isEmpty(from)) {
			from = "所有";
		} else {
			from = "[" + from.replaceAll(";", "]或[") + "]";
		}
		if (TextUtils.isEmpty(keyword)) {
			keyword = "任何";
		} else {
			keyword = "[" + keyword.replaceAll(";", "]或[") + "]";
		}
		tip = "将转发来自包含" + from + "的号码,并且包含" + keyword + "的字符的信息到[" + target + "]";
		return tip;
	}

	@Override
	public void onBackPressed() {
		if (System.currentTimeMillis() - lastPressed > 800) {
			lastPressed = System.currentTimeMillis();
			shortToast("再按一次退出");
		} else {
			if (Constant.started) {
				longToast("服务已启动,后台自动转发");
			}
			super.onBackPressed();
		}
	}

	@Override
	protected void onDestroy() {
		if (Constant.started) {
			saveDataToSP();
		}
		super.onDestroy();
	}

	@Override
	protected void onResume() {
		refresh();
		super.onResume();
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		if (v instanceof EditText) {
			EditText et = (EditText) v;

			switch (v.getId()) {
			case R.id.et_received_from:
			case R.id.et_rexstring:
			case R.id.et_target_num:
				if (!Constant.started) {
					if (hasFocus) {
						et.setTextSize(18f);
					} else {
						et.setTextSize(12f);
					}
				}
				break;
			default:
				break;
			}
		}
	}

	@Override
	protected void onStart() {
		IntentFilter filter = new IntentFilter(Constant.ACTION);
		registerReceiver(receiver, filter);
		super.onStart();
	}

	@Override
	protected void onStop() {
		unregisterReceiver(receiver);
		super.onStop();
	}

	class SMSSavedReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			refresh();
		}
	}

	/**
	 * position :当前页面，及你点击滑动的页面 offset:当前页面偏移的百分比 offsetPixels:当前页面偏移的像素位置
	 */
	@Override
	public void onPageScrolled(int position, float offset, int offsetPixels) {
		// 在0和1之间滑动
		if (position <= 1) {
			// 滑动过程中
			if (offset != 0) {
				fgAll.setSlidingAlpha(1 - offset);
				fgTransed.setSlidingAlpha(offset);
				rl_records.setAlpha((float) (2 * Math.abs(offset - 0.5)));
				if (offset > 0.5) {
					rl_records.setBackgroundColor(fgTransed.getColor());
				} else if (offset < 0.5) {
					rl_records.setBackgroundColor(fgAll.getColor());
				} else {
					rl_records.setBackgroundColor(Color.TRANSPARENT);
				}
			}
			// 滑动结束
			if (position == 1 && offset == 0) {// 结束停在1
				fgTransed.setSlidingAlpha(1);
				rl_records.setBackgroundColor(fgTransed.getColor());
			} else if (position == 0 && offset == 0) {// 结束停在0
				fgAll.setSlidingAlpha(1);
				rl_records.setBackgroundColor(fgAll.getColor());
			}
		}

	}

	/**
	 * state滑动中的状态 有三种状态（0，1，2） 1：正在滑动 2：滑动完毕 0：什么都没做。
	 */
	@Override
	public void onPageScrollStateChanged(int state) {

	}

	@Override
	public void onPageSelected(int position) {
		page = position;
		switch (position) {
		case 0:
			rl_records.setBackgroundColor(fgAll.getColor());
			fgAll.startAnime();
			break;
		case 1:
			rl_records.setBackgroundColor(fgTransed.getColor());
			fgTransed.startAnime();
			break;
		default:
			break;
		}
	}

	public void setUpVis(boolean vis) {
		findViewById(R.id.view_up).setVisibility(vis ? View.VISIBLE : View.GONE);
	}
}
