package com.example.pam;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

public class AcceuilActivity extends AppCompatActivity {

    Button btn_singIn;
    TextView txt_singUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acceuil);

        btn_singIn = findViewById(R.id.btn_coHome);
        txt_singUp = findViewById(R.id.textView_pasencorecompteHome);

        txt_singUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intSignUp = new Intent(AcceuilActivity.this, MainActivity.class);
                startActivity(intSignUp);
            }
        });

        btn_singIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intSignUp = new Intent(AcceuilActivity.this, LoginActivity.class);
                startActivity(intSignUp);
            }
        });

        //Notifications

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel =
                    new NotificationChannel("Notifications", "Notifications", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            assert manager != null;
            manager.createNotificationChannel(channel);
        }
        FirebaseMessaging.getInstance().subscribeToTopic("general")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "Ca marche";
                        if (!task.isSuccessful()) {
                            msg = "Echec";
                        }
                        Toast.makeText(AcceuilActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
