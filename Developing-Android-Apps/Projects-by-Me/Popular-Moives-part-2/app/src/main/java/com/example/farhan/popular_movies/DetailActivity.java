package com.example.farhan.popular_movies;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.farhan.popular_movies.AndroidArchitectureComponentsSetUps.AddFavoriteViewModel;
import com.example.farhan.popular_movies.AndroidArchitectureComponentsSetUps.AddTaskViewModelFactory;
import com.example.farhan.popular_movies.AndroidArchitectureComponentsSetUps.AppDatabase;
import com.example.farhan.popular_movies.AndroidArchitectureComponentsSetUps.AppExecutors;
import com.example.farhan.popular_movies.SetUpRetrofit.RetrofitClient;
import com.example.farhan.popular_movies.model.BaseModel;
import com.example.farhan.popular_movies.model.Favorites;
import com.example.farhan.popular_movies.model.Movies;
import com.example.farhan.popular_movies.model.Reviews;
import com.example.farhan.popular_movies.model.Trailers;
import com.example.farhan.popular_movies.utils.Constants;
import com.example.farhan.popular_movies.utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivity extends AppCompatActivity {

    private static final String TAG = DetailActivity.class.getSimpleName();

    TextView movieTitle;
    ImageView movieImg;
    TextView movieRating;
    TextView movieReleaseDate;
    TextView moviePlot;
    LinearLayout trailer_root;
    List<Trailers> trailersList;
    TextView noTrailerAvailable;
    LinearLayout reviews_root;
    List<Reviews> reviewsList;
    TextView noReviewsAvailable;
    int id;
    private AppDatabase mDb;
    TextView favoriteLabel;
    ImageView btnFavorite;
    private Boolean isFavorite = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        checkBundle();

        apiCallsIfNetworkIsAvailable();

    }

    private void checkBundle() {
        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.containsKey("moviesObj")) {
            Movies movies = extras.getParcelable("moviesObj");

            movieTitle = findViewById(R.id.movie_title);
            movieImg = findViewById(R.id.movie_image);
            movieRating = findViewById(R.id.movie_rating);
            movieReleaseDate = findViewById(R.id.movie_release_date);
            moviePlot = findViewById(R.id.movie_release_plot);

            if (null != movies) {
                id = movies.getId();
                movieTitle.setText(movies.getTitle());

                if (movies.getPoster_path().length() > 300) {
                    byte[] decodedString = Base64.decode(movies.getPoster_path(), Base64.DEFAULT);
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    movieImg.setImageBitmap(decodedByte);
                } else {
                    Picasso.with(this)
                            .load(Constants.IMAGE_URL + movies.getPoster_path())
                            .placeholder(R.drawable.img_placeholder)
                            .error(R.drawable.img_error)
                            .into(movieImg);
                }

                movieRating.setText(movies.getVote_average());
                movieReleaseDate.setText(movies.getRelease_date());
                moviePlot.setText(movies.getOverview());
                initViews();
            }
        } else {
            Toast.makeText(this, "No Movie Info Found", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void initViews() {
        mDb = AppDatabase.getInstance(getApplicationContext());
        favoriteLabel = findViewById(R.id.favorite_label);
        btnFavorite = findViewById(R.id.favorite_button);

        reviews_root = findViewById(R.id.reviews_root);
        trailer_root = findViewById(R.id.trailer_root);
        trailersList = new ArrayList<>();
        reviewsList = new ArrayList<>();
        noTrailerAvailable = findViewById(R.id.tv_no_trailer_available);
        noReviewsAvailable = findViewById(R.id.tv_no_reviews_available);
        setUpFavoriteButtonImage();

        btnFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFavorite) {
                    isFavorite = false;
                    deleteFavorite();
                } else {
                    isFavorite = true;
                    insertFavorite();
                }
            }
        });
    }

    private void insertFavorite() {
        String title = movieTitle.getText().toString();
        String releaseDate = movieReleaseDate.getText().toString();
        byte[] imageByte = toByte(((BitmapDrawable) movieImg.getDrawable()).getBitmap());
        String rating = movieRating.getText().toString();
        String overview = moviePlot.getText().toString();

        final Favorites favoritesObj = new Favorites(id, title, releaseDate, imageByte, rating, overview, isFavorite);

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDb.favoriteDao().insertTask(favoritesObj);
                Log.e(TAG, "run: insertFavorite Successfully");
            }
        });
    }

    private void deleteFavorite() {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                Favorites favorites = mDb.favoriteDao().loadFavoriteByIdWithNoLiveData(id);
                mDb.favoriteDao().deleteTask(favorites);
                finish();
            }
        });

    }

    private void setUpFavoriteButtonImage() {
        AddTaskViewModelFactory factory = new AddTaskViewModelFactory(mDb, id);
        final AddFavoriteViewModel viewModel = ViewModelProviders.of(this, factory).get(AddFavoriteViewModel.class);
        viewModel.getTask().observe(DetailActivity.this, new Observer<Favorites>() {
            @Override
            public void onChanged(@Nullable Favorites favorites) {
                Log.e(TAG, "run: setUpFavoriteButtonImage Successfully");
                if (favorites != null) {
                    isFavorite = favorites.getIs_favorite();
                    checkFavorite();
                } else {
                    isFavorite = false;
                    checkFavorite();
                }
            }
        });

    }

    private void checkFavorite() {
        if (isFavorite) {
            btnFavorite.setImageResource(R.drawable.ic_star_yellow);
        } else {
            btnFavorite.setImageResource(R.drawable.ic_star_gray);
        }
    }

    private void apiCallsIfNetworkIsAvailable() {
        if (NetworkUtils.isNetworkAvailable(this)) {
            getTrailersApi(id);
            getReviewsApi(id);
        } else {
            Toast.makeText(this, "No network is found", Toast.LENGTH_SHORT).show();
        }
    }
    // I searched it from Retrofit docs
    private void getTrailersApi(int id) {
        RetrofitClient.getInstance().getService().getMovieTrailer(id, Constants.API_KEY).enqueue(new Callback<BaseModel<Trailers>>() {
            @Override
            public void onResponse(Call<BaseModel<Trailers>> call, Response<BaseModel<Trailers>> response) {
                Log.e(TAG, "onResponse: Successful getTrailersApi");
                if (response.body() != null) {
                    trailersList.clear();
                    trailersList.addAll(response.body().getResults());
                    makeTrailersList(trailersList);
                }
            }

            @Override
            public void onFailure(Call<BaseModel<Trailers>> call, Throwable t) {
                Log.e(TAG, "onFailure: getTrailersApi " + t.getMessage());
                Toast.makeText(DetailActivity.this, "Please Re-Try", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // I searched it from Retrofit docs
    private void getReviewsApi(int id) {
        RetrofitClient.getInstance().getService().getMovieReviews(id, Constants.API_KEY).enqueue(new Callback<BaseModel<Reviews>>() {
            @Override
            public void onResponse(Call<BaseModel<Reviews>> call, Response<BaseModel<Reviews>> response) {
                Log.e(TAG, "onResponse: Successful getReviewsApi");
                if (response.body() != null) {
                    reviewsList.clear();
                    reviewsList.addAll(response.body().getResults());
                    makeReviewsList(reviewsList);
                }
            }

            @Override
            public void onFailure(Call<BaseModel<Reviews>> call, Throwable t) {
                Log.e(TAG, "onFailure: getReviewsApi" + t.getMessage());
                Toast.makeText(DetailActivity.this, "Please Re-Try", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // I searched it from StackOverFlow
    private void makeReviewsList(List<Reviews> reviews) {
        if (reviews.size() == 0) {
            showNoReviewsAvailable();
            return;
        }

        LayoutInflater inflater = LayoutInflater.from(this);
        hideNoReviewAvailable();
        for (int i = 0; i < reviews.size(); i++) {
            Reviews review = reviews.get(i);
            View v = inflater.inflate(R.layout.reviews_item_view, reviews_root, false);
            TextView tvContent = v.findViewById(R.id.tv_reviews);
            tvContent.setText(review.getContent());
            reviews_root.addView(v);
        }
    }

    // I searched it from StackOverFlow
    private void makeTrailersList(List<Trailers> videos) {

        if (videos.size() == 0) {
            showNoTrailerAvailable();
            return;
        }

        LayoutInflater inflater = LayoutInflater.from(this);
        hideNoTrailerAvailable();
        for (int i = 0; i < videos.size(); i++) {
            final Trailers trailers = videos.get(i);
            View view = inflater.inflate(R.layout.video_item_view, trailer_root, false);
            TextView tvTitle = view.findViewById(R.id.tv_trailer_title);
            tvTitle.setText("Trailer No. " + (i + 1));
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.YOUTUBE_URL + trailers.getKey()));
                    startActivity(intent);
                }
            });
            trailer_root.addView(view);
        }
    }

    private void showNoTrailerAvailable() {
        noTrailerAvailable.setVisibility(View.VISIBLE);
    }

    private void hideNoTrailerAvailable() {
        noTrailerAvailable.setVisibility(View.GONE);
    }

    private void showNoReviewsAvailable() {
        noReviewsAvailable.setVisibility(View.VISIBLE);
    }

    private void hideNoReviewAvailable() {
        noReviewsAvailable.setVisibility(View.GONE);
    }

    // I searched it from StackOverFlow
    public static byte[] toByte(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, outputStream);
        return outputStream.toByteArray();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
