package com.example.project.ui.dashboard;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
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
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.project.Event;
import com.example.project.MainActivity;
import com.example.project.R;
import com.example.project.ui.LoginActivity;
import com.example.project.ui.home.HomeFragment;
import com.squareup.picasso.Picasso;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DashboardFragment extends Fragment {
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_PICK = 2;
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

        etDate.setFocusable(false);
        etDate.setOnClickListener(v -> showDatePickerDialog());

        btnUpload.setOnClickListener(v -> showImagePickerOptions());

        btnSave.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String date = etDate.getText().toString().trim();
            String desc = etDesc.getText().toString().trim().replace(",", " ");

            if (name.isEmpty() || date.isEmpty() || desc.isEmpty()) {
                Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (currentPhotoPath != null) {
                String safeName = name.replaceAll("[^a-zA-Z0-9-_]", "_");
                String imageName = safeName + ".jpg";
                String savedPath = copyImageToInternalStorage(requireContext(), Uri.fromFile(new File(currentPhotoPath)), imageName);
                Event event = new Event(name, date, desc, savedPath != null ? savedPath : "");
                File csv = new File(requireContext().getExternalFilesDir(null), "events.csv");
                appendEventToCsv(event, csv);
                Toast.makeText(getContext(), "Event saved!", Toast.LENGTH_SHORT).show();
                clearForm();

                // Move to HomeFragment
                NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
                navController.navigate(R.id.navigation_home);

            } else {
                Toast.makeText(getContext(), "Please select an image", Toast.LENGTH_SHORT).show();
            }
        });
        return root;
    }

    private void checkPermissions() {
        if (ContextCompat.checkSelfPermission(requireContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(requireContext(),
                        Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(requireContext(),
                        Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.CAMERA
                    }, REQUEST_STORAGE_PERMISSION);
        }
    }

    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(),
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    String selectedDate = String.format("%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay);
                    etDate.setText(selectedDate);
                },
                year, month, day
        );

        Calendar minDate = Calendar.getInstance(); // today
        Calendar maxDate = Calendar.getInstance();
        maxDate.add(Calendar.MONTH, 1); // 1 month ahead

        datePickerDialog.getDatePicker().setMinDate(minDate.getTimeInMillis());
        datePickerDialog.getDatePicker().setMaxDate(maxDate.getTimeInMillis());
        datePickerDialog.show();
    }

    private void showImagePickerOptions() {
        pickImageFromGallery();
    }

    private void pickImageFromGallery() {
        Intent pickIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickIntent, REQUEST_IMAGE_PICK);
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
            File image = File.createTempFile(imageFileName, ".jpg", storageDir);
            currentPhotoPath = image.getAbsolutePath();
            return image;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        try (Cursor cursor = requireContext().getContentResolver().query(contentUri, proj, null, null, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                return cursor.getString(column_index);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == getActivity().RESULT_OK) {
            File imgFile = new File(currentPhotoPath);
            if (imgFile.exists()) {
                Picasso.get().load(imgFile).into(ivPosterPreview);
            }
        } else if (requestCode == REQUEST_IMAGE_PICK && resultCode == getActivity().RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            if (selectedImageUri != null) {
                currentPhotoPath = getRealPathFromURI(selectedImageUri);
                Picasso.get().load(selectedImageUri).into(ivPosterPreview);
            }
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
    }

    public String copyImageToInternalStorage(Context context, Uri imageUri, String imageName) {
        try {
            File dir = new File(context.getFilesDir(), "posters");
            if (!dir.exists()) dir.mkdirs();

            File newFile = new File(dir, imageName);

            try (InputStream in = context.getContentResolver().openInputStream(imageUri);
                 OutputStream out = new FileOutputStream(newFile)) {

                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
            }

            return newFile.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
