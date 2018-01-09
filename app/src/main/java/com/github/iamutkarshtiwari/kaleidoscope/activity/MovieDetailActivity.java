package com.github.iamutkarshtiwari.kaleidoscope.activity;

import android.content.res.Resources;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.github.iamutkarshtiwari.kaleidoscope.R;
import com.github.iamutkarshtiwari.kaleidoscope.models.Movie;
import com.github.iamutkarshtiwari.kaleidoscope.network.ApiBase;
import com.github.iamutkarshtiwari.kaleidoscope.utils.MyTextView;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieDetailActivity extends AppCompatActivity {

    @BindView(R.id.toolbar_title) MyTextView toolbarTitle;
    @BindView(R.id.favourite_fab) FloatingActionButton fab;
//    @BindView(R.id.movie_title) MyTextView movieTitle;
//    @BindView(R.id.movie_release_date) MyTextView movieReleaseDate;
    @BindView(R.id.movie_rating) MyTextView movieRating;
    @BindView(R.id.movie_popularity) MyTextView moviePopularity;
    @BindView(R.id.movie_plot) MyTextView moviePlot;
    @BindView(R.id.movie_image) ImageView moviePoster;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbarTitle.setText(R.string.movie);

        Movie movie = (Movie) getIntent().getParcelableExtra("parcel_data");
        displayMovieData(movie);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fab.setSelected(!fab.isSelected());
                fab.setImageResource(fab.isSelected() ? R.drawable.ic_star_fill : R.drawable.ic_star_hollow);
                Drawable drawable = fab.getDrawable();
                if (drawable instanceof Animatable) {
                    ((Animatable) drawable).start();
                }
            }
        });
    }

    public void displayMovieData(Movie movie) {

        // Downloads the movie image from url
        Picasso.with(this)
                .load(ApiBase.POSTER_BASE + movie.getPosterPath())
                .fit()
                .error(this.getResources().getDrawable(R.drawable.no_image_found))
                .into(moviePoster);

        Resources res = getResources();
        // Format rating to one decimal place
        DecimalFormat formatter = new DecimalFormat("#.#");
        String rating = formatter.format(movie.getVoteAverage());
        movieRating.setText(res.getString(R.string.rating_format, rating));
        formatter = new DecimalFormat("#");
        moviePopularity.setText(formatter.format(movie.getPopularity()));
        String releaseDate = movie.getReleaseDate();
//        movieReleaseDate.setText(String.format("%s", releaseDate.split("-")[0]));
        moviePlot.setText(movie.getOverview());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
