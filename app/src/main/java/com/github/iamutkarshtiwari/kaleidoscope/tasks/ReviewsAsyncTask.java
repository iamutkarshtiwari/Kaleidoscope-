package com.github.iamutkarshtiwari.kaleidoscope.tasks;

import android.util.Log;
import android.widget.ProgressBar;

import com.github.iamutkarshtiwari.kaleidoscope.BuildConfig;
import com.github.iamutkarshtiwari.kaleidoscope.models.Review;
import com.github.iamutkarshtiwari.kaleidoscope.models.ReviewsResult;
import com.github.iamutkarshtiwari.kaleidoscope.utils.HttpUtil;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by utkarshtiwari on 17/02/18.
 */

public class ReviewsAsyncTask extends CommonAsyncTask<Review> {

    private static final String TAG = "ReviewsAsyncTask";
    private final long mMovieId;

    public ReviewsAsyncTask(long movieId, ProgressBar mProgressBar, FetchDataListener mListener) {
        super(mProgressBar, mListener);
        this.mMovieId = movieId;
    }

    @Override
    protected ArrayList<Review> doInBackground(Void... params) {
        Call<ReviewsResult> createdCall = HttpUtil.getService().getReviewsResults(mMovieId, BuildConfig.API_KEY);
        try {
            Response<ReviewsResult> result = createdCall.execute();
            return result.body().results;
        } catch (IOException e) {
            Log.e(TAG, "IOException occurred in doInBackground()");
        }
        return null;
    }
}