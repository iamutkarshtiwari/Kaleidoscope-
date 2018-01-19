package com.github.iamutkarshtiwari.kaleidoscope.network;

import android.util.Log;

import com.github.iamutkarshtiwari.kaleidoscope.models.ResponseList;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.HashMap;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by utkarshtiwari on 13/01/18.
 */

public class NetworkService {

    private static final String TAG = "KALEIDOSCOPE";
    private static final int DISCOVERY_LIST = 1;
    private static final int ORDER_BY_POPULARITY = 2;
    private static final int ORDER_BY_TOP_RATED = 3;

    private TheMovieDbInterface theMovieDbApi;
    private HashMap<Integer, Observable<ResponseList>> mObservableCache = new HashMap<>();

    public NetworkService() {
        this(ApiBase.BASE_URL);
    }

    public NetworkService(String baseUrl) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiBase.BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        theMovieDbApi = retrofit.create(TheMovieDbInterface.class);
    }


    public Observable<ResponseList> getPreparedObservable(int listType) {
        Observable<ResponseList> mUnPreparedObservable = null;
        Observable<ResponseList> mPreparedObservable = null;

        try {
            if (getFromCache(listType) == null) {
                if (listType == DISCOVERY_LIST) {
                    Log.e(TAG, "Api Called");
                    mUnPreparedObservable = getAPI().getDiscoverMovies(1, ApiBase.LOCALE_EN_US, ApiBase.API_KEY, ApiBase.POPULARITY_ORDER_DESC, false, false);
                } else if (listType == ORDER_BY_POPULARITY) {
                    mUnPreparedObservable = getAPI().getPopularMovies(1, "en-US", ApiBase.API_KEY);
                } else if (listType == ORDER_BY_TOP_RATED) {
                    mUnPreparedObservable = getAPI().getTopRatedMovies(1, "en-US", ApiBase.API_KEY);
                }

                mPreparedObservable = mUnPreparedObservable.subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread());
                mPreparedObservable.publish().autoConnect();
                addToCache(listType, mPreparedObservable);
            } else {

                Log.e(TAG, "Called from cache");
                mPreparedObservable = getFromCache(listType);
            }
        } catch (Throwable e) {
            Log.e(TAG, e.toString());
        }
        return mPreparedObservable;
    }

    public TheMovieDbInterface getAPI() {
        return theMovieDbApi;
    }


    public void addToCache(int id, Observable<ResponseList> observable) {
        mObservableCache.put(id, observable);
    }

    public void removeFromCache(int id) {
        mObservableCache.remove(id);
    }

    public Observable<ResponseList> getFromCache(int id) {
        return mObservableCache.get(id);
    }


}