package com.example.avma1997.todo;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;


import java.io.Serializable;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by Avma1997 on 6/23/2017.
 */

public class ToDoListActivity extends AppCompatActivity {
    AutoCompleteTextView titleTextView;
    EditText dateTextView;
    EditText timeTextView;
    Spinner spinner;
    SeekBar seekBar;
    long date;
    long time;
    long sum;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.todolistactivity);
        titleTextView = (AutoCompleteTextView) findViewById(R.id.todoTitleTextView);
        String[] suggestions = {"Go Biking", "Go Cyling", "Go Running", "Do exercise", "Drink water"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, suggestions);
        titleTextView.setAdapter(adapter);

        dateTextView = (EditText) findViewById(R.id.tododateTextView);
        dateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar newCalendar = Calendar.getInstance();
                int month = newCalendar.get(Calendar.MONTH);  // Current month
                int year = newCalendar.get(Calendar.YEAR);   // Current year
                showDatePicker(ToDoListActivity.this, year, month, 1);
            }
        });


        timeTextView = (EditText) findViewById(R.id.todotimeTextView);
        timeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(ToDoListActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        timeTextView.setText(selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
                time= (long)(3600 *hour + minute*60)+19800;

            }
        });


        Button submitButton = (Button) findViewById(R.id.expenseDetailSubmitButton);
        spinner = (Spinner)findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapters = ArrayAdapter.createFromResource(this,
                R.array.catogery_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapters);
        seekBar=(SeekBar) findViewById(R.id.priorityseekbar);


        final int id = getIntent().getIntExtra("to_do", -1);
        if (id != -1) {
            ToDoOpenHelper todoOpenHelper = new ToDoOpenHelper(this);
            SQLiteDatabase database = todoOpenHelper.getReadableDatabase();
            Cursor cursor = database.query(ToDoOpenHelper.TODO_TABLE_NAME, null, ToDoOpenHelper.TODO_ID + " = " + id, null, null, null, null);
            cursor.moveToNext();

                String title = cursor.getString(cursor.getColumnIndex(ToDoOpenHelper.TODO_TITLE));
                String date = cursor.getString(cursor.getColumnIndex(ToDoOpenHelper.TODO_DATE));
                String time = cursor.getString(cursor.getColumnIndex(ToDoOpenHelper.TODO_TIME));
              int position = cursor.getInt(cursor.getColumnIndex(ToDoOpenHelper.TODO_CATOGERY));
              int progress=cursor.getInt(cursor.getColumnIndex(ToDoOpenHelper.TODO_PRIORITY));
                titleTextView.setText(title);
                dateTextView.setText(date);
                timeTextView.setText(time);
                spinner.setSelection(position);
                seekBar.setProgress(progress);


        }
            submitButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String title = titleTextView.getText().toString();
                    String dates = dateTextView.getText().toString();
                    String times = timeTextView.getText().toString();
                   int pos= spinner.getSelectedItemPosition();
                    int prior=seekBar.getProgress();

                    ToDoOpenHelper todoOpenHelper = new ToDoOpenHelper(ToDoListActivity.this);
                    SQLiteDatabase database = todoOpenHelper.getWritableDatabase();

                    ContentValues cv = new ContentValues();
                    cv.put(ToDoOpenHelper.TODO_TITLE, title);
                    cv.put(ToDoOpenHelper.TODO_DATE, dates);
                    cv.put(ToDoOpenHelper.TODO_TIME, times);
                    cv.put(ToDoOpenHelper.TODO_CATOGERY,pos);
                    cv.put(ToDoOpenHelper.TODO_PRIORITY,prior);


//
                    if (id != -1) {
                        database.update(ToDoOpenHelper.TODO_TABLE_NAME, cv, ToDoOpenHelper.TODO_ID + " = " + id, null);
                    }
                    else
                    {
                        database.insert(ToDoOpenHelper.TODO_TABLE_NAME, null, cv);
                    }
                    //  ToDoList t=new ToDoList(title,date,time);
                    Intent i = new Intent();
                       i.putExtra("priority", prior);
                    setResult(RESULT_OK, i);
                    finish();
                   sum=date+time+19800;

                    AlarmManager am=(AlarmManager)ToDoListActivity.this.getSystemService(Context.ALARM_SERVICE);
                    Intent intent=new Intent(ToDoListActivity.this,AlarmReceiver.class);
                    intent.putExtra("title_todo",title);
                    PendingIntent pendingIntent= PendingIntent.getBroadcast(ToDoListActivity.this,1,intent,0);
                    am.set(AlarmManager.RTC,sum*1000+1000,pendingIntent);
                    Log.d("mytime",sum+"");

                }
            });


        }
    public void showDatePicker(Context context, int initialYear, int initialMonth, int initialDay) {

        // Creating datePicker dialog object
        // It requires context and listener that is used when a date is selected by the user.

        DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                new DatePickerDialog.OnDateSetListener() {

                    //This method is called when the user has finished selecting a date.
                    // Arguments passed are selected year, month and day
                    @Override
                    public void onDateSet(DatePicker datepicker, int year, int month, int day) {

                        // To get epoch, You can store this date(in epoch) in database
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(year, month, day);
                        date = calendar.getTime().getTime();
                        int hours=calendar.get(Calendar.HOUR_OF_DAY);
                        int minutes=calendar.get(Calendar.MINUTE);
                        date=date-(hours*3600)-(minutes*60);

                        // Setting date selected in the edit text
                        dateTextView.setText(day + "/" + (month + 1) + "/" + year);
                    }
                }, initialYear, initialMonth, initialDay);

        //Call show() to simply show the dialog
        datePickerDialog.show();

    }

}







