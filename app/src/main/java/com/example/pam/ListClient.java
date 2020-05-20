package com.example.pam;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.List;

public class ListClient extends AppCompatActivity {
    Button btnLogout;
    RecyclerView mFirestoreList;
    FirebaseFirestore firebaseFirestore;
    FirestoreRecyclerAdapter adapter;
    String nom;
    String prenom;
    String uid;
    List patient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_client);
        btnLogout = findViewById(R.id.btn_deconnexionClient);
        mFirestoreList = findViewById(R.id.firestoreListClient);
        firebaseFirestore = FirebaseFirestore.getInstance();
        uid = FirebaseAuth.getInstance().getUid();

        //Read informations client
        DocumentReference user = firebaseFirestore.collection("Users").document(uid);
        user.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot doc = task.getResult();
                    assert doc != null;
                    patient = (List) doc.get("patientList");
                }
            }
        });

        Query query = firebaseFirestore.collection("Users").document(uid).collection("patientList");

        //RecyclerOptions
        FirestoreRecyclerOptions<Client> options = new FirestoreRecyclerOptions.Builder<Client>()
                .setQuery(query, Client.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<Client, ListClient.ProductsViewHolder>(options) {
            @NonNull
            @Override
            public ListClient.ProductsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_client_single, parent, false);
                return new ListClient.ProductsViewHolder(view);
            }

            @SuppressLint("SetTextI18n")
            @Override
            protected void onBindViewHolder(@NonNull ListClient.ProductsViewHolder holder, int position, @NonNull Client model) {
                holder.list_nom.setText(model.getNom());
                holder.list_prenom.setText(model.getPrenom() + "");
            }
        };




        mFirestoreList.setHasFixedSize(true);
        mFirestoreList.setLayoutManager(new LinearLayoutManager(this));
        mFirestoreList.setAdapter(adapter);
    }
    private static class ProductsViewHolder extends RecyclerView.ViewHolder {
        private TextView list_nom;
        private TextView list_prenom;

        ProductsViewHolder(@NonNull View itemView) {
            super(itemView);

            list_nom = itemView.findViewById(R.id.list_nom);
            list_prenom = itemView.findViewById(R.id.list_prenom);
        }

    }
}
