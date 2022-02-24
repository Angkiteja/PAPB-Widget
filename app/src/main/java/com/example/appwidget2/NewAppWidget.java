package com.example.appwidget2;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RemoteViews;

import androidx.viewbinding.BuildConfig;

import java.text.DateFormat;
import java.util.Date;

/**
 * Implementation of App Widget functionality.
 */
public class NewAppWidget extends AppWidgetProvider {
    //AppWidgetProvider extends dari broadcastReceiver

//    isi counter disimpan di sharedPreferences
    private final static String mSharedPref = BuildConfig.BUILD_TYPE;
    //key dari counternya
    private final static String COUNT_KEY = "count";
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

//        CharSequence widgetText = context.getString(R.string.appwidget_text);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);
        views.setTextViewText(R.id.appwidget_id, String.valueOf(appWidgetId));


        //pertama kali function ini dipanggil, nol
        SharedPreferences pref = context.getSharedPreferences(mSharedPref, 0);
        int count = pref.getInt(COUNT_KEY+appWidgetId, 0);
        count++;
//kemudian dimasukkan ke sharedPreferences (1 ini langsung di save)
//        jadi nanti yg kebaca ketika function dipanggil lagi, yg kebaca 1, bukan 0

        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(COUNT_KEY + appWidgetId, count);
        editor.apply();

        //kenapa COUNT_KEY+appWidgetId kenapa ga COUNT_KEY aja ?
//        karena widget nya banyak, setiap kondisi beda update nya dipanggil  berapa kali tergantung trigger        jamnya mengambil format date
//        misal bikin 2 widget yg sama, ntar klo pencet counter malah sama angkanya antar widget

        String currentTime = DateFormat.getTimeInstance(DateFormat.SHORT).format(new Date());
        String output = String.valueOf(count) + "@" + currentTime;

        views.setTextViewText(R.id.appwidget_update, output);


//        bikin intent untuk update
        Intent intentUpdate = new Intent(context, NewAppWidget.class);
        intentUpdate.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);


//        ID array, appWidget mana saja yg harus di update
        int[] idsArray = new int[] {appWidgetId};
//        key nya pake AppWidgetManager, klo ga ntar AppWidhgetnya ga keupdate
        intentUpdate.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, idsArray);


        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, appWidgetId, intentUpdate, PendingIntent.FLAG_UPDATE_CURRENT);

        //function update button
        views.setOnClickPendingIntent(R.id.update_btn, pendingIntent);




        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            //disini ngecek appWidget mana yg harus diupdate?
            updateAppWidget(context, appWidgetManager, appWidgetId);

        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}