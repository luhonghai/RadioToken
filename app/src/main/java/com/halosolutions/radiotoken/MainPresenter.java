package com.halosolutions.radiotoken;

import android.support.annotation.NonNull;

import com.halosolutions.library.RadioToken;

import net.grandcentrix.thirtyinch.TiPresenter;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by luhonghai on 6/23/17.
 */

public class MainPresenter extends TiPresenter<MainView> {

    private final RadioToken radioToken;

    public MainPresenter(RadioToken radioToken) {
        this.radioToken = radioToken;
    }

    @Override
    protected void onAttachView(@NonNull MainView view) {
        super.onAttachView(view);
        fetchToken(36.473144, 138.970151);
    }

    public void fetchToken(Double lat, Double lon) {
        radioToken.request(lat, lon)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(authToken -> {
                    sendToView(v -> v.showResult(authToken));
                }, throwable -> {
                    Timber.e(throwable);
                    sendToView(v -> v.showError(throwable));
                });
        ;
    }
}
