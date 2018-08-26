package com.example.farhan.mybaking.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import com.example.farhan.mybaking.MainActivity;
import com.example.farhan.mybaking.R;
import com.example.farhan.mybaking.model.Recipe;
import com.example.farhan.mybaking.utils.Prefs;

/**
 * Implementation of App Widget functionality.
 */

// Learn from github Repo's and StackOverFlow and from the following course
public class RecipeWidgetProvider extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        Bundle options = appWidgetManager.getAppWidgetOptions(appWidgetId);
        int width = options.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH);
        RemoteViews rv;
        if (width < 150) {
            rv = getSingleItemOnSmallWidth(context);
            Log.e("RecipeWidgetProvider", "in if");
        } else {
            Recipe recipe = Prefs.getRecipePref(context);
            if (recipe != null) {
                Log.e("RecipeWidgetProvider", "in else");
                rv = getListOnLargeWidth(context, appWidgetManager, appWidgetId, recipe);
            } else {
                rv = getSingleItemOnSmallWidth(context);
                Log.e("RecipeWidgetProvider", "recipeObj is null");
            }
        }
        appWidgetManager.updateAppWidget(appWidgetId, rv);
    }

    public static RemoteViews getSingleItemOnSmallWidth(Context context) {
        Log.e("getSingleItemOnSmalWidt", "in");
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_widget_provider);
        views.setViewVisibility(R.id.img_widget_recipe_image, View.VISIBLE);
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        views.setOnClickPendingIntent(R.id.img_widget_recipe_image, pendingIntent);
        return views;
    }

    public static RemoteViews getListOnLargeWidth(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Recipe recipe) {
        Log.e("getListOnLargeWidth", "in");
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_widget_provider);
        views.setViewVisibility(R.id.img_widget_recipe_image, View.GONE);

        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent= PendingIntent.getActivity(context, 0, intent, 0);
        views.setTextViewText(R.id.tv_widget_recipe_name, recipe.getName());
        views.setOnClickPendingIntent(R.id.tv_widget_recipe_name, pendingIntent);

        Intent intentList = new Intent(context, RecipeWidgetService.class);
        intentList.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        views.setRemoteAdapter(R.id.list_view_widget_recipe, intentList);

        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.list_view_widget_recipe);
        appWidgetManager.updateAppWidget(appWidgetId, views);
        return views;
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    public static void updateAppWidgets(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
        Recipe recipe = Prefs.getRecipePref(context);
        if (recipe != null) {
            RecipeWidgetService.updateWidget(context, recipe);
            Log.e("onAppWidgetOptionsChan", "runs");
        } else {
            Log.e("RecipeWidgetProvider", "recipeObj is null");
        }

        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
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

