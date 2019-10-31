package com.nagycsongor.calendarapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class RegisterActivity extends AppCompatActivity {
    private TextView emailTextView;
    private TextView nameTextView;
    private TextView passwordTextView;
    private TextView passwordAgainTextView;
    private Button registerButton;
    private TextView loginTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        emailTextView = findViewById(R.id.registerEmailEditText);
        nameTextView = findViewById(R.id.registerNameTextView);
        passwordTextView = findViewById(R.id.registerPasswordEditText);
        passwordAgainTextView = findViewById(R.id.registerPasswordAgainEditText);
        registerButton = findViewById(R.id.registerRegisterButton);
        loginTextView = findViewById(R.id.registerLoginTextView);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registering();
            }
        });
        loginTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {  startActivity(new Intent(getApplicationContext(),LoginActivity.class)); }
        });

    }

    private void registering() {
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }
}
