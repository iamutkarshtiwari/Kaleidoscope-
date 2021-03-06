package com.github.iamutkarshtiwari.kaleidoscope.activity;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.iamutkarshtiwari.kaleidoscope.R;
import com.github.iamutkarshtiwari.kaleidoscope.adapters.HomeViewPagerAdapter;
import com.github.iamutkarshtiwari.kaleidoscope.utils.MyTextView;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeActivity extends AppCompatActivity {

    private  HomeActivity mCurrInstance;
    private HomeViewPagerAdapter mViewPagerAdapter;

    @BindView(R.id.search_icon) ImageView searchIcon;
    @BindView(R.id.view_pager) ViewPager viewPager;
    @BindView(R.id.tab_layout) TabLayout tabLayout;
//    @BindView(R.id.toolbar) Toolbar toolbar;

    @OnClick(R.id.search_bar_layout) void sendToSearchActivity() {
        //  Intent intent = new Intent(HomeActivity.this, SearchActivity.class);
        //  startActivity(intent);
    }


    /**
     * Returns the context of this activity
     *
     * @return
     */
    public Context getContext() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        mCurrInstance = this;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
//            w.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            w.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);

        mViewPagerAdapter = new HomeViewPagerAdapter(getSupportFragmentManager(), this);
        viewPager.setAdapter(mViewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        // Setup tab buttons
        setupTabIcons();
        // Initialize picasso settings
        setupPicasso();

//        loadJSON();
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
            Log.d("Kaleidoscope", "Picasso singleton already exists");
        }
    }

    /**
     * Updates the tab title
     * @param tabIndex index of the tab from viewpager
     * @param title to be updated
     */
    public void updateTabTitle(int tabIndex, String title) {
        MyTextView tabtitle = (MyTextView) (tabLayout.getTabAt(tabIndex).getCustomView().findViewById(R.id.tab_title));
        tabtitle.setText(title);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_menu, menu);
        return true;
    }

}
