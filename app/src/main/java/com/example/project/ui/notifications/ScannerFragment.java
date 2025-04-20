package com.example.project.ui.notifications;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.project.R;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ScannerFragment extends Fragment {

    private final List<String> scannedRollNumbers = new ArrayList<>();
    private TextView rollNumbersTextView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scanner, container, false);

        Button scanBtn = view.findViewById(R.id.scanBtn);
        rollNumbersTextView = view.findViewById(R.id.rollNumbersTextView);

        scanBtn.setOnClickListener(v -> startBarcodeScanner());

        loadExistingRolls();
        updateScannedRollNumbers();

        return view;
    }

    private void startBarcodeScanner() {
        IntentIntegrator integrator = IntentIntegrator.forSupportFragment(this);
        integrator.setPrompt("Scan the student ID barcode");
        integrator.setBeepEnabled(true);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.initiateScan();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() != null) {
                String scannedValue = result.getContents();

                if (!scannedRollNumbers.contains(scannedValue)) {
                    scannedRollNumbers.add(scannedValue);
                    saveRollToCsv(scannedValue);

                    Toast.makeText(getContext(), "Scanned: " + scannedValue, Toast.LENGTH_SHORT).show();
                    updateScannedRollNumbers();
                } else {
                    Toast.makeText(getContext(), "Already scanned: " + scannedValue, Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getContext(), "Scan canceled", Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void saveRollToCsv(String rollNumber) {
        File file = new File(requireContext().getFilesDir(), "scanned_rolls.csv");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            writer.write(rollNumber);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Failed to save roll", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadExistingRolls() {
        File file = new File(requireContext().getFilesDir(), "scanned_rolls.csv");

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
                Toast.makeText(getContext(), "Error loading previous scans", Toast.LENGTH_SHORT).show();
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
