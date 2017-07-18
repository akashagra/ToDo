package com.example.avma1997.todo;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Parcel;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;




public class MainActivity extends AppCompatActivity implements ToDoListAdapter.OnCheckBoxClicked {
    FloatingActionButton fab;
    ListView listView;

    ArrayList<ToDoList> todoList;
    ToDoListAdapter todoListAdapter;
    int priority;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.todoListView);
        todoList = new ArrayList<>();


        todoListAdapter = new ToDoListAdapter(this, todoList);
        todoListAdapter.setOnListCheckBoxclickedListener(this);

        listView.setAdapter(todoListAdapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                ToDoList t= todoList.get(position);

                int ID=t.id;






                Intent i = new Intent(MainActivity.this, ToDoListActivity.class);
                i.putExtra("to_do",ID);


                startActivityForResult(i, 1);
//              ExpenseDetailActivity.title = "abcd";

//                Toast.makeText(MainActivity.this, expenseList.get(position)
//
//
//         + " Clicked ", Toast.LENGTH_SHORT).show();

            }
        });
listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
{

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        ToDoList t=todoList.get(position);
       final int ID= t.id;
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Do you want to remove the Task?");
        builder.setCancelable(false);

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


                ToDoOpenHelper todoOpenHelper = new ToDoOpenHelper(MainActivity.this);

                SQLiteDatabase database = todoOpenHelper.getWritableDatabase();
                database.delete(ToDoOpenHelper.TODO_TABLE_NAME, ToDoOpenHelper.TODO_ID + " = " + ID, null);
                updateToDoList();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
        return true;
    }
});
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent i = new Intent(MainActivity.this, ToDoListActivity.class);


//                i.putExtra("expense", expenseList.get(position));
                //startActivity(i);
                startActivityForResult(i, 1);


            }
        });
        updateToDoList();

    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
          priority=data.getIntExtra("priority",-1);
                updateToDoList();

                Log.i("MainActivityTag", "Result_Ok");
            } else if (resultCode == RESULT_CANCELED) {

            }
        }
    }


    @Override

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void updateToDoList() {

        ToDoOpenHelper todoOpenHelper = new ToDoOpenHelper(this);
        todoList.clear();
        SQLiteDatabase database = todoOpenHelper.getReadableDatabase();
        Cursor cursor = database.query(ToDoOpenHelper.TODO_TABLE_NAME, null, null, null, null, null,ToDoOpenHelper.TODO_PRIORITY+ " DESC ");

        while (cursor.moveToNext()) {

            String title = cursor.getString(cursor.getColumnIndex(ToDoOpenHelper.TODO_TITLE));
            String date = cursor.getString(cursor.getColumnIndex(ToDoOpenHelper.TODO_DATE));
            String time = cursor.getString(cursor.getColumnIndex(ToDoOpenHelper.TODO_TIME));
            int id=cursor.getInt(cursor.getColumnIndex(ToDoOpenHelper.TODO_ID));
            ToDoList t = new ToDoList( title, date,time,id);
            todoList.add(t);
        }
        todoListAdapter.notifyDataSetChanged();

    }

    @Override
    public void listCheckBoxClicked(final int position) {

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Are you sure you have completed the task?");
        builder.setCancelable(false);

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int checkid=todoList.get(position).id;
                ToDoOpenHelper todoOpenHelper = new ToDoOpenHelper(MainActivity.this);

                 SQLiteDatabase database = todoOpenHelper.getWritableDatabase();
                 database.delete(ToDoOpenHelper.TODO_TABLE_NAME,ToDoOpenHelper.TODO_ID + " = " + checkid ,null);
                 updateToDoList();
Toast.makeText(MainActivity.this,"Task Finished",Toast.LENGTH_SHORT).show();


            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();

    }
}