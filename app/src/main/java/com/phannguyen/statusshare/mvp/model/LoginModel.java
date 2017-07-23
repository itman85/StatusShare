package com.phannguyen.statusshare.mvp.model;

import com.phannguyen.statusshare.firebase.FibCallback;
import com.phannguyen.statusshare.firebase.FirebaseHelper;
import com.phannguyen.statusshare.mvp.base.ILoginMVP;

/**
 * Created by phannguyen on 4/12/17.
 */

public class LoginModel implements ILoginMVP.Model {
    private ILoginMVP.ModelCallback modelCallback;

    public LoginModel(ILoginMVP.ModelCallback modelCallback) {
        this.modelCallback = modelCallback;
    }

    @Override
    public void executeLogin(String email, String password) {
        FirebaseHelper.Instance().firebaseLogin(email, password, new FibCallback<String>() {
            @Override
            public void onSuccess(String response) {
                modelCallback.loginSuccessful();
            }

            @Override
            public void onError(String error) {
                modelCallback.loginFailed(error);
            }
        });
    }
}
