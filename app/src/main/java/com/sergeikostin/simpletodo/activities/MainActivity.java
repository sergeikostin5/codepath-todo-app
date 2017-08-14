package com.sergeikostin.simpletodo.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.sergeikostin.simpletodo.ItemsAdapter;
import com.sergeikostin.simpletodo.R;
import com.sergeikostin.simpletodo.util.DataPersistUtil;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private List<String> items;
    private FloatingActionButton mAddItemButton;
    private ItemsAdapter mItemsAdapter;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        mRecyclerView = (RecyclerView) findViewById( R.id.rvItems );
        mAddItemButton = (FloatingActionButton) findViewById( R.id.btnAddItem );

        items = DataPersistUtil.readItems(this);

        mItemsAdapter = new ItemsAdapter( this, items );

        mRecyclerView.setAdapter( mItemsAdapter );
        LinearLayoutManager layoutManager = new LinearLayoutManager( this );
        mRecyclerView.setLayoutManager( layoutManager );
        mRecyclerView.addItemDecoration(new DividerItemDecoration( this, layoutManager.getOrientation() ));

        mAddItemButton.setOnClickListener( new OnClickListener() {
            @Override public void onClick( View v ) {
                EditText etNewItem = (EditText) findViewById( R.id.etNewItem );
                String itemText = etNewItem.getText().toString();
                if( !TextUtils.isEmpty( itemText )) {
                    items.add( itemText );
                    mItemsAdapter.notifyItemChanged( items.size() - 1 );
                    etNewItem.setText( "" );
                    hideKeyboard();
                    DataPersistUtil.writeItems( MainActivity.this, items );
                }else{
                    Toast.makeText(MainActivity.this, "Task can not be blank", Toast.LENGTH_SHORT).show();
                }
            }
        } );
    }

    private void hideKeyboard(){
        InputMethodManager imm = (InputMethodManager) getSystemService( Context.INPUT_METHOD_SERVICE);
        if (null != getCurrentFocus())
            imm.hideSoftInputFromWindow(getCurrentFocus()
                    .getApplicationWindowToken(), 0);
    }

    @Override public void onActivityResult( int requestCode, int resultCode, Intent data ) {
        if(resultCode == RESULT_OK && requestCode == ItemsAdapter.REQUEST_CODE){
            String name = data.getExtras().getString( ItemsAdapter.ITEM_EXTRA_VALUE );
            int pos = data.getExtras().getInt( ItemsAdapter.ITEM_EXTRA_POSITION );
            items.set( pos, name );
            DataPersistUtil.writeItems( this, items );
            mItemsAdapter.notifyItemChanged( pos );
        }
    }
}
