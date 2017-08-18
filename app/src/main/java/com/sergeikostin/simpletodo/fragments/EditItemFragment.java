package com.sergeikostin.simpletodo.fragments;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.sergeikostin.simpletodo.R;
import com.sergeikostin.simpletodo.model.TodoItem;
import com.sergeikostin.simpletodo.model.TodoItem.Priority;
import com.sergeikostin.simpletodo.model.TodoItem.Status;

import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditItemFragment extends DialogFragment {

    private EditText mEditTaskTitle, mEditTaskNotes;
    private TextView mEditDueDateView;
    private Calendar mCalendar;
    private Spinner mPrioritySpinner, mProgressSpinner;
    private EditorTodoItemListener mEditorListener;
    private TodoItem mTodoItem;
    private boolean mEdited;

    public interface EditorTodoItemListener{
        void onEditorAction(TodoItem item, boolean edited);
        void showEditDialog(String title, TodoItem item);
    }

    public EditItemFragment() {
        // Required empty public constructor
    }

    public static EditItemFragment newInstance( String title, EditorTodoItemListener listener, @Nullable TodoItem item) {

        EditItemFragment frag = new EditItemFragment();
        frag.mEditorListener = listener;
        frag.mTodoItem = item;
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog( Bundle savedInstanceState) {
        String title = getArguments().getString("title");
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_edit_item, null);
        alertDialogBuilder.setView( view );


        // Get field from view
        mEditTaskTitle = (EditText) view.findViewById(R.id.edit_task);

        // Show soft keyboard automatically and request focus to field
        mEditTaskTitle.requestFocus();

        mEditTaskNotes = (EditText) view.findViewById( R.id.task_notes );

        mEditDueDateView = (TextView) view.findViewById( R.id.due_date_picker );
        mCalendar = Calendar.getInstance();
        String currentDate = mCalendar.get( Calendar.MONTH ) + "/" + mCalendar.get( Calendar.DATE ) + "/" + mCalendar.get( Calendar.YEAR );
        mEditDueDateView.setText( currentDate );

        // Priority Spinner
        mPrioritySpinner = (Spinner) view.findViewById( R.id.priority_level );

        ArrayAdapter<CharSequence> priorityAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.priorities_array, android.R.layout.simple_spinner_item);

        priorityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        mPrioritySpinner.setAdapter(priorityAdapter);

        // Progress Spinner
        mProgressSpinner = (Spinner) view.findViewById( R.id.progress_status );

        ArrayAdapter<CharSequence> progressAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.progress_array, android.R.layout.simple_spinner_item);

        progressAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        mProgressSpinner.setAdapter(progressAdapter);


        setOnClickListeners();
        if(mTodoItem != null) fillTodoItem(mTodoItem);

        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setPositiveButton("Save",  new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String taskTitle = mEditTaskTitle.getText().toString();
                String taskNotes = mEditTaskNotes.getText().toString();
                String taskDueDate = mEditDueDateView.getText().toString();
                String taskPriority = mPrioritySpinner.getSelectedItem().toString();
                String taskProgress = mProgressSpinner.getSelectedItem().toString();
                if(TextUtils.isEmpty( taskTitle )) {
                    Toast.makeText(getActivity(), "Task can not be blank", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(mTodoItem == null) {
                    mTodoItem = new TodoItem();
                    mEdited = false;
                }else{
                    mEdited = true;
                }
                mTodoItem.setName( taskTitle );
                mTodoItem.setDate( taskDueDate );
                mTodoItem.setDescription( taskNotes );
                mTodoItem.setPriority( Priority.valueOf( taskPriority ));
                mTodoItem.setStatus( Status.valueOf( taskProgress ));
                mEditorListener.onEditorAction( mTodoItem, mEdited );
            }
        });
        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (dialog != null ) {
                    dialog.dismiss();
                }
            }

        });

        return alertDialogBuilder.create();
    }

    private void fillTodoItem( TodoItem todoItem ) {
        mEditTaskTitle.setText( todoItem.getName() );
        if(!TextUtils.isEmpty( todoItem.getDescription() )){
            mEditTaskNotes.setText( todoItem.getDescription() );
        }
        mEditDueDateView.setText( todoItem.getDate() );
        mPrioritySpinner.setSelection( todoItem.getPriority().ordinal() );
        mProgressSpinner.setSelection( todoItem.getStatus().ordinal() );
    }


    @Override public void onResume() {
        // Get existing layout params for the window
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        // Assign window properties to fill the parent
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
        // Call super onResume after sizing
        super.onResume();
    }

    private void setOnClickListeners(){
        mEditDueDateView.setOnClickListener( new OnClickListener() {
            @Override public void onClick( View v ) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet( DatePicker view, int year,
                                                   int monthOfYear, int dayOfMonth) {

                                mEditDueDateView.setText(dayOfMonth + "/" + monthOfYear + "/" + year);

                            }
                        }, mCalendar.get( Calendar.YEAR ), mCalendar.get( Calendar.MONTH ), mCalendar.get( Calendar.DATE ));
                datePickerDialog.show();
            }
        } );
    }


}
