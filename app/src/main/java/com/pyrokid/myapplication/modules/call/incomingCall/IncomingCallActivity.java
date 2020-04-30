package com.pyrokid.myapplication.modules.call.incomingCall;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.pyrokid.myapplication.R;
import com.pyrokid.myapplication.common.BaseActivity;
import com.pyrokid.myapplication.modules.call.call.CallActivity;
import com.pyrokid.myapplication.service.SinchService;
import com.sinch.android.rtc.PushPair;
import com.sinch.android.rtc.calling.Call;
import com.sinch.android.rtc.calling.CallEndCause;
import com.sinch.android.rtc.video.VideoCallListener;

import java.util.List;

import moxy.presenter.InjectPresenter;

public class IncomingCallActivity extends BaseActivity implements IncomingCallView {


    @InjectPresenter
    IncomingCallPresenter incomingCallPresenter;


    static final String TAG = IncomingCallActivity.class.getSimpleName();
    private String mCallId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incoming_call);

        Button answer = findViewById(R.id.answerButton);
        answer.setOnClickListener(mClickListener);
        Button decline = findViewById(R.id.declineButton);
        decline.setOnClickListener(mClickListener);

        mCallId = getIntent().getStringExtra(SinchService.CALL_ID);
    }

    @Override
    protected void onServiceConnected() {
        Call call = getSinchServiceInterface().getCall(mCallId);
        if (call != null) {
            call.addCallListener(new SinchCallListener());
            TextView remoteUser = findViewById(R.id.remoteUser);
            remoteUser.setText(call.getRemoteUserId());

        } else {
            Log.e(TAG, "Started with invalid callId, aborting");
            finish();
        }
    }

    private void answerClicked() {
        Call call = getSinchServiceInterface().getCall(mCallId);
        if (call != null) {
            call.answer();
            Intent intent = new Intent(this, CallActivity.class);
            intent.putExtra(SinchService.CALL_ID, mCallId);
            startActivity(intent);
        } else {
            finish();
        }
    }

    private void declineClicked() {
        Call call = getSinchServiceInterface().getCall(mCallId);
        if (call != null) {
            call.hangup();
        }
        finish();
    }

    private class SinchCallListener implements VideoCallListener {

        @Override
        public void onCallEnded(Call call) {
            CallEndCause cause = call.getDetails().getEndCause();
            Log.d(TAG, "Call ended, cause: " + cause.toString());
            finish();
        }

        @Override
        public void onShouldSendPushNotification(Call call, List<PushPair> list) {

        }

        @Override
        public void onCallEstablished(Call call) {
            Log.d(TAG, "Call established");
        }

        @Override
        public void onCallProgressing(Call call) {
            Log.d(TAG, "Call progressing");
        }


        @Override
        public void onVideoTrackAdded(Call call) {
            // Display some kind of icon showing it's a video call
        }
        @Override
        public void onVideoTrackPaused(Call call) {
            // Display some kind of icon showing it's a video call
        }
        @Override
        public void onVideoTrackResumed(Call call) {
            // Display some kind of icon showing it's a video call
        }
    }

    private View.OnClickListener mClickListener = v -> {
        switch (v.getId()) {
            case R.id.answerButton:
                answerClicked();
                break;
            case R.id.declineButton:
                declineClicked();
                break;
        }
    };

}
