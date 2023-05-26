package com.example.b6231774_project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class newProfile extends AppCompatActivity {
    ImageView img;
    String colorString = "#FFFFFF";
    User us;
    TextView nametext;
    dbHandler db = new dbHandler(newProfile.this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_profile);
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
        setTb();
    }

    private void setTb() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("New Profile");
        setSupportActionBar(toolbar);
    }


    public void Changecolor(View v){
        String color = "Color";
        img = (ImageView) findViewById(R.id.profileimage);
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

    public void saveProfile(View v){
        int meter = 0;
        if(nametext.equals("")){
            nametext.setHint("this field must not be empty.");
            meter += 1;
        }
        if (meter != 0)
            return;

        int res = 0;
        res = db.addProfile(nametext.getText().toString(), colorString);
        if (res == 2) {
            Toast t = Toast.makeText(newProfile.this, "Somethings went wrong", Toast.LENGTH_SHORT);
            t.show();
            return;
        }
        else if (res == 1) {
            Intent i = new Intent(newProfile.this,Profileactivity.class);
            i.putExtra("Toast", "Profile added");
            startActivity(i);
        }
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
    public void cancel(View v){
        this.finish();
    }

}