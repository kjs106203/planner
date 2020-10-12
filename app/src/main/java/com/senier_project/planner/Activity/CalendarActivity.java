package com.senier_project.planner.Activity;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.senier_project.planner.DB.DBEntry;
import com.senier_project.planner.DB.DBManager;
import com.senier_project.planner.R;
import com.senier_project.planner.adapter.*;
import com.senier_project.planner.adapter.ToDoAdapter;

import java.security.MessageDigest;
import java.util.Calendar;

public class CalendarActivity extends AppCompatActivity implements ListItemClickListener {


    ImageView back;
    RecyclerView recyclerView;
    FloatingActionButton addicon;

    DBManager dbManager;
    ToDoAdapter adapter;
    SQLiteDatabase db;


    int s_year = 0;
    int s_month = 0;
    int s_day = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("ACTIVITY", "MainActivity has created.");
        setContentView(R.layout.activity_cal);
        getHashKey();

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        });


        addicon = findViewById(R.id.btnAddPlan);
        addicon.setOnClickListener(new View.OnClickListener() {

            //add아이콘을 눌렀을 때 선택되어진 날짜값을 AddActivity로 보내준다.
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), AddPlanActivity.class);
                String str = s_year + "-" + s_month + "-" + s_day;
                intent.putExtra("datestr", str);

                startActivity(intent);

            }
        });



        Cursor cursor = getAllTasks();
        adapter = new ToDoAdapter(this, cursor, this);
        recyclerView.setAdapter(adapter);



        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                long id = (long) viewHolder.itemView.getTag();
                removeTask(id);

                adapter.swapCursor(getAllTasks());
            }
        }).attachToRecyclerView(recyclerView);



        CalendarView calendarView = findViewById(R.id.CalendarView);

        DBManager dbManager = new DBManager(this);
        db = dbManager.getWritableDatabase();

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public
            void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {

                // TODO Auto-generated method stub

                s_year = year;
                s_month = month + 1;
                s_day = dayOfMonth;

                String str = s_year + "-" + s_month + "-" + s_day;


                adapter.swapCursor(getAllTasks());
            }

        });



        Calendar calendar = Calendar.getInstance();
        s_year = calendar.get(Calendar.YEAR);
        s_month = calendar.get(Calendar.MONTH) + 1;
        s_day = calendar.get(Calendar.DAY_OF_MONTH);


    }


    private void removeTask(long id) {

        db.delete(DBEntry.ToDoDBEntry.TABLE_NAME,
                DBEntry.ToDoDBEntry._ID + "=" + id,
                null);

        Toast.makeText(this,"일정이 삭제되었습니다",Toast.LENGTH_SHORT).show();
    }


    public void onResume() {
        super.onResume();
        getAllTasks();
    }

    private Cursor getAllTasks(){

        DBManager dbManager = new DBManager(getApplicationContext());
        SQLiteDatabase db = dbManager.getReadableDatabase();

        String[] projection = {
                BaseColumns._ID,
                DBEntry.ToDoDBEntry.TITLE,
                DBEntry.ToDoDBEntry.DATE,
                DBEntry.ToDoDBEntry.TIME,
                DBEntry.ToDoDBEntry.LOCATION,
                DBEntry.ToDoDBEntry.GEO_X,
                DBEntry.ToDoDBEntry.GEO_Y
        };

        String whereClause = "date = ?";
        String[] whereVal = {constructDate(s_year,s_month,s_day)};

        return  db.query(
                DBEntry.ToDoDBEntry.TABLE_NAME,
                projection,
                whereClause,
                whereVal,
                null,
                null,
                null
        );


    }


    @Override
    protected void onStart(){
        super.onStart();
        adapter.swapCursor(getAllTasks());
    }


    //일정을 클릭했을 때 id값을 AddPlanActivity로 보내준다.
    @Override
    public void onClick(View view, int postion) {
        long id = (long) view.getTag();
        Intent intent = new Intent(CalendarActivity.this, AddPlanActivity.class);
        intent.putExtra("id",id);
        startActivity(intent);
    }

    private String constructDate(int year, int month, int day) {
        StringBuilder builder = new StringBuilder();
        builder.append(year);
        builder.append("-");
        builder.append(month);
        builder.append("-");
        builder.append(day);

        return builder.toString();
    }

    //해시키값 가져오기(api등록)
    private void getHashKey() {
        try{
            @SuppressLint("PackageManagerGetSignatures")
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String key = new String(Base64.encode(md.digest(), 0));
                Log.d("Hash key :",key);
            }
        } catch (Exception e){
            Log.e("name not found", e.toString());
        }
    }



    }




