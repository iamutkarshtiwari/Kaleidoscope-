package com.github.iamutkarshtiwari.kaleidoscope.models;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
public class Movie implements Parcelable {
    private Double vote_average;
    private String title;
    private String poster_path;
    private String backdrop_path;
    private String release_date;
    private String overview;
    private String original_language;
    private int revenue;
    private int runtime;
    private int budget;
    private ArrayList<Integer> genre_ids;

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public int getRevenue() {
        return revenue;
    }

    public void setRevenue(int revenue) {
        this.revenue = revenue;
    }

    public int getRuntime() {
        return runtime;
    }

    public void setRuntime(int runtime) {
        this.runtime = runtime;
    }

    public int getBudget() {
        return budget;
    }

    public void setBudget(int budget) {
        this.budget = budget;
    }

    public Double getVote_average() {
        return vote_average;
    }

    public void setVote_average(Double vote_average) {
        this.vote_average = vote_average;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public ArrayList<Integer> getGenre_ids() {
        return genre_ids;
    }

    public void setGenre_ids(ArrayList<Integer> genre_ids) {
        this.genre_ids = genre_ids;
    }

    public String getOriginal_language() {
        return original_language;
    }

    public void setOriginal_language(String original_language) {
        this.original_language = original_language;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public void setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }

    protected Movie(Parcel in) {
        vote_average = in.readByte() == 0x00 ? null : in.readDouble();
        title = in.readString();
        poster_path = in.readString();
        backdrop_path = in.readString();
        release_date = in.readString();
        overview = in.readString();
        original_language = in.readString();
        revenue = in.readInt();
        runtime = in.readInt();
        budget = in.readInt();
        if (in.readByte() == 0x01) {
            genre_ids = new ArrayList<Integer>();
            in.readList(genre_ids, Integer.class.getClassLoader());
        } else {
            genre_ids = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (vote_average == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeDouble(vote_average);
        }
        dest.writeString(title);
        dest.writeString(poster_path);
        dest.writeString(backdrop_path);
        dest.writeString(release_date);
        dest.writeString(overview);
        dest.writeString(original_language);
        dest.writeInt(revenue);
        dest.writeInt(runtime);
        dest.writeInt(budget);
        if (genre_ids == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(genre_ids);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}