// File: app/src/main/java/com/example/project/ui/home/HomeViewModel.java
package com.example.project.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.project.R;

import java.util.ArrayList;
import java.util.List;

public class HomeViewModel extends ViewModel {
    private final MutableLiveData<List<Event>> events = new MutableLiveData<>();

    public HomeViewModel() {
        loadEvents();
    }

    public LiveData<List<Event>> getEvents() {
        return events;
    }

    private void loadEvents() {
        List<Event> list = new ArrayList<>();
        list.add(new Event(
                "Figma Unleashed",
                "April 19, 2025",
                "A UI/UX workshop by FOSS‑CIT",
                R.drawable.bg_hackathon
        ));
        list.add(new Event(
                "Intern 101: SMARTAIL",
                "March 1, 2025",
                "AI internship insights",
                R.drawable.bg_docker
        ));
        // …add more as needed…
        events.setValue(list);
    }
}
