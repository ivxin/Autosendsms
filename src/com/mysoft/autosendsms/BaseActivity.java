package com.mysoft.autosendsms;

import android.support.v4.app.FragmentActivity;
import android.widget.Toast;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class BaseActivity extends FragmentActivity {

	protected void shortToast(String string) {
		Toast.makeText(this, string, Toast.LENGTH_SHORT).show();
	}

	public void shortToast(int resId) {
		Toast.makeText(this, resId, Toast.LENGTH_SHORT).show();
	}

	protected void longToast(String string) {
		Toast.makeText(this, string, Toast.LENGTH_LONG).show();
	}

	protected void longToast(int resId) {
		Toast.makeText(this, resId, Toast.LENGTH_LONG).show();
	}

	public void showTextDialog(String title,String text) {
		SweetAlertDialog sd = new SweetAlertDialog(this,SweetAlertDialog.SUCCESS_TYPE);
		sd.setTitleText(title);
		sd.setContentText(text);
		sd.setCancelable(true);
		sd.setCanceledOnTouchOutside(true);
		sd.show();
	}
}
