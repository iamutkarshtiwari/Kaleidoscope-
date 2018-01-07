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

public class HomeFragment extends Fragment {


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

        if (this.mDataSource.compareToIgnoreCase("network") == 0) {
            loadFromNetwork(DISCOVERY_LIST);
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

        String[] tabNames = getContext().getResources().getStringArray(R.array._1st_tab_names);
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

        ((HomeActivity) getActivity()).updateTabTitle(0, tabTitle);
        Toast.makeText(getContext(), listType + "", Toast.LENGTH_SHORT).show();

        try {
            mCompositeDisposable.add(responseListObservable
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(this::handleResponse, this::handleError));
        } catch (NullPointerException e) {
            Log.e(TAG, "You passed a wrong parameter");
        }
    }

    public void handleResponse(ResponseList responseList) {
        if (mAdapter != null) {
            mAdapter.setAdapterData(new ArrayList<>(responseList.getResults()));
        }
    }

    private void handleError(Throwable error) {
        Toast.makeText(getContext(), "Error " + error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        Log.e("ERROR: ", error.getLocalizedMessage());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.submenu_popularity) {
            mAdapter.clearAdapterData();
            loadFromNetwork(ORDER_BY_POPULARITY);
            return true;
        } else if (id == R.id.submenu_top_rated) {
            mAdapter.clearAdapterData();
            loadFromNetwork(ORDER_BY_TOP_RATED);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        if (!(mCompositeDisposable == null || mCompositeDisposable.isDisposed())) {
            mCompositeDisposable.clear();
        }
        super.onDestroy();
    }
}