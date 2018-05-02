package com.example.jonib.jpicslibrary;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.jpics.jpics.JPics;
import com.srx.widget.PullToLoadView;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.main_app_bar)
    Toolbar mToolBar;
    @BindView(R.id.pullToLoadView)
    PullToLoadView myList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(mToolBar);
        //Objects.requireNonNull(getSupportActionBar()).setTitle("Pin Pictures");
        JPics.Config.setCacheCapacity(15);


        new Pagination(this, myList).initializePaginator();

    }
}
