package com.example.b6231774_project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class Profileactivity extends AppCompatActivity {
    User ar;
    ArrayList<User> usersarr = new ArrayList<User>();
    private static CustomAdapter adapter;
    dbHandler db = new dbHandler(Profileactivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profileactivity);
        setTb();
        getProfile();
    }

    private void getProfile() {
        usersarr = db.get_userlist();
        ListView li = (ListView) findViewById(R.id.listview);
        int layoutID = android.R.layout.simple_list_item_1;
        Drawable dr = null;
        Context c = getApplicationContext();
        adapter = new CustomAdapter(c, 0 , usersarr);
        li.setAdapter(adapter);
        checktoask();
        li.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                User o = usersarr.get(i);
                Intent is = new Intent(Profileactivity.this, setPActivity.class);
                is.putExtra("UserData", o);
                startActivity(is);
            }
        });
    }

    public void onBackPressed(){
        startActivity(new Intent(Profileactivity.this, MainActivity.class));
    }

    private void setTb() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("My profile");
        setSupportActionBar(toolbar);
    }

    private void checktoask() {
        Intent i = getIntent();
        if (i.hasExtra("Toast"))
            Toast.makeText(this, i.getExtras().getString("Toast"), Toast.LENGTH_SHORT).show();
    }

    public void newP(View v){
        startActivity(new Intent(Profileactivity.this, newProfile.class));
    }


}