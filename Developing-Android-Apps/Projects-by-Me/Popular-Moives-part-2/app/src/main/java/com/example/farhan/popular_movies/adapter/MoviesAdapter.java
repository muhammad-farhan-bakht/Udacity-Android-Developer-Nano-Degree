package com.example.farhan.popular_movies.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.farhan.popular_movies.R;
import com.example.farhan.popular_movies.model.Movies;
import com.example.farhan.popular_movies.utils.Constants;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.movieViewHolder> {

    private Context context;
    private List<Movies> moviesArrayList;
    private MoviesAdapterOnClickHandler mClickHandler;

    public interface MoviesAdapterOnClickHandler {
        void onClick(Movies moviesObj);
    }

    public void setMoviesAdapterOnClickHandler(MoviesAdapterOnClickHandler mClickHandler) {
        this.mClickHandler = mClickHandler;
    }

    @Override
    public movieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        int layoutIdForListItem = R.layout.item_view;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForListItem, parent, false);
        return new movieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(movieViewHolder holder, final int position) {
        final Movies movies = moviesArrayList.get(position);

        if (movies.getPoster_path().length() > 300) {
            byte[] decodedString = Base64.decode(movies.getPoster_path(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            holder.moviePoster.setImageBitmap(decodedByte);

        } else {
            Picasso.with(context)
                    .load(Constants.IMAGE_URL + movies.getPoster_path())
                    .placeholder(R.drawable.img_placeholder)
                    .error(R.drawable.img_error)
                    .into(holder.moviePoster);

        }

        holder.moviePoster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mClickHandler != null) {
                    mClickHandler.onClick(movies);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (null == moviesArrayList) return 0;
        return moviesArrayList.size();
    }

    public class movieViewHolder extends RecyclerView.ViewHolder {
        ImageView moviePoster;

        public movieViewHolder(View itemView) {
            super(itemView);
            moviePoster = itemView.findViewById(R.id.movie_Poster);

        }
    }

    public void setMovieData(List<Movies> moviesArrayList) {
        this.moviesArrayList = moviesArrayList;
        notifyDataSetChanged();
    }
}
