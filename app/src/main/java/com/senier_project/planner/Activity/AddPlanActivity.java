package com.senier_project.planner.Activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.senier_project.planner.DB.DBEntry;
import com.senier_project.planner.DB.DBManager;
import com.senier_project.planner.R;

public class AddPlanActivity extends AppCompatActivity {

    private Button btnAdd;

    double x;
    double y;

    int s_year;
    int s_month;
    int s_day;

    long id;

    EditText txtTitle;
    EditText dp;
    EditText tp;
    EditText placename;

    TextView title;
    TextView txtLocation;

    SQLiteDatabase db;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_plan);

        title  = (TextView)findViewById(R.id.title);
        title.setText("add");




        //메인 캘린더 화면에서 날짜값을 가져온다.
        Intent intent = getIntent();
        String datestr = intent.getExtras().getString("datestr");


        dp = (EditText)findViewById(R.id.datepickerdialog);
        dp.setText(datestr);


        //날짜선택창
        DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                String date_selected = String.valueOf(year)+ "-"+String.valueOf(monthOfYear)+"-"+String.valueOf(dayOfMonth);
                dp.setText(date_selected);

            }
        };
        dp.setOnClickListener(view -> {
            DatePickerDialog dialog = new DatePickerDialog(this, listener, s_year, s_month, s_day);
            dialog.show();

        });


        //시간선택창
        tp = (EditText)findViewById(R.id.timepickerdialog);
        TimePickerDialog.OnTimeSetListener listener_time = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String time_selected = String.valueOf(hourOfDay)+ " : "+String.valueOf(minute);
                tp.setText(time_selected);
            }

        };
        tp.setOnClickListener(view -> {
            TimePickerDialog dialog = new TimePickerDialog(this,android.R.style.Theme_Holo_Light_Dialog_NoActionBar,listener_time, 8 , 00 , true);
            dialog.show();

        });


        txtTitle = findViewById(R.id.txtTitle);
        dp = findViewById(R.id.datepickerdialog);
        placename = findViewById(R.id.placename);


        //MapActivity에서 검색된 장소명을 가져온다.
        Intent output = getIntent();
        String receiveLoc = output.getExtras().getString("location");
        placename = (EditText)findViewById(R.id.placename);
        placename.setText(receiveLoc);


        DBManager dbManager = new DBManager(getApplicationContext());
        db = dbManager.getWritableDatabase();


        //MainActivity에서 일정목록 클릭시 보낸 id값을 가져온다.
        Intent intent1 = getIntent();
        if(intent1 != null && intent1.hasExtra("id")) {
            id = intent1.getLongExtra("id",1);
            getTask(id);

            title.setText("Update");
        }


        btnAdd = findViewById(R.id.btnAdd);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(txtTitle.getText().length() == 0) {
                    return;
                }

                if (title.getText().equals("add")) {
                    String title = txtTitle.getText().toString();
                    String date = dp.getText().toString();
                    String time = tp.getText().toString();
                    String place = placename.getText().toString();
                    addNewTask(title,date,time,place);

                } else {
                    updateTask(id);
                    Toast.makeText(AddPlanActivity.this,"수정되었습니다.",Toast.LENGTH_SHORT).show();
                }

                finish();
            }
        });

        placename = findViewById(R.id.placename);
        placename.setOnClickListener(view -> {
            Intent activityMap = new Intent(getApplicationContext(), MapActivity.class);
            startActivityForResult(activityMap, 1);
        });


        Button btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(view -> {
            finish();
        });



    }


    private void getTask(long id){

        Cursor cursor = db.query(DBEntry.ToDoDBEntry.TABLE_NAME,
                null,
                DBEntry.ToDoDBEntry._ID + "=?",
                new String[]{String.valueOf(id)},
                null,
                null,
                null,
                null);
        cursor.moveToFirst();

        String title = cursor.getString(cursor.getColumnIndexOrThrow(DBEntry.ToDoDBEntry.TITLE));
        String time = cursor.getString(cursor.getColumnIndexOrThrow(DBEntry.ToDoDBEntry.TIME));
        String date = cursor.getString(cursor.getColumnIndexOrThrow(DBEntry.ToDoDBEntry.DATE));
        String place = cursor.getString(cursor.getColumnIndexOrThrow(DBEntry.ToDoDBEntry.LOCATION));

        txtTitle.setText(title);
        dp.setText(date);
        tp.setText(time);
        placename.setText(place);

    }

    private void updateTask(long id){
        String title = txtTitle.getText().toString();
        String date = dp.getText().toString();
        String time = tp.getText().toString();
        String place = placename.getText().toString();

        ContentValues values = new ContentValues();
        values.put(DBEntry.ToDoDBEntry.TITLE, title);
        values.put(DBEntry.ToDoDBEntry.DATE, date);
        values.put(DBEntry.ToDoDBEntry.TIME, time);
        values.put(DBEntry.ToDoDBEntry.LOCATION, place);
        values.put(DBEntry.ToDoDBEntry.GEO_X, String.valueOf(x));
        values.put(DBEntry.ToDoDBEntry.GEO_Y, String.valueOf(y));

        db.update(DBEntry.ToDoDBEntry.TABLE_NAME,
                values,
                DBEntry.ToDoDBEntry._ID + "=?",
                new String[]{String.valueOf(id)});
    }


    private void addNewTask(String title,String date, String time, String place) {

        ContentValues values = new ContentValues();
        values.put(DBEntry.ToDoDBEntry.TITLE, title);
        values.put(DBEntry.ToDoDBEntry.DATE, date);
        values.put(DBEntry.ToDoDBEntry.TIME, time);
        values.put(DBEntry.ToDoDBEntry.LOCATION, place);
        values.put(DBEntry.ToDoDBEntry.GEO_X, String.valueOf(x));
        values.put(DBEntry.ToDoDBEntry.GEO_Y, String.valueOf(y));


        db.insert(DBEntry.ToDoDBEntry.TABLE_NAME,null,values);

        Toast.makeText(this,"일정이 등록되었습니다", Toast.LENGTH_SHORT).show();

    }





    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        placename.setText(data.getStringExtra("location"));
        x = data.getDoubleExtra("x", 127.00);
        y = data.getDoubleExtra("y", 37.56);
    }



}

