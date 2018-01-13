package com.github.iamutkarshtiwari.kaleidoscope.fragments;

import android.content.Intent;
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
import com.github.iamutkarshtiwari.kaleidoscope.activity.HomeActivity;
import com.github.iamutkarshtiwari.kaleidoscope.activity.MovieDetailActivity;
import com.github.iamutkarshtiwari.kaleidoscope.adapters.HomeRecyclerAdapter;
import com.github.iamutkarshtiwari.kaleidoscope.adapters.ItemClickListener;
import com.github.iamutkarshtiwari.kaleidoscope.models.Movie;
import com.github.iamutkarshtiwari.kaleidoscope.models.ResponseList;
import com.github.iamutkarshtiwari.kaleidoscope.network.ApiBase;
import com.github.iamutkarshtiwari.kaleidoscope.network.NetworkLoader;
import com.github.iamutkarshtiwari.kaleidoscope.network.TheMovieDbInterface;
import com.github.iamutkarshtiwari.kaleidoscope.utils.GridColumnCalculator;
import com.github.iamutkarshtiwari.kaleidoscope.utils.MyTextView;
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

public class HomeFragment extends Fragment implements NetworkLoader.OnResultReceived {


    private static final String TAG = "KALEIDOSCOPE";
    private static final int DISCOVERY_LIST = 1;
    private static final int ORDER_BY_POPULARITY = 2;
    private static final int ORDER_BY_TOP_RATED = 3;

    RecyclerView recyclerView;
    @BindView(R.id.tab_title)
    MyTextView fragmentTitle;

    private CompositeDisposable mCompositeDisposable;
    private String mDataSource;
    private TheMovieDbInterface mRequestInterface;
    private NetworkLoader mNetLoad;
    private HomeRecyclerAdapter mAdapter;
    private GridLayoutManager mLayoutManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(
                R.layout.pager_fragment, container, false);
        this.mDataSource = getArguments().getString("data_source");
        View view = inflater.inflate(R.layout.custom_home_tab, container, false);
        ButterKnife.bind(this, view);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
        recyclerView = view.findViewById(R.id.recycler_view);
        mNetLoad = new NetworkLoader(getContext());
        mNetLoad.setOnResultListener(this);

        if (this.mDataSource.compareToIgnoreCase("network") == 0) {
            mNetLoad.loadFromNetwork(DISCOVERY_LIST);
        } else if (this.mDataSource.compareToIgnoreCase("database") == 0) {

        }


        // Change column count based on screen orientation
        int numberOfColumns = GridColumnCalculator.calculateNoOfColumns(getContext());

        ItemClickListener itemClickListener = new ItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent = new Intent(getActivity(), MovieDetailActivity.class);
                Movie selectedMovie = mAdapter.getAdapterData().get(position);
                intent.putExtra("parcel_data", selectedMovie);
                startActivity(intent);
            }
        };

        mAdapter = new HomeRecyclerAdapter(getActivity(), new ArrayList<Movie>(), itemClickListener);
        mLayoutManager = new GridLayoutManager(getContext(), numberOfColumns);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mAdapter);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.submenu_popularity) {
            mAdapter.clearAdapterData();
            mNetLoad.loadFromNetwork(ORDER_BY_POPULARITY);
            return true;
        } else if (id == R.id.submenu_top_rated) {
            mAdapter.clearAdapterData();
            mNetLoad.loadFromNetwork(ORDER_BY_TOP_RATED);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    

    @Override
    public void onDestroy() {
        mNetLoad.clearCompositeDisposable();
        super.onDestroy();
    }

    @Override
    public void getResult(ResponseList responseList) {
        if (mAdapter != null) {
            mAdapter.setAdapterData(new ArrayList<>(responseList.getResults()));
        }
    }
}