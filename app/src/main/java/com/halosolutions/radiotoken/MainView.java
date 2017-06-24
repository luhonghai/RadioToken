package com.halosolutions.radiotoken;

import com.halosolutions.library.data.AuthToken;

import net.grandcentrix.thirtyinch.TiView;
import net.grandcentrix.thirtyinch.callonmainthread.CallOnMainThread;

/**
 * Created by luhonghai on 6/23/17.
 */

public interface MainView extends TiView {

    @CallOnMainThread
    void showError(Throwable throwable);

    @CallOnMainThread
    void showResult(AuthToken authToken);
}
