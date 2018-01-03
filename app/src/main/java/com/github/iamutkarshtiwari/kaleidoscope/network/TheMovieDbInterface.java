package com.github.iamutkarshtiwari.kaleidoscope.network;

import com.github.iamutkarshtiwari.kaleidoscope.models.Movie;
import com.github.iamutkarshtiwari.kaleidoscope.models.ResponseList;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by utkarshtiwari on 30/12/17.
 */

public interface TheMovieDbInterface {

    @GET("movie/popular/")
    Observable<ResponseList> getPopularMovies(@Query("page") int page,
                                          @Query("language") String language,
                                          @Query("api_key") String api_key);

    @GET("movie/top_rated/")
    Observable<ResponseList> getTopRatedMovies(@Query("page") int page,
                                             @Query("language") String language,
                                             @Query("api_key") String api_key);

}