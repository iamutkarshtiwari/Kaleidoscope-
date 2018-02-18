package com.github.iamutkarshtiwari.kaleidoscope.utils;

import com.github.iamutkarshtiwari.kaleidoscope.network.TheMovieDbInterface;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by utkarshtiwari on 17/02/18.
 */

public class HttpUtil {

    private static TheMovieDbInterface service;
    static {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.MOVIE_URL).addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(TheMovieDbInterface.class);
    }

    public static TheMovieDbInterface getService() {
        return service;
    }
}