package com.example.b6231774_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class PopupActivity extends AppCompatActivity {
    Task task;
    dbHandler db = new dbHandler(PopupActivity.this);
    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*.9),(int) (height*.6));

        WindowManager.LayoutParams param = getWindow().getAttributes();
        param.gravity = Gravity.CENTER;
        param.x = 0;
        param.y = 0;

        getWindow().setAttributes(param);

        setDetails();
    }



    private void setDetails() {

        Intent intent = getIntent();
        task = (Task) intent.getExtras().getSerializable("TaskD");

        TextView title = (TextView) findViewById(R.id.displaytitle);
        TextView desc = (TextView) findViewById(R.id.desdis);
        TextView date = (TextView) findViewById(R.id.ddate);
        TextView rem = (TextView) findViewById(R.id.drem);

        Button bt = (Button) findViewById(R.id.cpbtn);
        if (task.getStatus().equals("Complete"))
            bt.setEnabled(false);
        title.setText(task.getSubject());
        desc.setText(task.getDescription());
        date.setText(task.getDate2());
        rem.setText(task.getStatus() + "(" + task.getremaintime() + ".)");

    }

    public void completeTask(View view){
        int result = 0;

        result = db.setTomcomplete(task.getId());
        Toast t;
        if (result == 2) {
            t = Toast.makeText(PopupActivity.this, "Somethings went wrong", Toast.LENGTH_SHORT);
            t.show();
            return;
        }
        else if (result == 1) {
            Intent i = new Intent(PopupActivity.this,TaskActivity.class);
            i.putExtra("Toast", "Task Complete");
            startActivity(i);
        }
    }

    public void ToEdit(View view){
        Intent i = new Intent(PopupActivity.this,EditTask.class);
        i.putExtra("Task", task);
        startActivity(i);
    }
}