package com.mysoft.utils;

import java.io.IOException;
import java.util.ArrayList;

import com.mysoft.db.DBserver;
import com.mysoft.entity.SMS;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.widget.Toast;

public class SMSSendingHandler {
	private Context context;
	private SMS newSms;
	private SharedPreferences sp;
	private boolean smsSended = false;
	private static final int FINISHED = 3;
	private MyHandler handler = new MyHandler();
	private String numRex;
	private String rex;
	private String target;

	public SMSSendingHandler(Context context, SMS newSms) {
		this.context = context;
		this.newSms = newSms;
	}

	public void start() {
		sp = context.getSharedPreferences(Constant.SP_FILE_NAME, Context.MODE_PRIVATE);
		numRex = sp.getString(Constant.NUM_REX_KEY, "").trim();
		rex = sp.getString(Constant.REX_KEY, "").trim();
		target = sp.getString(Constant.TARGET_KEY, "").trim();
		if (!TextUtils.isEmpty(target)) {
			try {
				target = target.substring(0, target.indexOf("("));
				target = target.trim().replaceAll("\\s*", "");
			} catch (Exception e) {
				target = sp.getString(Constant.TARGET_KEY, "").trim();
			}
		}
		newSms.setTarget(target);
		if (sp.getBoolean(Constant.STARTED_KEY, false)) {
			new MyThread().start();
		}
	}

	/**
	 * 处理和发送消息
	 * 
	 */
	private void handleSMS() {
		if (newSms == null) {
			return;
		}
		String smsContent = newSms.getContent();
		String smsAddress = newSms.getAddress();
		boolean targetGood = false;
		boolean numRexGood = false;
		boolean rexGood = false;
		// 判断要发送目标的号码可用
		if (!TextUtils.isEmpty(target) && target.replaceAll("[0-9]", "").length() == 0) {
			targetGood = true;
		}
		// 判断符合收信号码规则
		String[] numRexs = numRex.split(";");
		if (numRexs.length == 0) {
			if (TextUtils.isEmpty(numRex) || smsAddress.contains(numRex)) {
				numRexGood = true;
			}
		} else {
			for (int i = 0; i < numRexs.length; i++) {
				if (smsAddress.contains(numRexs[i].trim())) {
					numRexGood = true;
				}
			}
		}
		// 判断符合内容过滤规则
		String[] rexs = rex.split(";");
		if (rexs.length == 0) {
			if (TextUtils.isEmpty(rex) || smsContent.contains(rex)) {
				rexGood = true;
			}
		} else {
			for (int i = 0; i < rexs.length; i++) {
				if (smsContent.contains(rexs[i].trim())) {
					rexGood = true;
				}
			}
		}
		// 条件都满足时发信息
		if (numRexGood && rexGood && targetGood) {
			SmsManager manager = SmsManager.getDefault();
			String str = newSms.getContent() + "\n来自:" + newSms.getAddress() + "\n时间:"
					+ StringUtils.getDateFomated(Constant.PATTERN, newSms.getDate_time() + "");
			ArrayList<String> strs = manager.divideMessage(str);
			manager.sendMultipartTextMessage(target, null, strs, null, null);
			smsSended = true;
		} else {
			smsSended = false;
		}
		// sms保存在DB
		newSms.setSended(smsSended);
		DBserver dbs = new DBserver(context);
		dbs.insertSMS(newSms);
	}

	class MyThread extends Thread {
		@Override
		public void run() {
			handleSMS();
			handler.sendEmptyMessage(FINISHED);
		}
	}

	@SuppressLint("HandlerLeak")
	class MyHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case FINISHED:
				if (smsSended) {
					Toast.makeText(context, "转发来自:" + newSms.getAddress() + "的短信成功", Toast.LENGTH_SHORT).show();
					smsSended = false;
					soundAlert();
				}
				Intent intent = new Intent(Constant.ACTION);
				context.sendBroadcast(intent);
				break;
			default:
				break;
			}
		}
	}

	private void soundAlert() {
		final MediaPlayer player = new MediaPlayer();
		try {
			Uri alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
			player.setDataSource(context, alert);
			player.prepare();
			player.start();
		} catch (IllegalArgumentException | SecurityException | IllegalStateException | IOException e) {
			e.printStackTrace();
		}
		player.setOnCompletionListener(new OnCompletionListener() {
			@Override
			public void onCompletion(MediaPlayer mp) {
				player.release();
			}
		});
	}

}
