package com.github.iamutkarshtiwari.kaleidoscope.network;

import com.github.iamutkarshtiwari.kaleidoscope.fragments.HomeFragment;

/**
 * Created by utkarshtiwari on 16/01/18.
 */

public interface NetworkInteractor {
    void loadRetroData(int listType);

    void bindView(HomeFragment view);

    void unBindView();

    void rxUnSubscribe();

    void rxSubscribe();
}