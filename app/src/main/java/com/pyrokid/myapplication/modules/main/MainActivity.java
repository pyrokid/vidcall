package com.pyrokid.myapplication.modules.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.pyrokid.myapplication.R;
import com.pyrokid.myapplication.common.BaseActivity;
import com.pyrokid.myapplication.modules.call.call.CallActivity;
import com.pyrokid.myapplication.service.SinchService;
import com.sinch.android.rtc.calling.Call;

import moxy.presenter.InjectPresenter;

public class MainActivity extends BaseActivity implements MainView {


    @InjectPresenter
    MainPresenter mainPresenter;

    private Button mCallButton;
    private EditText mCallName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCallName = (EditText) findViewById(R.id.callName);
        mCallButton = (Button) findViewById(R.id.callButton);
        mCallButton.setEnabled(false);
        mCallButton.setOnClickListener(buttonClickListener);

        Button stopButton = (Button) findViewById(R.id.stopButton);
        stopButton.setOnClickListener(buttonClickListener);
    }

    @Override
    protected void onServiceConnected() {
        TextView userName = (TextView) findViewById(R.id.loggedInName);
        userName.setText(getSinchServiceInterface().getUserName());
        mCallButton.setEnabled(true);
    }

    private void stopButtonClicked() {
        if (getSinchServiceInterface() != null) {
            getSinchServiceInterface().stopClient();
        }
        finish();
    }

    private void callButtonClicked() {
        String userName = mCallName.getText().toString();
        if (userName.isEmpty()) {
            Toast.makeText(this, "Please enter a user to call", Toast.LENGTH_LONG).show();
            return;
        }

        Call call = getSinchServiceInterface().callUserVideo(userName);
        String callId = call.getCallId();

        Intent callScreen = new Intent(this, CallActivity.class);
        callScreen.putExtra(SinchService.CALL_ID, callId);
        startActivity(callScreen);
    }

    private View.OnClickListener buttonClickListener = v -> {
        switch (v.getId()) {
            case R.id.callButton:
                callButtonClicked();
                break;

            case R.id.stopButton:
                stopButtonClicked();
                break;

        }
    };
}
