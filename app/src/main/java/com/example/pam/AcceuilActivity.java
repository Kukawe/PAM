package com.example.pam;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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
    }
}
