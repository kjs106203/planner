package com.senier_project.planner.Activity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;


import com.senier_project.planner.DB.DBManager;
import com.senier_project.planner.R;
import com.senier_project.planner.adapter.TextPageAdapter;
import com.senier_project.planner.adapter.ToDoAdapter_Route;
import com.senier_project.planner.fragment.Page1;
import com.senier_project.planner.fragment.Page2;

import java.util.Calendar;


public class MainActivity extends AppCompatActivity {

    //현재 날짜 가져옴
    Calendar calendar = Calendar.getInstance();
    int s_year = calendar.get(Calendar.YEAR);
    int s_month = calendar.get(Calendar.MONTH) + 1;
    int s_day = calendar.get(Calendar.DAY_OF_MONTH);

    public double longitude;
    public double latitude;

    ToDoAdapter_Route adapter;
    TextView DateNow;
    ImageView Calendar_IMG;

    DBManager dbManager;
    SQLiteDatabase db;

    RecyclerView recyclerView;

    NotificationCompat.Builder notiBuilder;
    NotificationManagerCompat notiManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //현재 날짜 화면에 표시
        DateNow = (TextView) findViewById(R.id.date);
        DateNow.setText(s_year + "\n" + s_month + "월" + s_day + "일");

        //달력아이콘 클릭시 달력화면으로 이동
        Calendar_IMG = (ImageView) findViewById(R.id.calendar_IMG);
        Calendar_IMG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CalendarActivity.class);
                startActivity(intent);

            }
        });




        //뷰페이져
        ViewPager pager = findViewById(R.id.viewpager);
        pager.setOffscreenPageLimit(3);

        TextPageAdapter adapter = new TextPageAdapter(getSupportFragmentManager());

        //페이지 추가
        Page1 page1 = new Page1();
        adapter.addItem(page1);

        Page2 page2 = new Page2();
        adapter.addItem(page2);

        pager.setAdapter(adapter);


    }



}



