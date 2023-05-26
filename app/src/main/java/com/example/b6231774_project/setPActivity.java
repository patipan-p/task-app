package com.example.b6231774_project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class setPActivity extends AppCompatActivity {
    ImageView img;
    String colorString = "#FFFFFF";
    User us;
    TextView nametext;
    dbHandler db = new dbHandler(setPActivity.this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_pactivity);
        img = (ImageView) findViewById(R.id.profileimage);
        nametext = (TextView) findViewById(R.id.nametext);
        nametext.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });
        setAll();
        setTb();
    }


    private void setTb() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Edit Profile");
        setSupportActionBar(toolbar);
    }

    private void setAll() {
        Intent it = getIntent();
        us = (User) it.getExtras().getSerializable("UserData");
        colorString = us.getColor();
        int cs = Color.parseColor(us.getColor());
        Drawable dr = getDrawable(R.drawable.round);
        dr.setColorFilter(cs, PorterDuff.Mode.SRC_ATOP);
        img.setBackground(dr);
        nametext.setText(us.getName());
    }

    public void Changecolor(View v){
        String color = "Color";
        ImageButton ibtn;
        switch (v.getId()){
            case R.id.Color1:
                ibtn = (ImageButton) findViewById(R.id.Color1);
                break;
            case R.id.Color2:
                ibtn = (ImageButton) findViewById(R.id.Color2);
                break;
            case R.id.Color3:
                ibtn = (ImageButton) findViewById(R.id.Color3);
                break;
            case R.id.Color4:
                ibtn = (ImageButton) findViewById(R.id.Color4);
                break;
            case R.id.Color5:
                ibtn = (ImageButton) findViewById(R.id.Color5);
                break;
            case R.id.Color6:
                ibtn = (ImageButton) findViewById(R.id.Color6);
                break;
            case R.id.Color7:
                ibtn = (ImageButton) findViewById(R.id.Color7);
                break;
            case R.id.Color8:
                ibtn = (ImageButton) findViewById(R.id.Color8);
                break;
            default:
                ibtn = (ImageButton) findViewById(R.id.Color1);
        }
        colorString = ibtn.getTag().toString();
        int cs = Color.parseColor(colorString);
        Drawable dr = getDrawable(R.drawable.round);
        dr.setColorFilter(cs, PorterDuff.Mode.SRC_ATOP);
        img.setBackground(dr);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu, this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_taskedit, menu);
        return true;
    }

    public void saveProfile(View v){
        int meter = 0;
        if(nametext.equals("")){
            nametext.setHint("this field must not be empty.");
            meter += 1;
        }
        if (meter != 0)
            return;

        int res = 0;
        res = db.saveProfile(nametext.getText().toString(), colorString, us.getId());
        if (res == 2) {
            Toast t = Toast.makeText(setPActivity.this, "Somethings went wrong", Toast.LENGTH_SHORT);
            t.show();
            return;
        }
        else if (res == 1) {
            Intent i = new Intent(setPActivity.this,Profileactivity.class);
            i.putExtra("Toast", "Profile saved");
            startActivity(i);
        }
    }

    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if(id == R.id.del){
            new AlertDialog.Builder(this)
                    .setMessage("Do you want to delete this profile?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            int res = 0;
                            res = db.delprofile(us.getId());
                            Toast t;
                            if (res == 2) {
                                t = Toast.makeText(setPActivity.this, "Somethings went wrong", Toast.LENGTH_SHORT);
                                t.show();
                                return;
                            }
                            if (res == 5) {
                                t = Toast.makeText(setPActivity.this, "Cant delete default profile", Toast.LENGTH_SHORT);
                                t.show();
                                return;
                            }
                            else if (res == 1) {
                                Intent i3= new Intent(setPActivity.this,Profileactivity.class);
                                i3.putExtra("Toast", "Profile deleted");
                                startActivity(i3);
                            }
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
        }
        return true;
    }
    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
    public void cancel(View v){
        this.finish();
    }
}