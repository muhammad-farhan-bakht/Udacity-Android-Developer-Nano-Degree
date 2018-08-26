package com.example.farhan.mybaking;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.farhan.mybaking.fragments.DetailsFragment;
import com.example.farhan.mybaking.fragments.StepsDetailsFragment;
import com.example.farhan.mybaking.model.Recipe;
import com.example.farhan.mybaking.model.Step;
import com.example.farhan.mybaking.utils.Constants;
import com.example.farhan.mybaking.widget.RecipeWidgetService;

public class DetailActivity extends AppCompatActivity implements DetailsFragment.onStepsDetailsItemViewClickListener {

    private static final String TAG = DetailActivity.class.getSimpleName();
    Recipe mRecipe;
    FragmentManager fragmentManager;
    boolean mTabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mTabLayout = findViewById(R.id.tab_layout_detail_main_root_container) != null;
        fragmentManager = getSupportFragmentManager();
        String mToolBarTittle = getRecipeObjectFromIntent().getName();

        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            if (mToolBarTittle != null) {
                getSupportActionBar().setTitle(mToolBarTittle);
            }
        }

        if (!mTabLayout) {
            if (savedInstanceState == null) {
                sendBundleAndStartDetailsFragment();
            }
        } else {
            if (savedInstanceState == null) {
                sendBundleAndStartTabDetailsFragment();
                sendBundleAndStartTabStepDetailsFragment();
            }
        }
    }

    private void sendBundleAndStartTabDetailsFragment() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.RECIPE_OBJECT_KEY, mRecipe);
        DetailsFragment detailsFragment = new DetailsFragment();
        bundle.putBoolean(Constants.CHECK_TAB_FOR_DETAILS_FRAGMENT, true);
        detailsFragment.setArguments(bundle);

        fragmentManager.beginTransaction()
                .add(R.id.frame_container_fragment_details, detailsFragment)
                .commit();
    }

    private void sendBundleAndStartTabStepDetailsFragment() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.Steps_OBJECT_KEY, mRecipe.getSteps().get(0));
        bundle.putBoolean(Constants.CHECK_TAB_FOR_STEP_DETAILS_FRAGMENT, true);
        StepsDetailsFragment stepsDetailsFragment = new StepsDetailsFragment();
        stepsDetailsFragment.setArguments(bundle);

        fragmentManager.beginTransaction()
                .add(R.id.frame_container_fragment_stepsDetails, stepsDetailsFragment)
                .commit();
    }

    private void sendBundleAndStartDetailsFragment() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.RECIPE_OBJECT_KEY, mRecipe);
        DetailsFragment detailsFragment = new DetailsFragment();
        detailsFragment.setArguments(bundle);

        fragmentManager.beginTransaction()
                .add(R.id.frame_root_container_detail_activity, detailsFragment)
                .commit();
    }

    private Recipe getRecipeObjectFromIntent() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.containsKey(Constants.RECIPE_OBJECT_KEY)) {
            mRecipe = bundle.getParcelable(Constants.RECIPE_OBJECT_KEY);
            Log.e(TAG, "onCreate: " + mRecipe);
        } else {
            Log.e(TAG, "onCreate: bundle is null " + bundle);
        }
        return mRecipe;
    }

    @Override
    public boolean onSupportNavigateUp() {
        super.onBackPressed();
        return true;
    }

    @Override
    public void onItemClickListener(Step stepObj) {
        sendBundleAndStartStepsDetailsFragmentFromDetailsFragment(stepObj);
    }

    private void sendBundleAndStartStepsDetailsFragmentFromDetailsFragment(Step stepObj) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.Steps_OBJECT_KEY, stepObj);
        bundle.putBoolean(Constants.CHECK_TAB_FOR_STEP_DETAILS_FRAGMENT, true);
        StepsDetailsFragment stepsDetailsFragment = new StepsDetailsFragment();
        stepsDetailsFragment.setArguments(bundle);

        fragmentManager.beginTransaction()
                .replace(R.id.frame_container_fragment_stepsDetails, stepsDetailsFragment)
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.details_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.btn_details_menu_add_to_widget) {
            RecipeWidgetService.updateWidget(this, mRecipe);
            Toast.makeText(this, "Added To Widget", Toast.LENGTH_SHORT).show();

            return true;
        } else
            return super.onOptionsItemSelected(item);
    }
}
