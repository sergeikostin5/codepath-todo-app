package com.sergeikostin.simpletodo.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import com.sergeikostin.simpletodo.ItemsAdapter;
import com.sergeikostin.simpletodo.R;
import com.sergeikostin.simpletodo.fragments.EditItemFragment;
import com.sergeikostin.simpletodo.fragments.EditItemFragment.EditorTodoItemListener;
import com.sergeikostin.simpletodo.model.TodoItem;
import com.sergeikostin.simpletodo.util.DataBaseHelper;

import java.util.List;

public class MainActivity extends AppCompatActivity implements EditorTodoItemListener {

    private RecyclerView mRecyclerView;
    private List<TodoItem> items;
    private FloatingActionButton mAddItemButton;
    private ItemsAdapter mItemsAdapter;
    private DataBaseHelper mDbHelper;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setLogo( R.drawable.zastavka );
        myToolbar.setTitle( "" );
        setSupportActionBar(myToolbar);

        mDbHelper = DataBaseHelper.getInstance( this );


        mRecyclerView = (RecyclerView) findViewById( R.id.rvItems );
        mAddItemButton = (FloatingActionButton) findViewById( R.id.btnAddItem );

        items = mDbHelper.getAllItems();

        mItemsAdapter = new ItemsAdapter( this, this, items );

        mRecyclerView.setAdapter( mItemsAdapter );
        LinearLayoutManager layoutManager = new LinearLayoutManager( this );
        mRecyclerView.setLayoutManager( layoutManager );
        mRecyclerView.addItemDecoration(new DividerItemDecoration( this, layoutManager.getOrientation() ));

        mAddItemButton.setOnClickListener( new OnClickListener() {
            @Override public void onClick( View v ) {
               showEditDialog("New Task", null);
            }
        } );
    }

    private void hideKeyboard(){
        InputMethodManager imm = (InputMethodManager) getSystemService( Context.INPUT_METHOD_SERVICE);
        if (null != getCurrentFocus())
            imm.hideSoftInputFromWindow(getCurrentFocus()
                    .getApplicationWindowToken(), 0);
    }

    @Override
    public void showEditDialog( String title, @Nullable TodoItem item ) {
        FragmentManager fm = getSupportFragmentManager();

        EditItemFragment editNameDialogFragment = EditItemFragment.newInstance(title, this, item);
        editNameDialogFragment.show(fm, "fragment_edit_name");
    }

    @Override public void onEditorAction( TodoItem item, boolean edited ) {
        if(edited){
            mDbHelper.updateItem( item );
        }else {
            item.setId( mDbHelper.addItem( item ) );
            items.add( item );
        }
        mItemsAdapter.notifyDataSetChanged(  );
    }
}
