package com.github.iamutkarshtiwari.kaleidoscope.fragments;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.iamutkarshtiwari.kaleidoscope.R;
import com.github.iamutkarshtiwari.kaleidoscope.adapters.HomeRecyclerAdapter;
import com.github.iamutkarshtiwari.kaleidoscope.models.Movie;
import com.github.iamutkarshtiwari.kaleidoscope.models.ResponseList;
import com.github.iamutkarshtiwari.kaleidoscope.network.ApiBase;
import com.github.iamutkarshtiwari.kaleidoscope.network.TheMovieDbInterface;
import com.github.iamutkarshtiwari.kaleidoscope.utils.GridColumnCalculator;
import com.github.iamutkarshtiwari.kaleidoscope.utils.JSONParser;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeFragment extends Fragment {


    private static final String TAG = "KALEIDOSCOPE";
    private static final int ORDER_BY_POPULARITY = 1;
    private static final int ORDER_BY_TOP_RATED = 2;
    private static final int DISCOVERY_LIST = 3;

    private String mDataSource;
    private CompositeDisposable mCompositeDisposable;
    private TheMovieDbInterface mRequestInterface;
    private HomeRecyclerAdapter mAdapter;
    private GridLayoutManager mLayoutManager;

    @BindView(R.id.recycler_view) RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(
                R.layout.pager_fragment, container, false);
        this.mDataSource = getArguments().getString("data_source");
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
        ButterKnife.bind(this, view);

        if (this.mDataSource.compareToIgnoreCase("network") == 0) {
            loadFromNetwork(DISCOVERY_LIST);
        }

    }

    public void loadFromNetwork(int listType) {
        mCompositeDisposable = new CompositeDisposable();

        if (mRequestInterface == null) {
            mRequestInterface = new Retrofit.Builder()
                    .baseUrl(ApiBase.BASE_URL)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build().create(TheMovieDbInterface.class);
        }

        Observable<ResponseList> responseListObservable = null;
        if (listType == ORDER_BY_POPULARITY) {
            responseListObservable = mRequestInterface.getPopularMovies(1, "en-US", ApiBase.API_KEY);
        } else if (listType == ORDER_BY_TOP_RATED) {
            responseListObservable = mRequestInterface.getTopRatedMovies(1, "en-US", ApiBase.API_KEY);
        } else if (listType == DISCOVERY_LIST) {
            responseListObservable = mRequestInterface.getDiscoverMovies(1,ApiBase.LOCALE_EN_US, ApiBase.API_KEY, ApiBase.POPULARITY_ORDER_ASC, false, false);
        }

        Toast.makeText(getContext(), listType + "", Toast.LENGTH_SHORT).show();

        try {
            mCompositeDisposable.add(responseListObservable
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(this::handleResponse, this::handleError));
        } catch (Exception e) {
            Log.e(TAG, "Response list observable is empty");
        }
    }

    public void handleResponse(ResponseList responseList) {

        // Change column count based on screen orientation
        int numberOfColumns = GridColumnCalculator.calculateNoOfColumns(getContext());

        if (mAdapter == null) {
            mAdapter = new HomeRecyclerAdapter(getActivity(), new ArrayList<>(responseList.getResults()));
        } else {
            mAdapter.updateAdapterData(new ArrayList<>(responseList.getResults()));
        }
        mLayoutManager = new GridLayoutManager(getContext(), numberOfColumns);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mAdapter);
    }

    private void handleError(Throwable error) {
        Toast.makeText(getContext(), "Error "+error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        Log.e("ERROR: ", error.getLocalizedMessage());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.submenu_popularity) {
            loadFromNetwork(ORDER_BY_POPULARITY);
            return true;
        } else if (id == R.id.submenu_top_rated) {
            loadFromNetwork(ORDER_BY_TOP_RATED);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}