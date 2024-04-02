package com.example.taskManagement;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TaskAdapter extends RecyclerView.Adapter<TaskViewHolder> {
    private ArrayList<com.example.taskManagement.Task> tasks = new ArrayList<>();

    public TaskAdapter(ArrayList<Task> tasks) {
        if (tasks != null) {
            Log.d("TaskAdapter", "tasks is not null");
            this.tasks = tasks;
        }
    }
    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("TaskAdapter", "Creating view");
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.task_view, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        com.example.taskManagement.Task task = tasks.get(position);
        Log.d("TaskAdapter", "Showing task: " + task);
        holder.show(task);
    }

    @Override
    public int getItemCount() {
        if (tasks != null) {
            Log.d("TaskAdapter", "tasks size: " + tasks.size());
            return tasks.size();
        }
        return tasks.size();
    }

}