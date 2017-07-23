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
import com.phannguyen.statusshare.mvp.base.ILoginMVP;
import com.phannguyen.statusshare.mvp.presenter.LoginPresenter;
import com.phannguyen.statusshare.ui.activities.base.BaseActivity;
import com.phannguyen.statusshare.utils.MultipleClickableInString;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.phannguyen.statusshare.R.id.coordinatorLayout;

/**
 * Created by phannguyen on 4/12/17.
 */

public class LoginActivity extends BaseActivity implements  ILoginMVP.View {
    @Bind(R.id.loginUsername)
    EditText edtUsername;
    @Bind(R.id.loginPassword)
    EditText edtPassword;
    @Bind(R.id.signuptxt)
    TextView tvSignup;
    @Bind(coordinatorLayout)
    CoordinatorLayout parentView;
    ProgressDialog pDialog;
    ILoginMVP.Presenter presenter;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this, this);
        Button logon = (Button)findViewById(R.id.loginButton);
        logon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(presenter.validateLogin(edtUsername.getText().toString(), edtPassword.getText().toString())){
                    InputMethodManager inputMethodManager =
                            (InputMethodManager) getSystemService(
                                    Activity.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(edtPassword.getApplicationWindowToken(), 0);
                    pDialog.setMessage("Login...");
                    pDialog.show();
                    presenter.loginAction(edtUsername.getText().toString(), edtPassword.getText().toString());
                }
            }
        });
        presenter = new LoginPresenter(this);
        if(presenter.isLogin()){
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);
            this.finish();
        }
        pDialog = new ProgressDialog(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        String signupText = getString(R.string.signup_text);

        MultipleClickableInString multipleClickableInString = new MultipleClickableInString(tvSignup, signupText);
        multipleClickableInString.addClickableItemsInPairs(signupText, new MultipleClickableInString.IItemClickListener() {
            @Override
            public void onItemClicked() {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
                LoginActivity.this.finish();
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);

            }
        });
    }

    @Override
    public void showUserNameError(int resid) {
        edtUsername.setError(getString(resid));
    }

    @Override
    public void showPasswordError(int resId) {
        edtPassword.setError(getString(resId));
    }

    @Override
    public void loginSuccessful() {

        if(pDialog!=null)
            pDialog.dismiss();
        new Handler().postDelayed(()->{
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);
            this.finish();
            overridePendingTransition(R.anim.activity_enter,R.anim.activity_exit);
        },200);

    }

    @Override
    public void loginFailed(String error) {
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
    protected void onPause() {
        super.onPause();
        if(pDialog!=null)
            pDialog.dismiss();
    }
}
