package com.example.taskManagement;

import androidx.appcompat.app.AppCompatActivity;

import android.icu.text.DateFormat;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;

public class ShowTask extends AppCompatActivity {

    private Task task;
    TextView title;
    TextView description;
    TextView deadline;
    Switch doneSwitch;
    Switch favoris;
    ImageView image;
    Boolean deleting = false;

    Button deleteBtn;

    DateFormat df = DateFormat.getDateTimeInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_task);
        task = (Task) getIntent().getSerializableExtra("task");

        title = findViewById(R.id.taskTitle);
        description = findViewById(R.id.taskDescription);
        deadline = findViewById(R.id.taskDeadline);
        doneSwitch = findViewById(R.id.switchDone);
        image = findViewById(R.id.taskImage);
        deleteBtn = findViewById(R.id.deleteBtn);
        favoris= findViewById(R.id.switchfavoris);

        title.setText(task.getTitle());
        description.setText(task.getDescription());
        deadline.setText(
                "Deadline: " + df.format(task.getDeadline().getTime())
        );
        doneSwitch.setChecked(task.getDone());
        favoris.setChecked(intToBoolean(task.getFavoris()));


        //favoris.setChecked(task.getFavoris());
        if (task.getImgUri() != null &&
                !task.getImgUri().isEmpty()) {
            image.setImageURI(Uri.parse(task.getImgUri()));
        }

        doneSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                task.setDone(!task.getDone());
            }
        });
        favoris.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (intToBoolean(task.getFavoris())){
                    task.setFavoris(0);
                }else{
                    task.setFavoris(1);
                }

            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection("tasks").document(task.getDocUri()).delete();
                deleting = true;
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        if (deleting) {
            super.onDestroy();
        } else {
            String documentId = task.getDocUri();
            db.collection("tasks").document(documentId).set(task);
            super.onDestroy();
        }
    }

    public static boolean intToBoolean(int value) {
        if (value == 0) {
            return false;
        } else if (value == 1) {
            return true;
        } else {
            throw new IllegalArgumentException("Le paramètre doit être soit 0 soit 1.");
        }
    }
}