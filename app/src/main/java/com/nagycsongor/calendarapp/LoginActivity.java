package com.nagycsongor.calendarapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {
    private TextView emailTextView;
    private TextView passwordTextView;
    private Button loginButton;
    private TextView registerTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        emailTextView = findViewById(R.id.loginEmailEditText);
        passwordTextView = findViewById(R.id.loginPasswordEditText);
        loginButton = findViewById(R.id.loginLoginButton);
        registerTextView = findViewById(R.id.loginRegisterTextView);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
        registerTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { startActivity(new Intent(getApplicationContext(),RegisterActivity.class)); }
        });

    }

    private void login(){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }
}

