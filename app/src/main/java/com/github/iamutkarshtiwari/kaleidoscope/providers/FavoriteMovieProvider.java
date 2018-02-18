package com.github.iamutkarshtiwari.kaleidoscope.providers;

import android.content.Context;
import android.net.Uri;


import com.github.iamutkarshtiwari.kaleidoscope.BuildConfig;
import com.github.iamutkarshtiwari.kaleidoscope.models.Movie;

import java.util.List;

import nl.littlerobots.cupboard.tools.provider.CupboardContentProvider;
import nl.littlerobots.cupboard.tools.provider.UriHelper;

import static nl.qbusict.cupboard.CupboardFactory.cupboard;

/**
 * Created by utkarshtiwari on 17/02/18.
 */
public class FavoriteMovieProvider extends CupboardContentProvider {

    public static final String AUTHORITY = BuildConfig.APPLICATION_ID + ".provider";
    public static final String DB_NAME = "movies.db";

    static {
        cupboard().register(Movie.class);
    }

    public FavoriteMovieProvider() {
        super(AUTHORITY, DB_NAME, 1);
    }

    public static Movie getMovie(Context context, long id) {
        UriHelper uriHelper = UriHelper.with(FavoriteMovieProvider.AUTHORITY);
        Uri moviesUri = uriHelper.getUri(Movie.class);
        return cupboard().withContext(context).query(moviesUri, Movie.class).withSelection("id = ?", "" + id).get();
    }

    public static void deleteMovie(Context context, long id) {
        UriHelper uriHelper = UriHelper.with(FavoriteMovieProvider.AUTHORITY);
        Uri moviesUri = uriHelper.getUri(Movie.class);
        cupboard().withContext(context).delete(moviesUri, "id = ?", id + "");
    }

    public static void putMovie(Context context, Movie mMovie) {
        UriHelper uriHelper = UriHelper.with(FavoriteMovieProvider.AUTHORITY);
        Uri movieUri = uriHelper.getUri(Movie.class);
        cupboard().withContext(context).put(movieUri, mMovie);
    }

    public static List<Movie> getFavorites(Context context){
        UriHelper uriHelper = UriHelper.with(FavoriteMovieProvider.AUTHORITY);
        Uri movieUri = uriHelper.getUri(Movie.class);
        return cupboard().withContext(context).query(movieUri, Movie.class).list();
    }
}