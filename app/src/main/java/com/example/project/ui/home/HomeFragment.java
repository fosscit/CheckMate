package com.example.project.ui.home;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.project.Event;
import com.example.project.EventAdapter;
import com.example.project.R;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private EventAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        RecyclerView rv = root.findViewById(R.id.recyclerView);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));

        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    1);  // Request code
        }


        File csv = new File(requireContext().getExternalFilesDir(null), "events.csv");
        List<Event> events = loadEventsFromCsv(csv);
        adapter = new EventAdapter(events);
        rv.setAdapter(adapter);

        return root;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed to load the event data
                File csv = new File(requireContext().getExternalFilesDir(null), "events.csv");
                adapter.updateList(loadEventsFromCsv(csv));
            } else {
                // Permission denied, handle accordingly
                Toast.makeText(getContext(), "Permission denied! Unable to load images.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        File csv = new File(requireContext().getExternalFilesDir(null), "events.csv");
        adapter.updateList(loadEventsFromCsv(csv));
    }

    private List<Event> loadEventsFromCsv(File csvFile) {
        List<Event> list = new ArrayList<>();
        if (!csvFile.exists()) return list;

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] cols = line.split(",", 4);
                if (cols.length == 4) {
                    list.add(new Event(
                            cols[0], cols[1], cols[2], cols[3]
                    ));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }
}