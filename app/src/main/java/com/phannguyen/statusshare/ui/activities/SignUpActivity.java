package com.phannguyen.statusshare.ui.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.phannguyen.statusshare.R;
import com.phannguyen.statusshare.mvp.base.ISignupMVP;
import com.phannguyen.statusshare.mvp.presenter.SignupPresenter;
import com.phannguyen.statusshare.ui.activities.base.BaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.phannguyen.statusshare.R.id.coordinatorLayout;

/**
 * Created by phannguyen on 4/12/17.
 */

public class SignUpActivity extends BaseActivity implements ISignupMVP.View {

    @Bind(R.id.nickName)
    EditText edtNickName;
    @Bind(R.id.loginUsername)
    EditText edtUsername;
    @Bind(R.id.loginPassword)
    EditText edtPassword;
    @Bind(R.id.confirmloginPassword)
    EditText edtConfirmPassword;
    @Bind(coordinatorLayout)
    CoordinatorLayout parentView;
    ProgressDialog pDialog;
    ISignupMVP.Presenter presenter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this, this);
        Button signupBtn = (Button)findViewById(R.id.signupButton);
        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(presenter.validateSignup(edtUsername.getText().toString(),
                        edtPassword.getText().toString(),
                        edtConfirmPassword.getText().toString(),
                        edtNickName.getText().toString())) {
                    InputMethodManager inputMethodManager =
                            (InputMethodManager) getSystemService(
                                    Activity.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(edtConfirmPassword.getApplicationWindowToken(), 0);
                    pDialog.setMessage("Signup ... ");
                    pDialog.show();
                    presenter.signupAction(edtUsername.getText().toString(),
                            edtPassword.getText().toString(),
                            edtNickName.getText().toString());
                }

            }
        });

        Button cancelBtn = (Button)findViewById(R.id.cancelButton);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
                SignUpActivity.this.finish();
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);

            }
        });

        presenter = new SignupPresenter(this);
        pDialog = new ProgressDialog(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(pDialog!=null)
            pDialog.dismiss();
    }

    @Override
    public void signupSuccessful() {
        if(pDialog!=null)
            pDialog.dismiss();
        new Handler().postDelayed(()->{
            Intent intent = new Intent(SignUpActivity.this, HomeActivity.class);
            startActivity(intent);
            this.finish();
            overridePendingTransition(R.anim.activity_enter,R.anim.activity_exit);
        },200);
    }

    @Override
    public void signupFailed(String error) {
        if(pDialog!=null)
            pDialog.dismiss();
        Snackbar snackbar = Snackbar.make(parentView, error, Snackbar.LENGTH_LONG);
        View view = snackbar.getView();
        CoordinatorLayout.LayoutParams params=(CoordinatorLayout.LayoutParams)view.getLayoutParams();
        params.gravity = Gravity.TOP;
        view.setLayoutParams(params);
        view.setBackgroundColor(ContextCompat.getColor(this, R.color.gray_2));
        TextView textView = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(ContextCompat.getColor(this, R.color.red_button));
        snackbar.show();

        InputMethodManager inputMethodManager = (InputMethodManager)this
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(this.getCurrentFocus(), 0);

    }

    @Override
    public void showNickNameError(int resid) {
        edtNickName.setError(getString(resid));
    }

    @Override
    public void showEmailError(int resid) {
        edtUsername.setError(getString(resid));
    }

    @Override
    public void showPasswordError(int resid) {
        edtPassword.setError(getString(resid));
    }

    @Override
    public void showConfirmPasswordError(int resid) {
        edtConfirmPassword.setError(getString(resid));
    }
}
