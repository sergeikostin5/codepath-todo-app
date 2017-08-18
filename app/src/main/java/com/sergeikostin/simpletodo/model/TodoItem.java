package com.sergeikostin.simpletodo.model;

/**
 * Created by sergei.kostin on 7/23/17.
 */

public class TodoItem {

    private String mName;
    private String mDescription;
    private String mDate;
    private long id;

    public long getId() {
        return id;
    }

    public void setId( long id ) {
        this.id = id;
    }

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

    public String getDate() {
        return mDate;
    }

    public void setDate( String date ) {
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

    @Override public String toString() {
        return "TodoItem{" +
                "mName='" + mName + '\'' +
                "mId='" + id + '\'' +
                ", mDescription='" + mDescription + '\'' +
                ", mDate='" + mDate + '\'' +
                ", mPriority=" + mPriority +
                ", mStatus=" + mStatus +
                '}';
    }

    public enum Priority{
        LOW,
        MEDIUM,
        HIGH
    }

    public enum Status{
        TODO,
        WORKING,
        DONE
    }
}
