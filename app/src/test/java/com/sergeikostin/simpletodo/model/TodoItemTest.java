package com.sergeikostin.simpletodo.model;

import com.sergeikostin.simpletodo.model.TodoItem.Priority;

import org.junit.Test;

/**
 * Created by sergei.kostin on 8/16/17.
 */
public class TodoItemTest {

    @Test
    public void testItem(){
        Priority pr = Priority.valueOf( "LOW" );
        System.out.println(pr.ordinal());

    }

}