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

	static final String NUMBER_LIST_KEY = "numbers_to_answer";
	static final String SPLIT_CHAR = ";";
	static final String NULL_STRING = "null";

	@Override
	public void onReceive(Context context, Intent intent) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		String startBorder = preferences.getString("start_border", "");
		String endBorder = preferences.getString("end_border", "");
		String prefix = preferences.getString("prefix", "");
		String postfix = preferences.getString("postfix", "");
		Bundle bundle = intent.getExtras();
		SmsMessage[] messages = null;
		String messageReceived = "";
		if (bundle != null) {
			Object[] pdus = (Object[]) bundle.get("pdus");
			messages = new SmsMessage[pdus.length];
			for (int i = 0; i < messages.length; i++) {
				messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
				if (checkNumber(context, messages[i].getOriginatingAddress())) {
					messageReceived += messages[i].getMessageBody().toString();
					String outMessage = "";
					if ((startBorder.equals("") && endBorder.equals(""))) {
						outMessage = messageReceived;
					} else {
						if (startBorder.equals("")) {
							outMessage = messageReceived.replace(" " + endBorder, "");
						} else {
							if (endBorder.equals("")) {
								outMessage = messageReceived.replace(startBorder + " ", "");
							} else {
								Matcher matcher = Pattern.compile(
										Pattern.quote(startBorder) + "(.*?)" + Pattern.quote(endBorder)).matcher(
										messageReceived);
								while (matcher.find()) {
									outMessage += matcher.group(1);
								}
							}
						}

					}
					SmsManager sms = SmsManager.getDefault();
					if (!outMessage.equals("")) {
						if (!prefix.equals("")) {
							outMessage = prefix + " " + outMessage;
						}
						if (!postfix.equals("")) {
							outMessage = outMessage + " " + postfix;
						}
						sms.sendTextMessage(messages[i].getOriginatingAddress(), null, outMessage, null, null);
						Toast toast = Toast.makeText(
								context,
								context.getResources().getString(R.string.response_message) + " "
										+ messages[i].getOriginatingAddress(), Toast.LENGTH_LONG);
						toast.show();
					}
				}
			}
		}
	}
	
	private boolean checkNumber(Context context, String incomingNumber) {
		boolean result = false;
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
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
}