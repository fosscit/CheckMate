package com.example.project.ui.notifications;

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

import java.util.ArrayList;
import java.util.List;

public class ScannerFragment extends Fragment {

    private final List<String> scannedRollNumbers = new ArrayList<>();
    private TextView rollNumbersTextView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scanner, container, false);

        // grab references
        Button scanBtn = view.findViewById(R.id.scanBtn);
        rollNumbersTextView = view.findViewById(R.id.rollNumbersTextView);

        // set up click
        scanBtn.setOnClickListener(v -> startBarcodeScanner());

        // show any already‐scanned numbers
        updateScannedRollNumbers();

        return view;
    }

    private void startBarcodeScanner() {
        // Just launch ZXing’s built‐in scanner
        IntentIntegrator integrator = IntentIntegrator.forSupportFragment(this);
        integrator.setPrompt("Scan the student ID barcode");
        integrator.setBeepEnabled(true);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.initiateScan();  // no orientation hacks here
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() != null) {
                // add to list and show toast
                scannedRollNumbers.add(result.getContents());
                Toast.makeText(getContext(),
                        "Scanned: " + result.getContents(),
                        Toast.LENGTH_SHORT).show();
                updateScannedRollNumbers();
            } else {
                Toast.makeText(getContext(), "Scan canceled", Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
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
