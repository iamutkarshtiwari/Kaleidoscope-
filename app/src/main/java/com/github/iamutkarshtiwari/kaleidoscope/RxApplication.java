package com.github.iamutkarshtiwari.kaleidoscope;

import android.app.Application;

import com.github.iamutkarshtiwari.kaleidoscope.network.NetworkService;

/**
 * Created by utkarshtiwari on 16/01/18.
 */

public class RxApplication extends Application {

    private NetworkService networkService;

    @Override
    public void onCreate() {
        super.onCreate();
        networkService = new NetworkService();
    }

    public NetworkService getNetworkService(){
        return networkService;
    }
}