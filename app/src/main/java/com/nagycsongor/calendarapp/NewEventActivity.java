package com.nagycsongor.calendarapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class NewEventActivity extends AppCompatActivity {
    private static final String TAG = "NewEventActivity";
    private EditText titleEditText;
    private TextView dateTextView;
    private TextView reminderTextView;
    private Spinner reminderSpinner;
    private EditText inviteFriendsEditText;
    private EditText locationEditText;
    private EditText descriptionEditText;
    private Button saveButton;
    private CustomDateTimePicker custom;
    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;
    private FirebaseDatabase database;
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        titleEditText = findViewById(R.id.titleEditText);
        dateTextView = findViewById(R.id.dateTextView);
        reminderTextView = findViewById(R.id.reminderTextView);
        reminderSpinner = findViewById(R.id.reminderSpinner);
        inviteFriendsEditText = findViewById(R.id.inviteFriendsEditText);
        locationEditText = findViewById(R.id.locationEditText);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        saveButton = findViewById(R.id.saveButton);
        mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mEditor = mPreferences.edit();

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.reminder,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        reminderSpinner.setAdapter(adapter);
        reminderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                reminderTextView.setText(parent.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        custom = new CustomDateTimePicker(this,
                new CustomDateTimePicker.ICustomDateTimeListener() {

                    @Override
                    public void onSet(Dialog dialog, Calendar calendarSelected,
                                      Date dateSelected, int year, String monthFullName,
                                      String monthShortName, int monthNumber, int day,
                                      String weekDayFullName, String weekDayShortName,
                                      int hour24, int hour12, int min, int sec,
                                      String AM_PM) {
                        //                        ((TextInputEditText) findViewById(R.id.edtEventDateTime))
                        dateTextView.setText("");
                        dateTextView.setText(year
                                + "-" + (monthNumber + 1) + "-" + calendarSelected.get(Calendar.DAY_OF_MONTH)
                                + " " + hour24 + ":" + min
                                + ":" + sec);
                        Date date = new Date(year, (monthNumber+1), calendarSelected.get(Calendar.DAY_OF_MONTH), hour24, min, sec);
                        Log.d("X", date.toString());
                    }

                    @Override
                    public void onCancel() {

                    }
                });
        custom.set24HourFormat(true);
        custom.setDate(Calendar.getInstance());

        dateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                custom.showDialog();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveEvent();
            }
        });

    }

    private void saveEvent() {
        //title
        final String title = titleEditText.getText().toString();
        if (title.isEmpty())
        {
            titleEditText.setError("Title field is empty!");
            titleEditText.requestFocus();
            return;
        }
        Log.i(TAG,title);

        //date
        final Date eventDate;
        String dateString = dateTextView.getText().toString();
        if (dateString.isEmpty())
        {
            dateTextView.setError("Date field is empty!");
            dateTextView.requestFocus();
            return;
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            eventDate = format.parse(dateString);
            Log.i(TAG,eventDate.toString());
            //System.out.println(eventDate);

        } catch (ParseException e) {
            e.printStackTrace();
            dateTextView.setError("Date field is incorrect!");
            dateTextView.requestFocus();
            return;
        }

        //reminder
        final String reminder = reminderTextView.getText().toString();
        if (reminder.isEmpty())
        {
            reminderTextView.setError("Reminder field is empty!");
            reminderTextView.requestFocus();
            return;
        }
        Log.i(TAG,reminder);

        final Date reminderDate;
        Calendar cal = Calendar.getInstance();
        cal.setTime(eventDate);
        switch (reminder){
            case "30min":{
                cal.add(Calendar.MINUTE, -30);
                break;
            }
            case "1hour":{
                cal.add(Calendar.MINUTE, -60);
                break;
            }
            case "5hour":{
                cal.add(Calendar.MINUTE, -300);
                break;
            }
            case "1day":{
                cal.add(Calendar.MINUTE, -1440);
                break;
            }
            case "1week":{
                cal.add(Calendar.MINUTE, -10080);
                break;
            }
        }
        Log.i(TAG,cal.getTime().toString());

        reminderDate = cal.getTime();
        Log.i(TAG,reminderDate.toString());

        //location
        final String location = locationEditText.getText().toString();
        if (location.isEmpty())
        {
            locationEditText.setError("Location field is empty!");
            locationEditText.requestFocus();
            return;
        }
        Log.i(TAG,location);

        //description
        final String description = descriptionEditText.getText().toString();
        if (description.isEmpty())
        {
            descriptionEditText.setError("Description field is empty!");
            descriptionEditText.requestFocus();
            return;
        }
        Log.i(TAG,description);



        Event event = new Event(title,eventDate,reminderDate,location,description);
        String loginEmail = mPreferences.getString("email", "");
        myRef.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean isUserExist = false;
                for(DataSnapshot userSnapshot : dataSnapshot.getChildren()){
                    User userExist = userSnapshot.getValue(User.class);
                    if (userExist.getEmail().equals(loginEmail)){
                        String key = myRef.child("Users").child(userSnapshot.getKey()).child("events").push().getKey();
                        myRef.child("Users").child(userSnapshot.getKey()).child("events").child(key).setValue(event);
                        Toast.makeText(getApplicationContext(), "Event successfully added!", Toast.LENGTH_SHORT).show();
                        isUserExist = true;
                        break;
                    }
                }
                if (!isUserExist){
                    Toast.makeText(getApplicationContext(), "You are not logged in!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //;Log.d(TAG, "onCancelled: Why I here?");
            }
        });
    }
}
