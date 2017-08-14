package com.sergeikostin.simpletodo.model;

import java.util.Date;

/**
 * Created by sergei.kostin on 7/23/17.
 */

public class TodoItem {

    private String mName;
    private String mDescription;
    private Date mDate;

    public String getName() {
        return mName;
    }

    public void setName( String name ) {
        mName = name;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription( String description ) {
        mDescription = description;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate( Date date ) {
        mDate = date;
    }

    public Priority getPriority() {
        return mPriority;
    }

    public void setPriority( Priority priority ) {
        mPriority = priority;
    }

    public Status getStatus() {
        return mStatus;
    }

    public void setStatus( Status status ) {
        mStatus = status;
    }

    private Priority mPriority;
    private Status mStatus;



    public enum Priority{
        HIGH,
        LOW,
        MEDIUM
    }

    public enum Status{
        DONE,
        TODO,
        INPROGRESS
    }
}
