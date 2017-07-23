package com.phannguyen.statusshare.mvp.presenter;

import com.phannguyen.statusshare.R;
import com.phannguyen.statusshare.firebase.FirebaseHelper;
import com.phannguyen.statusshare.mvp.base.ILoginMVP;
import com.phannguyen.statusshare.mvp.model.LoginModel;

/**
 * Created by phannguyen on 4/12/17.
 */

public class LoginPresenter implements ILoginMVP.Presenter,ILoginMVP.ModelCallback {
    private ILoginMVP.View _view;
    private ILoginMVP.Model _model;

    public LoginPresenter(ILoginMVP.View _view) {
        this._view = _view;
        _model = new LoginModel(this);
    }

    @Override
    public void loginSuccessful() {
        _view.loginSuccessful();
    }

    @Override
    public void loginFailed(String error) {
        _view.loginFailed(error);
    }

    @Override
    public boolean validateLogin(String email, String password) {
        boolean res = true;
        if(email==null || "".equals(email)) {
            _view.showUserNameError(R.string.username_error);
            res = false;
        }
        if(password==null || "".equals(password)) {
            _view.showPasswordError(R.string.password_error);
            res = false;
        }
        return res;
    }

    @Override
    public void loginAction(String email, String password) {
        _model.executeLogin(email,password);
    }

    @Override
    public boolean isLogin() {
        return FirebaseHelper.Instance().isLogin();
    }
}
