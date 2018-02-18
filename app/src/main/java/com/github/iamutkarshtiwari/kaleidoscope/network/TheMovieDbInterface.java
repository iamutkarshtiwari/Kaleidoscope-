package com.github.iamutkarshtiwari.kaleidoscope.network;

import com.github.iamutkarshtiwari.kaleidoscope.models.Movie;
import com.github.iamutkarshtiwari.kaleidoscope.models.ResponseList;
import com.github.iamutkarshtiwari.kaleidoscope.models.ReviewsResult;
import com.github.iamutkarshtiwari.kaleidoscope.models.TrailersResult;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.observables.ConnectableObservable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
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

    @GET("discover/movie/")
    Observable<ResponseList> getDiscoverMovies(@Query("page") int page,
                                                                     @Query("language") String language,
                                                                     @Query("api_key") String api_key,
                                                                     @Query("sort_by") String sort_by,
                                                                     @Query("include_adult") Boolean include_adult,
                                                                     @Query("include_video") Boolean include_video);

    @GET("/3/movie/{id}/videos")
    Call<TrailersResult> getTrailersResults(@Path("id") long movieId, @Query("api_key") String apiKey);

    @GET("/3/movie/{id}/reviews")
    Call<ReviewsResult> getReviewsResults(@Path("id") long movieId, @Query("api_key") String apiKey);

}