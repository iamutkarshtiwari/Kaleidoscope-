package com.github.iamutkarshtiwari.kaleidoscope.interfaces;

/**
 * Created by richa.khanna on 6/11/16.
 */
public interface DBUpdateListener {
    void onSuccess(int operationType);

    void onFailure();
}
