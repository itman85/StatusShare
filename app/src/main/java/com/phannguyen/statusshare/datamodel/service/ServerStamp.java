package com.phannguyen.statusshare.datamodel.service;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by phannguyen on 4/16/17.
 */

public class ServerStamp extends RealmObject{
    long serverStamp;
    @PrimaryKey
    int id;


    public ServerStamp() {
    }

    public ServerStamp(long serverStamp,int id) {
        this.serverStamp = serverStamp;
        this.id = id;

    }

    public long getServerStamp() {
        return serverStamp;
    }

    public void setServerStamp(long serverStamp) {
        this.serverStamp = serverStamp;
    }
}
