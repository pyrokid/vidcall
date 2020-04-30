package com.pyrokid.myapplication.modules.main;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.pyrokid.myapplication.R;
import com.pyrokid.myapplication.common.BaseActivity;

import moxy.presenter.InjectPresenter;

public class MainActivity extends BaseActivity implements MainView {


    @InjectPresenter
    MainPresenter mainPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }
}
