package com.example.pam;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class HomeActivity extends AppCompatActivity {
    Button btnLogout;
    Button btnMedecin;
    FirebaseAuth mFirebaseAuth;
    RecyclerView mFirestoreList;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth.AuthStateListener mAuthStateListener;
    String uid;
    FirestoreRecyclerAdapter adapter;
    String nom;
    String prenom;
    int SEND_SMS_PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        btnMedecin = findViewById(R.id.btnMedecin);
        btnLogout = findViewById(R.id.btn_deconnexion);
        mFirestoreList = findViewById(R.id.firestoreList);
        firebaseFirestore = FirebaseFirestore.getInstance();
        uid = FirebaseAuth.getInstance().getUid();
        //Query
        Query query = firebaseFirestore.collection("Users").document(uid).collection("data");

        //SMS
        btnMedecin.setEnabled(false);
        if (checkPermission(Manifest.permission.SEND_SMS)){
            btnMedecin.setEnabled(true);
        }else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, SEND_SMS_PERMISSION_REQUEST_CODE);
        }

        //RecyclerOptions
        FirestoreRecyclerOptions<Data> options = new FirestoreRecyclerOptions.Builder<Data>()
                .setQuery(query, Data.class)
                .build();

        //Read informations client
        DocumentReference user = firebaseFirestore.collection("Users").document(uid);
        user.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                             @Override
                                             public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                 if (task.isSuccessful()) {
                                                     DocumentSnapshot doc = task.getResult();
                                                     StringBuilder fields = new StringBuilder("");
                                                     nom = (String) doc.get("firstName");
                                                     prenom = (String) doc.get("lastName");
                                                 }
                                             }
                                         });


                adapter = new FirestoreRecyclerAdapter<Data, ProductsViewHolder>(options) {
                    @NonNull
                    @Override
                    public ProductsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_data_single, parent, false);
                        return new ProductsViewHolder(view);
                    }

                    @Override
                    protected void onBindViewHolder(@NonNull ProductsViewHolder holder, int position, @NonNull Data model) {
                        holder.list_name.setText(model.getName());
                        holder.list_value.setText(model.getLastValue() + "");
                    }
                };

        mFirestoreList.setHasFixedSize(true);
        mFirestoreList.setLayoutManager(new LinearLayoutManager(this));
        mFirestoreList.setAdapter(adapter);




        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFirebaseAuth.getInstance().signOut();
                Intent inToMain = new Intent(HomeActivity.this, LoginActivity.class);
                startActivity(inToMain);
            }
        });


        }

    public void onSend(View view) {
        if (checkPermission(Manifest.permission.SEND_SMS)){
            SmsManager smg = SmsManager.getDefault();
            smg.sendTextMessage("0610287782", null,"Le patient "+ nom + prenom + " a besoin d'aide!", null,null);
            Toast.makeText(this, "Message envoyé!", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this, "Permission refusée!", Toast.LENGTH_SHORT).show();
        }
    }


    private class ProductsViewHolder extends RecyclerView.ViewHolder {
        private TextView list_name;
        private TextView list_value;

        public ProductsViewHolder(@NonNull View itemView) {
            super(itemView);

            list_name = itemView.findViewById(R.id.list_name);
            list_value = itemView.findViewById(R.id.list_lastValue);
        }

    }

    public boolean checkPermission(String permission){
        int check = ContextCompat.checkSelfPermission(this,permission);
        return (check == PackageManager.PERMISSION_GRANTED);
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }
}

