package com.example.pam;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    EditText emailId, password, birthDay, nom, prenom, phone, status;
    Button btnSignUp;
    TextView tvSignIn;
    FirebaseAuth mFirebaseAuth;
    FirebaseFirestore mFirestore;
    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFirestore = FirebaseFirestore.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();
        emailId = findViewById(R.id.editText_mailINS);
        password = findViewById(R.id.editText_mdpINS);
        birthDay = findViewById(R.id.editTextBirth);
        nom = findViewById(R.id.editTextNom);
        prenom = findViewById(R.id.editTextPrenom);
        phone = findViewById(R.id.editTextTel);
        status = findViewById(R.id.editTextStatus);
        btnSignUp = findViewById(R.id.btnInscription);
        tvSignIn = findViewById(R.id.textView_dejacompte);


        btnSignUp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final String email = emailId.getText().toString();
                String pwd = password.getText().toString();
                final String birth = birthDay.getText().toString();
                final String nm = nom.getText().toString();
                final String prnm = prenom.getText().toString();
                final String tel = phone.getText().toString();
                final String sts = status.getText().toString();

                if (email.isEmpty()) {

                    emailId.setError("Veuillez entrer votre adresse mail");
                    emailId.requestFocus();

                } else if (pwd.isEmpty()) {

                    password.setError("Veuillez entrer votre mot de passe");
                    password.requestFocus();

                } else if (birth.isEmpty()) {

                    birthDay.setError("Veuillez entrer votre date d'anniversaire");
                    birthDay.requestFocus();

                } else if (nm.isEmpty()) {

                    nom.setError("Veuillez entrer votre nom");
                    nom.requestFocus();
                } else if (prnm.isEmpty()) {

                    prenom.setError("Veuillez entrer votre prénom");
                    prenom.requestFocus();
                } else if (tel.isEmpty()) {

                    phone.setError("Veuillez entrer votre téléphone");
                    phone.requestFocus();

                } else if (sts.isEmpty()) {

                    status.setError("Veuillez entrer votre status");
                    status.requestFocus();

                } else if (email.isEmpty() && pwd.isEmpty() && pwd.isEmpty() && birth.isEmpty() && nm.isEmpty() && prnm.isEmpty() && tel.isEmpty() && sts.isEmpty()) {

                    Toast.makeText(MainActivity.this, "Les champs sont vides", Toast.LENGTH_SHORT).show();
                } else if (!(email.isEmpty() && pwd.isEmpty() && pwd.isEmpty() && birth.isEmpty() && nm.isEmpty() && prnm.isEmpty() && tel.isEmpty() && sts.isEmpty())) {
                    mFirebaseAuth.createUserWithEmailAndPassword(email, pwd).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(MainActivity.this, "Email ou mot de passe incorrect, ressayez", Toast.LENGTH_SHORT).show();
                            } else {
                                uid = mFirebaseAuth.getCurrentUser().getUid();
                                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                                intent.putExtra("uid", uid);
                                startActivity(intent);
                                Map<String, String> userMap = new HashMap<>();
                                userMap.put("email", email);
                                userMap.put("lastName", nm);
                                userMap.put("firstName", prnm);
                                userMap.put("phoneNumber", tel);
                                userMap.put("status", sts);
                                userMap.put("birthDate", birth);

                                mFirestore.collection("Users").document(uid).set(userMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                        Toast.makeText(MainActivity.this, "Compte crée!", Toast.LENGTH_SHORT).show();

                                    }

                                }
                                );
                            }
                        }
                    });
                }
                else{
                    Toast.makeText(MainActivity.this, "Une erreur est survenue!",Toast.LENGTH_SHORT).show();
                }
            }
        });

        tvSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new  Intent(MainActivity.this,LoginActivity.class);
                startActivity(i);

            }
        });


    }
}
