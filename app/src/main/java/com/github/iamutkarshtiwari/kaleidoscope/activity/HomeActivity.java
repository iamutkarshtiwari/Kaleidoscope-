package com.github.iamutkarshtiwari.kaleidoscope.activity;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.iamutkarshtiwari.kaleidoscope.R;
import com.github.iamutkarshtiwari.kaleidoscope.adapters.HomeViewPagerAdapter;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

public class HomeActivity extends AppCompatActivity {

    private static HomeActivity currInstance;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private HomeViewPagerAdapter viewPagerAdapter;

    /**
     * Returns the context of this activity
     *
     * @return
     */
    public static Context getContext() {
        return currInstance.getApplicationContext();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        currInstance = this;

        // Floating action button listener
        final RelativeLayout fab = (RelativeLayout) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Snackbar fabSnack = Snackbar.make(view, R.string.sell_an_item, Snackbar.LENGTH_LONG);
                fabSnack.setAction("Action", null).show();
                fabSnack.getView().addOnAttachStateChangeListener( new View.OnAttachStateChangeListener() {
                    @Override
                    public void onViewAttachedToWindow( View v ) {

                    }

                    @Override
                    public void onViewDetachedFromWindow( View v ) {
                        fab.setTranslationY( 0 );
                    }
                });
                fabSnack.show();
            }
        });

        // Mercari search click listener
        RelativeLayout mercariSearchBar = (RelativeLayout) findViewById(R.id.search_bar_layout);
        mercariSearchBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(HomeActivity.this, SearchActivity.class);
//                startActivity(intent);
            }
        });

        viewPager = (ViewPager) findViewById(R.id.view_pager);
        viewPagerAdapter = new HomeViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);
        // Setup tab buttons
        setupTabIcons();
        // Initialize picasso settings
        setupPicasso();
    }

    /**
     * Sets up the tab icon and title layout
     */
    private void setupTabIcons() {
        int numberOfTabs = tabLayout.getTabCount();
        final String[] tabNames = getResources().getStringArray(R.array.home_tab_names);
        for (int i = 0; i < numberOfTabs; i++) {
            View tabView = getLayoutInflater().inflate(R.layout.custom_home_tab, null);
            ImageView tabImage = (ImageView) tabView.findViewById(R.id.tab_icon);
            int imageID = getResources().getIdentifier("ic_tab_" + (i + 1), "drawable", getPackageName());
            tabImage.setBackgroundResource(imageID);
            TextView textView = (TextView) tabView.findViewById(R.id.tab_title);
            textView.setText(tabNames[i]);
            tabLayout.getTabAt(i).setCustomView(tabView);
        }
    }

    /**
     * Initialize picasso instance to download
     * images from redirecting urls
     */
    private void setupPicasso() {
        Picasso.Builder builder = new Picasso.Builder(getContext());
        builder.downloader(new OkHttp3Downloader(getContext()));
        Picasso built = builder.build();
        built.setLoggingEnabled(true);
        try {
            Picasso.setSingletonInstance(built);
        } catch (IllegalStateException exception) {
            Log.e("MercariApp", "Singleton already exists");
        }
    }
}
