package com.phannguyen.statusshare.firebase;

/**
 * Created by phannguyen on 4/13/17.
 */

public interface FibCallback<T> {
    void onSuccess(T response);
    void onError(String error);
}
