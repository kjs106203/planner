package com.senier_project.planner.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBManager extends SQLiteOpenHelper {

    public static final String DB_NAME = "plafic.db";
    public static final int DB_VER = 1;

    public DBManager(Context context) {
        super(context, DB_NAME, null, DB_VER);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_TABLE = "CREATE TABLE " +
                DBEntry.ToDoDBEntry.TABLE_NAME + " (" +
                DBEntry.ToDoDBEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DBEntry.ToDoDBEntry.TITLE + " TEXT NOT NULL, " +
                DBEntry.ToDoDBEntry.DATE + " TEXT, " +
                DBEntry.ToDoDBEntry.TIME + " TEXT, " +
                DBEntry.ToDoDBEntry.LOCATION + " TEXT, " +
                DBEntry.ToDoDBEntry.GEO_X + " TEXT, " +
                DBEntry.ToDoDBEntry.GEO_Y + " TEXT"+
                ");";
        db.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + DBEntry.ToDoDBEntry.TABLE_NAME);
        onCreate(db);

    }
}
