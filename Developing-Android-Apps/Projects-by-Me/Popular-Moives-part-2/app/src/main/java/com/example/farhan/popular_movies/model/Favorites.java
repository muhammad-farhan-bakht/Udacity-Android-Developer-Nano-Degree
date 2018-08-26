package com.example.farhan.popular_movies.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "favorites")
public class Favorites {

    @PrimaryKey
    private int id;
    private String title;
    private String release_date;
    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    private byte[] image;
    private String vote_average;
    private String overview;
    private Boolean is_favorite;

    public Favorites(int id, String title, String release_date, byte[] image, String vote_average, String overview, Boolean is_favorite) {
        this.id = id;
        this.title = title;
        this.release_date = release_date;
        this.image = image;
        this.vote_average = vote_average;
        this.overview = overview;
        this.is_favorite = is_favorite;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
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

    public Boolean getIs_favorite() {
        return is_favorite;
    }

    public void setIs_favorite(Boolean is_favorite) {
        this.is_favorite = is_favorite;
    }
}
