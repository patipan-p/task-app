package com.example.b6231774_project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class EditTask extends AppCompatActivity {

    CompactCalendarView cpCals;
    User ar;
    Task task;
    dbHandler db = new dbHandler(EditTask.this);
    String dttext;
    EditText datetime = null;
    EditText subject = null;
    EditText desc = null;
    SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);
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
        setTaxt();
    }

    private void setTaxt() {
        Intent i = getIntent();
        if (!i.hasExtra("Task"))
            return;
        task = (Task) i.getExtras().getSerializable("Task");
        subject.setText(task.getSubject());
        desc.setText(task.getDescription());
        datetime.setText(task.getDate2());
        dttext = task.getDate();
    }

    private void setTb() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Edit Task");
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

                new TimePickerDialog(EditTask.this, timeSetListener,calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),false).show();
            }
        };

        new DatePickerDialog(EditTask.this, dateSetListener,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();
    }
    public void cancel(View v){
        this.finish();
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
    public void SaveTask(View view){
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
        res = db.editTask(stext, dtext, dttext, id);
        Toast t;
        if (res == 2) {
            t = Toast.makeText(EditTask.this, "Somethings went wrong", Toast.LENGTH_SHORT);
            t.show();
            return;
        }
        else if (res == 1) {
            Intent i = new Intent(EditTask.this,TaskActivity.class);
            i.putExtra("Toast", "Task updated");
            startActivity(i);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu, this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_taskedit, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if(id == R.id.del){
            new AlertDialog.Builder(this)
                    .setMessage("Do you want to delete this task?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            int res = 0;
                            res = db.deltask(task.getId());
                            Toast t;
                            if (res == 2) {
                                t = Toast.makeText(EditTask.this, "Somethings went wrong", Toast.LENGTH_SHORT);
                                t.show();
                                return;
                            }
                            else if (res == 1) {
                                Intent i3= new Intent(EditTask.this,TaskActivity.class);
                                i3.putExtra("Toast", "Task deleted");
                                startActivity(i3);
                            }
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
        }
        return true;
    }


}