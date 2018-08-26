package com.example.farhan.popular_movies.model;

import android.os.Parcel;
import android.os.Parcelable;

// i have used Parcelable Interface in my other Projects that's how i know
public class Movies implements Parcelable {

    private String title;
    private String release_date;
    private String poster_path;
    private String vote_average;
    private String overview;
    private int id;

    public Movies() {
    }

    public Movies(String title, String release_date, String poster_path, String vote_average, String overview, int id) {
        this.title = title;
        this.release_date = release_date;
        this.poster_path = poster_path;
        this.vote_average = vote_average;
        this.overview = overview;
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public String getVote_average() {
        return vote_average;
    }

    public void setVote_average(String vote_average) {
        this.vote_average = vote_average;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    protected Movies(Parcel in) {
        title = in.readString();
        release_date = in.readString();
        poster_path = in.readString();
        vote_average = in.readString();
        overview = in.readString();
        id = in.readInt();
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(release_date);
        dest.writeString(poster_path);
        dest.writeString(vote_average);
        dest.writeString(overview);
        dest.writeInt(id);
    }
}
