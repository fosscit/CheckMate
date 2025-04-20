package com.example.project.ui.home;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project.R;

public class CreateEventActivity extends AppCompatActivity {

    private EditText eventNameEditText, eventDateEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        eventNameEditText = findViewById(R.id.eventNameEditText);
        eventDateEditText = findViewById(R.id.eventDateEditText);

        // On Click to Create Event and return to Dashboard
        findViewById(R.id.createEventButton).setOnClickListener(v -> {
            String eventName = eventNameEditText.getText().toString();
            String eventDate = eventDateEditText.getText().toString();

            if (eventName.isEmpty() || eventDate.isEmpty()) {
                Toast.makeText(this, "Please enter all details", Toast.LENGTH_SHORT).show();
                return;
            }

            // Pass the data back to DashboardFragment
            Intent resultIntent = new Intent();
            resultIntent.putExtra("event_name", eventName);
            resultIntent.putExtra("event_date", eventDate);

            setResult(Activity.RESULT_OK, resultIntent);
            finish();  // Finish the activity and return to DashboardFragment
        });
    }
}
