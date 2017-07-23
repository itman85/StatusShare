package com.phannguyen.statusshare.global;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.provider.Settings;

import com.phannguyen.statusshare.datamodel.service.ServerStamp;
import com.phannguyen.statusshare.utils.FnUtils;

import io.realm.Realm;

/**
 * Created by phannguyen on 4/16/17.
 */

public class AppPresenter {
    private static final String TAG = "AppPresenter";
    private static AppPresenter instance;
    private static Object lock = new Object();
    private long lastestStatusCreatedServerStamp = -1;
    private long bottomStatusCreatedServerStamp = Long.MAX_VALUE;
    private long lastestStatusDeletedServerStamp = -1;
    private String deviceId;
    private int screenHeight;
    public static AppPresenter Instance() {
        synchronized (lock) {
            if (instance == null) {
                instance = new AppPresenter();
            }
        }
        return instance;
    }

    public  AppPresenter(){
    }

    @SuppressLint("HardwareIds")
    public void initGetDeviceId(Context context){
         deviceId = Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        screenHeight = FnUtils.getScreenHeight();
    }

    public long getLastStatusTime(){
        if(lastestStatusCreatedServerStamp>-1)
            return lastestStatusCreatedServerStamp;
        else{
            ServerStamp time = Realm.getDefaultInstance().where(ServerStamp.class).equalTo("id", FnUtils.LAST_POST_SERVER_STAMP_ID).findFirst();
            if(time!=null){
                lastestStatusCreatedServerStamp = time.getServerStamp();
                return lastestStatusCreatedServerStamp;
            }
        }
        return 0;
    }

    public void setLastStatusTime(long time){
        lastestStatusCreatedServerStamp = time;
        updateLastTimeInRealm(time,FnUtils.LAST_POST_SERVER_STAMP_ID);

    }

    private void updateLastTimeInRealm(long time,int id){
        final ServerStamp stamp = new ServerStamp(time, id);
        new Handler().post(()->{
            final Realm realm = Realm.getDefaultInstance();
            realm.executeTransactionAsync(r->r.insertOrUpdate(stamp),
                    realm::close,
                    e->realm.close());

        });
    }

    public long getBottomStatusTime(){
        return bottomStatusCreatedServerStamp;
    }

    public void setBottomStatusTime(long time){
        if(bottomStatusCreatedServerStamp>time)
            bottomStatusCreatedServerStamp = time;
    }

    public long getLastDeletedStatusTime(){
        if(lastestStatusDeletedServerStamp>-1)
            return lastestStatusDeletedServerStamp;
        else{
            ServerStamp time = Realm.getDefaultInstance().where(ServerStamp.class).equalTo("id", FnUtils.LAST_DELETE_SERVER_STAMP_ID).findFirst();
            if(time!=null){
                lastestStatusDeletedServerStamp = time.getServerStamp();
                return lastestStatusDeletedServerStamp;
            }
        }
        return getLastStatusTime();
    }

    public void setLastDeletedStatusTime(long time){
        lastestStatusDeletedServerStamp = time;
        updateLastTimeInRealm(time,FnUtils.LAST_DELETE_SERVER_STAMP_ID);

    }

    public void clear(){
        lastestStatusCreatedServerStamp = -1;
        bottomStatusCreatedServerStamp = Long.MAX_VALUE;
        lastestStatusDeletedServerStamp = -1;
    }

    public String deviceId(){
        return deviceId;
    }

    public int getScreenHeight(){
        return screenHeight;
    }
}
