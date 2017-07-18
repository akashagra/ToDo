package com.example.avma1997.todo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Avma1997 on 6/28/2017.
 */

public class ToDoOpenHelper  extends SQLiteOpenHelper{
    public final static String TODO_TABLE_NAME  = "todolist";
    public final static String TODO_TITLE  = "title";
    public final static String TODO_DATE = "date" ;
    public final static String TODO_TIME = "time" ;
    public final static String TODO_ID = "_id" ;
    public final static String TODO_CATOGERY="catogery";
    public final static String TODO_PRIORITY="priority";



    public ToDoOpenHelper(Context context) {
        super(context,"todo.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "create table " + TODO_TABLE_NAME +" ( " + TODO_ID +
                " integer primary key autoincrement, "
                + TODO_TITLE +" text, "
                + TODO_DATE + " text, "
                + TODO_CATOGERY + " integer, "
                + TODO_PRIORITY + " integer, "
                + TODO_TIME + " text);";
        db.execSQL(query);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
