package com.falconsocka.mywidgetapp

import android.annotation.SuppressLint
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import java.util.*
import android.app.PendingIntent
import android.content.ComponentName
import android.graphics.Color


var REFRESH_ACTION = "android.appwidget.action.MY_STATUS_UPDATE"

class MyAppWidget : AppWidgetProvider() {

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)

        val timeZone = TimeZone.getTimeZone("GMT+1:00")
        println("Time rn is ${Calendar.getInstance(timeZone).get(Calendar.HOUR_OF_DAY)}:${Calendar.getInstance(timeZone).get(Calendar.MINUTE)}")

        if (intent.action == REFRESH_ACTION) {
            val views = RemoteViews(context.packageName, R.layout.my_app_widget)
            views.setTextViewText(R.id.appwidget_text, "YES TODAY!")

            val actualDay = Calendar.getInstance(TimeZone.getTimeZone("GMT+1:00")).get(Calendar.DAY_OF_YEAR)
            saveDay(context,actualDay)
            views.setInt(R.id.myRandomId, "setBackgroundColor", Color.GREEN)

            AppWidgetManager.getInstance(context).updateAppWidget(
                ComponentName(context, MyAppWidget::class.java), views
            )
        }

    }

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onDeleted(context: Context, appWidgetIds: IntArray) {
        // When the user deletes the widget, delete the preference associated with it.
        //TODO add delete of prefs
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
        val views = RemoteViews(context.packageName, R.layout.my_app_widget)
        views.setOnClickPendingIntent(R.id.myRandomId, getPenIntent(context))//onClick
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
        // Just for test
        saveDay(context,10)
    }
}

internal fun updateAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int
) {

    val views = RemoteViews(context.packageName, R.layout.my_app_widget)
    views.setOnClickPendingIntent(R.id.myRandomId, getPenIntent(context))//onClick

    val savedDay = getSavedDay(context)
    val actualDay = Calendar.getInstance(TimeZone.getTimeZone("GMT+1:00")).get(Calendar.DAY_OF_YEAR)

    println("$savedDay SAVED DAY")
    if(savedDay == -1 || savedDay == 1) {
        saveDay(context,appWidgetId)
        return
    }

    if(savedDay < actualDay) {
        views.setTextViewText(R.id.appwidget_text, "NO TODAY $savedDay")
        views.setInt(R.id.myRandomId, "setBackgroundColor", Color.RED)
    }
    else {
        views.setTextViewText(R.id.appwidget_text, "YES TODAY $savedDay")
        views.setInt(R.id.myRandomId, "setBackgroundColor", Color.GREEN)
    }

    // Instruct the widget manager to update the widget
    appWidgetManager.updateAppWidget(appWidgetId, views)
}

@SuppressLint("UnspecifiedImmutableFlag")
private fun getPenIntent(context: Context): PendingIntent? {
    val intent = Intent(context, MyAppWidget::class.java)
    intent.action = REFRESH_ACTION
    return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
}