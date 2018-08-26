package com.example.farhan.mybaking.widget;

import android.content.Context;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.farhan.mybaking.R;
import com.example.farhan.mybaking.model.Recipe;
import com.example.farhan.mybaking.utils.Prefs;

// Learn from github Repo's and StackOverFlow

public class RecipeListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private Context mContext;
    private Recipe recipeObj;

    public RecipeListRemoteViewsFactory(Context applicationContext) {
        mContext = applicationContext;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        recipeObj = Prefs.getRecipePref(mContext);
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        if (recipeObj.getIngredients().size() != 0) {
            return recipeObj.getIngredients().size();
        } else {
            return 0;
        }
    }

    @Override
    public RemoteViews getViewAt(int position) {

        RemoteViews mRemoteViews = new RemoteViews(mContext.getPackageName(), R.layout.widget_ingredients_item_view);
        mRemoteViews.setTextViewText(R.id.tv_widget_ingredient_item_text_text, recipeObj.getIngredients().get(position).getIngredient());
        return mRemoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
