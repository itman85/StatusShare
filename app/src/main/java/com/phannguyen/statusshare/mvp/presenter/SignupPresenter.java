package com.phannguyen.statusshare.mvp.presenter;

import com.phannguyen.statusshare.R;
import com.phannguyen.statusshare.mvp.base.ISignupMVP;
import com.phannguyen.statusshare.mvp.model.SignupModel;
import com.phannguyen.statusshare.utils.FnUtils;

/**
 * Created by phannguyen on 4/12/17.
 */

public class SignupPresenter implements ISignupMVP.Presenter,ISignupMVP.ModelCallback {
    private ISignupMVP.View _view;
    private ISignupMVP.Model _model;

    public SignupPresenter(ISignupMVP.View _view) {
        this._view = _view;
        _model = new SignupModel(this);
    }

    @Override
    public void signupSuccessful() {
        _view.signupSuccessful();
    }

    @Override
    public void signupFailed(String error) {
        _view.signupFailed(error);
    }

    @Override
    public void signupAction(String email, String password, String nickname) {
        _model.executeSignup(email,password,nickname);
    }

    @Override
    public boolean validateSignup(String email, String password, String confirmPass, String nickname) {
        boolean res = true;
        if(nickname==null || "".equals(nickname)){
            _view.showNickNameError(R.string.nickname_error);
            res = false;
        }

        if(email==null || "".equals(email)){
            _view.showEmailError(R.string.email_empty_error);
            res = false;
        }else{
            if(!FnUtils.isValidEmailAddress(email)){
                _view.showEmailError(R.string.email_format_error);
                res = false;
            }
        }

        if(password==null || "".equals(password)){
            _view.showPasswordError(R.string.password_error);
            res = false;
        }else{
            if(password.length()<6){
                _view.showPasswordError(R.string.password_format_error);
                res = false;
            }else{
                if(!password.equals(confirmPass)){
                    _view.showConfirmPasswordError(R.string.password_confirm_error);
                    res = false;
                }
            }
        }

        return res;
    }
}
