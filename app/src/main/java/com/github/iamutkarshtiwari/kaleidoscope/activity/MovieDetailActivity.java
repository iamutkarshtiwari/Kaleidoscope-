package com.github.iamutkarshtiwari.kaleidoscope.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.iamutkarshtiwari.kaleidoscope.R;
import com.github.iamutkarshtiwari.kaleidoscope.models.Genres;
import com.github.iamutkarshtiwari.kaleidoscope.models.Movie;
import com.github.iamutkarshtiwari.kaleidoscope.models.Review;
import com.github.iamutkarshtiwari.kaleidoscope.models.Trailer;
import com.github.iamutkarshtiwari.kaleidoscope.network.ApiBase;
import com.github.iamutkarshtiwari.kaleidoscope.providers.FavoriteMovieProvider;
import com.github.iamutkarshtiwari.kaleidoscope.tasks.CommonAsyncTask;
import com.github.iamutkarshtiwari.kaleidoscope.tasks.ReviewsAsyncTask;
import com.github.iamutkarshtiwari.kaleidoscope.tasks.TrailersAsyncTask;
import com.github.iamutkarshtiwari.kaleidoscope.utils.Constants;
import com.github.iamutkarshtiwari.kaleidoscope.utils.MyTextView;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class MovieDetailActivity extends AppCompatActivity  {
    private Movie mMovie;
    private boolean mAddedInFavorite;
    private ArrayList<Trailer> mTrailers;
    private ArrayList<Review> mReviews;
    private Trailer mMainTrailer;
    private TrailersAsyncTask trailersAsyncTask;
    private ReviewsAsyncTask reviewsAsyncTask;

    @BindView(R.id.toolbar_title)
    MyTextView toolbarTitle;
    @BindView(R.id.rating_bar)
    MaterialRatingBar ratingBar;
    @BindView(R.id.movie_title)
    MyTextView movieTitle;
    @BindView(R.id.movie_year)
    MyTextView movieReleaseYear;
    @BindView(R.id.movie_rating)
    MyTextView movieRating;
    @BindView(R.id.movie_popularity)
    MyTextView moviePopularity;
    @BindView(R.id.movie_votes)
    MyTextView movieVotes;
    @BindView(R.id.movie_genres)
    MyTextView movieGenres;
    @BindView(R.id.movie_image)
    ImageView moviePoster;
    @BindView(R.id.synopsis_data)
    MyTextView movieSynopsis;
    @BindView(R.id.favourite)
    ImageView favoriteLabel;
    @BindView(R.id.movie_detail_review_progress_bar)
    ProgressBar mReviewsProgressBar;
    @BindView(R.id.movie_detail_trailer_progress_bar)
    ProgressBar mTrailersProgressBar;
    @BindView(R.id.movie_detail_trailer_container)
    LinearLayout mTrailerLinearLayout;
    @BindView(R.id.empty_trailer_list)
    TextView mDetailMovieEmptyTrailers;
    @BindView(R.id.movie_detail_review_container)
    LinearLayout mReviewLinearLayout;
    @BindView(R.id.empty_review_list)
    TextView mDetailMovieEmptyReviews;
    @BindView(R.id.favourite_fab)
    FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        toolbarTitle.setText(R.string.movie);

        Log.e("Kaledioscope", "Movie mMovei initialized");
        Movie mMovie = getIntent().getParcelableExtra("movie_data");
        displayMovieData(mMovie);

        if (mMovie != null) {
            mAddedInFavorite = FavoriteMovieProvider.getMovie(this, mMovie.getId()) != null;
            int visibility = mAddedInFavorite ? View.VISIBLE : View.GONE;
            favoriteLabel.setVisibility(visibility);

            int id = mAddedInFavorite ? R.drawable.ic_star_fill : R.drawable.ic_star_hollow;
            floatingActionButton.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), id));
        }

        if (savedInstanceState != null) {
            mTrailers = savedInstanceState.getParcelableArrayList(Constants.TRAILERS);
            mReviews = savedInstanceState.getParcelableArrayList(Constants.REVIEWS);
            mAddedInFavorite = savedInstanceState.getBoolean(Constants.FAVORITE);
            mMainTrailer = savedInstanceState.getParcelable(Constants.MAIN_TRAILER);
            addTrailerViews(mTrailers);
            addReviewViews(mReviews);
        } else {
            executeTasks(mMovie);
        }


    }


    public void displayMovieData(Movie movie) {

        String imageUrl = "";
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            imageUrl = ApiBase.POSTER_BASE + movie.getPosterPath();
        } else {
            imageUrl = Constants.IMAGE_MOVIE_URL + Constants.IMAGE_SIZE_W780 + movie.getBackdropPath();
        }

        // Downloads the movie image from url
        Picasso.with(this)
                .load(imageUrl)
                .placeholder(R.drawable.ic_movie_placeholder)
                .fit()
                .error(this.getResources().getDrawable(R.drawable.no_image_found))
                .into(moviePoster);

        Resources res = getResources();
        // Format rating to one decimal place
        DecimalFormat formatter = new DecimalFormat("#.#");
        String rating = formatter.format(movie.getVoteAverage());
        movieRating.setText(res.getString(R.string.rating_format, rating));
        ratingBar.setRating(Float.parseFloat(formatter.format(movie.getVoteAverage())));

        formatter = new DecimalFormat("#");
        moviePopularity.setText(formatter.format(movie.getPopularity()));
        movieTitle.setText(movie.getTitle());
        movieVotes.setText(String.valueOf(movie.getVoteCount()));
        String releaseDate = movie.getReleaseDate();
        movieReleaseYear.setText(String.format("%s", releaseDate.split("-")[0]));

        // Generate genres label
        ArrayList<Integer> genreList = movie.getGenreIds();
        String genreLabel = "";
        String separator = "";
        for (int genre : genreList) {
            genreLabel += separator + Genres.GENRE_MAP.get(genre);
            separator = ", ";
        }
        movieGenres.setText(String.format("%s", genreLabel));
        movieSynopsis.setText(movie.getOverview());
    }

    private void executeTasks(Movie mMovieData) {

        if (mMovieData == null) {
            return;
        }

        trailersAsyncTask = new TrailersAsyncTask(mMovieData.getId(), mTrailersProgressBar, new CommonAsyncTask.FetchDataListener<Trailer>() {
            @Override
            public void onFetchData(ArrayList<Trailer> resultList) {
//                Log.d(TAG, "TrailersAsyncTask.onFetchData() returned: " + resultList);
                mTrailers = resultList;
                addTrailerViews(mTrailers);
            }
        });

        reviewsAsyncTask = new ReviewsAsyncTask(mMovieData.getId(), mReviewsProgressBar, new CommonAsyncTask.FetchDataListener<Review>() {
            @Override
            public void onFetchData(ArrayList<Review> resultList) {
//                Log.d(TAG, "ReviewsAsyncTask.onFetchData() returned: " + resultList);
                mReviews = resultList;
                addReviewViews(mReviews);
            }
        });

        trailersAsyncTask.execute();
        reviewsAsyncTask.execute();

    }


    /**
     * Dynamically added trailers views in the view container
     * @param resultList list of Trailer
     */
    private void addTrailerViews(List<Trailer> resultList) {

        final LayoutInflater inflater = LayoutInflater.from(this);

        boolean emptyList = resultList == null || resultList.isEmpty();

        if (resultList != null && !resultList.isEmpty()) {
            for (Trailer trailer : resultList) {
                final String key = trailer.key;
                final View trailerView = inflater.inflate(R.layout.list_item_trailer, mTrailerLinearLayout, false);
                ImageView trailerImage = ButterKnife.findById(trailerView, R.id.trailer_poster_image_view);
                ImageView playImage = ButterKnife.findById(trailerView, R.id.play_trailer_image_view);
                playImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openYouTubeIntent(key);
                    }
                });

                Picasso.with(this)
                        .load(String.format(Constants.YOU_TUBE_IMG_URL, trailer.key))
                        .placeholder(R.drawable.ic_movie_placeholder)
                        .error(R.drawable.ic_movie_placeholder)
                        .into(trailerImage);
                mTrailerLinearLayout.addView(trailerView);
            }
        }
        mDetailMovieEmptyTrailers.setVisibility(emptyList ? View.VISIBLE : View.GONE);

    }

    /**
     * Dynamically added reviews views in the view container
     * @param resultList list of Review
     */
    private void addReviewViews(List<Review> resultList) {

        final LayoutInflater inflater = LayoutInflater.from(this);
        boolean emptyList = resultList == null || resultList.isEmpty();

        if (!emptyList) {
            for (Review review : resultList) {
                final View reviewView = inflater.inflate(R.layout.list_item_review, mReviewLinearLayout, false);
                TextView reviewAuthor = reviewView.findViewById(R.id.list_item_review_author_text_view);
                TextView reviewContent = reviewView.findViewById(R.id.list_item_review_content_text_view);
                reviewAuthor.setText(review.author);
                reviewContent.setText(review.content);
                mReviewLinearLayout.addView(reviewView);
            }
        }
        mDetailMovieEmptyReviews.setVisibility(emptyList ? View.VISIBLE : View.GONE);
    }

    private void openYouTubeIntent(String key) {
        Intent youTubeIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.YOU_TUBE_VIDEO_URL + key));
        youTubeIntent.putExtra("force_fullscreen", true);
        startActivity(youTubeIntent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        // disable going back to the MainActivity
        super.onBackPressed();
    }

    @Override
    public void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(Constants.FAVORITE, mAddedInFavorite);
        outState.putParcelableArrayList(Constants.TRAILERS, mTrailers);
        outState.putParcelableArrayList(Constants.REVIEWS, mReviews);
    }

    @OnClick(R.id.favourite_fab)
    public void toggleFavorite() {
        //TODO Mladen add fade out/fade in animation for FAB
        if (!mAddedInFavorite) {
            FavoriteMovieProvider.putMovie(this, mMovie);
        } else {
            FavoriteMovieProvider.deleteMovie(this, mMovie.getId());
        }
        mAddedInFavorite = !mAddedInFavorite;
        int id = mAddedInFavorite ? R.drawable.ic_star_fill : R.drawable.ic_star_hollow;
        floatingActionButton.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), id));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (trailersAsyncTask != null) {
            trailersAsyncTask.cancel(true);
            trailersAsyncTask = null;
        }

        if (reviewsAsyncTask != null) {
            reviewsAsyncTask.cancel(true);
            reviewsAsyncTask = null;
        }
    }

}
