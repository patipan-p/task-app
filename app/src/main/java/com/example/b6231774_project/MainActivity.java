package com.example.b6231774_project;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.b6231774_project.databinding.ActivityMainBinding;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.google.android.material.navigation.NavigationView;

import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {
    CompactCalendarView cpCals;
    dbHandler db = new dbHandler(MainActivity.this);
    User ar;
    Task task;
    ArrayList<Task> tasklist = new ArrayList<Task>();
    Toolbar toolbar = null;
    SharedPreferences sharedPref;
    String tasksub;
    String taskdescr;
    String taskrem;
    TextView tasks;
    TextView taskdesc;
    TextView taskremain;
    int cs;
    DrawerLayout drawer;
    public static String Preference = "USPREF";
    ArrayList<User> sg = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        cpCals = (CompactCalendarView) findViewById(R.id.calendarV);
        cpCals.setLocale(TimeZone.getTimeZone("en"),Locale.ENGLISH);
        cpCals.setUseThreeLetterAbbreviation(true);
        //Set date to textview
        SetAll();
        CheckSharepref();
        cpCals.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                List<Event> events = cpCals.getEvents(dateClicked);
                //Log.d(TAG, "Day was clicked: " + dateClicked + " with events " + events);
                tasks = (TextView) findViewById(R.id.subjectText);
                taskdesc = (TextView) findViewById(R.id.desctext);
                taskremain = (TextView) findViewById(R.id.remainingTime);
                for (int k = 0 ; k < tasklist.size(); k++){
                    if (!events.isEmpty() && tasklist.get(k).getId() == events.get(0).getData())
                    {
                        tasksub = tasklist.get(k).getSubject();
                        taskdescr = tasklist.get(k).getDescription();
                        taskrem = tasklist.get(k).getremaintime();
                        tasks.setText(tasksub);
                        taskdesc.setText(taskdescr);
                        taskremain.setText(taskrem);
                    }
                    //else
                    //{
                    //    tasksub = tasklist.get(0).getSubject();
                    //    taskdescr = tasklist.get(0).getDescription();
                    //    taskrem = tasklist.get(0).getremaintime();
                    //    tasks.setText(tasksub);
                    //    taskdesc.setText(taskdescr);
                    //    taskremain.setText(taskrem);
                    //}
                }
            }
            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                TextView t = (TextView) findViewById(R.id.month_title);
                t.setText(new SimpleDateFormat("MMMM YYYY").format(firstDayOfNewMonth));
            }
        });


    }


    public boolean onCreateOptionsMenu(Menu menu){
        return true;
    }


    public void SetAll(){
        TextView date;
        date = (TextView) findViewById(R.id.dateshow);
        date.setText("Today : " + getDateTime());
        TextView t = (TextView) findViewById(R.id.month_title);
        t.setText(new SimpleDateFormat("MMMM YYYY").format(cpCals.getFirstDayOfCurrentMonth()));
    }

    private void CheckSharepref() {
        sharedPref = getSharedPreferences(Preference, Context.MODE_PRIVATE);
        int id = 0;
        if (sharedPref.getAll().containsKey("UserID")){
            id = sharedPref.getInt("UserID", 0);
            ar = db.get_user(ar, id);
        }
        else
        {
            ar = db.get_user(ar, 0);
            sharedPref = getApplicationContext().getSharedPreferences(Preference, 0);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putInt("UserID", ar.getId());
            editor.commit();
        }
        SetTheme();
        setTasklist();
    }

    void SetTheme() {
        //get main color;
        String css = ar.getColor();
        cs = Color.parseColor(css);

        int black = Color.parseColor("#000000");
        //set toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("  "  + ar.getName());
        toolbar.setTitleTextColor(black);
        Drawable dr = getDrawable(R.drawable.round);
        dr.setColorFilter(cs, PorterDuff.Mode.SRC_ATOP);
        toolbar.setLogo(dr);
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(MainActivity.this,"H", Toast.LENGTH_SHORT).show();
                drawer.openDrawer(GravityCompat.START);
            }
        });
        setSupportActionBar(toolbar);
        NavigationView nv = (NavigationView) findViewById(R.id.nav_view);
        View header = nv.getHeaderView(0);
        TextView headtext = (TextView) header.findViewById(R.id.TitleHead);
        headtext.setText(ar.getName());
        ImageView imageView = (ImageView) header.findViewById(R.id.imageViewHead);
        imageView.setBackground(dr);
        Menu menu = nv.getMenu();
        sg = db.getListUser(ar.getId());
        for(int j = 0 ; j < sg.size(); j ++ ){
            menu.add(sg.get(j).getName());
        }
        Menu menu2 = nv.getMenu();
        for(int j = 0 ; j < sg.size(); j ++ ){
            int finalJ = j;
            MenuItem item = menu2.findItem(j).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    sharedPref = getApplicationContext().getSharedPreferences(Preference, 0);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putInt("UserID", sg.get(finalJ).getId());
                    editor.commit();
                    Intent intent2 = new Intent(MainActivity.this, MainActivity.class);
                    //intent2.putExtra("UserID2", sg.get(finalJ));
                    startActivity(intent2);
                    return true;
                }
            });
        }
        //set theme
    }




    public void onBackPressed(){
       new AlertDialog.Builder(MainActivity.this)
               .setMessage("Do you wan to exit the app?")
               .setCancelable(false)
               .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialogInterface, int i) {
                       android.os.Process.killProcess(android.os.Process.myPid());
                   }
               })
               .setNegativeButton("No", null)
               .show();
    }

    private void setTasklist() {

        tasklist = db.loadNewesttask(ar.getId());
        tasks = (TextView) findViewById(R.id.subjectText);
        taskdesc = (TextView) findViewById(R.id.desctext);
        taskremain = (TextView) findViewById(R.id.remainingTime);
        if (!tasklist.isEmpty())
        {
            tasksub = tasklist.get(0).getSubject();
            taskdescr = tasklist.get(0).getDescription();
            taskrem = tasklist.get(0).getStatus() + " " + tasklist.get(0).getremaintime();
            tasks.setText(tasksub);
            taskdesc.setText(taskdescr);
            taskremain.setText(taskrem);
        }

        for (int j = 0 ;  j < tasklist.size(); j++){
            Event ev = new Event(cs, tasklist.get(j).getMilli(), tasklist.get(j).getId());
            cpCals.addEvent(ev);
        }
    }


    private String getDateTime() {
        DateFormat df = new SimpleDateFormat("EEEE dd MMMM yyyy");
        return df.format(new Date());
    }


    public void newTask(View view){
        Intent i = new Intent(this, AddTaskActivity.class);
        startActivity(i);
    }

    public void ToTask(View view) {
        Intent intent = new Intent(this, TaskActivity.class);
        startActivity(intent);
    }

    public void ToSetting(View view) {
        Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
        startActivity(intent);
    }



}