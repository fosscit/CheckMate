package com.example.project.ui.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

    private List<Event> eventList;
    private final SimpleDateFormat parser =
            new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);

    public EventAdapter(List<Event> eventList) {
        this.eventList = eventList;
    }

    /** Called by HomeFragment to swap in a new list */
    public void updateEvents(List<Event> events) {
        this.eventList = events;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_event, parent, false);
        return new EventViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int pos) {
        Event e = eventList.get(pos);
        holder.title.setText(e.getTitle());
        holder.date.setText(e.getDate());
        holder.description.setText(e.getDescription());

        // Compare year/month/day only
        boolean isToday = false;
        try {
            Date d    = parser.parse(e.getDate());
            Calendar c1 = Calendar.getInstance(), c2 = Calendar.getInstance();
            c1.setTime(d);
            isToday = c1.get(Calendar.YEAR)  == c2.get(Calendar.YEAR)
                    && c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH)
                    && c1.get(Calendar.DAY_OF_MONTH)
                    == c2.get(Calendar.DAY_OF_MONTH);
        } catch (ParseException ex) {
            ex.printStackTrace();
        }

        holder.takeAttendance.setEnabled(isToday);
        holder.takeAttendance.setOnClickListener(view -> {
            if (!holder.takeAttendance.isEnabled()) return;
            Toast.makeText(
                    view.getContext(),
                    "Taking attendance for “" + e.getTitle() + "”",
                    Toast.LENGTH_SHORT
            ).show();
            // TODO: launch your actual attendance logic here
        });
    }

    @Override
    public int getItemCount() {
        return eventList != null ? eventList.size() : 0;
    }

    static class EventViewHolder extends RecyclerView.ViewHolder {
        final TextView title, date, description;
        final Button takeAttendance;

        EventViewHolder(@NonNull View v) {
            super(v);
            title           = v.findViewById(R.id.eventTitle);
            date            = v.findViewById(R.id.eventDate);
            description     = v.findViewById(R.id.eventDescription);
            takeAttendance  = v.findViewById(R.id.btnTakeAttendance);
        }
    }
}
