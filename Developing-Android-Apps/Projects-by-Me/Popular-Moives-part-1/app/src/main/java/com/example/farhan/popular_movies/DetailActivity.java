package com.example.farhan.popular_movies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.farhan.popular_movies.model.Movies;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Movies movies = extras.getParcelable("moviesObj");

            TextView movieTitle = findViewById(R.id.movie_title);
            ImageView movieImg = findViewById(R.id.movie_image);
            TextView movieRating = findViewById(R.id.movie_rating);
            TextView movieReleaseDate = findViewById(R.id.movie_release_date);
            TextView moviePlot = findViewById(R.id.movie_release_plot);

            if (null != movies) {
                movieTitle.setText(movies.getMovieTitle());
                Picasso.with(this)
                        .load(movies.getMovieImgUrl())
                        .placeholder(R.drawable.img_placeholder)
                        .error(R.drawable.img_error)
                        .into(movieImg);
                movieRating.setText(movies.getMovieAverageVote());
                movieReleaseDate.setText(movies.getMovieReleaseDate());
                moviePlot.setText(movies.getMoviePlot());
            }
        }else {
            Toast.makeText(this, "No Movie Info Found", Toast.LENGTH_SHORT).show();
        }
    }
}
