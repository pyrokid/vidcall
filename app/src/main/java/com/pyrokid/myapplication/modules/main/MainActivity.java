package com.pyrokid.myapplication.modules.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pyrokid.myapplication.R;
import com.pyrokid.myapplication.common.BaseActivity;
import com.pyrokid.myapplication.modules.call.call.CallActivity;
import com.pyrokid.myapplication.service.SinchService;
import com.sinch.android.rtc.calling.Call;

import java.util.ArrayList;
import java.util.List;

import moxy.presenter.InjectPresenter;

public class MainActivity extends BaseActivity implements MainView, ContactAdapter.ItemClickListener {


    @InjectPresenter
    MainPresenter mainPresenter;

    private Button mCallButton;
    private EditText mCallName;
    private RecyclerView mContactList;
    private ContactAdapter contactAdapter;
    private List<String> contacts = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContactList = findViewById(R.id.contactList);
        mContactList.setLayoutManager(new LinearLayoutManager(this));

        contacts.add("Billy");
        contacts.add("Ross");
        contacts.add("Boss");


        contactAdapter = new ContactAdapter(this, contacts);
        contactAdapter.setClickListener(this);
        mContactList.setAdapter(contactAdapter);


        mCallName = findViewById(R.id.callName);
        mCallButton = findViewById(R.id.callButton);
        mCallButton.setEnabled(false);
        mCallButton.setOnClickListener(buttonClickListener);

        Button stopButton = findViewById(R.id.stopButton);
        stopButton.setOnClickListener(buttonClickListener);
    }

    @Override
    protected void onServiceConnected() {
        TextView userName = findViewById(R.id.loggedInName);
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
            Toast.makeText(this, R.string.enter_user_name_message, Toast.LENGTH_LONG).show();
            return;
        }

        Call call = getSinchServiceInterface().callUserVideo(userName);
        Intent callScreen = new Intent(this, CallActivity.class);
        callScreen.putExtra(SinchService.CALL_ID, call.getCallId());
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

    @Override
    public void onItemClick(View view, int position) {
        mCallName.setText(contacts.get(position));
    }
}
