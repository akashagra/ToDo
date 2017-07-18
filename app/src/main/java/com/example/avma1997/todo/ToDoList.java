package com.example.avma1997.todo;

import java.io.Serializable;

/**
 * Created by Avma1997 on 6/23/2017.
 */

public class ToDoList implements Serializable{
    int id;
    String title;
    String date;
    String time;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public ToDoList(String title, String date, String time,int id) {
        this.id=id;
        this.title = title;
        this.date = date;
        this.time = time;



    }


}
