package com.example.farhan.popular_movies.model;

import android.os.Parcel;
import android.os.Parcelable;

// i have used Parcelable Interface in my other Projects that's how i know
public class Movies implements Parcelable {

    private String movieTitle;
    private String movieReleaseDate;
    private String movieImgUrl;
    private String movieAverageVote;
    private String moviePlot;

    public Movies(String movieTitle, String movieReleaseDate, String movieImgUrl, String movieAverageVote, String moviePlot) {
        this.movieTitle = movieTitle;
        this.movieReleaseDate = movieReleaseDate;
        this.movieImgUrl = movieImgUrl;
        this.movieAverageVote = movieAverageVote;
        this.moviePlot = moviePlot;
    }

    protected Movies(Parcel in) {
        movieTitle = in.readString();
        movieReleaseDate = in.readString();
        movieImgUrl = in.readString();
        movieAverageVote = in.readString();
        moviePlot = in.readString();
    }

    public static final Creator<Movies> CREATOR = new Creator<Movies>() {
        @Override
        public Movies createFromParcel(Parcel in) {
            return new Movies(in);
        }

        @Override
        public Movies[] newArray(int size) {
            return new Movies[size];
        }
    };

    public String getMovieTitle() {
        return movieTitle;
    }

    public String getMovieReleaseDate() {
        return movieReleaseDate;
    }

    public String getMovieImgUrl() {
        return movieImgUrl;
    }

    public String getMovieAverageVote() {
        return movieAverageVote;
    }

    public String getMoviePlot() {
        return moviePlot;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(movieTitle);
        dest.writeString(movieReleaseDate);
        dest.writeString(movieImgUrl);
        dest.writeString(movieAverageVote);
        dest.writeString(moviePlot);
    }
}
