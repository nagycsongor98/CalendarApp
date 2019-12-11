package com.nagycsongor.calendarapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

class HomeEventAdapter extends RecyclerView.Adapter<HomeEventAdapter.ViewHolder> {

    private ArrayList<String> events = new ArrayList<>();
    private Context context;

    public HomeEventAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.home_event_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }


    @Override
    public int getItemCount() {
        return events.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ConstraintLayout parent;
        private TextView eventName;
        private TextView location;
        private TextView date;
        private TextView description;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            parent = itemView.findViewById(R.id.homeParent);
            eventName = itemView.findViewById(R.id.homeEventName);
            location = itemView.findViewById(R.id.homeLocation);
            date = itemView.findViewById(R.id.homeDate);
            description = itemView.findViewById(R.id.homeDescription);

        }
    }
}