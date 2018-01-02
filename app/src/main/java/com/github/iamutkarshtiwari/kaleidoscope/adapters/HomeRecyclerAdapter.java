package com.github.iamutkarshtiwari.kaleidoscope.adapters;

import android.app.Activity;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.iamutkarshtiwari.kaleidoscope.models.Movie;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.StringJoiner;

import com.github.iamutkarshtiwari.kaleidoscope.R;

public class HomeRecyclerAdapter extends RecyclerView.Adapter<MovieViewHolder> {

    private static final String POSTER_BASE = "http://image.tmdb.org/t/p/w342/";

    ArrayList<Movie> items;
    Activity activity;

    public HomeRecyclerAdapter(Activity activity, ArrayList<Movie> items) {
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
        // Set favourie tag
//        if (items.get(position).getStatus().equalsIgnoreCase("sold_out")) {
//            holder.soldOut.setVisibility(View.VISIBLE);
//        } else {
//            holder.soldOut.setVisibility(View.GONE);
//        }

        // Downloads the movie image from url
//        Uri.Builder builder = new Uri.Builder();
//        builder.authority(POSTER_BASE)
//                .appendPath();
//        String posterUrl = builder.build().toString();
//
//        Uri.Builder builder = new Uri.Builder();
//        builder.scheme("https")
//                .authority("www.myawesomesite.com")
//                .appendPath("t")
//                .appendPath("p")
//                .appendPath("w342")
//                .appendQueryParameter("type", "1")
//                .appendQueryParameter("sort", "relevance")
//                .fragment("section-name");
//        String myUrl = builder.build().toString();


        Picasso.with(activity)
                .load(POSTER_BASE + items.get(position).getPoster_path())
                .fit()
                .error(activity.getResources().getDrawable(R.drawable.image_not_found))
                .into(holder.movieImage);



        // Format price to currency style
        DecimalFormat formatter = new DecimalFormat("#.#");
        String rating = formatter.format(items.get(position).getVote_average());
        holder.movieRating.setText(rating);
        holder.movieName.setText(items.get(position).getTitle());
        String releaseDate = items.get(position).getRelease_date();
        holder.movieYear.setText(String.format("%s", releaseDate.split("-")[0]));

        // Generate genres label
        ArrayList<Integer> genreList = items.get(position).getGenre_ids();
        String genreLabel = "";
        String separator = "";
        for (int genre : genreList) {
            genreLabel = separator + genre;
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

    /**
     * Updates the adapter item list with new list
     * @param newData list
     */
    public void updateAdapterData(ArrayList<Movie> newData) {
        this.items = newData;
    }
}