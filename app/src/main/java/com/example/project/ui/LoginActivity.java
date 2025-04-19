package com.example.project.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project.MainActivity;
import com.example.project.R;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.loginButton);

        // Create CSV with default user if not already created
        createUserCsvIfNeeded();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLoginClicked();
            }
        });
    }

    private void createUserCsvIfNeeded() {
        File file = new File(getFilesDir(), "users.csv");

        if (!file.exists()) {
            try (FileWriter fw = new FileWriter(file);
                 BufferedWriter bw = new BufferedWriter(fw);
                 PrintWriter pw = new PrintWriter(bw)) {

                pw.println("Username,Password");
                pw.println("FOSS28,12345678");

            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Failed to create user CSV", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void onLoginClicked() {
        String inputUsername = usernameEditText.getText().toString().trim();
        String inputPassword = passwordEditText.getText().toString().trim();

        if (TextUtils.isEmpty(inputUsername) || TextUtils.isEmpty(inputPassword)) {
            Toast.makeText(this, "Please enter both username and password", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            File file = new File(getFilesDir(), "users.csv");

            if (!file.exists()) {
                Toast.makeText(this, "User data not found", Toast.LENGTH_SHORT).show();
                return;
            }

            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            boolean found = false;

            // Skip header
            reader.readLine();

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    String username = parts[0].trim();
                    String password = parts[1].trim();

                    if (inputUsername.equals(username) && inputPassword.equals(password)) {
                        found = true;
                        break;
                    }
                }
            }

            reader.close();

            if (found) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Invalid credentials", Toast.LENGTH_SHORT).show();
            }

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error reading user data", Toast.LENGTH_SHORT).show();
        }
    }
}
