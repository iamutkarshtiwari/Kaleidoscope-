package com.github.iamutkarshtiwari.kaleidoscope.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.github.iamutkarshtiwari.kaleidoscope.models.Movies;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

import com.github.iamutkarshtiwari.kaleidoscope.R;

public class HomeRecyclerAdapter extends RecyclerView.Adapter<MovieViewHolder> {

    ArrayList<Movies> items;
    Activity activity;

    public HomeRecyclerAdapter(Activity activity, ArrayList<Movies> items) {
        this.activity = activity;
        this.items = items;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_view_movie_item, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, final int position) {
        // Set sold status tag
        if (items.get(position).getStatus().equalsIgnoreCase("sold_out")) {
            holder.soldOut.setVisibility(View.VISIBLE);
        } else {
            holder.soldOut.setVisibility(View.GONE);
        }

        // Downloads the movie image from url
        Glide.with(activity)
                .load(items.get(position).getPhotoURL())
                .fitCenter()
                .error(activity.getResources().getDrawable(R.drawable.image_not_found))
                .into(holder.movieImage);

        // Format price to currency style
        DecimalFormat formatter = new DecimalFormat("#,###");
        String price = formatter.format(items.get(position).getPrice());
        holder.moviePrice.setText(activity.getResources().getString(R.string.movie_price, "$", price));
        holder.movieName.setText(items.get(position).getName());
        holder.movieLikes.setText(String.format("%s", items.get(position).getLikesCount()));
        holder.movieComments.setText(String.format("%s", items.get(position).getCommentsCount()));
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    /**
     * Updates the adapter item list with new list
     * @param newData list
     */
    public void updateAdapterData(ArrayList<Movies> newData) {
        this.items = newData;
    }
}