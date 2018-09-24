package com.example.xyzreader.ui.fragments;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.ShareCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.xyzreader.R;
import com.example.xyzreader.Utils.Misc;
import com.example.xyzreader.data.ArticleLoader;
import com.example.xyzreader.ui.ArticleDetailActivity;
import com.example.xyzreader.ui.ArticleListActivity;
import com.example.xyzreader.ui.adapters.ArticleDetailsAdapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * A fragment representing a single Article detail screen. This fragment is
 * either contained in a {@link ArticleListActivity} in two-pane mode (on
 * tablets) or a {@link ArticleDetailActivity} on handsets.
 */
public class ArticleDetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final String TAG = "ArticleDetailFragment";
    public static final String BUNDLE_RECYCLER_LAYOUT = "recycler_layout";
    private static final String SAVE_ARTICLE_AUTHOR_DATE = "authorAndDate";
    private static final String SAVE_ARTICLE_BODY = "body";
    private static final String SAVE_IMAGE = "image";
    private static final String SAVE_ARTICLE_NAME = "name";
    private static String cursorImage;
    private static String cursorAuthorAndDate;
    private static ArrayList<String> cursorBody;
    private static String cursorArticleName;
    public static final String ARG_ITEM_ID = "item_id";
    private Cursor mCursor;
    private long mItemId;
    private View mRootView;
    private RecyclerView.LayoutManager mLayoutManager;
    private boolean mIsTablet;
    private boolean mHeaderAnimating = false;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private Toolbar mToolbar;
    private ImageView mPhoto;
    private ConstraintLayout mRootArticleHeader;
    private TextView mArticleTitleText;
    private TextView mArticleAuthorDateText;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private FloatingActionButton fab;
    private RecyclerView mRecyclerView;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ArticleDetailFragment() {
    }

    public static ArticleDetailFragment newInstance(long itemId) {
        Bundle arguments = new Bundle();
        arguments.putLong(ARG_ITEM_ID, itemId);
        ArticleDetailFragment fragment = new ArticleDetailFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_article_detail, container, false);

        mCollapsingToolbarLayout = mRootView.findViewById(R.id.collapsing_toolbar_detail);
        fab = mRootView.findViewById(R.id.fab_share);
        mPhoto = mRootView.findViewById(R.id.photo);
        mRootArticleHeader = mRootView.findViewById(R.id.article_header_group);
        mArticleTitleText = mRootView.findViewById(R.id.article_title_text);
        mArticleAuthorDateText = mRootView.findViewById(R.id.article_author_date_text);
        mRecyclerView = mRootView.findViewById(R.id.body_reycler);
        mToolbar = mRootView.findViewById(R.id.toolbar_details);

        if (savedInstanceState != null) {
            Parcelable savedRecyclerLayoutState = savedInstanceState.getParcelable(BUNDLE_RECYCLER_LAYOUT);
            mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
            mLayoutManager.onRestoreInstanceState(savedRecyclerLayoutState);
            if (mRootView.findViewById(R.id.app_bar_detail) != null)
                ((AppBarLayout) mRootView.findViewById(R.id.app_bar_detail)).setExpanded(false);

            bindViews();
        }

        if (getArguments() != null && getArguments().containsKey(ARG_ITEM_ID)) {
            mItemId = getArguments().getLong(ARG_ITEM_ID);
            mIsTablet = getResources().getBoolean(R.bool.isTablet);

            ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            setHasOptionsMenu(true);

            getActivity().getLoaderManager().initLoader(0, null, this);

        } else {
            Misc.showSnackBar(getActivity().getApplicationContext(), getView(), R.string.no_intent_found, true);
            getActivity().finish();
        }
        return mRootView;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        // Searched it from StackOverFlow
        if (mLayoutManager != null)
        outState.putParcelable(BUNDLE_RECYCLER_LAYOUT, mLayoutManager.onSaveInstanceState());
        outState.putString(SAVE_ARTICLE_AUTHOR_DATE, cursorAuthorAndDate);
        outState.putStringArrayList(SAVE_ARTICLE_BODY, cursorBody);
        outState.putString(SAVE_IMAGE, cursorImage);

        outState.putString(SAVE_ARTICLE_NAME, cursorArticleName);
    }

    private void bindViews() {
        if (mRootView == null) {
            return;
        }

        Glide.with(getActivity().getApplicationContext())
                .asBitmap()
                .load(cursorImage)
                .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL))
                .into(new BitmapImageViewTarget(mPhoto) {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        super.onResourceReady(resource, transition);
                        Palette.from(resource).generate(new Palette.PaletteAsyncListener() {
                            @Override
                            public void onGenerated(@NonNull Palette palette) {
                                int articleContainerColor;
                                if (palette.getDarkVibrantSwatch() != null)
                                    articleContainerColor = palette.getDarkVibrantSwatch().getRgb();
                                else
                                    articleContainerColor = palette.getDarkMutedColor(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.colorPrimary));

                                int textViewColor;
                                if (palette.getLightMutedSwatch() != null)
                                    textViewColor = palette.getLightMutedSwatch().getRgb();
                                else
                                    textViewColor = palette.getLightVibrantColor(ContextCompat.getColor(getActivity().getApplicationContext(), android.R.color.white));

                                mRootArticleHeader.setBackgroundColor(articleContainerColor);
                                mArticleTitleText.setTextColor(textViewColor);
                                mArticleAuthorDateText.setTextColor(textViewColor);
                            }
                        });
                    }
                });

        if (mCollapsingToolbarLayout != null)
            mCollapsingToolbarLayout.setTitle(cursorArticleName);

        mToolbar.setTitle(cursorArticleName);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareArticle();
            }
        });
        slideUp(fab, 200);

        mArticleTitleText.setText(cursorArticleName);
        mArticleAuthorDateText.setText(cursorAuthorAndDate);

        setupRecyclerView();
    }

    private void setupRecyclerView() {

        List<String> article = cursorBody;

        if (mIsTablet && mRootArticleHeader != null) {
            article.add(0, "<br/>");
        }

        ArticleDetailsAdapter adapter = new ArticleDetailsAdapter(getActivity().getApplicationContext(), article);
        mRecyclerView.setAdapter(adapter);

        if (mLayoutManager == null)
            mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);

        if (mIsTablet && mRootArticleHeader != null) {
            mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    if (dy > 20)
                        hideHeader();
                    else if (dy < -20)
                        showHeader();
                }
            });
        }

        slideUp(mRecyclerView, 500);

        if (mIsTablet)
            showHeader();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return ArticleLoader.newInstanceForItemId(getActivity(), mItemId);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        if (!isAdded()) {
            if (cursor != null) {
                cursor.close();
            }
            return;
        }
        mCursor = cursor;

        if (mCursor != null && !mCursor.moveToFirst()) {
            Log.e(TAG, "Error reading item detail cursor");
            mCursor.close();
            mCursor = null;
        }
        cursorImage = mCursor.getString(ArticleLoader.Query.PHOTO_URL);
        cursorAuthorAndDate = getResources().getString(R.string.article_author_date, parsePublishedDate(), mCursor.getString(ArticleLoader.Query.AUTHOR));
        cursorBody = getSplitBody(mCursor.getString(ArticleLoader.Query.BODY));
        cursorArticleName = mCursor.getString(ArticleLoader.Query.TITLE);

        bindViews();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (mIsTablet)
                getActivity().onBackPressed();
            else
                getActivity().supportFinishAfterTransition();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void shareArticle() {
        Intent chooser = ShareCompat.IntentBuilder.from(getActivity())
                .setType("text/plain")
                .setText(String.format("%s - %s", cursorArticleName, cursorAuthorAndDate))
                .getIntent();
        Intent intent = Intent.createChooser(chooser, getString(R.string.action_share));
        startActivity(intent);
    }

    private Date parsePublishedDate() {
        try {
            String date = mCursor.getString(ArticleLoader.Query.PUBLISHED_DATE);
            Log.e(TAG,"date: "+date);
            return dateFormat.parse(date);
        } catch (ParseException ex) {
            Log.e(TAG, ex.getMessage());
            Log.i(TAG, "passing today's date");
            return new Date();
        }
    }

    public ArrayList<String> getSplitBody(String body) {
        String[] splitArray = body.split("\r\n\r\n");
        return new ArrayList<>(Arrays.asList(splitArray));
    }

    // Searched it from StackOverFlow
    public void slideUp(View view, int timeMilli) {
        view.setVisibility(View.VISIBLE);
        TranslateAnimation animate = new TranslateAnimation(
                0,
                0,
                view.getHeight(),
                0);
        animate.setDuration(timeMilli);
        animate.setFillAfter(true);
        view.startAnimation(animate);
    }

    // Searched it from StackOverFlow
    public void hideHeader() {
        if (!mHeaderAnimating && mRootArticleHeader.getVisibility() == View.VISIBLE) {
            mRootArticleHeader.animate()
                    .alpha(0f)
                    .translationY(-mRootArticleHeader.getHeight())
                    .withStartAction(new Runnable() {
                        @Override
                        public void run() {
                            mHeaderAnimating = true;
                        }
                    })
                    .withEndAction(new Runnable() {
                        @Override
                        public void run() {
                            mRootArticleHeader.setVisibility(View.GONE);
                            mHeaderAnimating = false;
                        }
                    })
                    .start();
        }
    }

    public void showHeader() {
        if (!mHeaderAnimating && mRootArticleHeader.getVisibility() == View.GONE) {
            mRootArticleHeader.animate()
                    .alpha(1f)
                    .translationY(0)
                    .withStartAction(new Runnable() {
                        @Override
                        public void run() {
                            mRootArticleHeader.setVisibility(View.VISIBLE);
                            mHeaderAnimating = true;
                        }
                    })
                    .withEndAction(new Runnable() {
                        @Override
                        public void run() {
                            mHeaderAnimating = false;
                        }
                    })
                    .start();
        }
    }
}
