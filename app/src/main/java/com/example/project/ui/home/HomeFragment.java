package com.example.project.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
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

        File csv = new File(requireContext().getExternalFilesDir(null), "events.csv");
        List<Event> events = loadEventsFromCsv(csv);
        adapter = new EventAdapter(events);
        rv.setAdapter(adapter);

        return root;
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