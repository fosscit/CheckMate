package com.example.project.ui.notifications;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.project.R;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AttendanceFragment extends Fragment {

    private ListView attendanceListView;
    private static final int REQUEST_CODE_EXPORT = 1;
    private File lastExportedFile;  // Remember the file selected for export

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_attendance, container, false);
        attendanceListView = view.findViewById(R.id.attendanceListView);
        loadAttendanceFiles();
        return view;
    }

    private void loadAttendanceFiles() {
        File dir = requireContext().getFilesDir();
        File[] files = dir.listFiles((file, name) -> name.endsWith("_attendance.csv"));

        List<String> fileNames = new ArrayList<>();
        if (files != null) {
            for (File f : files) {
                fileNames.add(f.getName());
            }
        }

        if (fileNames.isEmpty()) {
            fileNames.add("No attendance records found.");
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, fileNames);
        attendanceListView.setAdapter(adapter);

        attendanceListView.setOnItemClickListener((parent, view, position, id) -> {
            String selectedFile = fileNames.get(position);
            if (!selectedFile.equals("No attendance records found.")) {
                showAttendanceDetails(selectedFile);
            }
        });
    }

    private void showAttendanceDetails(String fileName) {
        File file = new File(requireContext().getFilesDir(), fileName);
        lastExportedFile = file;  // Save for export

        StringBuilder builder = new StringBuilder();

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (!line.isEmpty()) {
                    String[] parts = line.split(",");
                    if (parts.length >= 3) {
                        String roll = parts[0];
                        String date = parts[1];
                        String time = parts[2];
                        builder.append("• ").append(roll).append(" - ").append(date).append(" at ").append(time).append("\n");
                    } else {
                        builder.append("• ").append(line).append("\n"); // fallback if format is off
                    }
                }
            }

            // Show in AlertDialog
            View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_attendance_details, null);
            TextView contentTextView = dialogView.findViewById(R.id.attendanceContent);
            contentTextView.setText(builder.toString());

            new androidx.appcompat.app.AlertDialog.Builder(requireContext())
                    .setTitle(fileName.replace("_", " "))
                    .setView(dialogView)
                    .setPositiveButton("Close", null)
                    .setNegativeButton("Export to Downloads", (dialog, which) -> exportToDownloads())
                    .show();

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Error reading file", Toast.LENGTH_SHORT).show();
        }
    }

    private void exportToDownloads() {
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.setType("text/csv");
        intent.putExtra(Intent.EXTRA_TITLE, lastExportedFile.getName());
        startActivityForResult(intent, REQUEST_CODE_EXPORT);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_EXPORT && resultCode == Activity.RESULT_OK && data != null) {
            Uri uri = data.getData();
            if (uri != null && lastExportedFile != null) {
                writeFileToUri(uri, lastExportedFile);
            }
        }
    }

    private void writeFileToUri(Uri uri, File sourceFile) {
        try (BufferedReader reader = new BufferedReader(new FileReader(sourceFile));
             OutputStream out = requireContext().getContentResolver().openOutputStream(uri);
             BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                StringBuilder quotedLine = new StringBuilder();

                for (int i = 0; i < parts.length; i++) {
                    if (i > 0) quotedLine.append(",");
                    quotedLine.append("\"").append(parts[i].trim()).append("\"");
                }

                writer.write(quotedLine.toString());
                writer.newLine();
            }

            Toast.makeText(getContext(), "File exported successfully", Toast.LENGTH_SHORT).show();

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Failed to export file", Toast.LENGTH_SHORT).show();
        }
    }
}
