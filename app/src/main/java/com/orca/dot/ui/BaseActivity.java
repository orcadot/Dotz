package com.orca.dot.ui;

import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.orca.dot.R;

public class BaseActivity extends AppCompatActivity {

    private Toolbar mToolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        getToolbar();
    }

    protected Toolbar getToolbar(){
        if(mToolBar == null){
            mToolBar = (Toolbar) findViewById(R.id.toolbar);
            if(mToolBar != null) {
                setSupportActionBar(mToolBar);
                getSupportActionBar().setDisplayShowHomeEnabled(true);
            }
        }
        return mToolBar;
    }

    protected void setActivityTitle(String title){
        setTitle(title);
    }
}
