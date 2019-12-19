package com.nagycsongor.calendarapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class UserEventActivity extends AppCompatActivity {


    private EditText titleEditText;
    private TextView dateTextView;
    private TextView reminderTextView;
    private Spinner reminderSpinner;
    private EditText inviteFriendsEditText;
    private EditText locationEditText;
    private EditText descriptionEditText;
    private Button saveButton;
    private CustomDateTimePicker custom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_event);

        titleEditText = findViewById(R.id.editTitleEvent);
        dateTextView = findViewById(R.id.editDate);
        inviteFriendsEditText = findViewById(R.id.editInviteFriend);
        locationEditText = findViewById(R.id.editLocation);
        descriptionEditText = findViewById(R.id.editDescription);
        saveButton = findViewById(R.id.saveEdit);

    }
}
