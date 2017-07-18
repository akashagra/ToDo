package com.example.avma1997.todo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Avma1997 on 6/23/2017.
 */

public class ToDoListAdapter extends ArrayAdapter<ToDoList> {
    ArrayList<ToDoList> todoList;
    Context context;
    OnCheckBoxClicked listener;
    void setOnListCheckBoxclickedListener( OnCheckBoxClicked listener)
    {
        this.listener=listener;
    }

    public ToDoListAdapter(Context context, ArrayList<ToDoList> todoList) {
        super(context, 0);
        this.context = context;
        this.todoList = todoList;
    }

    public int getCount() {
        return todoList.size();
    }

    static class ToDoViewHolder {

        TextView nameTextView;
        TextView dateTextView;
        TextView timeTextView;
        CheckBox checkBox;

        ToDoViewHolder(TextView nameTextView, TextView dateTextView, TextView timeTextView, CheckBox checkBox) {
            this.nameTextView = nameTextView;
            this.dateTextView = dateTextView;
            this.timeTextView = timeTextView;
            this.checkBox = checkBox;
        }

    }

    public View getView( final int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item, null);
            convertView.setBackgroundResource(R.drawable.shape);
            TextView nameTextView = (TextView) convertView.findViewById(R.id.todoNameTextView);
            TextView dateTextView = (TextView) convertView.findViewById(R.id.tododateTextView);
            TextView timeTextView = (TextView) convertView.findViewById(R.id.todoTimeTextView);
            CheckBox checkbox = (CheckBox) convertView.findViewById(R.id.todoCheckBox);
            ToDoViewHolder todoViewHolder = new ToDoViewHolder(nameTextView, dateTextView, timeTextView, checkbox);
            convertView.setTag(todoViewHolder);

        }
        final ToDoList t = todoList.get(position);

        ToDoViewHolder todoViewHolder = (ToDoViewHolder) convertView.getTag();

        todoViewHolder.nameTextView.setText(t.title);
        todoViewHolder.dateTextView.setText(t.date);
        todoViewHolder.timeTextView.setText(t.time);
        todoViewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                listener.listCheckBoxClicked(position);


            }
        });


        return convertView;
    }


    public interface OnCheckBoxClicked {
        void listCheckBoxClicked(int position);
    }

}
