package com.example.taskManagement;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    EditText emailInput;
    EditText passwordInput;
    Button loginBtn;

    EditText emailRInput;
    EditText passwordRInput;
    Button registerBtn;

    Button createAccBtn;

    LinearLayout authView;
    LinearLayout testView;
    LinearLayout registerView;

    private FirebaseAuth mAuth;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        loginBtn = findViewById(R.id.loginBtn);

        emailRInput = findViewById(R.id.emailRInput);
        passwordRInput = findViewById(R.id.passwordRInput);
        registerBtn = findViewById(R.id.registerBtn);

        createAccBtn = findViewById(R.id.createAccBtn);

        authView = findViewById(R.id.authView);
        testView = findViewById(R.id.testView);
        registerView = findViewById(R.id.registerView);

        loginBtn.setOnClickListener(this);
        registerBtn.setOnClickListener(this);
        createAccBtn.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    void updateUI(FirebaseUser currentUser) {
        if (currentUser != null) {

            testView.setVisibility(View.GONE);
            authView.setVisibility(View.GONE);
            registerView.setVisibility(View.GONE);
            Intent intent = new Intent(this, HomeActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity.this, 0, intent, PendingIntent.FLAG_MUTABLE);
            intent.putExtra("userId", currentUser.getUid());
            startActivity(intent);
        } else {
            testView.setVisibility(View.GONE);
            registerView.setVisibility(View.GONE);
            authView.setVisibility(View.VISIBLE);
        }
    }

    void loginClick() {
        String email = emailInput.getText().toString();
        String password = passwordInput.getText().toString();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }

    void registerClick() {
        registerView.setVisibility(View.VISIBLE);
        authView.setVisibility(View.GONE);
        testView.setVisibility(View.GONE);
    }


    void createAccClick() {
        String email = emailRInput.getText().toString();
        String password = passwordRInput.getText().toString();
        Log.d("CreatingAcc", "Creating Acc with: " + email + ":" + password);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Failure", "Failed to create user");
                        e.printStackTrace();
                    }
                });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.loginBtn) {
            loginClick();
        }
        else if (v.getId() == R.id.registerBtn) {
            registerClick();
        }
        else if (v.getId() == R.id.createAccBtn) {
            createAccClick();
        }
    }
}