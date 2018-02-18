package com.github.iamutkarshtiwari.kaleidoscope.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.github.iamutkarshtiwari.kaleidoscope.R;
import com.github.iamutkarshtiwari.kaleidoscope.RxApplication;
import com.github.iamutkarshtiwari.kaleidoscope.activity.MovieDetailActivity;
import com.github.iamutkarshtiwari.kaleidoscope.adapters.HomeRecyclerAdapter;
import com.github.iamutkarshtiwari.kaleidoscope.adapters.ItemClickListener;
import com.github.iamutkarshtiwari.kaleidoscope.models.Movie;
import com.github.iamutkarshtiwari.kaleidoscope.models.ResponseList;
import com.github.iamutkarshtiwari.kaleidoscope.network.NetworkInteractor;
import com.github.iamutkarshtiwari.kaleidoscope.network.NetworkPresenter;
import com.github.iamutkarshtiwari.kaleidoscope.network.NetworkService;
import com.github.iamutkarshtiwari.kaleidoscope.network.TheMovieDbInterface;
import com.github.iamutkarshtiwari.kaleidoscope.utils.GridColumnCalculator;
import com.github.iamutkarshtiwari.kaleidoscope.utils.MyTextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.CompositeDisposable;

public class HomeFragment extends Fragment implements NetworkPresenter.OnRetroCallListener {


    private static final String TAG = "KALEIDOSCOPE";
    private static final String EXTRA_RX = "EXTRA_RX";
    private static final int DISCOVERY_LIST = 1;
    private static final int ORDER_BY_POPULARITY = 2;
    private static final int ORDER_BY_TOP_RATED = 3;

    RecyclerView recyclerView;
    @BindView(R.id.tab_title)
    MyTextView fragmentTitle;
    ProgressBar progressBar;
    MyTextView noResultMessage;

    private CompositeDisposable mCompositeDisposable;
    private String mDataSource;
    private TheMovieDbInterface mRequestInterface;
    private NetworkService service;
    private boolean rxCallInWorks = false;
    private NetworkInteractor networkPresenter;
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

        progressBar = view.findViewById(R.id.progress_bar);
        noResultMessage = view.findViewById(R.id.no_result_text);

        // Change column count based on screen orientation
        int numberOfColumns = GridColumnCalculator.calculateNoOfColumns(getContext());

        ItemClickListener itemClickListener = new ItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent = new Intent(getActivity(), MovieDetailActivity.class);
                Movie selectedMovie = mAdapter.getAdapterData().get(position);
                intent.putExtra("movie_data", selectedMovie);
                startActivity(intent);
            }
        };

        mAdapter = new HomeRecyclerAdapter(getActivity(), new ArrayList<Movie>(), itemClickListener);
        mLayoutManager = new GridLayoutManager(getContext(), numberOfColumns);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mAdapter);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        service = ((RxApplication) getActivity().getApplication()).getNetworkService();
        networkPresenter = new NetworkPresenter(service);
        networkPresenter.bindView(this);


        if (this.mDataSource.compareToIgnoreCase("network") == 0) {
            networkPresenter.loadRetroData(DISCOVERY_LIST);
            Log.d(TAG, "Started the call");
        } else if (this.mDataSource.compareToIgnoreCase("database") == 0) {

        }
    }

    public void showRxResults(ArrayList<Movie> movieList){
        mAdapter.setAdapterData(movieList);
    }

    public void showRxFailure(Throwable throwable){
        makeToast("Error making network request", Toast.LENGTH_SHORT);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.submenu_popularity) {
            mAdapter.clearAdapterData();
            networkPresenter.loadRetroData(ORDER_BY_POPULARITY);
            return true;
        } else if (id == R.id.submenu_top_rated) {
            mAdapter.clearAdapterData();
            networkPresenter.loadRetroData(ORDER_BY_TOP_RATED);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void makeToast(String message, int duration) {
        Toast.makeText(getContext(), message, duration).show();
    }


    @Override
    public void onPause() {
        super.onPause();
        networkPresenter.rxUnSubscribe();

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(EXTRA_RX, rxCallInWorks);
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    public void onDestroy() {
        networkPresenter.rxUnSubscribe();
        super.onDestroy();
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        networkPresenter.unBindView();
    }

    private void toggleProgressBar(boolean isShown) {
        int visibility = isShown ? View.VISIBLE : View.GONE;
        progressBar.setVisibility(visibility);
    }

    private void toggleErrorMessage(boolean isShown) {
        int visibility = isShown ? View.VISIBLE : View.GONE;
        noResultMessage.setVisibility(visibility);
    }

    @Override
    public void onCallStarted() {
        toggleProgressBar(true);
        toggleErrorMessage(false);
    }

    @Override
    public void onError() {
        toggleProgressBar(false);
        toggleErrorMessage(true);

    }

    @Override
    public void onSuccess() {
        toggleProgressBar(false);
        toggleErrorMessage(false);
    }
}