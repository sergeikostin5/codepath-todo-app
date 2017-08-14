package com.sergeikostin.simpletodo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sergeikostin.simpletodo.activities.EditItemActivity;
import com.sergeikostin.simpletodo.util.DataPersistUtil;

import java.util.List;

/**
 * Created by sergei.kostin on 7/23/17.
 */

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ViewHolder> {

    public static final int REQUEST_CODE = 20;
    public static final String ITEM_EXTRA_POSITION = "item_extra_position";
    public static final String ITEM_EXTRA_VALUE = "item_extra_value";

    private List<String> mItems;
    private Activity mActivity;

    public ItemsAdapter( Activity activity, List<String> items){
        mActivity = activity;
        mItems = items;
    }

    @Override public ItemsAdapter.ViewHolder onCreateViewHolder( ViewGroup parent, int viewType ) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from( context );
        View itemView = inflater.inflate( android.R.layout.simple_list_item_1, parent, false );
        return new ViewHolder( itemView );
    }

    @Override public void onBindViewHolder( ViewHolder holder, int position ) {
        String itemName = mItems.get( position );

        TextView textView = holder.itemNameView;
        textView.setText( itemName );
    }

    @Override public int getItemCount() {
        return mItems.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView itemNameView;

        public ViewHolder( final View itemView){
            super(itemView);

            itemNameView = (TextView) itemView.findViewById( android.R.id.text1 );
            itemView.setOnLongClickListener( new OnLongClickListener() {
                @Override public boolean onLongClick( View v ) {
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION){
                        mItems.remove( pos );
                        notifyItemRemoved( pos );
                        DataPersistUtil.writeItems( mActivity, mItems );
                        return true;
                    }
                    return false;
                }
            } );

            itemView.setOnClickListener( new OnClickListener() {
                @Override public void onClick( View v ) {
                    Intent intent = new Intent(mActivity, EditItemActivity.class);
                    int pos = getAdapterPosition();
                    intent.putExtra( ITEM_EXTRA_POSITION, pos );
                    intent.putExtra( ITEM_EXTRA_VALUE, itemNameView.getText().toString() );
                    mActivity.startActivityForResult(intent, REQUEST_CODE);
                }
            } );
        }

    }

}
