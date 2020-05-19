package com.example.pam;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class HomeActivity extends AppCompatActivity {
    Button btnLogout;
    FirebaseAuth mFirebaseAuth;
    private RecyclerView mFirestoreList;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    String uid;
    private FirestoreRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        btnLogout = findViewById(R.id.btn_deconnexion);
        mFirestoreList = findViewById(R.id.firestoreList);
        firebaseFirestore = FirebaseFirestore.getInstance();
        uid = FirebaseAuth.getInstance().getUid();
        Log.v("caca", "uid"+ uid);
        //Query
        Query query = firebaseFirestore.collection("Users").document(uid).collection("data");

        //RecyclerOptions
        FirestoreRecyclerOptions<Data> options = new FirestoreRecyclerOptions.Builder<Data>()
                .setQuery(query, Data.class)
                .build();

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


    private class ProductsViewHolder extends RecyclerView.ViewHolder {
        private TextView list_name;
        private TextView list_value;

        public ProductsViewHolder(@NonNull View itemView) {
            super(itemView);

            list_name = itemView.findViewById(R.id.list_name);
            list_value = itemView.findViewById(R.id.list_lastValue);
        }

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

