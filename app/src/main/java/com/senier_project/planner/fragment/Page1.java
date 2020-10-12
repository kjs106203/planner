package com.senier_project.planner.fragment;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.senier_project.planner.RouteActivity;
import com.senier_project.planner.DB.DBEntry;
import com.senier_project.planner.DB.DBManager;
import com.senier_project.planner.R;
import com.senier_project.planner.adapter.ListItemClickListener;
import com.senier_project.planner.adapter.ToDoAdapter_Route;

import java.util.Calendar;
import java.util.Vector;

public class Page1 extends Fragment implements ListItemClickListener {

    Vector<String> vectorPlan;

    Calendar calendar = Calendar.getInstance();
    int s_year = calendar.get(Calendar.YEAR);
    int s_month = calendar.get(Calendar.MONTH) + 1;
    int s_day = calendar.get(Calendar.DAY_OF_MONTH);

    DBManager dbManager;
    SQLiteDatabase db;
    RecyclerView recyclerView;
    ToDoAdapter_Route adapter;



    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.page1, container, false);


        Context context = rootView.getContext();
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView_task);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        Cursor cursor = getAllTasks();


        adapter = new ToDoAdapter_Route(getContext(),cursor,this);
        recyclerView.setAdapter(adapter);

        return rootView;

    }




    public Cursor getAllTasks(){

        dbManager = new DBManager(getActivity());
        db = dbManager.getReadableDatabase();

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


    private String constructDate(int year, int month, int day) {
        StringBuilder builder = new StringBuilder();
        builder.append(year);
        builder.append("-");
        builder.append(month);
        builder.append("-");
        builder.append(day);

        return builder.toString();
    }


        @Override
    public void onClick(View view, int postion) {

            Intent intent = new Intent(getActivity(), RouteActivity.class);

            String[] whereVal = {constructDate(s_year,s_month,s_day)};
            Cursor cursor = (Cursor) dbManager.getReadableDatabase().query(DBEntry.ToDoDBEntry.TABLE_NAME,
                                                                            null,
                                                                            "date=?",
                                                                             whereVal,
                                                                            null,
                                                                            null,
                                                                            null);

            cursor.moveToPosition(postion);

            String X = cursor.getString(cursor.getColumnIndexOrThrow(DBEntry.ToDoDBEntry.GEO_X));
            String Y = cursor.getString(cursor.getColumnIndexOrThrow(DBEntry.ToDoDBEntry.GEO_Y));

            intent.putExtra("X",X);
            intent.putExtra("Y",Y);

            startActivity(intent);
    }
}