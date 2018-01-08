package com.github.iamutkarshtiwari.kaleidoscope.network;

import com.github.iamutkarshtiwari.kaleidoscope.BuildConfig;

/**
 * Created by utkarshtiwari on 01/01/18.
 */

public final class ApiBase {
    // You API_KEY goes here
    public static final String API_KEY = BuildConfig.API_KEY;
    public static final String BASE_URL = "https://api.themoviedb.org/3/";
    public static final String POSTER_BASE = "http://image.tmdb.org/t/p/w342/";
    public static final String POPULARITY_ORDER_ASC = "popularity.asc";
    public static final String POPULARITY_ORDER_DESC = "popularity.desc";
    public static final String LOCALE_EN_US = "en-US";
}


