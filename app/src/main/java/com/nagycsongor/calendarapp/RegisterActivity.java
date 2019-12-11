package com.nagycsongor.calendarapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
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

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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
    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;

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
        mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mEditor = mPreferences.edit();


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
            passwordAgainEditText.setError("Password again field is empty!");
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
                    String hashPassword = passwordHash(password, email.getBytes());
                    progressBar.setVisibility(View.VISIBLE);
                    String key = myRef.child("Users").push().getKey();
                    User user = new User(email, name, hashPassword);
                    myRef.child("Users").child(key).setValue(user);
//                    myRef.child("Users").child(email).setValue(user);
                    mEditor.putString("email", email);
                    mEditor.commit();
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

    /**
     * Generate a password hash by using (email as) salt.
     *
     * @param password, salt
     *
     * @return hashed password
     */
    public String passwordHash(String password, byte[] salt) {

        String generatedPassword = null;

        try {
            // Create MessageDigest instance for MD5
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            // Add password bytes to digest
            messageDigest.update(salt);
            // Get the hash's bytes
            byte[] bytes = messageDigest.digest(password.getBytes());
            // This bytes[] has bytes in decimal format;
            // Convert it to hexadecimal format
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            // Get complete hashed password in hex format
            generatedPassword = sb.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return generatedPassword;

    }
}
