package com.github.iamutkarshtiwari.kaleidoscope.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.iamutkarshtiwari.kaleidoscope.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.movie_image) ImageView movieImage;
    @BindView(R.id.favourite) ImageView isFav;
    @BindView(R.id.movie_price) TextView movieRating;
    @BindView(R.id.movie_name) TextView movieName;
    @BindView(R.id.movie_year) TextView movieYear;
    @BindView(R.id.movie_genres) TextView movieGenres;

    public MovieViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
