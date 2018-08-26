package com.example.farhan.mybaking.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.farhan.mybaking.R;
import com.example.farhan.mybaking.adapters.IngredientsAdapter;
import com.example.farhan.mybaking.adapters.StepsAdapter;
import com.example.farhan.mybaking.model.Recipe;
import com.example.farhan.mybaking.model.Step;
import com.example.farhan.mybaking.utils.Constants;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailsFragment extends Fragment implements StepsAdapter.StepsAdapterOnClickHandler {

    private static final String TAG = "DetailsFragment";
    private Boolean mTabLayout = false;
    private FragmentManager fragmentManager;
    onStepsDetailsItemViewClickListener mCallback;

    public DetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_details, container, false);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(true);

        fragmentManager = getActivity().getSupportFragmentManager();

        init(view);

        return view;
    }

    private void init(View view) {

        Recipe recipeObj = getRecipeObjectFromIntent();

        RecyclerView mRecyclerViewIngredients = view.findViewById(R.id.rc_view_details_ingredients);
        IngredientsAdapter ingredientsAdapter = new IngredientsAdapter();
        LinearLayoutManager ingredientsLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerViewIngredients.setLayoutManager(ingredientsLayoutManager);
        mRecyclerViewIngredients.setAdapter(ingredientsAdapter);
        ingredientsAdapter.setIngredientsData(recipeObj.getIngredients());

        RecyclerView mRecyclerViewSteps = view.findViewById(R.id.rc_view_details_steps);
        StepsAdapter stepsAdapter = new StepsAdapter();
        stepsAdapter.StepsAdapterOnClickHandler(this);
        LinearLayoutManager stepsLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerViewSteps.setLayoutManager(stepsLayoutManager);
        mRecyclerViewSteps.setAdapter(stepsAdapter);
        stepsAdapter.setStepsData(recipeObj.getSteps());
    }

    @Override
    public void onClick(Step stepObj) {
        if (!mTabLayout) {
            sendBundleAndStartStepsDetailsFragment(stepObj);
        } else {
            mCallback.onItemClickListener(stepObj);
        }
    }

    private void sendBundleAndStartStepsDetailsFragment(Step stepObj) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.Steps_OBJECT_KEY, stepObj);
        StepsDetailsFragment stepsDetailsFragment = new StepsDetailsFragment();
        stepsDetailsFragment.setArguments(bundle);

        fragmentManager.beginTransaction()
                .addToBackStack("StepsDetailsFragment")
                .add(R.id.frame_root_container_detail_activity, stepsDetailsFragment)
                .commit();
    }

    public interface onStepsDetailsItemViewClickListener {
        void onItemClickListener(Step stepObj);
    }

    private Recipe getRecipeObjectFromIntent() {
        Recipe mRecipe = null;
        Bundle bundle = this.getArguments();
        if (bundle != null && bundle.containsKey(Constants.RECIPE_OBJECT_KEY)) {
            mRecipe = bundle.getParcelable(Constants.RECIPE_OBJECT_KEY);
            mTabLayout = bundle.getBoolean(Constants.CHECK_TAB_FOR_DETAILS_FRAGMENT, false);
            Log.e(TAG, "onCreate: " + mRecipe);
        } else {
            Log.e(TAG, "onCreate: bundle is null " + bundle);
        }
        return mRecipe;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mCallback = (onStepsDetailsItemViewClickListener) context;
        } catch (ClassCastException ex) {
            Log.e(TAG, ex.getMessage());
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().onBackPressed();
        }
        return true;
    }

}
