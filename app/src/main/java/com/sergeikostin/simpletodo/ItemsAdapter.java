package com.sergeikostin.simpletodo;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sergeikostin.simpletodo.fragments.EditItemFragment.EditorTodoItemListener;
import com.sergeikostin.simpletodo.model.TodoItem;
import com.sergeikostin.simpletodo.util.DataBaseHelper;

import java.util.List;

/**
 * Created by sergei.kostin on 7/23/17.
 */

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ViewHolder> {

    private List<TodoItem> mItems;
    private EditorTodoItemListener mEditListener;
    private Activity mActivity;

    public ItemsAdapter( Activity activity, EditorTodoItemListener listener, List<TodoItem> items){
        mActivity = activity;
        mEditListener = listener;
        mItems = items;
    }

    @Override public ItemsAdapter.ViewHolder onCreateViewHolder( ViewGroup parent, int viewType ) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from( context );
        View itemView = inflater.inflate( R.layout.list_item_view, parent, false );
        return new ViewHolder( itemView );
    }

    @Override public void onBindViewHolder( ViewHolder holder, int position ) {
        TodoItem item = mItems.get( position );

        TextView taskTitle = holder.itemNameView;
        TextView taskPriority = holder.itemPriorityView;
        TextView taskDueDate = holder.itemDueDate;
        taskTitle.setText( item.getName() );
        taskDueDate.setText( "Due Date: " + item.getDate() );

        int color;
        switch ( item.getPriority() ){
            case LOW:
                color = Color.GREEN;
                break;
            case MEDIUM:
                color = Color.BLUE;
                break;
            case HIGH:
                color = Color.RED;
                break;
            default:
                color = Color.GREEN;;
        }
        taskPriority.setText( item.getPriority().toString() );
        taskPriority.setTextColor( color );

    }

    @Override public int getItemCount() {
        return mItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView itemNameView;
        public TextView itemPriorityView;
        public TextView itemDueDate;

        public ViewHolder( final View itemView){
            super(itemView);

            itemNameView = (TextView) itemView.findViewById( R.id.item_title );
            itemPriorityView = (TextView) itemView.findViewById( R.id.item_priority );
            itemDueDate = (TextView) itemView.findViewById( R.id.due_date );
            itemView.setOnLongClickListener( new OnLongClickListener() {
                @Override public boolean onLongClick( View v ) {
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION){
                        long id = mItems.get( pos ).getId();
                        mItems.remove( pos );
                        notifyItemRemoved( pos );
                        DataBaseHelper.getInstance( mActivity ).deleteItem( id );
                        return true;
                    }
                    return false;
                }
            } );

            itemView.setOnClickListener( new OnClickListener() {
                @Override public void onClick( View v ) {
                    TodoItem item = mItems.get( getAdapterPosition());
                    mEditListener.showEditDialog( "Edit Task" , item);
                }
            } );
        }

    }

}
