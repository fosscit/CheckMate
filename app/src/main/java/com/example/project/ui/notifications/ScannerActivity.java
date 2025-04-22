package com.example.project.ui.notifications;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import com.example.project.R;

public class ScannerActivity extends AppCompatActivity {

    private final List<String> scannedRollNumbers = new ArrayList<>();
    private TextView rollNumbersTextView;
    private String eventName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);

        eventName = getIntent().getStringExtra("event_name");
        if (eventName == null || eventName.isEmpty()) {
            Toast.makeText(this, "Event name missing!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        rollNumbersTextView = findViewById(R.id.rollNumbersTextView);
        Button scanBtn = findViewById(R.id.scanBtn);

        scanBtn.setOnClickListener(v -> startBarcodeScanner());

        loadExistingRolls();
        updateScannedRollNumbers();
    }

    private void startBarcodeScanner() {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setPrompt("Scan the student ID barcode");
        integrator.setBeepEnabled(true);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() != null) {
                String scannedValue = result.getContents();

                if (!scannedRollNumbers.contains(scannedValue)) {
                    scannedRollNumbers.add(scannedValue);
                    saveRollToCsv(scannedValue);
                    Toast.makeText(this, "Scanned: " + scannedValue, Toast.LENGTH_SHORT).show();
                    updateScannedRollNumbers();
                } else {
                    Toast.makeText(this, "Already scanned: " + scannedValue, Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Scan canceled", Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void saveRollToCsv(String rollNumber) {
        File file = new File(getFilesDir(), eventName + "_attendance.csv");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            writer.write(rollNumber);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to save roll", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadExistingRolls() {
        File file = new File(getFilesDir(), eventName + "_attendance.csv");

        if (file.exists()) {
            try (Scanner scanner = new Scanner(file)) {
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine().trim();
                    if (!line.isEmpty() && !scannedRollNumbers.contains(line)) {
                        scannedRollNumbers.add(line);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Error loading previous scans", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void updateScannedRollNumbers() {
        StringBuilder sb = new StringBuilder("Scanned Roll Numbers:\n");
        for (String roll : scannedRollNumbers) {
            sb.append("• ").append(roll).append("\n");
        }
        rollNumbersTextView.setText(sb.toString());
    }
}
