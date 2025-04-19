package com.example.project.ui.dashboard;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.project.R;

import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

    private List<Event> eventList;

    // Constructor
    public EventAdapter(List<Event> eventList) {
        this.eventList = eventList;
    }

    @Override
    public EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate the event item layout
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_item, parent, false);

        return new EventViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(EventViewHolder holder, int position) {
        Event event = eventList.get(position);

        // Bind the event data to the views
        holder.tvEventName.setText(event.getEventName());
        holder.tvEventDate.setText(event.getEventDate());
        holder.tvEventDescription.setText(event.getEventDescription());

        // For the event poster, you could load an image from a URL or resource.
        // Example: If using Glide or Picasso to load an image
        // Glide.with(holder.ivEventPoster.getContext()).load(event.getEventPosterUrl()).into(holder.ivEventPoster);
        holder.ivEventPoster.setImageResource(event.getEventPosterResource());
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    // ViewHolder class to hold the item views
    public class EventViewHolder extends RecyclerView.ViewHolder {
        public TextView tvEventName, tvEventDate, tvEventDescription;
        public ImageView ivEventPoster;

        public EventViewHolder(View itemView) {
            super(itemView);
            tvEventName = itemView.findViewById(R.id.tvName);
            tvEventDate = itemView.findViewById(R.id.tvEventDate);
            tvEventDescription = itemView.findViewById(R.id.tvEventDescription);
            ivEventPoster = itemView.findViewById(R.id.ivEventPoster);
        }
    }
}
