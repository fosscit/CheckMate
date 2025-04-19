package com.example.project.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.R;

import java.util.ArrayList;
import java.util.List;

public class DashboardFragment extends Fragment {

    private RecyclerView recyclerView;
    private EventAdapter eventAdapter;
    private List<Event> eventList;

    public DashboardFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize event list and adapter
        eventList = new ArrayList<>();
        eventList.add(new Event("Event 1", "2025-04-20", "This is a description of event 1", R.drawable.event_poster_1));
        eventList.add(new Event("Event 2", "2025-04-21", "This is a description of event 2", R.drawable.event_poster_2));

        eventAdapter = new EventAdapter(eventList);
        recyclerView.setAdapter(eventAdapter);

        return view;
    }
}
