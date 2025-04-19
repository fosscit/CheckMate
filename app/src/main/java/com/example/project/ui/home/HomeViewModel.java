package com.example.project.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

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
        list.add(new Event("Figma Unleashed",    "April 19, 2025", "A UI/UX workshop by FOSS‑CIT"));
        list.add(new Event("Intern 101: SMARTAIL","March 1, 2025",      "AI internship insights"));
        // …add more as needed…
        events.setValue(list);
    }
}
