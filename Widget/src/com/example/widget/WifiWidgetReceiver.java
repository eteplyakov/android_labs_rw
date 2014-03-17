package com.example.widget;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.widget.RemoteViews;

public class WifiWidgetReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.wifi_widget);
		WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		if (intent.getAction().equals(ReceiverHelpers.WIDGET_UPDATE_ACTION)) {
			if (wifiManager.isWifiEnabled()) {
				wifiManager.setWifiEnabled(false);
			} else {
				wifiManager.setWifiEnabled(true);
			}
		} else {
			if (wifiManager.isWifiEnabled()) {
				remoteViews.setInt(R.id.wifi_image, "setAlpha", 255);
			} else {
				remoteViews.setInt(R.id.wifi_image, "setAlpha", 100);
			}
		}
		WifiWidgetProvider.pushWidgetUpdate(context.getApplicationContext(), remoteViews);
	}

}
