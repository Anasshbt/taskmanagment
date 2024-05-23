package com.example.taskManagement;

import androidx.appcompat.app.AppCompatActivity;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class FirstActivity extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first);

        DocumentReference docRef = db.collection("isFingerprintActive").document("isFingerprintActive");




        ImageView imageView = findViewById(R.id.imageView);
        int gifResourceId = R.drawable.taskmanagment4;

        RequestOptions options = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.ALL);

        Glide.with(this)
                .asGif()
                .load(gifResourceId)
                .apply(options)
                .into(imageView);



        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                docRef.get().addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        boolean isFingerprintActive = documentSnapshot.getBoolean("isFingerprintActive");

                        if (isFingerprintActive) {
                            Intent intent = new Intent(FirstActivity.this, Empreinteview.class);
                            PendingIntent pendingIntent = PendingIntent.getActivity(FirstActivity.this, 0, intent, PendingIntent.FLAG_MUTABLE);
                            startActivity(intent);
                            finish();
                        } else {
                            Intent intent = new Intent(FirstActivity.this, MainActivity.class);
                            PendingIntent pendingIntent = PendingIntent.getActivity(FirstActivity.this, 0, intent, PendingIntent.FLAG_MUTABLE);
                            startActivity(intent);
                            finish();
                        }
                    }
                });



            }
        }, 2000);
    }
}