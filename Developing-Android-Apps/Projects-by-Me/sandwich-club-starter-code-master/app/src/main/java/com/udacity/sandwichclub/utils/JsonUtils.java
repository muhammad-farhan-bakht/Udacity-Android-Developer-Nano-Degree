package com.udacity.sandwichclub.utils;

import android.util.Log;

import com.udacity.sandwichclub.model.Sandwich;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonUtils {

    public static Sandwich parseSandwichJson(String json) throws JSONException {

        final String TAG = "JsonUtils";

        final String NAME = "name";
        final String MAIN_NAME = "mainName";
        final String ALSO_KNOWN_AS = "alsoKnownAs";
        final String PLACE_OF_ORIGIN = "placeOfOrigin";
        final String DESCRIPTION = "description";
        final String IMAGE_URL = "image";
        final String INGREDIENTS = "ingredients";


        JSONObject sandwich = new JSONObject(json);
        JSONObject name = sandwich.getJSONObject(NAME);

        String mainName = name.getString(MAIN_NAME);
        Log.d(TAG, "mainName: " + mainName);

        // Learn from StackOverFlow how to get JsonArray from JSON Response
        JSONArray alsoKnownAsJsonArray = name.getJSONArray(ALSO_KNOWN_AS);
        List<String> alsoKnownAsList = new ArrayList<>();
        if (alsoKnownAsJsonArray != null) {
            for (int i = 0; i < alsoKnownAsJsonArray.length(); i++) {
                alsoKnownAsList.add(alsoKnownAsJsonArray.get(i).toString());
            }
        }
        Log.d(TAG, "alsoKnownAsList: " + alsoKnownAsJsonArray);

        String placeOfOrigin = sandwich.getString(PLACE_OF_ORIGIN);
        Log.d(TAG, "placeOfOrigin: " + placeOfOrigin);

        String description = sandwich.getString(DESCRIPTION);
        Log.d(TAG, "description: " + description);

        String image = sandwich.getString(IMAGE_URL);
        Log.d(TAG, "image: " + image);

        // Learn from StackOverFlow how to get JsonArray from JSON Response
        JSONArray ingredientsJsonArray = sandwich.getJSONArray(INGREDIENTS);
        List<String> ingredientsList = new ArrayList<>();
        if (ingredientsJsonArray != null) {
            for (int i = 0; i < ingredientsJsonArray.length(); i++) {
                ingredientsList.add(ingredientsJsonArray.get(i).toString());
            }
        }
        Log.d(TAG, "ingredientsList: " + ingredientsJsonArray);

        return new Sandwich(mainName, alsoKnownAsList, placeOfOrigin, description, image, ingredientsList);
    }

}
