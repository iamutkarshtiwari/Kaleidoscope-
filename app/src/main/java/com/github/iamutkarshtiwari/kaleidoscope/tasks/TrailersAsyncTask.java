package com.github.iamutkarshtiwari.kaleidoscope.tasks;

import android.util.Log;
import android.widget.ProgressBar;

import com.github.iamutkarshtiwari.kaleidoscope.BuildConfig;
import com.github.iamutkarshtiwari.kaleidoscope.models.Trailer;
import com.github.iamutkarshtiwari.kaleidoscope.models.TrailersResult;
import com.github.iamutkarshtiwari.kaleidoscope.utils.HttpUtil;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by utkarshtiwari on 17/02/18.
 */

public class TrailersAsyncTask extends CommonAsyncTask<Trailer> {

    private static final String TAG = "TrailersAsyncTask";

    private long mMovieId;

    public TrailersAsyncTask(long mMovieId, ProgressBar mProgressBar, FetchDataListener mListener) {
        super(mProgressBar, mListener);
        this.mMovieId = mMovieId;
    }


    @Override
    protected ArrayList<Trailer> doInBackground(Void... params) {

        Call<TrailersResult> createdCall = HttpUtil.getService().getTrailersResults(mMovieId, BuildConfig.API_KEY);
        try {
            Response<TrailersResult> result = createdCall.execute();
            ArrayList<Trailer> arrayList = result.body().results;
            if (arrayList != null) {
                return arrayList;
            } else {
                throw new IOException();
            }
        } catch (IOException e) {
            Log.e(TAG, "IOException occurred in doInBackground()");
        }
        return null;
    }

}