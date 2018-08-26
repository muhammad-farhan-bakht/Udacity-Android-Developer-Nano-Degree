package com.example.farhan.mybaking.widget;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViewsService;

import com.example.farhan.mybaking.model.Recipe;
import com.example.farhan.mybaking.utils.Prefs;

// Learn from github Repo's and StackOverFlow and from the following course
public class RecipeWidgetService extends RemoteViewsService {

    public static void updateWidget(Context context, Recipe recipe) {
        Prefs.clearRecipePref(context);
        Prefs.saveRecipePref(context, recipe);

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, RecipeWidgetProvider.class));
        RecipeWidgetProvider.updateAppWidgets(context, appWidgetManager, appWidgetIds);
        Log.e("updateWidget", "runs");
    }

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        return new RecipeListRemoteViewsFactory(getApplicationContext());
    }
}
