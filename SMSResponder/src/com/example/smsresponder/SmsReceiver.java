package com.example.smsresponder;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.widget.Toast;

public class SmsReceiver extends BroadcastReceiver {

	private static String NUMBER_LIST_KEY = "numbers_to_answer";
	private static String SPLIT_CHAR = ";";
	private static String NULL_STRING = "null";
	private static Context context_;

	private static boolean checkNumber(String incomingNumber) {
		boolean result = false;
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context_);
		String numbers = prefs.getString(NUMBER_LIST_KEY, NULL_STRING);
		if (!numbers.equals(NULL_STRING)) {
			String[] numbersArray = numbers.split(SPLIT_CHAR);
			for (int i = 0; i < numbersArray.length; i++) {
				if (numbersArray[i].equals(incomingNumber)) {
					result = true;
					break;
				}
			}
		}
		return result;
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		context_ = context;
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		String startBorder = preferences.getString("start_border", null);
		String endBorder = preferences.getString("end_border", null);
		String prefix = preferences.getString("prefix", null);
		String postfix = preferences.getString("postfix", null);
		Bundle bundle = intent.getExtras();
		SmsMessage[] messages = null;
		String messageReceived = "";
		if (bundle != null) {
			Object[] pdus = (Object[]) bundle.get("pdus");
			messages = new SmsMessage[pdus.length];
			for (int i = 0; i < messages.length; i++) {
				messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
				if (checkNumber(messages[i].getOriginatingAddress())) {
					messageReceived += messages[i].getMessageBody().toString();
					String outMessage = "";
					if (startBorder.equals(null) && endBorder.equals(null)) {
						outMessage = messageReceived;
					} else {
						Matcher m = Pattern.compile(Pattern.quote(startBorder) + "(.*?)" + Pattern.quote(endBorder))
								.matcher(messageReceived);
						while (m.find()) {
							outMessage += m.group(1);
						}
					}
					SmsManager sms = SmsManager.getDefault();
					if (!outMessage.equals("")) {
						sms.sendTextMessage(messages[i].getOriginatingAddress(), null, prefix + outMessage + postfix,
								null, null);
						Toast toast = Toast.makeText(context,
								context.getResources().getString(R.string.response_message)+" " + messages[i].getOriginatingAddress(), Toast.LENGTH_LONG);
						toast.show();
					}
				}
			}
		}
	}
}