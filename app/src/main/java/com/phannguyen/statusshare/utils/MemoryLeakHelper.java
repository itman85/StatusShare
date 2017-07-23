package com.phannguyen.statusshare.utils;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

/**
 * Created by phannguyen on 4/19/17.
 */

/**
 * This class uses for detecting memory leak, set ENABLE = true to enable detecting memory leak
 */
public class MemoryLeakHelper {
    static final boolean ENABLE = false;
    private static final String TAG = "MemoryLeakHelper";
    private static MemoryLeakHelper instance;
    private static final Object lock = new Object();
    private RefWatcher refWatcher;

    public static MemoryLeakHelper Instance() {
        synchronized (lock) {
            if (instance == null) {
                instance = new MemoryLeakHelper();
            }
        }
        return instance;
    }

    public MemoryLeakHelper(){
        //
    }
    public boolean init(Application app){
        if(ENABLE){
            if (LeakCanary.isInAnalyzerProcess(app)) {
                return false;
            }
            refWatcher =  LeakCanary.install(app);
        }
        return true;
    }

    public void addWatch(Object target){
        if(ENABLE){
            refWatcher.watch(target);
        }
    }

    public void analysis(){
        if(ENABLE) {
            System.gc();
        }
    }

}
