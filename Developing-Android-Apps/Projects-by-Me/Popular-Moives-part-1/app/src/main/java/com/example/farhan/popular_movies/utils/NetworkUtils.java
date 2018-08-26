package com.example.farhan.popular_movies.utils;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();

    private static final String STATIC_MOVIES_BASE_URL = "https://api.themoviedb.org/3/movie/";

    private static final String API_KEY_PARAM = "241b3a964095b482854706c732ef0ebc";
    private static final String API_KEY = "api_key";

    private static final String LANGUAGE_PARAM = "language";
    private static final String LANGUAGE = "en-US";

    private static final String PAGE_PARAM = "page";
    private static final String PAGE = "1";


    public static URL buildUrl(String sortBy) {
        Uri builtUri = Uri.parse(STATIC_MOVIES_BASE_URL).buildUpon()
                .appendPath(sortBy)
                .appendQueryParameter(API_KEY, API_KEY_PARAM)
                .appendQueryParameter(LANGUAGE_PARAM, LANGUAGE)
                .appendQueryParameter(PAGE_PARAM, PAGE)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Log.e(TAG, "Built URI " + url);
        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}
