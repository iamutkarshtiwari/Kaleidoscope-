package com.github.iamutkarshtiwari.kaleidoscope.utils;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Created by utkarshtiwari on 30/12/17.
 */

public class GridColumnCalculator {
    public static int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int noOfColumns = (int) (dpWidth / 150);
        return noOfColumns;
    }
}