package com.example.taskManagement;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;

public class AddTask extends AppCompatActivity {

    EditText titleInput;
    EditText descriptionInput;
    Button timeBtn;
    Button dateBtn;
    Button createBtn;
    Button imageBtn;

    Task task = new Task();

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String userId;


    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addtask);
        userId = getIntent().getStringExtra("userId");

        titleInput = findViewById(R.id.createTaskTitleInput);
        descriptionInput = findViewById(R.id.createTaskDescriptionInput);
        timeBtn = findViewById(R.id.createTaskTimeBtn);
        dateBtn = findViewById(R.id.createTaskDateBtn);
        createBtn = findViewById(R.id.createTaskBtn);
        imageBtn = findViewById(R.id.createTaskAddImageBtn);

        timeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar currentTime = Calendar.getInstance();
                int hour = currentTime.get(Calendar.HOUR_OF_DAY);
                int minute = currentTime.get(Calendar.MINUTE);
                TimePickerDialog timePicker;
                timePicker = new TimePickerDialog(AddTask.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        task.getDeadline().set(Calendar.HOUR, view.getHour());
                        task.getDeadline().set(Calendar.MINUTE, view.getMinute());
                    }
                },hour, minute, false);
                timePicker.show();
            }
        });

        dateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar currentDate = Calendar.getInstance();
                int day = currentDate.get(Calendar.DAY_OF_MONTH);
                int month = currentDate.get(Calendar.MONTH);
                int year = currentDate.get(Calendar.YEAR);
                DatePickerDialog datePicker;
                datePicker = new DatePickerDialog(AddTask.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        task.getDeadline().set(Calendar.YEAR, view.getYear());
                        task.getDeadline().set(Calendar.MONTH, view.getMonth());
                        task.getDeadline().set(Calendar.DAY_OF_MONTH, view.getDayOfMonth());
                    }
                }, year, month, day);
                datePicker.show();
            }
        });

        imageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.with(AddTask.this)
                        .crop()
                        .compress(1024)
                        .maxResultSize(1080, 1080)
                        .start();
            }
        });

        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = titleInput.getText().toString();
                String description = descriptionInput.getText().toString();

                task.setTitle(title);
                task.setDescription(description);
                task.setOwnerId(userId);


                saveTask(task);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            //Image Uri will not be null for RESULT_OK
            if (data != null) {
                task.setImgUri(data.getData().toString());
            }
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show();
        }
    }

    void saveTask(Task task) {
        db.collection("tasks").add(task);
        finish();
    }
}