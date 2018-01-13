package com.github.iamutkarshtiwari.kaleidoscope.network;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.Toast;

import com.github.iamutkarshtiwari.kaleidoscope.R;
import com.github.iamutkarshtiwari.kaleidoscope.activity.HomeActivity;
import com.github.iamutkarshtiwari.kaleidoscope.adapters.HomeRecyclerAdapter;
import com.github.iamutkarshtiwari.kaleidoscope.fragments.HomeFragment;
import com.github.iamutkarshtiwari.kaleidoscope.models.ResponseList;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.ContentValues.TAG;

/**
 * Created by utkarshtiwari on 13/01/18.
 */

public class NetworkLoader {

    private static final String TAG = "KALEIDOSCOPE";
    private static final int DISCOVERY_LIST = 1;
    private static final int ORDER_BY_POPULARITY = 2;
    private static final int ORDER_BY_TOP_RATED = 3;

    private Context context;
    private CompositeDisposable mCompositeDisposable;
    private TheMovieDbInterface mRequestInterface;
    private OnResultReceived onResultReceived;

    public interface OnResultReceived {
        void getResult(ResponseList responseList);
    }

    public NetworkLoader(Context context) {
        this.context = context;
    }

    public void setOnResultListener(OnResultReceived resultReceived) {
        this.onResultReceived = resultReceived;
    }

    private CompositeDisposable getCompositeDisposable() {
        if (mCompositeDisposable == null || mCompositeDisposable.isDisposed()) {
            mCompositeDisposable = new CompositeDisposable();
        }
        return mCompositeDisposable;
    }

    public void loadFromNetwork(int listType) {
        mCompositeDisposable = getCompositeDisposable();

        if (mRequestInterface == null) {
            mRequestInterface = new Retrofit.Builder()
                    .baseUrl(ApiBase.BASE_URL)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build().create(TheMovieDbInterface.class);
        }

        String[] tabNames = context.getResources().getStringArray(R.array._1st_tab_names);
        String tabTitle = "";

        Observable<ResponseList> responseListObservable = null;
        if (listType == DISCOVERY_LIST) {
            responseListObservable = mRequestInterface.getDiscoverMovies(1, ApiBase.LOCALE_EN_US, ApiBase.API_KEY, ApiBase.POPULARITY_ORDER_DESC, false, false);
            tabTitle = tabNames[0];
        } else if (listType == ORDER_BY_POPULARITY) {
            responseListObservable = mRequestInterface.getPopularMovies(1, "en-US", ApiBase.API_KEY);
            tabTitle = tabNames[1];
        } else if (listType == ORDER_BY_TOP_RATED) {
            responseListObservable = mRequestInterface.getTopRatedMovies(1, "en-US", ApiBase.API_KEY);
            tabTitle = tabNames[2];
        }

        ((HomeActivity) context).updateTabTitle(0, tabTitle);
        Toast.makeText(context, listType + "", Toast.LENGTH_SHORT).show();

        try {
            mCompositeDisposable.add(responseListObservable
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(this::handleResponse, this::handleError));
        } catch (NullPointerException e) {
            Log.e(TAG, "You passed a wrong parameter");
        }
    }

    private void handleResponse(ResponseList responseList) {
        onResultReceived.getResult(responseList);
    }

    private void handleError(Throwable error) {
        Toast.makeText(context, "Error " + error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        Log.e("ERROR: ", error.getLocalizedMessage());
    }

    public void  clearCompositeDisposable() {
        if (!(mCompositeDisposable == null || mCompositeDisposable.isDisposed())) {
            mCompositeDisposable.dispose();
        }
    }


}


