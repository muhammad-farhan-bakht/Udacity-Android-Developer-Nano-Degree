package com.example.farhan.mybaking.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.farhan.mybaking.model.Recipe;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class Prefs {

    private static final String TAG = "Prefs";
    private static final String RECIPE_PREF_KEY = "recipeObjPrefKey";
    private static final String RECIPE_OBJECT_PREF_KEY = "recipeObjKey";

    public static void saveRecipePref(Context context, Recipe recipe) {
        SharedPreferences mSharedPreferences = context.getSharedPreferences(RECIPE_PREF_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString;
        try {
            jsonString = objectMapper.writeValueAsString(recipe);
            mEditor.putString(RECIPE_OBJECT_PREF_KEY, jsonString);
            mEditor.apply();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            Log.e(TAG, "saveRecipePref: JsonProcessingException " + e.getMessage());
        }
    }

    public static Recipe getRecipePref(Context context) {
        SharedPreferences mSharedPreferences = context.getSharedPreferences(RECIPE_PREF_KEY, Context.MODE_PRIVATE);
        String JsonString = mSharedPreferences.getString(RECIPE_OBJECT_PREF_KEY, "");
        ObjectMapper objectMapper = new ObjectMapper();
        Recipe recipeObj = null;
        try {
            recipeObj = objectMapper.readValue(JsonString, Recipe.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return recipeObj;
    }

    public static void clearRecipePref(Context context) {
        SharedPreferences mSharedPreferences = context.getSharedPreferences(RECIPE_PREF_KEY, Context.MODE_PRIVATE);
        if (mSharedPreferences != null) {
            SharedPreferences.Editor mEditor = mSharedPreferences.edit();
            mEditor.clear();
            mEditor.apply();
        } else {
            Log.e(TAG, "clearRecipePref: else is null");
        }
    }
}
