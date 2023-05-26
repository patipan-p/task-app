package com.example.b6231774_project;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class TaskActivity extends AppCompatActivity {
    public static String Preference = "USPREF";
    SharedPreferences sharedPref;
    Task task;
    ArrayAdapter<Task> aa;
    dbHandler db =  new dbHandler(TaskActivity.this);
    int its = 0;
    ArrayList<Task> arr = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        checktoask();
        setTask();
        setTb();

    }

    public void onBackPressed(){
        startActivity(new Intent(TaskActivity.this, MainActivity.class));
    }

    private void checktoask() {
        Intent i = getIntent();
        if (i.hasExtra("Toast"))
            Toast.makeText(this, i.getExtras().getString("Toast"), Toast.LENGTH_SHORT).show();
    }

    private void setTask() {
        sharedPref = getSharedPreferences(Preference, Context.MODE_PRIVATE);
        ListView li = (ListView) findViewById(R.id.listview);
        arr = db.Loadtask(sharedPref.getInt("UserID", 0), its);
        int layoutID = android.R.layout.simple_list_item_1;
        aa = new ArrayAdapter<Task>(this, layoutID , arr);
        li.setAdapter(aa);

        li.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long rowId) {
                Intent i = new Intent(TaskActivity.this, PopupActivity.class);
                i.putExtra("TaskD", arr.get(position));
                startActivity(i);
            }
        });

    }

    public void newTask(View view){
            Intent i = new Intent(this, AddTaskActivity.class);
            startActivity(i);
    }

    private void setTb() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Task");
        setSupportActionBar(toolbar);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu, this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_task, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if(id == R.id.hs){
            Intent i = new Intent(TaskActivity.this, HistoryTask.class);
            startActivity(i);
        }
        return true;
    }

    public void openfilter(View view){
        final int[] index = {0};
        String[] fills = new String[]{"Default", "Upcoming", "Late"};
        new AlertDialog.Builder(TaskActivity.this)
                .setTitle("Filter task")
                .setCancelable(true)
                .setSingleChoiceItems(fills, its, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        its = i;
                    }
                }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            setTask();
                    }
                })
                .show();
    }

}