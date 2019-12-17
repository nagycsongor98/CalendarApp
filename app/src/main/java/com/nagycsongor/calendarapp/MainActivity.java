package com.nagycsongor.calendarapp;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private CalendarView calendarView;
    private List<EventDay> events = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private ArrayList<Event> myEvents = new ArrayList<>();
    private FloatingActionButton fab;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;
    private String userEmail;
    private String userKey;
    private Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fab = findViewById(R.id.floatingActionButton);
        calendarView = findViewById(R.id.calendarView);
        mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
//        mEditor = mPreferences.edit();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        userEmail = mPreferences.getString("email", "");
        userKey = mPreferences.getString("userKey", "");
        context = this;

        if (!userEmail.isEmpty()) {
            myRef.child("Users").child(userKey).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                    for (DataSnapshot dbEvent : dataSnapshot.child("events").getChildren()) {
                        Event event = dbEvent.getValue(Event.class);
                        myEvents.add(event);


                    }


                    recyclerView = findViewById(R.id.recyclerView);
                    adapter = new HomeEventAdapter(context, myEvents);
                    recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
                    recyclerView.setAdapter(adapter);


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), NewEventActivity.class));
            }
        });


        Calendar calendar = Calendar.getInstance();
        events.add(new EventDay(calendar, R.drawable.sample_three_icons));


////or
//        events.add(new EventDay(calendar, new Drawable()));
////or if you want to specify event label color
//        events.add(new EventDay(calendar, R.drawable.sample_three_icons, Color.parseColor("#228B22")));
//
        calendarView.setEvents(events);

    }
}
