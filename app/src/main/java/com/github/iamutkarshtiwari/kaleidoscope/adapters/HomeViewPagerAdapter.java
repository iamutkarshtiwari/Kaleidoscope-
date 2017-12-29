package com.github.iamutkarshtiwari.kaleidoscope.adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.github.iamutkarshtiwari.kaleidoscope.fragments.HomeFragment;

public class HomeViewPagerAdapter extends FragmentPagerAdapter {

    public HomeViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        Bundle args = null;
        if (position == 0) {
            fragment = new HomeFragment();
            args = new Bundle();
            args.putString("data_source", "men.json");
            fragment.setArguments(args);

        } else if (position == 1) {
            fragment = new HomeFragment();
            args = new Bundle();
            args.putString("data_source", "all.json");
            fragment.setArguments(args);

        } else if (position == 2) {
            fragment = new HomeFragment();
            args = new Bundle();
            args.putString("data_source", "women.json");
            fragment.setArguments(args);
            fragment.setArguments(args);
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }
}