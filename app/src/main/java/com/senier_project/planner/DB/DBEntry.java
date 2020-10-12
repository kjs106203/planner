package com.senier_project.planner.DB;

import android.provider.BaseColumns;


public class DBEntry {

    public static final class ToDoDBEntry implements BaseColumns {

        public static final String TABLE_NAME = "plafic";
        public static final String TITLE = "title";
        public static final String DATE = "date";
        public static final String TIME = "time";
        public static final String LOCATION = "location";
        public static final String GEO_X = "geo_x";
        public static final String GEO_Y = "geo_y";


    }
}



