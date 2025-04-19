package com.example.project.ui.dashboard;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import com.example.project.Event;
import com.example.project.R;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.squareup.picasso.Picasso;
public class DashboardFragment extends Fragment {
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_STORAGE_PERMISSION = 100;
    private EditText etName, etDate, etDesc;
    private ImageView ivPosterPreview;
    private String currentPhotoPath;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);

        etName = root.findViewById(R.id.etName);
        etDate = root.findViewById(R.id.etDate);
        etDesc = root.findViewById(R.id.etDescription);
        ivPosterPreview = root.findViewById(R.id.ivPosterPreview);
        Button btnSave = root.findViewById(R.id.btnSave);
        Button btnUpload = root.findViewById(R.id.btnUpload);

        checkPermissions();

        btnUpload.setOnClickListener(v -> dispatchTakePictureIntent());

        btnSave.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String date = etDate.getText().toString().trim();
            String desc = etDesc.getText().toString().trim().replace(",", " ");

            if (name.isEmpty() || date.isEmpty() || desc.isEmpty()) {
                Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            Event event = new Event(name, date, desc, currentPhotoPath != null ? currentPhotoPath : "");
            File csv = new File(requireContext().getExternalFilesDir(null), "events.csv");
            appendEventToCsv(event, csv);
            Toast.makeText(getContext(), "Event saved!", Toast.LENGTH_SHORT).show();
            clearForm();
        });

        return root;
    }

    private void checkPermissions() {
        if (ContextCompat.checkSelfPermission(requireContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_STORAGE_PERMISSION);
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(requireActivity().getPackageManager()) != null) {
            File photoFile = createImageFile();
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(requireContext(),
                        "com.example.project.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private File createImageFile() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        try {
            File image = File.createTempFile(
                    imageFileName,
                    ".jpg",
                    storageDir
            );
            currentPhotoPath = image.getAbsolutePath();
            return image;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void appendEventToCsv(Event e, File csvFile) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(csvFile, true))) {
            String line = String.join(",",
                    e.getEventName(),
                    e.getEventDate(),
                    e.getEventDescription(),
                    e.getPosterPath()) + "\n";
            bw.write(line);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void clearForm() {
        etName.setText("");
        etDate.setText("");
        etDesc.setText("");
        currentPhotoPath = null;
        ivPosterPreview.setImageResource(R.drawable.bg_docker);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == getActivity().RESULT_OK) {
            File imgFile = new File(currentPhotoPath);
            if (imgFile.exists()) {
                Picasso.get()
                        .load(imgFile)
                        .into(ivPosterPreview);
            }
        }
    }
}