package com.example.taskManagement;

import android.content.Intent;
import android.icu.text.DateFormat;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


public class TaskViewHolder extends RecyclerView.ViewHolder {
    private final CardView container;
    private final TextView title;
    private final TextView date;
    private final ImageView image;
    private final LinearLayout isDone;
    private Task task;
    private DateFormat df = DateFormat.getDateTimeInstance();


    public TaskViewHolder(@NonNull View itemView) {
        super(itemView);

        title = itemView.findViewById(R.id.cardTitle);
        date = itemView.findViewById(R.id.cardDate);
        image = itemView.findViewById(R.id.imageView);
        container = itemView.findViewById(R.id.container);
        isDone = itemView.findViewById(R.id.isDone);

        container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ShowTask.class);
                intent.putExtra("task", task);
                v.getContext().startActivity(intent);
            }
        });

    }

    public void show(com.example.taskManagement.Task task) {
        this.task = task;
        title.setText(this.task.getTitle());
        date.setText(
                "Deadline: " + df.format(this.task.getDeadline().getTime())
        );
        if (this.task.getImgUri() != null &&
                !this.task.getImgUri().isEmpty()
        ) {
            image.setImageURI(
                    Uri.parse(task.getImgUri())
            );
        }

        if (!this.task.getDone()) {
            isDone.setBackgroundResource(R.drawable.baseline_toggle_off_24);
        } else {
            isDone.setBackgroundResource(R.drawable.baseline_toggle_on_24);
        }
    }
}
