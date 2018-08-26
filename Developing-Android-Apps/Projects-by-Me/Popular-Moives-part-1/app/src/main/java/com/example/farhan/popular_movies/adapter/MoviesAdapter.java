package com.example.farhan.popular_movies.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.farhan.popular_movies.R;
import com.example.farhan.popular_movies.model.Movies;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.movieViewHolder> {

    private Context context;
    private ArrayList<Movies> moviesArrayList;
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

        Picasso.with(context)
                .load(movies.getMovieImgUrl())
                .placeholder(R.drawable.img_placeholder)
                .error(R.drawable.img_error)
                .into(holder.moviePoster);

        // I searched it from StackOverFlow
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

    public void setMovieData(ArrayList<Movies> moviesArrayList) {
        this.moviesArrayList = moviesArrayList;
        notifyDataSetChanged();
    }
}
