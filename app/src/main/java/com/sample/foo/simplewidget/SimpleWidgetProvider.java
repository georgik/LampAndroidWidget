package com.sample.foo.simplewidget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RemoteViews;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import cz.msebera.android.httpclient.Header;

public class SimpleWidgetProvider extends AppWidgetProvider {

    private void getHttpRequest(String state) {
        AsyncHttpClient asyncClient = new AsyncHttpClient();
        asyncClient.get("http://192.168.1.50/relay=" + state, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }

        });
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        final int count = appWidgetIds.length;

        for (int i = 0; i < count; i++) {
            int widgetId = appWidgetIds[i];
            String value = "off";
            SharedPreferences prefs = context.getSharedPreferences("LampApp", 0);
            boolean isRelayEnabled = prefs.getBoolean("relayState", false);
            isRelayEnabled = !isRelayEnabled;
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("relayState", isRelayEnabled);
            editor.commit();

            if (isRelayEnabled) {
                value = "on";
            }
            getHttpRequest(value);


            RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                    R.layout.simple_widget);
            remoteViews.setTextViewText(R.id.textView, value.toUpperCase());

            Intent intent = new Intent(context, SimpleWidgetProvider.class);
            intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                    0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setOnClickPendingIntent(R.id.actionButton, pendingIntent);

            appWidgetManager.updateAppWidget(widgetId, remoteViews);
        }
    }


}
