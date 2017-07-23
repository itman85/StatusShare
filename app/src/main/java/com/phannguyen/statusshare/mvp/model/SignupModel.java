package com.phannguyen.statusshare.mvp.model;

import com.phannguyen.statusshare.firebase.FibCallback;
import com.phannguyen.statusshare.firebase.FirebaseHelper;
import com.phannguyen.statusshare.mvp.base.ISignupMVP;

/**
 * Created by phannguyen on 4/12/17.
 */
public class SignupModel implements ISignupMVP.Model {
    private ISignupMVP.ModelCallback modelCallback;

    public SignupModel(ISignupMVP.ModelCallback modelCallback) {
        this.modelCallback = modelCallback;
    }

    @Override
    public void executeSignup(String email, String password, String nickname) {
        FirebaseHelper.Instance().firebaseSignupAccount(nickname,email,password, new FibCallback<String>() {
            @Override
            public void onSuccess(String response) {
                modelCallback.signupSuccessful();
            }

            @Override
            public void onError(String error) {
                modelCallback.signupFailed(error);
            }
        });
    }
}
