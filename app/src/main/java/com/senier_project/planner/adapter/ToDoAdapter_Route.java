package com.senier_project.planner.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.senier_project.planner.DB.DBEntry;
import com.senier_project.planner.R;

import java.util.ArrayList;

public class ToDoAdapter_Route extends RecyclerView.Adapter<ToDoAdapter_Route.ToDoViewHolder> {


    private Context mContext;
    private Cursor cursor;
    private ListItemClickListener listItemClickListener;

    public ToDoAdapter_Route(Context mContext, Cursor cursor, ListItemClickListener listItemClickListener) {

        this.mContext = mContext;
        this.cursor = cursor;
        this.listItemClickListener = listItemClickListener;
    }

    @NonNull
    @Override
    public ToDoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item,parent,false);


        return new ToDoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ToDoViewHolder holder, int position) {

        if(!cursor.moveToPosition(position)) {
            return;
        }

        String textview1 = cursor.getString(cursor.getColumnIndexOrThrow(DBEntry.ToDoDBEntry.TITLE));
        String textview2 = cursor.getString(cursor.getColumnIndexOrThrow(DBEntry.ToDoDBEntry.TIME));

        long id = cursor.getLong(cursor.getColumnIndexOrThrow(DBEntry.ToDoDBEntry._ID));

        holder.itemView.setTag(id);

        holder.textView1.setText(textview1);
        holder.textView2.setText(textview2);

    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }

    public class ToDoViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener{

        TextView textView1, textView2;

        public ToDoViewHolder(@NonNull View itemView) {
            super(itemView);

            textView1 = itemView.findViewById(R.id.textview1);
            textView2 = itemView.findViewById(R.id.textview2);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            listItemClickListener.onClick(v, getAdapterPosition());
        }
    }

    public void swapCursor(Cursor newCursor) {
        if(cursor != null) {
            cursor.close();
        }

        cursor = newCursor;
        if(newCursor != null){
            this.notifyDataSetChanged();
        }
    }
}
