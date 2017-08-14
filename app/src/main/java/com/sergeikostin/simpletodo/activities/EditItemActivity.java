package com.sergeikostin.simpletodo.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.sergeikostin.simpletodo.ItemsAdapter;
import com.sergeikostin.simpletodo.R;

public class EditItemActivity extends AppCompatActivity {

    private EditText mEditItem;
    private Button mSaveButton;
    private Intent mIntent;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_edit_item );
        mEditItem = (EditText) findViewById( R.id.edit_item );
        mSaveButton = (Button) findViewById( R.id.save_item );

        mIntent = getIntent();
        mEditItem.setText( mIntent.getStringExtra( ItemsAdapter.ITEM_EXTRA_VALUE ) );
        final int pos =  mIntent.getIntExtra( ItemsAdapter.ITEM_EXTRA_POSITION, 0 );

        mSaveButton.setOnClickListener( new OnClickListener() {
            @Override public void onClick( View v ) {
                Intent data = new Intent(  );
                data.putExtra( ItemsAdapter.ITEM_EXTRA_VALUE, mEditItem.getText().toString() );
                data.putExtra( ItemsAdapter.ITEM_EXTRA_POSITION, pos );
                setResult( RESULT_OK, data );
                finish();
            }
        } );

    }
}
