package com.halosolutions.radiotoken;

import android.app.Application;

import timber.log.Timber;

/**
 * Created by luhonghai on 6/24/17.
 */

public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Timber.plant(new Timber.DebugTree());
    }
}
