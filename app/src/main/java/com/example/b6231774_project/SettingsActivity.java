package com.example.b6231774_project;

import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceFragmentCompat;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;

public class SettingsActivity extends AppCompatActivity {
    dbHandler db = new dbHandler(SettingsActivity.this);
    User ar;
    SharedPreferences sharedPref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        setTb();
        setUser();
    }

    private void setUser() {
        sharedPref = getSharedPreferences("USPREF", Context.MODE_PRIVATE);
        int id = 0;
        id = sharedPref.getInt("UserID", 0);
        ar = db.get_user(ar, id);

        String css = ar.getColor();
        int cs = Color.parseColor(css);

        TextView tname = (TextView) findViewById(R.id.textname);
        tname.setText(ar.getName());
        ImageView im = (ImageView) findViewById(R.id.ims);
        Drawable dr = getDrawable(R.drawable.round);
        dr.setColorFilter(cs, PorterDuff.Mode.SRC_ATOP);
        im.setBackground(dr);
    }

    private void setTb() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Settings");
        setSupportActionBar(toolbar);
    }

    public void resetApp(View view){
        new AlertDialog.Builder(SettingsActivity.this)
                .setTitle("Do you want to reset everything?")
                .setMessage("Note that the app will be close after reset.")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        try {
                            // clearing app data
                            if (Build.VERSION_CODES.KITKAT <= Build.VERSION.SDK_INT) {
                                ((ActivityManager)getSystemService(ACTIVITY_SERVICE)).clearApplicationUserData(); // note: it has a return value!
                            } else {
                                String packageName = getApplicationContext().getPackageName();
                                Runtime runtime = Runtime.getRuntime();
                                runtime.exec("pm clear "+packageName);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    public void abtme(View view){
        new AlertDialog.Builder(SettingsActivity.this)
                .setTitle("About me")
                .setMessage("B6231774 Patipan Promrak\nSuranaree university of Technology\n\nResource uses: \nCompact calendar : https://github.com/SundeepK/CompactCalendarView")
                .setNegativeButton("OK",null)
                .show();
    }

    public void toprf(View viwe){
        startActivity(new Intent(SettingsActivity.this,Profileactivity.class));
    }

}