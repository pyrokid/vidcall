package com.pyrokid.myapplication.modules.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.pyrokid.myapplication.R;
import com.pyrokid.myapplication.common.BaseActivity;
import com.pyrokid.myapplication.modules.main.MainActivity;
import com.pyrokid.myapplication.service.SinchService;
import com.sinch.android.rtc.SinchError;

import moxy.presenter.InjectPresenter;

public class LoginActivity extends BaseActivity implements SinchService.StartFailedListener, LoginView {


    @InjectPresenter
    LoginPresenter loginPresenter;


    private Button mLoginButton;
    private EditText mLoginName;
    private ProgressDialog mSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mLoginName = findViewById(R.id.loginName);

        mLoginButton = findViewById(R.id.loginButton);
        mLoginButton.setEnabled(false);

        mLoginButton.setOnClickListener(view -> loginClicked());
    }

    @Override
    protected void onServiceConnected() {
        mLoginButton.setEnabled(true);
        getSinchServiceInterface().setStartListener(this);
    }

    @Override
    protected void onPause() {
        if (mSpinner != null) {
            mSpinner.dismiss();
        }
        super.onPause();
    }

    @Override
    public void onStartFailed(SinchError error) {
        Toast.makeText(this, error.toString(), Toast.LENGTH_LONG).show();
        if (mSpinner != null) {
            mSpinner.dismiss();
        }
    }

    @Override
    public void onStarted() {
        openMainActivity();
    }

    private void loginClicked() {
        String userName = mLoginName.getText().toString();

        if (userName.isEmpty()) {
            Toast.makeText(this, R.string.please_enter_name_message, Toast.LENGTH_LONG).show();
            return;
        }

        if (!userName.equals(getSinchServiceInterface().getUserName())) {
            getSinchServiceInterface().stopClient();
        }

        if (!getSinchServiceInterface().isStarted()) {
            getSinchServiceInterface().startClient(userName);
            showSpinner();
        } else {
            openMainActivity();
        }
    }

    private void openMainActivity() {
        Intent mainActivity = new Intent(this, MainActivity.class);
        startActivity(mainActivity);
    }

    private void showSpinner() {
        mSpinner = new ProgressDialog(this);
        mSpinner.setTitle(getString(R.string.logging_in_message));
        mSpinner.setMessage(getString(R.string.please_wait_message));
        mSpinner.show();
    }
}
