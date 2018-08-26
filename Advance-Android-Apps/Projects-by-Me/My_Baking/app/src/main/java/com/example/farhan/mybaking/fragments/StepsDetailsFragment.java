package com.example.farhan.mybaking.fragments;


import android.content.Context;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.farhan.mybaking.R;
import com.example.farhan.mybaking.model.Step;
import com.example.farhan.mybaking.utils.Constants;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 */
public class StepsDetailsFragment extends Fragment {
    private static final String TAG = "StepsDetailsFragment";
    private SimpleExoPlayer mExoPlayer;
    private SimpleExoPlayerView mPlayerView;
    private Step mStep;
    private TextView mStepDescription;
    private ImageView mThumbNail;
    private long mCurrentPosition = 0;
    private boolean mPlayWhenReady = true;
    private String descriptionText;
    private Boolean mTabLayout = false;

    public StepsDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_steps_details, container, false);
        Log.e(TAG, "onCreate:");

        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(true);

        mStep = getStepObjectAndCheckTabFromIntent();

        if (savedInstanceState != null && savedInstanceState.containsKey(Constants.SEEK_TO_POSITION_KEY) && savedInstanceState.containsKey(Constants.STEP_DETAILS_DESCRIPTION_KEY)) {
            mCurrentPosition = savedInstanceState.getLong(Constants.SEEK_TO_POSITION_KEY);
            mPlayWhenReady = savedInstanceState.getBoolean(Constants.PLAY_WHEN_READY_KEY);
            descriptionText = savedInstanceState.getString(Constants.STEP_DETAILS_DESCRIPTION_KEY);
        }

        if (!mTabLayout) {
            init(view);

        } else {
            initForTab(view);
        }

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        Log.e(TAG, "onStart:");
        if (mStep.getVideoURL() != null && !TextUtils.isEmpty(mStep.getVideoURL())) {
            initializePlayer(Uri.parse(mStep.getVideoURL()));
        } else if (mStep.getThumbnailURL() != null && !TextUtils.isEmpty(mStep.getThumbnailURL())) {
            mThumbNail.setVisibility(View.VISIBLE);
            mPlayerView.setVisibility(View.GONE);
            Picasso.with(getContext())
                    .load(mStep.getThumbnailURL())
                    .into(mThumbNail);
        } else {
            mPlayerView.setVisibility(View.GONE);
            mThumbNail.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(mStep.getDescription())) {
            if (!TextUtils.isEmpty(descriptionText)) {
                mStepDescription.setText(descriptionText);
                Log.e(TAG, "onStart: Load Des text from saveIns");
            } else {
                mStepDescription.setText(mStep.getDescription());
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        releasePlayer();
    }

    private void init(View view) {
        mPlayerView = view.findViewById(R.id.exo_player_view);
        mStepDescription = view.findViewById(R.id.tv_step_details_text);
        mThumbNail = view.findViewById(R.id.img_step_details_image);
        if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            mPlayerView.getLayoutParams().height = getDeviceFullHeight();
            mPlayerView.getLayoutParams().width = getDeviceFullWidth();
            ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        } else {
            mPlayerView.getLayoutParams().height = getDeviceHalfHeight();
        }
    }

    private void initForTab(View view) {
        mPlayerView = view.findViewById(R.id.exo_player_view);
        mStepDescription = view.findViewById(R.id.tv_step_details_text);
        mThumbNail = view.findViewById(R.id.img_step_details_image);
        if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            mPlayerView.getLayoutParams().height = getDeviceHalfHeight();
            mPlayerView.getLayoutParams().width = getDeviceFullWidth();
        } else {
            mPlayerView.getLayoutParams().width = getDeviceFullWidth();
            mPlayerView.getLayoutParams().height = getDeviceHalfHeight();
        }
    }

    private Step getStepObjectAndCheckTabFromIntent() {
        Step mStep = null;
        Bundle bundle = this.getArguments();
        if (bundle != null && bundle.containsKey(Constants.Steps_OBJECT_KEY)) {
            mStep = bundle.getParcelable(Constants.Steps_OBJECT_KEY);
            mTabLayout = bundle.getBoolean(Constants.CHECK_TAB_FOR_STEP_DETAILS_FRAGMENT, false);
            Log.e(TAG, "onCreate: " + mStep);
        } else {
            Log.e(TAG, "onCreate: bundle is null " + bundle);
        }
        return mStep;
    }

    // Searched from StackOverFlow And GitHub Examples
    private void initializePlayer(Uri mediaUri) {
        if (mExoPlayer == null) {

            // Create a default LoadControl
            // Create a default TrackSelector
            // Create a default BandwidthMeter
            LoadControl loadControl = new DefaultLoadControl();
            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
            TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);

            // Create the player
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
            // Bind the player to the view.
            mPlayerView.setPlayer(mExoPlayer);

            // Measures bandwidth during playback. Can be null if not required.
            // Produces DataSource instances through which media data is loaded.
            // This is the MediaSource representing the media to be played.
            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getContext(), Util.getUserAgent(getContext(), getString(R.string.app_name)));
            ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();

            MediaSource videoSource = new ExtractorMediaSource(mediaUri, dataSourceFactory, extractorsFactory, null, null);
            // Prepare the player with the source.
            mExoPlayer.prepare(videoSource);

            if (mCurrentPosition != 0) {
                mExoPlayer.seekTo(mCurrentPosition);
                mExoPlayer.setPlayWhenReady(mPlayWhenReady);
            }

            mPlayerView.setVisibility(View.VISIBLE);
            mPlayerView.requestFocus();
        }
    }

    private void releasePlayer() {
        if (mExoPlayer != null) {
            mPlayWhenReady = mExoPlayer.getPlayWhenReady();
            mCurrentPosition = mExoPlayer.getCurrentPosition();

            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
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

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(Constants.SEEK_TO_POSITION_KEY, mCurrentPosition);
        outState.putBoolean(Constants.PLAY_WHEN_READY_KEY, mPlayWhenReady);
        outState.putString(Constants.STEP_DETAILS_DESCRIPTION_KEY, mStepDescription.getText().toString());
    }

    // Searched from StackOverFlow
    private int getDeviceHalfHeight() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowmanager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        windowmanager.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels / 2;
    }

    // Searched from StackOverFlow
    private int getDeviceFullHeight() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowmanager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        windowmanager.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    // Searched from StackOverFlow
    private int getDeviceFullWidth() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowmanager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        windowmanager.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

}
