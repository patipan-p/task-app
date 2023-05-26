package com.example.b6231774_project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class HistoryTask extends AppCompatActivity {
    public static String Preference = "USPREF";
    SharedPreferences sharedPref;
    Task task;
    ArrayAdapter<Task> aa;
    dbHandler db =  new dbHandler(HistoryTask.this);
    ArrayList<Task> arr = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_task);

        setTask();
        setTb();
    }

    public void onBackPressed(){
        startActivity(new Intent(HistoryTask.this, TaskActivity.class));
    }

    private void setTb() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Task History");
        setSupportActionBar(toolbar);
    }

    private void setTask() {
        sharedPref = getSharedPreferences(Preference, Context.MODE_PRIVATE);
        ListView li = (ListView) findViewById(R.id.listview);
        arr = db.LoadHStask(sharedPref.getInt("UserID", 0));
        int layoutID = android.R.layout.simple_list_item_1;
        aa = new ArrayAdapter<Task>(this, layoutID , arr);
        li.setAdapter(aa);

        li.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long rowId) {
                Intent i = new Intent(HistoryTask.this, PopupActivity.class);
                i.putExtra("TaskD", arr.get(position));
                startActivity(i);
            }
        });

    }
}