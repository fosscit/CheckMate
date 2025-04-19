// File: app/src/main/java/com/example/project/ui/home/HomeFragment.java
package com.example.project.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.R;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private EventAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout that contains rvEvents
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 1) Setup RecyclerView
        RecyclerView rv = view.findViewById(R.id.rvEvents);
        rv.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new EventAdapter(new java.util.ArrayList<>());
        rv.setAdapter(adapter);

        // 2) Observe ViewModel
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        homeViewModel.getEvents().observe(getViewLifecycleOwner(), events -> {
            adapter.updateEvents(events);
        });
    }
}
