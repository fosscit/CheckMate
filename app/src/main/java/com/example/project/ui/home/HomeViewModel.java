package com.example.project;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class HomeViewModel extends ViewModel {
    private final MutableLiveData<List<Event>> events = new MutableLiveData<>(new ArrayList<>());

    public LiveData<List<Event>> getEvents() {
        return events;
    }

    public void addEvent(Event event) {
        List<Event> current = events.getValue();
        if (current == null) current = new ArrayList<>();
        current.add(event);
        events.setValue(current);
    }
}
