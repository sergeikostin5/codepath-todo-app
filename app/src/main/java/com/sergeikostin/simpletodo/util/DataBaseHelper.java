package com.sergeikostin.simpletodo.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.sergeikostin.simpletodo.model.TodoItem;
import com.sergeikostin.simpletodo.model.TodoItem.Priority;
import com.sergeikostin.simpletodo.model.TodoItem.Status;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sergei.kostin on 8/17/17.
 */

public class DataBaseHelper extends SQLiteOpenHelper {

    private static final String TAG = SQLiteOpenHelper.class.getSimpleName();

    private static DataBaseHelper sInstance;

    private static final String DATABASE_NAME = "todoDa";
    private static final int DATABASE_VERSION = 2;

    // Table Names
    private static final String TABLE_TODO_ITEMS = "todo_items";

    // Post Table Columns
    private static final String KEY_TODO_ITEM_ID = "id";
    private static final String KEY_TODO_ITEM_TITLE = "title";
    private static final String KEY_TODO_ITEM_DESCRIPTION = "description";
    private static final String KEY_TODO_ITEM_DUE_DATE = "due_date";
    private static final String KEY_TODO_ITEM_PRIORITY = "priority";
    private static final String KEY_TODO_ITEM_STATUS = "status";

    private DataBaseHelper( Context context, String name, CursorFactory factory, int version ) {
        super( context, name, factory, version );
    }

    public static synchronized DataBaseHelper getInstance( Context context ) {
        if ( sInstance == null ) {
            sInstance = new DataBaseHelper( context.getApplicationContext(), DATABASE_NAME, null, DATABASE_VERSION );
        }
        Log.d(TAG, "instance created");
        return sInstance;
    }


    // Called when the database is created for the FIRST time.
    // If a database already exists on disk with the same DATABASE_NAME, this method will NOT be called.
    @Override public void onCreate( SQLiteDatabase db ) {
        String CREATE_TODO_ITEMS_TABLE = "CREATE TABLE " + TABLE_TODO_ITEMS +
                "(" +
                KEY_TODO_ITEM_ID + " INTEGER PRIMARY KEY," + // Define a primary key
                KEY_TODO_ITEM_TITLE + " TEXT," +
                KEY_TODO_ITEM_DESCRIPTION + " TEXT," +
                KEY_TODO_ITEM_DUE_DATE + " TEXT," +
                KEY_TODO_ITEM_PRIORITY + " TEXT," +
                KEY_TODO_ITEM_STATUS + " TEXT" +
                ")";


        db.execSQL( CREATE_TODO_ITEMS_TABLE );
    }

    // Called when the database needs to be upgraded.
    // This method will only be called if a database already exists on disk with the same DATABASE_NAME,
    // but the DATABASE_VERSION is different than the version of the database that exists on disk.
    @Override public void onUpgrade( SQLiteDatabase db, int oldVersion, int newVersion ) {
        if ( oldVersion != newVersion ) {
            // Simplest implementation is to drop all old tables and recreate them
            Log.d(TAG, "onUpgrade called");
            db.execSQL( "DROP TABLE IF EXISTS " + TABLE_TODO_ITEMS );
            onCreate( db );
        }
    }

    // Called when the database connection is being configured.
    // Configure database settings for things like foreign key support, write-ahead logging, etc.
    @Override
    public void onConfigure( SQLiteDatabase db ) {
        super.onConfigure( db );
        db.setForeignKeyConstraintsEnabled( true );
    }

    public long addItem( TodoItem item ) {

        // Create and/or open the database for writing
        SQLiteDatabase db = getWritableDatabase();

        // It's a good idea to wrap our insert in a transaction. This helps with performance and ensures
        // consistency of the database.
        db.beginTransaction();
        long id = 0;
        try {

            ContentValues values = new ContentValues();
            values.put( KEY_TODO_ITEM_TITLE, item.getName() );
            values.put( KEY_TODO_ITEM_DESCRIPTION, item.getDescription() );
            values.put( KEY_TODO_ITEM_DUE_DATE, item.getDate() );
            values.put( KEY_TODO_ITEM_PRIORITY, item.getPriority().toString() );
            values.put( KEY_TODO_ITEM_STATUS, item.getStatus().toString() );

            // Notice how we haven't specified the primary key. SQLite auto increments the primary key column.
            id = db.insertOrThrow( TABLE_TODO_ITEMS, null, values );
            db.setTransactionSuccessful();
            Log.d(TAG, "item " + item + "was inserted");
        } catch ( Exception e ) {
            Log.d( TAG, "Error while trying to add item to database" );
        } finally {
            db.endTransaction();
        }
        return id;
    }

    // Get all items in the database
    public List<TodoItem> getAllItems() {
        List<TodoItem> items = new ArrayList<>();

        // SELECT * FROM TODO_ITEMS

        String TODO_ITEMS_SELECT_QUERY = String.format( "SELECT * FROM %s", TABLE_TODO_ITEMS );

        // "getReadableDatabase()" and "getWriteableDatabase()" return the same object (except under low
        // disk space scenarios)
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery( TODO_ITEMS_SELECT_QUERY, null );
        try {
            if ( cursor.moveToFirst() ) {
                do {
                    TodoItem item = new TodoItem();
                    item.setName( cursor.getString( cursor.getColumnIndex( KEY_TODO_ITEM_TITLE ) ) );
                    item.setId( Long.parseLong(cursor.getString( cursor.getColumnIndex( KEY_TODO_ITEM_ID ) ) ) );
                    item.setDescription( cursor.getString( cursor.getColumnIndex( KEY_TODO_ITEM_DESCRIPTION ) ) );
                    item.setDate( cursor.getString( cursor.getColumnIndex( KEY_TODO_ITEM_DUE_DATE ) ) );
                    item.setPriority( Priority.valueOf( cursor.getString( cursor.getColumnIndex( KEY_TODO_ITEM_PRIORITY ) ) ) );
                    item.setStatus( Status.valueOf( cursor.getString( cursor.getColumnIndex( KEY_TODO_ITEM_STATUS ) ) ) );
                    Log.d(TAG, "item extracted " + item );
                    items.add( item );
                } while ( cursor.moveToNext() );
            }
        } catch ( Exception e ) {
            Log.d( TAG, "Error while trying to get items from database" );
        } finally {
            if ( cursor != null && !cursor.isClosed() ) {
                cursor.close();
            }
        }
        Log.d(TAG, "items size " + items.size() + "were returned");
        return items;
    }

    public boolean deleteItem(long id){
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        int count = 0;
        try {
            count = db.delete(TABLE_TODO_ITEMS, KEY_TODO_ITEM_ID + "=" + id, null);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to delete item");
        } finally {
            db.endTransaction();
        }
        return count > 0;
    }

    public void updateItem(TodoItem item){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put( KEY_TODO_ITEM_TITLE, item.getName() );
        values.put( KEY_TODO_ITEM_DESCRIPTION, item.getDescription() );
        values.put( KEY_TODO_ITEM_DUE_DATE, item.getDate() );
        values.put( KEY_TODO_ITEM_PRIORITY, item.getPriority().toString() );
        values.put( KEY_TODO_ITEM_STATUS, item.getStatus().toString() );
        db.update(TABLE_TODO_ITEMS, values, KEY_TODO_ITEM_ID + "=" + item.getId(), null);
    }
}
