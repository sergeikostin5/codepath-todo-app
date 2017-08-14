package com.sergeikostin.simpletodo.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.sergeikostin.simpletodo.R;

public class NewItemActivity extends AppCompatActivity {

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_new_item );
    }
}
