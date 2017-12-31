package com.github.iamutkarshtiwari.kaleidoscope.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.iamutkarshtiwari.kaleidoscope.R;

public class MovieViewHolder extends RecyclerView.ViewHolder {

    public ImageView movieImage, soldOut;
    public TextView moviePrice, movieName, movieLikes, movieComments;

    public MovieViewHolder(View itemView) {
        super(itemView);

        movieImage = (ImageView) itemView.findViewById(R.id.movie_image);
        soldOut = (ImageView) itemView.findViewById(R.id.favourite);
        moviePrice = (TextView) itemView.findViewById(R.id.movie_price);
        movieName = (TextView) itemView.findViewById(R.id.movie_name);
        movieLikes = (TextView) itemView.findViewById(R.id.movie_likes);
        movieComments = (TextView) itemView.findViewById(R.id.movie_comments);
    }
}
