package com.example.project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.R;
import com.example.project.Event;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AttendanceAdapter extends RecyclerView.Adapter<AttendanceAdapter.ViewHolder> {

    private final List<Event> eventList;
    private final Context context;

    public AttendanceAdapter(List<Event> events, Context ctx) {
        this.eventList = events;
        this.context = ctx;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_attendance, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Event event = eventList.get(position);
        holder.tvName.setText(event.getEventName());
        holder.tvDate.setText(event.getEventDate());
        holder.tvDesc.setText(event.getEventDescription());

        // Load image using Picasso or fallback
        String posterPath = event.getPosterPath();
        if (posterPath != null && !posterPath.isEmpty()) {
            File imageFile = new File(posterPath);
            if (imageFile.exists()) {
                Picasso.get().load(imageFile).into(holder.ivPoster);
            } else {
                holder.ivPoster.setImageResource(R.drawable.bg_docker);
            }
        } else {
            holder.ivPoster.setImageResource(R.drawable.bg_docker);
        }

        // Load attendance from file
        String filename = event.getEventName().replace(" ", "_") + "_attendance.csv";
        File file = new File(context.getFilesDir(), filename);
        List<String> rolls = new ArrayList<>();

        if (file.exists()) {
            try (Scanner scanner = new Scanner(file)) {
                while (scanner.hasNextLine()) {
                    rolls.add(scanner.nextLine().trim());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Clear previous
        holder.rollListLayout.removeAllViews();
        for (String roll : rolls) {
            TextView tv = new TextView(context);
            tv.setText("• " + roll);
            holder.rollListLayout.addView(tv);
        }

        holder.rollListLayout.setVisibility(View.GONE);
        holder.viewAttendanceButton.setOnClickListener(v -> {
            if (holder.rollListLayout.getVisibility() == View.VISIBLE) {
                holder.rollListLayout.setVisibility(View.GONE);
                holder.viewAttendanceButton.setText("View Attendance");
            } else {
                holder.rollListLayout.setVisibility(View.VISIBLE);
                holder.viewAttendanceButton.setText("Hide Attendance");
            }
        });
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvDate, tvDesc;
        ImageView ivPoster;
        Button viewAttendanceButton;
        LinearLayout rollListLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvDesc = itemView.findViewById(R.id.tvDesc);
            ivPoster = itemView.findViewById(R.id.ivPoster);
            viewAttendanceButton = itemView.findViewById(R.id.viewAttendanceButton);
            rollListLayout = itemView.findViewById(R.id.rollListLayout);
        }
    }
}
