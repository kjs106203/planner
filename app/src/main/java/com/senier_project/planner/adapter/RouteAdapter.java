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
import com.senier_project.planner.route_item;

import java.util.ArrayList;

public class RouteAdapter extends RecyclerView.Adapter<RouteAdapter.ViewHolder> {

    private ArrayList<String> mData = null ;

    // 생성자에서 데이터 리스트 객체를 전달받음.
    public RouteAdapter(ArrayList<String> list) {
        mData = list ;
    }


    @Override
    public RouteAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext() ;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;

        View view = inflater.inflate(R.layout.route_list, parent, false) ;
        RouteAdapter.ViewHolder vh = new RouteAdapter.ViewHolder(view) ;

        return vh ;
    }

    @Override
    public void onBindViewHolder(@NonNull RouteAdapter.ViewHolder holder, int position) {

        String item = mData.get(position) ;

        holder.total.setText(item);

    }



    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        TextView total;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            total= itemView.findViewById(R.id.duration);

        }
    }
}
