package com.phannguyen.statusshare.mvp.base;

/**
 * Created by phannguyen on 4/12/17.
 */

public interface ILoginMVP {

    interface View {
        void showUserNameError(int resid);
        void showPasswordError(int resId);
        void loginSuccessful();
        void loginFailed(String error);

    }

    interface Presenter {
        boolean validateLogin(String email,String password);
        void loginAction(String email,String password);
        boolean isLogin();
    }

    interface Model {
        void executeLogin(String email,String password);
    }

    interface ModelCallback{
        void loginSuccessful();
        void loginFailed(String error);

    }
}
