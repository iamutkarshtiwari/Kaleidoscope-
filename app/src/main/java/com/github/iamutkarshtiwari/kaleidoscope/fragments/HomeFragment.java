package com.github.iamutkarshtiwari.kaleidoscope.fragments;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.iamutkarshtiwari.kaleidoscope.R;
import com.github.iamutkarshtiwari.kaleidoscope.adapters.HomeRecyclerAdapter;
import com.github.iamutkarshtiwari.kaleidoscope.models.Movies;
import com.github.iamutkarshtiwari.kaleidoscope.utils.GridColumnCalculator;
import com.github.iamutkarshtiwari.kaleidoscope.utils.JSONParser;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private String dataSource;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(
                R.layout.pager_fragment, container, false);
        this.dataSource = getArguments().getString("data_source");
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        int mNoOfColumns = GridColumnCalculator.calculateNoOfColumns(getContext());

//        int numberOfColumns = 2;
//        // Change column count based on screen orientation
//        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
//            numberOfColumns = 3;
//        }

        JSONParser jsonParser = new JSONParser(this.dataSource);
        ArrayList<Movies> itemList = jsonParser.getResponseData();
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        HomeRecyclerAdapter adapter = new HomeRecyclerAdapter(getActivity(), itemList);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), mNoOfColumns);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }
}