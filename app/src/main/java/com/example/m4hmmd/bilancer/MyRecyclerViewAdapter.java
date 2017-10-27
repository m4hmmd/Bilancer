package com.example.m4hmmd.bilancer;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class MyRecyclerViewAdapter extends RecyclerView
        .Adapter<MyRecyclerViewAdapter
        .DataObjectHolder> {
    private ArrayList<Post> dashboardPosts;
    private static MyClickListener myClickListener;

    ///////// IT"S HERE!!
    public class DataObjectHolder extends RecyclerView.ViewHolder
            implements View
            .OnClickListener {
        TextView label;
        TextView description;
        TextView quota;
        TextView location;
        TextView dateTime;

        public DataObjectHolder(View itemView) {
            super(itemView);
            label = (TextView) itemView.findViewById(R.id.title);
            description = (TextView) itemView.findViewById(R.id.description);
            dateTime = (TextView) itemView.findViewById(R.id.datetime);
            quota = (TextView) itemView.findViewById(R.id.quota);
            location = (TextView) itemView.findViewById(R.id.location);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            myClickListener.onItemClick(getAdapterPosition(), v);
            Log.i("itemClicked", "true");











        }
    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public MyRecyclerViewAdapter(ArrayList<Post> myDataset) {
        dashboardPosts = myDataset;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view_row, parent, false);

        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
        holder.label.setText(dashboardPosts.get(position).getTitle());
        holder.description.setText(dashboardPosts.get(position).getDescription());
        holder.location.setText(dashboardPosts.get(position).getLocation());
        holder.quota.setText(Integer.toString(dashboardPosts.get(position).getParticipants().size())
                + "/" + Integer.toString(dashboardPosts.get(position).getQuota()));
        holder.dateTime.setText(dashboardPosts.get(position).getDate());
    }

    public void addItem(Post dataObj, int index) {
        dashboardPosts.add(index, dataObj);
        notifyItemInserted(index);
    }

    public void deleteItem(int index) {
        dashboardPosts.remove(index);
        notifyItemRemoved(index);
    }

    @Override
    public int getItemCount() {
        return dashboardPosts.size();
    }

    public interface MyClickListener {
        public void onItemClick(int position, View v);
    }
}