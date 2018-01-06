package com.github.iamutkarshtiwari.kaleidoscope.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.github.iamutkarshtiwari.kaleidoscope.fragments.HomeFragment;

import io.reactivex.disposables.CompositeDisposable;

public class HomeViewPagerAdapter extends FragmentPagerAdapter {

    private int mPagerCount = 2;
    private Context mContext;

    public HomeViewPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        Bundle args = null;
        if (position == 0) {
            fragment = new HomeFragment();
            args = new Bundle();
            args.putString("data_source", "network");
            fragment.setArguments(args);

        } else if (position == 1) {
            fragment = new HomeFragment();
            args = new Bundle();
            args.putString("data_source", "database");
            fragment.setArguments(args);

        }
        return fragment;
    }

    @Override
    public int getCount() {
        return mPagerCount;
    }


}