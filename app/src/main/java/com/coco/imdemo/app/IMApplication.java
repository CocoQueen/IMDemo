package com.coco.imdemo.app;

import android.app.Application;
import android.content.Context;

import com.tencent.qcloud.presentation.business.InitBusiness;
import com.tencent.qcloud.tlslibrary.service.TlsBusiness;

/**
 * Created by ydx on 18-5-29.
 */

public class IMApplication extends Application {
    Context appContext;

    @Override
    public void onCreate() {
        super.onCreate();
        appContext=this;
        initTXIM();
    }

    private void initTXIM() {
        InitBusiness.start(getApplicationContext(),4);
        TlsBusiness.init(getApplicationContext());

    }
}
