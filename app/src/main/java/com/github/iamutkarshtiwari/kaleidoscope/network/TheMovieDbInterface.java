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

    @GET("discover/movie/")
    Observable<ResponseList> getMovieList(@Query("page") int page,
                                          @Query("language") String language,
                                          @Query("sort_by") String sort_by,
                                          @Query("include_adult") Boolean include_adult,
                                          @Query("include_video") Boolean include_video,
                                          @Query("api_key") String api_key);

}