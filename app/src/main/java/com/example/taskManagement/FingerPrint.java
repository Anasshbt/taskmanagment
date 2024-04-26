package com.example.taskManagement;
import android.content.Context;
import android.hardware.biometrics.BiometricPrompt;
import android.os.Build;
import android.os.CancellationSignal;
import android.widget.Toast;
import androidx.annotation.RequiresApi;
import java.util.concurrent.Executor;

public class FingerPrint {

    @RequiresApi(api = Build.VERSION_CODES.P)
    public static void authenticate(Context context, Executor executor, BiometricPrompt.AuthenticationCallback callback) {
        BiometricPrompt biometricPrompt = new BiometricPrompt.Builder(context)
                .setTitle("Authentification par empreinte digitale")
                .setNegativeButton("Annuler", executor, (dialogInterface, i) -> {
                    // L'utilisateur a annulé l'authentification
                }).build();

        CancellationSignal cancellationSignal = new CancellationSignal();

        biometricPrompt.authenticate(cancellationSignal, executor, callback);
    }
}

