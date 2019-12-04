package com.nagycsongor.calendarapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NewEventActivity extends AppCompatActivity {
    private EditText titleEditText;
    private TextView dateTextView;
    private TextView reminderTextView;
    private Spinner reminderSpinner;
    private EditText inviteFriendsEditText;
    private EditText locationEditText;
    private EditText descriptionEditText;
    private Button saveButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);
        titleEditText = findViewById(R.id.titleEditText);
        dateTextView = findViewById(R.id.dateTextView);
        reminderTextView = findViewById(R.id.reminderTextView);
        reminderSpinner = findViewById(R.id.reminderSpinner);
        inviteFriendsEditText = findViewById(R.id.inviteFriendsEditText);
        locationEditText = findViewById(R.id.locationEditText);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        saveButton = findViewById(R.id.saveButton);

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

        //date
        final Date eventDate;
        String dateString = dateTextView.getText().toString();
        if (dateString.isEmpty())
        {
            dateTextView.setError("Date field is empty!");
            dateTextView.requestFocus();
            return;
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        try {
            eventDate = format.parse(dateString);
            System.out.println(eventDate);
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

        //location
        final String location = locationEditText.getText().toString();
        if (location.isEmpty())
        {
            locationEditText.setError("Location field is empty!");
            locationEditText.requestFocus();
            return;
        }

        //description
        final String description = descriptionEditText.getText().toString();
        if (description.isEmpty())
        {
            descriptionEditText.setError("Description field is empty!");
            descriptionEditText.requestFocus();
            return;
        }

        //Event event = new Event(title,eventDate,,location,description);

    }
}
