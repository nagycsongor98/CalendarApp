package com.nagycsongor.calendarapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegisterActivity extends AppCompatActivity {
    private EditText emailEditText;
    private EditText nameEditText;
    private EditText passwordEditText;
    private EditText passwordAgainEditText;
    private ProgressBar progressBar;
    private Button registerButton;
    private TextView loginTextView;
    private FirebaseDatabase database;
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();



        emailEditText = findViewById(R.id.registerEmailEditText);
        nameEditText = findViewById(R.id.registerNameEditText);
        passwordEditText = findViewById(R.id.registerPasswordEditText);
        passwordAgainEditText = findViewById(R.id.registerPasswordAgainEditText);
        registerButton = findViewById(R.id.registerRegisterButton);
        loginTextView = findViewById(R.id.registerLoginTextView);
        progressBar = findViewById(R.id.progressbar);
        progressBar.setVisibility(View.GONE);



        registerButton.setOnClickListener(v -> registration());
        loginTextView.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(),LoginActivity.class)));

    }

    private void registration() {

        final String email = emailEditText.getText().toString().trim();
        final String name = nameEditText.getText().toString().trim();
        final String password = passwordEditText.getText().toString().trim();
        String passwordAgain = passwordAgainEditText.getText().toString().trim();

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

        if (name.isEmpty())
        {
            nameEditText.setError("Name field is empty!");
            nameEditText.requestFocus();
            return;
        }

        if (password.isEmpty())
        {
            passwordEditText.setError("Password field is empty!");
            passwordEditText.requestFocus();
            return;
        }

        if (passwordAgain.isEmpty())
        {
            passwordAgainEditText.setError("Passeord again field is empty!");
            passwordAgainEditText.requestFocus();
            return;
        }

        if (!password.equals(passwordAgain))
        {
            passwordAgainEditText.setError("The two passwords doesn't match");
            passwordAgainEditText.requestFocus();
            return;
        }


        myRef.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean isUserExist = false;
                for(DataSnapshot userSnapshot : dataSnapshot.getChildren()){
                    User userExist = userSnapshot.getValue(User.class);
                    if (userExist.getEmail().equals(email)){
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(RegisterActivity.this, "This email already exists!", Toast.LENGTH_SHORT).show();
                        isUserExist = true;
                        break;
                    }
                }
                if (!isUserExist){
                    progressBar.setVisibility(View.VISIBLE);
                    String key = myRef.child("Users").push().getKey();
                    User user = new User(email, name, password);
                    myRef.child("Users").child(key).setValue(user);
                    Intent intent = new Intent(RegisterActivity.this,MainActivity.class);
                    startActivity(intent);
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
