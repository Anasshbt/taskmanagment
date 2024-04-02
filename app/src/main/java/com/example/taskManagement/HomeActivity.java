package com.example.taskManagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.math.BigInteger;
import java.util.ArrayList;




public class HomeActivity extends AppCompatActivity {
    RecyclerView tasksRecycler;
    //TaskAdapter tasksRecyclerAdapter;
    FloatingActionButton addTaskBtn;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;

    AlarmManager alarmManager;



    String userId;
    final Handler handler = new Handler();
    final int delay = 1 * 1000; // 1 seconds

    ArrayList<com.example.taskManagement.Task> tasks = new ArrayList<>();

    ImageButton logoutBtn;


       FragmentManager fm=getSupportFragmentManager();
       FragmentTransaction ft=fm.beginTransaction();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userId = getIntent().getStringExtra("userId");
        setContentView(R.layout.activity_home);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentcontainer, new HomeFragment());
        fragmentTransaction.commit();





        MaterialToolbar toolbar =findViewById(R.id.topappbar);
        DrawerLayout drawerLayout=findViewById(R.id.drawerlayout);
        NavigationView navigationView =findViewById(R.id.navigationview);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);

        addTaskBtn = findViewById(R.id.addTaskBtn);
        mAuth = FirebaseAuth.getInstance();





        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);



        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                drawerLayout.openDrawer(GravityCompat.START);

            }
        });
















        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @org.jetbrains.annotations.NotNull MenuItem item) {

                int itemId = item.getItemId();
                if (itemId == R.id.home){
                    // Intent intent = new Intent(HomeActivity.this, HomeActivity.class);
                    //startActivity(intent);
                    finish();

                } else if (itemId == R.id.about) {
                    Intent intent = new Intent(HomeActivity.this, About_us.class);
                    startActivity(intent);
                    finish();

                } else if (itemId == R.id.rateus) {
                    return true;

                }


                else{


                    return true;
                }
                drawerLayout.closeDrawer(GravityCompat.START);

                return true;
            }

        });


        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {


            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

            if (item.getItemId() == R.id.home) {
                HomeFragment home1=new HomeFragment();
                ft.replace(R.id.fragmentcontainer,home1);
            } else if (item.getItemId() == R.id.Calendar) {
               // Toast.makeText(HomeActivity.this, "Vous avez cliqué sur le menu calendar", Toast.LENGTH_SHORT).show();
                CalendarFragment calendar=new CalendarFragment();
                ft.replace(R.id.fragmentcontainer,calendar);

            } else if (item.getItemId() == R.id.Meet) {
                MeetFragment meet=new MeetFragment();
                ft.replace(R.id.fragmentcontainer,meet);

            } else if (item.getItemId() == R.id.Profile) {


                Intent intent = new Intent(getApplicationContext(), Profile.class);
                startActivity(intent);
                return true;
            }

            ft.commit();
            return true;
        });










        /*logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, FirstActivity.class);
                mAuth.signOut();
                startActivity(intent);
                finish();

            }
        });*/


        addTaskBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Clicked add task");
                Intent intent = new Intent(getApplicationContext(), AddTask.class);
                intent.putExtra("userId", userId);
                startActivity(intent);
            }
        });



        /*getTasksFromDB();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                System.out.println("Calling from handler");
                getTasksFromDB();
                handler.postDelayed(this, delay);
            }
        }, delay);*/

    }


    /*

    void getTasksFromDB() {

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
                                            Intent intent = new Intent(getApplicationContext(), TaskAlarm.class);
                                            PendingIntent pendingIntent = PendingIntent.getBroadcast(
                                                    getApplicationContext(),
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
*/



}