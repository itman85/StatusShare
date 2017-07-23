package com.phannguyen.statusshare.mvp.base;

/**
 * Created by phannguyen on 4/12/17.
 */

public interface ISignupMVP {

    interface View {
        void signupSuccessful();
        void signupFailed(String error);
        void showNickNameError(int resid);
        void showEmailError(int resid);
        void showPasswordError(int resid);
        void showConfirmPasswordError(int resid);

    }

    interface Presenter {
        void signupAction(String email, String password,String nickname);
        boolean validateSignup(String email, String password,String confirmPass,String nickname);

    }

    interface Model {
        void executeSignup(String email, String password,String nickname);

    }

    interface ModelCallback{
        void signupSuccessful();
        void signupFailed(String error);

    }
}
