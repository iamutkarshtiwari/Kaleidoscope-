package com.github.iamutkarshtiwari.kaleidoscope.network;

/**
 * Created by utkarshtiwari on 16/01/18.
 */

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.github.iamutkarshtiwari.kaleidoscope.R;
import com.github.iamutkarshtiwari.kaleidoscope.fragments.HomeFragment;
import com.github.iamutkarshtiwari.kaleidoscope.models.ResponseList;
import com.github.iamutkarshtiwari.kaleidoscope.utils.MyTextView;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

@SuppressWarnings("unchecked")
public class NetworkPresenter implements NetworkInteractor {

    public interface OnRetroCallListener {
        void onCallStarted();
        void onError();
        void onSuccess();
    }

    private static final String TAG = "KALEIDOSCOPE";

    private HomeFragment view;
    private NetworkService service;
    private Disposable mDisposable;
    private Observable<ResponseList> mResponseListObservable;
    private int mCurrentObservableId;
    private OnRetroCallListener onRetroCallListener;


    public NetworkPresenter(NetworkService service) {
        this.service = service;
    }

    public void bindView(HomeFragment view) {
        this.view = view;
        onRetroCallListener = view;
    }

    public void unBindView() {
        this.view = null;
    }


    public void loadRetroData(int listType) {
        mCurrentObservableId = listType;
        onRetroCallListener.onCallStarted();
        mResponseListObservable = service.getPreparedObservable(listType);
        rxSubscribe();
    }

    private void handleResponse(ResponseList responseList) {
        Log.d(TAG, "" + responseList.getResults().size());
        onRetroCallListener.onSuccess();
        if (this.view != null) {
            this.view.showRxResults(responseList.getResults());
            mDisposable.dispose();
            service.removeFromCache(mCurrentObservableId);
        }
    }

    private void handleError(Throwable error) {
        view.showRxFailure(error);
        onRetroCallListener.onError();
        Log.e("ERROR: ", error.getLocalizedMessage());
    }


    public void rxUnSubscribe() {
        if (!(mDisposable == null || mDisposable.isDisposed())) {
            mDisposable.dispose();
        }
    }

    public void rxSubscribe() {
        mDisposable = mResponseListObservable.subscribe(this::handleResponse, this::handleError);

    }
}
