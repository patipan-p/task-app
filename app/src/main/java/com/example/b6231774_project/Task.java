package com.example.b6231774_project;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.RelativeSizeSpan;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;
import java.util.Locale;


public class Task  implements Serializable {
    private Integer id;
    private String Subject;
    private String Description;
    private String Dateti;
    private String Status;
    private Integer User;

    public Integer getId() { return id; }
    public String getSubject(){return Subject;}
    public String getDescription(){return Description;}
    public String getDate(){return Dateti;}
    public String getStatus(){return Status;}
    public Integer getUser() { return User; }

    public Task(int _id, String _subject, String _desc, String _date, String _status, Integer _user){
        this.id = _id;
        this.Subject = _subject;
        this.Description = _desc;
        this.Dateti = _date;
        this.Status = _status;
        this.User = _user;
    }

    public String getStatus2() {
        if (Status.equals("Complete"))
            return Status + "ed";
        return Status + "!";
    }

    @Override
    public String toString() {
        //SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy, HH.mm", Locale.US);
        //String dateString = sdf.format(date);
        SpannableString s = new SpannableString(getSubject());
        s.setSpan(new AbsoluteSizeSpan(5000),0,s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return s + "\nTime remaining: " + getremaintime();
    }

    public String getDate2(){
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMMM-yyyy HH:mm:ss", Locale.ENGLISH);
        Date date = null;
        try {
            date = formatter.parse(Dateti);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat s = new SimpleDateFormat("dd MMM yyyy h:mm a");
        return s.format(date);
    }

    public String getremaintime() {
        String remSt = null;

        Date currentTime = Calendar.getInstance().getTime();
        long different = 0;
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMMM-yyyy HH:mm:ss", Locale.ENGLISH);
        Date date = null;
        try {
            date = formatter.parse(Dateti);
            different = date.getTime() - currentTime.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        remSt = getDays(different);

        return remSt;
    }

    private String getDays(long different) {
        String finalstr ="";
        int max = 0;
        String A = null;
        String days = null,hrs = null,mins = null,secs = null;
        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        if (different < 0){
           A = "ago";
           different = different * -1;
        }
        else
            A = "In";


        long day = different / daysInMilli;
        different = different%daysInMilli;

        long hour = different / hoursInMilli;
        different = different%hoursInMilli;

        long min = different / minutesInMilli;
        different = different%minutesInMilli;

        long second = different / secondsInMilli;
        different = different%secondsInMilli;

        if (day != 0)
        {
            if (day > 1)
                finalstr += " " +  day +  " Days";
            else{
                finalstr += " " +  day +  " Day";
            }
            max = max + 1;
        }
        if (hour != 0)
        {
            if (max == 1)
                finalstr += " and";
            if (hour > 1)
                finalstr += " " +  hour +  " hours";
            else{
                finalstr += " " +  hour  +  " hour";
            }
            max = max + 1;
        }
        if (min != 0)
        {
            if (max == 2)
            {
                if (A.equals("In")) return A + finalstr;
                if (A.equals("ago")) return finalstr + " " + A;
            }
            else if (max == 1)
                finalstr += " and";
            if (min > 1)
                finalstr += " " +  min +  " mins";
            else{
                finalstr += " " +  min +  " min";
            }
            max = max + 1;
        }
        if (second >= 0)
        {
            if (max == 2)
            {
                if (A.equals("In")) return A + finalstr;
                if (A.equals("ago")) return finalstr + " " + A;
            }
            else if (max == 1)
                finalstr += " and";
            if (second > 1)
                finalstr += " " +  second +  " seconds";
            else{
                finalstr += " " +  second * -1 +  " second";
            }
            max = max + 1;
        }
            if (A.equals("In")) return A + finalstr;
            if (A.equals("ago")) return finalstr + " " + A;

        return finalstr;
    }


    public long getMilli() {
        long time = 0;
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMMM-yyyy HH:mm:ss", Locale.ENGLISH);
        Date date = null;
        try {
            date = formatter.parse(Dateti);
            time = date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;
    }
}
