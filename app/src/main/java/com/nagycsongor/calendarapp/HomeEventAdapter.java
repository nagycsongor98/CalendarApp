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

    private ArrayList<Event> events = new ArrayList<>();
    private Context context;

    public HomeEventAdapter(Context context,ArrayList<Event> events) {
        this.context = context;
        this.events = events;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.home_event_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Event event = events.get(position);
        holder.eventName.setText(event.getTitle());
        holder.description.setText(event.getDescription());
        holder.location.setText(event.getLocation());
//        holder.date.setText(event.getEventDate().toString());

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
