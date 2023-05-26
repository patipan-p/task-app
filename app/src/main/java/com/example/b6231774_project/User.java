package com.example.b6231774_project;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import java.io.Serializable;

public class User implements Serializable {
    SharedPreferences sharedPref;
    private Integer id;
    private String Name;
    private String Photo;
    private String sColor;
    private String Def;

    public Integer getId() { return id; }
    public String getName(){return Name;}
    public String getPhoto(){return Photo;}
    public String getColor(){return sColor;}
    public String getDef(){return Def;}

    Context context;
    public User(int _id, String _name, String _photo, String _color, String _def){
        this.id = _id;
        this.Name = _name;
        this.sColor = _color;
        this.Photo = _photo;
        this.Def = _def;
    }

    public String getN() {

        //SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy, HH.mm", Locale.US);
        //String dateString = sdf.format(date);
        if (getDef().equals("Default"))
            return getName() + "(" + getDef() + ")";
        return getName();
    }


}
