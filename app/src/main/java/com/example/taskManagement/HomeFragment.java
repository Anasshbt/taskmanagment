package com.example.taskManagement;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.math.BigInteger;
import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private RecyclerView tasksRecycler;
    private TaskAdapter tasksRecyclerAdapter;
    ArrayList<com.example.taskManagement.Task> tasks = new ArrayList<com.example.taskManagement.Task>();
    private final Handler handler = new Handler();
    private final int delay = 1 * 1000; // 1 second

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private AlarmManager alarmManager;
    private String userId;

    @Override
    public void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        alarmManager = (AlarmManager) requireActivity().getSystemService(Context.ALARM_SERVICE);
        db = FirebaseFirestore.getInstance();
        userId = mAuth.getCurrentUser().getUid(); // Assuming user is already authenticated
        getTasksFromDB();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getTasksFromDB();
                handler.postDelayed(this, delay);
            }
        }, delay);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        tasksRecycler = rootView.findViewById(R.id.tasksRecycler);
        tasksRecycler.setHasFixedSize(true);
        tasksRecyclerAdapter = new TaskAdapter(tasks);
        tasksRecycler.setAdapter(tasksRecyclerAdapter);
        return rootView;
    }

    private void getTasksFromDB() {
        db.collection("tasks")
                .whereEqualTo("ownerId", userId)
                .get()
                .addOnCompleteListener(
                        new OnCompleteListener<QuerySnapshot>() {
                            @SuppressLint("ScheduleExactAlarm")
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    tasks.clear();
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        Log.d("tasks", document.getId() + " => " + document.getData());
                                        try {
                                            com.example.taskManagement.Task leTask = document.toObject(com.example.taskManagement.Task.class);
                                            leTask.setDocUri(document.getId());
                                            BigInteger taskId = new BigInteger(1, leTask.getDocUri().getBytes());
                                            Intent intent = new Intent(requireActivity(), TaskAlarm.class);
                                            PendingIntent pendingIntent = PendingIntent.getBroadcast(
                                                    requireActivity(),
                                                    taskId.intValue(),
                                                    intent,
                                                    PendingIntent.FLAG_UPDATE_CURRENT);
                                            alarmManager.setExact(
                                                    AlarmManager.RTC_WAKEUP,
                                                    leTask.getDeadline_int(),
                                                    pendingIntent
                                            );

                                            tasks.add(leTask);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    tasksRecyclerAdapter.notifyDataSetChanged();
                                } else {
                                    Log.d("tasks", "fetch not successful");
                                }
                            }
                        }
                );
    }
}
