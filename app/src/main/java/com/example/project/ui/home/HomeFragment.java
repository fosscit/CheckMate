package com.example.project.ui.home;

import android.os.Bundle;
import android.view.View;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.project.R;
import com.example.project.model.Event;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import android.content.Intent;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

public class HomeFragment extends Fragment {

    private HomeViewModel viewModel;
    private EventAdapter adapter;

    public HomeFragment() {
        super(R.layout.fragment_home); // your layout
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);

        adapter = new EventAdapter(new java.util.ArrayList<>());
        androidx.recyclerview.widget.RecyclerView rv = view.findViewById(R.id.rvEvents);
        rv.setLayoutManager(new LinearLayoutManager(requireContext()));
        rv.setAdapter(adapter);

        viewModel.getEvents().observe(getViewLifecycleOwner(), adapter::updateEvents);
    }
}
