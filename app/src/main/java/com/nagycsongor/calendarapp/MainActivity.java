package com.nagycsongor.calendarapp;


import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private CalendarView calendarView;
    private List<EventDay> events = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        calendarView = findViewById(R.id.calendarView);


        Calendar calendar = Calendar.getInstance();
        events.add(new EventDay(calendar, R.drawable.sample_three_icons));

        recyclerView = findViewById(R.id.recyclerView);
        adapter = new HomeEventAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(adapter);



////or
//        events.add(new EventDay(calendar, new Drawable()));
////or if you want to specify event label color
//        events.add(new EventDay(calendar, R.drawable.sample_three_icons, Color.parseColor("#228B22")));
//
        calendarView.setEvents(events);

    }
}
