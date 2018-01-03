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
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeFragment extends Fragment {

    private String mDataSource;
    private CompositeDisposable mCompositeDisposable;
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

        ButterKnife.bind(this, view);

        if (mDataSource.compareToIgnoreCase("network") == 0) {
            mCompositeDisposable = new CompositeDisposable();
            loadFromNetwork();
        } else {

        }

    }

    public void loadFromNetwork() {
        TheMovieDbInterface requestInterface = new Retrofit.Builder()
                .baseUrl(ApiBase.BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(TheMovieDbInterface.class);

        mCompositeDisposable.add(requestInterface.getPopularMovies(1, "en-US", ApiBase.API_KEY)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse,this::handleError));
    }

    public void handleResponse(ResponseList responseList) {

        // Change column count based on screen orientation
        int numberOfColumns = GridColumnCalculator.calculateNoOfColumns(getContext());

        mAdapter = new HomeRecyclerAdapter(getActivity(), new ArrayList<>(responseList.getResults()));
        mLayoutManager = new GridLayoutManager(getContext(), numberOfColumns);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mAdapter);
    }

    private void handleError(Throwable error) {
        Toast.makeText(getContext(), "Error "+error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        Log.e("ERROR: ", error.getLocalizedMessage());
    }
}