package com.example.b6231774_project;


import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class dbHandler extends SQLiteOpenHelper {

    // creating a constant variables for our database.
    // below variable is for our database name.
    private static final String DB_NAME = "remember";
    String now;
    SimpleDateFormat exf = new SimpleDateFormat("dd-MMMM-yyyy HH:mm:ss", Locale.ENGLISH);
    // below int is our database version
    private static final int DB_VERSION = 1;

    // FOR TABLE USERPROFILE
    private static final String TABLE_USER = "UserProfile";
    private static final String USER_ID_COL = "User_id";
    private static final String USER_NAME_COL = "Name";
    private static final String USER_PHOTO_COL = "Photo";
    private static final String USER_COLOR_COL = "Color";
    private static final String USER_DEF_COL = "Definition";

    // FOR TABLE TASK
    private static final String TABLE_TASK = "Task";
    private static final String TASK_ID_COL = "Task_id";
    private static final String TASK_SUBJECT_COL = "Subject";
    private static final String TASK_DESC_COL = "Description";
    private static final String TASK_DATETIME_COL = "DateTime";
    private static final String TASK_STATUS_COL = "STATUS";
    private static final String TASK_USER_ID = "User_id";


    // creating a constructor for our database handler.
    public dbHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    // below method is for creating a database by running a sqlite query
    @Override
    public void onCreate(SQLiteDatabase db) {
        // on below line we are creating
        // an sqlite query and we are
        // setting our column names
        // along with their data types.
        String query = "CREATE TABLE " + TABLE_USER + " ("
                + USER_ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + USER_NAME_COL + " TEXT,"
                + USER_PHOTO_COL + " TEXT,"
                + USER_COLOR_COL + " TEXT, "
                + USER_DEF_COL + " TEXT)";

        String query_task = "CREATE TABLE " + TABLE_TASK + " ("
                + TASK_ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + TASK_SUBJECT_COL + " TEXT,"
                + TASK_DESC_COL + " TEXT,"
                + TASK_DATETIME_COL + " DATETIME, "
                + TASK_STATUS_COL + " TEXT, "
                + TASK_USER_ID + " INTEGER)";
        db.execSQL(query);
        db.execSQL(query_task);
        //SQLiteDatabase db2 = this.getWritableDatabase();
        Date d = Calendar.getInstance().getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMMM-yyyy HH:mm:ss", Locale.ENGLISH);
        db.execSQL("INSERT INTO " + TABLE_USER + " VALUES (null,'MainProfile','M','#FC88AB','Default')");
        db.execSQL("INSERT INTO " + TABLE_USER + " VALUES (null,'Profile2','M','#FC88AB','')");
        db.execSQL("INSERT INTO " + TABLE_TASK + " VALUES (null,'Maintask','M','"+ formatter.format(d) +"','Not complete','1')");
        db.execSQL("INSERT INTO " + TABLE_TASK + " VALUES (null,'Maintask3','M','"+ formatter.format(d) +"','Complete','1')");
    }



    public User get_user(User ar, int id){
        SQLiteDatabase db = this.getReadableDatabase();
        String query;

        if (id > 0)
            query = "Select * from " + TABLE_USER + " Where " + USER_ID_COL + " = '" + id + "'";
        else
            query = "Select * from " + TABLE_USER + " Where " + USER_DEF_COL + " = 'Default'";

        Cursor cs = db.rawQuery(query, null);

        if (cs != null)
            if (cs.moveToFirst())
                ar = new User(cs.getInt(0), cs.getString(1), cs.getString(2), cs.getString(3), cs.getString(4));

        return ar;
    }

    // this method is use to add new course to our sqlite database.
    public void addNewCourse(String time, String bmr) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // this method is called to check if the table exists already.
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASK);
        onCreate(db);
    }



    public ArrayList<Task> loadNewesttask(Integer _id) {
        ArrayList<Task> list = new ArrayList<Task>();
        Task t = null;
        Calendar c = Calendar.getInstance();
        now = exf.format(c.getTime());
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "Select * from " + TABLE_TASK + " Where " + TASK_USER_ID + " = " + _id  + " AND Status != 'Complete' Order by " + TASK_DATETIME_COL + "";
        String query2 = "Select * from Task Where User_id = " + _id + "  AND Status != 'Complete' Order by DateTime";
        Cursor cs = db.rawQuery(query2, null);

        while(cs.moveToNext()){
            t = new Task(cs.getInt(0), cs.getString(1), cs.getString(2), cs.getString(3), cs.getString(4), cs.getInt(5));
            list.add(t);
        }
        return list;
    }

    public ArrayList<Task> Loadtask(int userID, int _id) {
        ArrayList<Task> list = new ArrayList<Task>();
        Task t = null;
        Calendar c = Calendar.getInstance();
        now = exf.format(c.getTime());
        SQLiteDatabase db = this.getReadableDatabase();
        String query = null;
        if (_id == 0)
            query = "Select * from Task Where User_id = " + userID + " AND  Status != 'Complete'  Order by DateTime ASC";

        if (_id == 1)
            query = "Select * from Task Where User_id = " + userID + " AND Status != 'Complete' AND DateTime >= '"+ now +"'   Order by DateTime";

        if (_id == 2)
            query = "Select * from Task Where User_id = " + userID + "  AND Status != 'Complete' AND DateTime <= '"+ now +"'  Order by DateTime DESC";

        Cursor cs = db.rawQuery(query, null);

        while(cs.moveToNext()){
            t = new Task(cs.getInt(0), cs.getString(1), cs.getString(2), cs.getString(3), cs.getString(4), cs.getInt(5));
            list.add(t);
        }
        return list;
    }

    public ArrayList<Task> LoadHStask(Integer _id) {
        ArrayList<Task> list = new ArrayList<Task>();
        Task t = null;

        SQLiteDatabase db = this.getReadableDatabase();
        String query = "Select * from Task Where User_id = " + _id + " AND Status == 'Complete'  Order by DateTime";
        Cursor cs = db.rawQuery(query, null);

        while(cs.moveToNext()){
            t = new Task(cs.getInt(0), cs.getString(1), cs.getString(2), cs.getString(3), cs.getString(4), cs.getInt(5));
            list.add(t);
        }
        return list;
    }



    public int insertTask(String stext, String dtext, String dttext, int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMMM-yyyy HH:mm:ss", Locale.ENGLISH);
        Date date = null;
        try {
            date = formatter.parse(dttext);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (date == null)
            return 2;
        db.execSQL("INSERT INTO " + TABLE_TASK + " VALUES (null,'" + stext + "','" + dtext + "', '" + formatter.format(date) + "', 'Not Complete', '" + id + "')");
        return 1;
    }

    public int setTomcomplete(Integer id) {

        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("UPDATE " + TABLE_TASK + " SET STATUS = 'Complete' WHERE Task_id = '" + id + "'");

        try {
            db.execSQL("UPDATE " + TABLE_TASK + " SET STATUS = 'Complete' WHERE Task_id = '" + id + "'");
        } catch (SQLException e) {
            return 2;
        }
        return 1;
    }

    public int editTask(String stext, String dtext, String dttext, int id) {

        SQLiteDatabase db = this.getWritableDatabase();

        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMMM-yyyy HH:mm:ss", Locale.ENGLISH);
        Date date = null;
        try {
            date = formatter.parse(dttext);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (date == null)
            return 2;
        db.execSQL("UPDATE  " + TABLE_TASK + " SET Subject = '" + stext + "' ,Description = '" + dtext + "' , DateTime = '" + dttext + "' WHERE Task_id = '" + id + "'");
        return 1;
    }

    public int deltask(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.execSQL("Delete From " + TABLE_TASK + " WHERE Task_id = '" + id + "'");
        } catch (SQLException e) {
            return 2;
        }
        return 1;
    }

    public ArrayList<User> get_userlist() {
        User us;
        ArrayList<User> users = new ArrayList<User>();
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            Cursor cs = db.rawQuery("Select * from " + TABLE_USER, null);
            while (cs.moveToNext())
            {
                us = new User(cs.getInt(0), cs.getString(1), cs.getString(2), cs.getString(3), cs.getString(4));
                users.add(us);
            }
        } catch (SQLException e) {
            return null;
        }
        return users;
    }

    public int delprofile(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        SQLiteDatabase db2 = this.getReadableDatabase();
        Cursor cs = db2.rawQuery("SELECT * from " + TABLE_USER + " WHERE User_id = '" + id + "'", null);
        while (cs.moveToNext()){
            if (cs.getString(4).equals("Default"))
                return 5;
        }
        try {
            db.execSQL("Delete From " + TABLE_TASK + " WHERE User_id = '" + id + "'");
        } catch (SQLException e) {
            return 2;
        }
        try {
            db.execSQL("Delete From " + TABLE_USER + " WHERE User_id = '" + id + "'");
        } catch (SQLException e) {
            return 2;
        }
        return 1;
    }

    public int addProfile(String nametext, String colorString) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.execSQL("Insert INTO " + TABLE_USER + " VALUES (null,'" + nametext + "','','" + colorString + "','')");
        } catch (SQLException e) {
            return 2;
        }
        return 1;

    }

    public int saveProfile(String nametext, String colorString, Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.execSQL("UPDATE " + TABLE_USER + " set Name = '" + nametext + "', Color = '" + colorString + "' WHERE User_id = '" + id + "'");
        } catch (SQLException e) {
            return 2;
        }
        return 1;
    }

    public ArrayList<User> getListUser(Integer id) {
        ArrayList<User> ar = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        User us;
        Cursor cs = db.rawQuery("SELECT * from " + TABLE_USER + " WHERE User_id != '" + id + "'", null);

        while (cs.moveToNext()){
            us = new User(cs.getInt(0), cs.getString(1), cs.getString(2), cs.getString(3), cs.getString(4));
            ar.add(us);
        }
        return ar;
    }
}
