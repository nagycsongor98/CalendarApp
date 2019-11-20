package com.nagycsongor.calendarapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    private TextView emailEditText;
    private TextView passwordEditText;
    private Button loginButton;
    private TextView registerTextView;
    private ProgressBar progressBar;
    private FirebaseDatabase database;
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        emailEditText = findViewById(R.id.loginEmailEditText);
        passwordEditText = findViewById(R.id.loginPasswordEditText);
        loginButton = findViewById(R.id.loginLoginButton);
        registerTextView = findViewById(R.id.loginRegisterTextView);
        progressBar = findViewById(R.id.progressbar);
        progressBar.setVisibility(View.GONE);

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

        final String email = emailEditText.getText().toString().trim();
        final String password = passwordEditText.getText().toString().trim();

        if (email.isEmpty())
        {
            emailEditText.setError("Email field is empty!");
            emailEditText.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            emailEditText.setError("Email form not correct!");
            emailEditText.requestFocus();
            return;
        }

        if (password.isEmpty())
        {
            passwordEditText.setError("Password field is empty!");
            passwordEditText.requestFocus();
            return;
        }

        progressBar.setVisibility(View.GONE);
        myRef.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean is = false;
                for(DataSnapshot userSnapshot : dataSnapshot.getChildren()){
                    User userExist = userSnapshot.getValue(User.class);
                    if (userExist.getEmail().equals(email) && userExist.getPassword().equals(password)){
                        progressBar.setVisibility(View.VISIBLE);
                        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                        startActivity(intent);
                        is = true;
                        break;
                    }
                }
                if (!is){
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "Email and password not match", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        progressBar.setVisibility(View.GONE);
    }
}

