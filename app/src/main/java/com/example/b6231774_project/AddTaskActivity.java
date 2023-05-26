package com.example.b6231774_project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddTaskActivity extends AppCompatActivity {
    CompactCalendarView cpCals;
    User ar;
    dbHandler db = new dbHandler(AddTaskActivity.this);
    String dttext;
    EditText datetime = null;
    EditText subject = null;
    EditText desc = null;
    SharedPreferences sharedPref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        datetime = (EditText)findViewById(R.id.datepickerbtn);
        subject = (EditText) findViewById(R.id.subjfield);
        desc = (EditText) findViewById(R.id.descfield);
        datetime.setInputType(InputType.TYPE_NULL);

        datetime.setOnClickListener(view -> DateTimePicker(datetime));
        subject.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });
        desc.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        setTb();
    }

    private void setTb() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Add New Task");
        setSupportActionBar(toolbar);
    }

    private void DateTimePicker(EditText datetime){
        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                calendar.set(Calendar.YEAR,year);
                calendar.set(Calendar.MONTH,month);
                calendar.set(Calendar.DAY_OF_MONTH,day);

                TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                            calendar.set(Calendar.HOUR_OF_DAY,hour);
                            calendar.set(Calendar.MINUTE,minute);

                        SimpleDateFormat sp = new SimpleDateFormat("dd MMM yyyy HH:mm a");
                        SimpleDateFormat sp2 = new SimpleDateFormat("dd-MMM-yyyy HH:mm:00");
                        dttext = sp2.format(calendar.getTime());
                        datetime.setText(sp.format(calendar.getTime()));
                    }
                };

                new TimePickerDialog(AddTaskActivity.this, timeSetListener,calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),false).show();
            }
        };

        new DatePickerDialog(AddTaskActivity.this, dateSetListener,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    public void InsertTask(View v){
        int meter  = 0;
        if (subject.getText().toString().equals("")) {
            subject.setHint("this field must not be empty.");
            meter += 1;
        }
        if (desc.getText().toString().equals("")) {
            desc.setHint("this field must not be empty.");
            meter += 1;
        }
        if (datetime.getText().toString().equals("")) {
            datetime.setHint("Please select date and time.");
            meter += 1;
        }
        if (meter != 0)
            return;

        sharedPref = getSharedPreferences("USPREF", Context.MODE_PRIVATE);
        int id = sharedPref.getInt("UserID", 0);

        String stext = subject.getText().toString();
        String dtext = desc.getText().toString();
        int res = 0;
        res = db.insertTask(stext, dtext, dttext, id);
        Toast t;
        if (res == 2) {
            t = Toast.makeText(AddTaskActivity.this, "Somethings went wrong", Toast.LENGTH_SHORT);
            t.show();
            return;
        }
        else if (res == 1) {
            Intent i = new Intent(AddTaskActivity.this,TaskActivity.class);
            i.putExtra("Toast", "Added new task");
            startActivity(i);
        }
    }



    public void cancel(View v){
        this.finish();
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }




}