package com.mysoft.autosendsms;

import com.mysoft.utils.StringUtils;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;
import cn.pedant.SweetAlert.SweetAlertDialog;
import net.simonvt.messagebar.MessageBar;
import net.simonvt.messagebar.MessageBar.OnMessageClickListener;

public class BaseActivity extends FragmentActivity {
	private static final String STATE_MESSAGEBAR = "net.simonvt.messagebar";
	private MessageBar mb;
	public Typeface face;
	private Handler handler;

	/**
	 * 重写后使用 系统默认字体大小 防止被用户修改系统字体大小被影响到
	 */
	@Override
	public Resources getResources() {
		Resources res = super.getResources();
		Configuration config = new Configuration();
		config.setToDefaults();
		res.updateConfiguration(config, res.getDisplayMetrics());
		return res;
	}

	@Override
	protected void onCreate(Bundle arg0) {
		face = StringUtils.getTypeface(this);
		super.onCreate(arg0);
	}
	
	protected void onPostDelayed(Runnable run,long delayed){
		if(handler==null)handler=new Handler();
		handler.postDelayed(run, delayed);
	}

	protected void shortToast(String string) {
		toast(string, 2000);
	}

	protected void longToast(String string) {
		toast(string, 3500);
	}

	protected void toast(String string, long duration) {
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M || Settings.canDrawOverlays(this)) {
			toast(string, null, duration, null);
		} else {
			Toast.makeText(this, string, Toast.LENGTH_SHORT).show();
		}
	}

	protected void toast(String string, String btn_msg, long duration, OnMessageClickListener listener) {
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M || Settings.canDrawOverlays(this)) {
			if (mb == null) {
				mb = new MessageBar(this);
			}
			mb.setOnClickListener(listener);
			mb.setDuration(duration);
			mb.show(string, btn_msg);
		} else {
			Toast.makeText(this, string, Toast.LENGTH_SHORT).show();
		}
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
		if (inState != null && mb != null && inState.getBundle(STATE_MESSAGEBAR) != null)
			mb.onRestoreInstanceState(inState.getBundle(STATE_MESSAGEBAR));
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if (outState == null)
			outState = new Bundle();
		if (mb != null)
			outState.putBundle(STATE_MESSAGEBAR, mb.onSaveInstanceState());
	}
}
