package com.ivxin.autosendsms;

import android.content.Intent;
import android.os.Bundle;

public class InitActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_init);
		finish();
		startActivity(new Intent(InitActivity.this,MainActivity.class));
		overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
		
	}
}
