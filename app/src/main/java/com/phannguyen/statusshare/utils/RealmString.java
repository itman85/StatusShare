package com.phannguyen.statusshare.utils;

import io.realm.RealmObject;

/**
 * Created by phannguyen on 4/15/17.
 */

public class RealmString extends RealmObject {
    String value;

    public RealmString() {
    }

    public RealmString(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
