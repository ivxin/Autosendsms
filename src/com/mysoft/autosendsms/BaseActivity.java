package com.mysoft.autosendsms;

import com.mysoft.utils.StringUtils;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import cn.pedant.SweetAlert.SweetAlertDialog;
import net.simonvt.messagebar.MessageBar;
import net.simonvt.messagebar.MessageBar.OnMessageClickListener;

public class BaseActivity extends FragmentActivity {
	private static final String STATE_MESSAGEBAR = "net.simonvt.messagebar";
	private MessageBar mb;
	public Typeface face;

	@Override
	protected void onCreate(Bundle arg0) {
		face = StringUtils.getTypeface(this);
		super.onCreate(arg0);
	}

	protected void shortToast(String string) {
		toast(string, 2000);
	}

	protected void longToast(String string) {
		toast(string, 3500);
	}

	protected void toast(String string, long duration) {
		if (mb == null) {
			mb = new MessageBar(this);
		}
		mb.setDuration(duration);
		mb.show(string);
	}

	protected void toast(String string,String btn_msg, long duration, OnMessageClickListener listener) {
		if (mb == null) {
			mb = new MessageBar(this);
		}
		mb.setOnClickListener(listener);
		mb.setDuration(duration);
		mb.show(string, btn_msg, R.drawable.ic_messagebar_undo);
	}

	public void showTextDialog(String title, String text) {
		SweetAlertDialog sd = new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE);
		sd.setTitleText(title);
		sd.setContentText(text);
		sd.setCancelable(true);
		sd.setCanceledOnTouchOutside(true);
		sd.show();
	}

	@Override
	protected void onRestoreInstanceState(Bundle inState) {
		super.onRestoreInstanceState(inState);
		mb.onRestoreInstanceState(inState.getBundle(STATE_MESSAGEBAR));
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putBundle(STATE_MESSAGEBAR, mb.onSaveInstanceState());
	}
}
