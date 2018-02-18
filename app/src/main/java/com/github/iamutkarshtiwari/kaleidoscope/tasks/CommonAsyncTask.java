package com.github.iamutkarshtiwari.kaleidoscope.tasks;

import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;

import java.util.ArrayList;

/**
 * Created by utkarshtiwari on 17/02/18.
 */

public abstract class CommonAsyncTask<T> extends AsyncTask<Void, Void, ArrayList<T>> {

    private ProgressBar mProgressBar;
    private FetchDataListener mListener;

    public interface FetchDataListener<T> {
        public void onFetchData(ArrayList<T> resultList);
    }

    public CommonAsyncTask(ProgressBar mProgressBar, FetchDataListener mListener) {
        this.mProgressBar = mProgressBar;
        this.mListener = mListener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onPostExecute(ArrayList<T> result) {
        super.onPostExecute(result);
        mProgressBar.setVisibility(View.GONE);
        if (mListener != null) {
            mListener.onFetchData(result);
        }
    }
}