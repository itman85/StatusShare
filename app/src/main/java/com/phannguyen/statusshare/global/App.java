package com.phannguyen.statusshare.global;

import android.app.Application;

import com.phannguyen.statusshare.utils.MemoryLeakHelper;

import io.realm.Realm;

/**
 * Created by phannguyen on 4/15/17.
 */

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        if(!MemoryLeakHelper.Instance().init(this))
            return;
        Realm.init(this);
        AppPresenter.Instance().initGetDeviceId(this.getBaseContext());
    }

}
