package com.example.farhan.mybaking;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.farhan.mybaking.adapters.MainAdapter;
import com.example.farhan.mybaking.forMyCustomTesting.SimpleIdlingResource;
import com.example.farhan.mybaking.model.Recipe;
import com.example.farhan.mybaking.setUpRetrofit.RetrofitClient;
import com.example.farhan.mybaking.utils.Constants;
import com.example.farhan.mybaking.utils.NetworkUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements MainAdapter.MainAdapterOnClickHandler {

    private static final String TAG = MainActivity.class.getSimpleName();
    private MainAdapter mMainAdapter;
    private ProgressBar mLoadingIndicator;
    private ImageView noInternetFoundImage;
    private boolean mTabLayout = false;


    @Nullable
    private SimpleIdlingResource mIdlingResource;

    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new SimpleIdlingResource();
        }
        return mIdlingResource;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get the IdlingResource instance
        getIdlingResource();
        mIdlingResource.setIdleState(false);
        mTabLayout = findViewById(R.id.tab_layout_main_root_view) != null;

        init();

        if (NetworkUtils.isNetworkAvailable(this)) {
            hideNoInternetFoundErrorMessage();
            loadRecipeData();
        } else {
            showNoInternetFoundErrorMessage();
        }
    }

    private void init() {
        mLoadingIndicator = findViewById(R.id.pb_loading_indicator);
        noInternetFoundImage = findViewById(R.id.img_main_no_internet_found);
        RecyclerView mRecyclerView = findViewById(R.id.rc_view_main);
        mMainAdapter = new MainAdapter();
        mMainAdapter.setMainAdapterOnClickHandler(MainActivity.this);

        if (!mTabLayout) {
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        } else {
            mRecyclerView.setLayoutManager(new GridLayoutManager(this, calculateNoOfColumns(MainActivity.this)));
            mRecyclerView.setHasFixedSize(true);
        }

        mRecyclerView.setAdapter(mMainAdapter);
    }

    @Override
    public void onClick(Recipe recipeObj) {
        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
        intent.putExtra(Constants.RECIPE_OBJECT_KEY, recipeObj);
        startActivity(intent);
    }

    private void loadRecipeData() {

        showLoadingProgressbar();
        RetrofitClient.getInstance().getService().getAllRecipes().enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(@NonNull Call<List<Recipe>> call, @NonNull Response<List<Recipe>> response) {
                if (response.body() != null) {
                    mMainAdapter.setRecipeData(response.body());
                    hideLoadingProgressbar();
                    Log.e(TAG, "onResponse: response.body() is "+response.body());
                    mIdlingResource.setIdleState(true);
                } else {
                    hideLoadingProgressbar();
                    Log.e(TAG, "onResponse: response.body() is null");
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Recipe>> call, @NonNull Throwable t) {
                hideLoadingProgressbar();
                Log.e(TAG, "onFailure: " + t.getMessage());
                Log.e(TAG, "onFailure: " + t.getCause());
                Log.e(TAG, "onFailure: " + t.getCause());

            }
        });
    }

    public static int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int scalingFactor = 500;
        int noOfColumns = (int) (dpWidth / scalingFactor);
        if (noOfColumns < 2)
            noOfColumns = 2;
        return noOfColumns;
    }

    private void showLoadingProgressbar() {
        mLoadingIndicator.setVisibility(View.VISIBLE);
    }

    private void hideLoadingProgressbar() {
        mLoadingIndicator.setVisibility(View.GONE);
    }

    private void showNoInternetFoundErrorMessage() {
        noInternetFoundImage.setVisibility(View.VISIBLE);
    }

    private void hideNoInternetFoundErrorMessage() {
        noInternetFoundImage.setVisibility(View.GONE);
    }


}
