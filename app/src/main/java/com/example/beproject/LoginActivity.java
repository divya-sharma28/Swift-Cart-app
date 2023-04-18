package com.example.beproject;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    TextView newUser;
    EditText EmailAddress, Password;
    Button loginButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        newUser = findViewById(R.id.newUser);
        EmailAddress = findViewById(R.id.EmailAddress);
        Password = findViewById(R.id.Password);
        loginButton = findViewById(R.id.loginButton);
        FirebaseAuth mAuth;
        newUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, AuthenticationActivity.class));

                finish();
            }
        });

        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }
            loginButton.setOnClickListener(v -> {
            String email = EmailAddress.getText().toString().trim();
            String password = Password.getText().toString().trim();
            if (email.isEmpty()) {
                EmailAddress.setError("Email is empty");
                EmailAddress.requestFocus();
                return;
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                EmailAddress.setError("Enter the valid email");
                EmailAddress.requestFocus();
                return;
            }
            if (password.isEmpty()) {
                Password.setError("Password is empty");
                Password.requestFocus();
                return;
            }
            if (password.length() < 6) {
                Password.setError("Length of password is more than 6");
                Password.requestFocus();
                return;
            }
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    Toast.makeText(LoginActivity.this, "Logged in successfully", Toast.LENGTH_SHORT).show();

                    finish();
                } else {
                    Toast.makeText(LoginActivity.this,
                            "Please Check Your login Credentials",
                            Toast.LENGTH_SHORT).show();
                }

            });
        });
    }
}