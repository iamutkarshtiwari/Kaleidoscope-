package com.github.iamutkarshtiwari.kaleidoscope.adapters;

import android.app.Activity;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.iamutkarshtiwari.kaleidoscope.models.Genres;
import com.github.iamutkarshtiwari.kaleidoscope.models.Movie;
import com.github.iamutkarshtiwari.kaleidoscope.network.ApiBase;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.StringJoiner;

import com.github.iamutkarshtiwari.kaleidoscope.R;

public class HomeRecyclerAdapter extends RecyclerView.Adapter<MovieViewHolder> {

    ArrayList<Movie> items;
    Activity activity;
    ItemClickListener mListener;

    public HomeRecyclerAdapter(Activity activity, ArrayList<Movie> items, ItemClickListener mListener) {
        this.activity = activity;
        this.items = items;
        this.mListener = mListener;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_view_movie_item, parent, false);
        return new MovieViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, final int position) {
        // Set favourie tag
//        if (items.get(position).getStatus().equalsIgnoreCase("sold_out")) {
//            holder.soldOut.setVisibility(View.VISIBLE);
//        } else {
//            holder.soldOut.setVisibility(View.GONE);
//        }

        // Downloads the movie image from url
        Picasso.with(activity)
                .load(ApiBase.POSTER_BASE + items.get(position).getPosterPath())
                .fit()
                .error(activity.getResources().getDrawable(R.drawable.no_image_found))
                .into(holder.movieImage);

        // Format price to currency style
        DecimalFormat formatter = new DecimalFormat("#.#");
        String rating = formatter.format(items.get(position).getVoteAverage());
        holder.movieRating.setText(rating);
        holder.movieName.setText(items.get(position).getTitle());
        String releaseDate = items.get(position).getReleaseDate();
        holder.movieYear.setText(String.format("%s", releaseDate.split("-")[0]));

        // Generate genres label
        ArrayList<Integer> genreList = items.get(position).getGenreIds();
        String genreLabel = "";
        String separator = "";
        for (int genre : genreList) {
            genreLabel += separator + Genres.GENRE_MAP.get(genre);
            separator = ", ";
        }
        holder.movieGenres.setText(String.format("%s", genreLabel));
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void clearAdapterData() {
        this.items.clear();
        notifyDataSetChanged();
    }

    /**
     * Updates the adapter item list with new list
     * @param newData list
     */
    public void setAdapterData(ArrayList<Movie> newData) {
        clearAdapterData();
        this.items = newData;
        notifyDataSetChanged();
    }

    public ArrayList<Movie> getAdapterData() {
        return this.items;
    }
}