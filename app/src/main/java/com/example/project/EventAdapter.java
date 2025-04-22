package com.example.project;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.ui.notifications.ScannerActivity;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

    private List<Event> eventList;

    public EventAdapter(List<Event> eventList) {
        this.eventList = eventList;
    }

    public void updateList(List<Event> newList) {
        this.eventList = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_event, parent, false);
        return new EventViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        Event event = eventList.get(position);

        holder.tvName.setText(event.getEventName());
        holder.tvDate.setText(event.getEventDate());
        holder.tvDesc.setText(event.getEventDescription());

        String posterPath = event.getPosterPath();

        if (posterPath != null && !posterPath.isEmpty()) {
            File imageFile = new File(posterPath);

            if (imageFile.exists()) {
                Picasso.get()
                        .load(imageFile)
                        .into(holder.ivPoster, new com.squareup.picasso.Callback() {
                            @Override
                            public void onSuccess() {
                                Log.d("EventAdapter", "Image loaded: " + posterPath);
                            }

                            @Override
                            public void onError(Exception e) {
                                Log.e("EventAdapter", "Failed to load image: " + posterPath, e);
                            }
                        });
            } else {
                Log.w("EventAdapter", "Image file not found: " + posterPath);
                holder.ivPoster.setImageResource(R.drawable.bg_docker); // Optional fallback image
            }
        } else {
            holder.ivPoster.setImageResource(R.drawable.bg_docker); // Optional fallback image
        }

        // Enable or disable the "Take Attendance" button based on whether it's today's date
        if (event.isToday()) {
            holder.takeAttendanceButton.setEnabled(true); // Enable if event date matches current date
        } else {
            holder.takeAttendanceButton.setEnabled(false); // Disable otherwise
        }
        holder.takeAttendanceButton.setOnClickListener(v -> {
            if (holder.takeAttendanceButton.isEnabled()) {
                android.content.Context context = v.getContext();
                android.content.Intent intent = new android.content.Intent(context, ScannerActivity.class);
                intent.putExtra("event_name", event.getEventName());
                context.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return eventList != null ? eventList.size() : 0;
    }

    static class EventViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvDate, tvDesc;
        ImageView ivPoster;
        Button takeAttendanceButton;

        EventViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvDesc = itemView.findViewById(R.id.tvDesc);
            ivPoster = itemView.findViewById(R.id.ivPoster);
            takeAttendanceButton = itemView.findViewById(R.id.takeAttendanceButton); // Reference the button here
        }
    }
}
