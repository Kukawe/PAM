    package com.example.pam;

    import androidx.annotation.NonNull;
    import androidx.appcompat.app.AppCompatActivity;

    import android.content.Intent;
    import android.os.Bundle;
    import android.view.View;
    import android.widget.Button;
    import android.widget.EditText;
    import android.widget.TextView;
    import android.widget.Toast;

    import com.google.android.gms.tasks.OnCompleteListener;
    import com.google.android.gms.tasks.Task;
    import com.google.firebase.auth.AuthResult;
    import com.google.firebase.auth.FirebaseAuth;
    import com.google.firebase.auth.FirebaseUser;

    public class LoginActivity extends AppCompatActivity {
        EditText emailId, password;
        Button btnSignIn;
        TextView tvSignUp;
        FirebaseAuth mFirebaseAuth;
        private FirebaseAuth.AuthStateListener mAuthStateListener;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_login);

            mFirebaseAuth = FirebaseAuth.getInstance();
            emailId = findViewById(R.id.editText_mailCO);
            password = findViewById(R.id.editText2_mdpCO);
            btnSignIn = findViewById(R.id.btnConnexion);
            tvSignUp = findViewById(R.id.textView_pasencorecompte);

            mAuthStateListener = new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
                    if( mFirebaseUser != null ){
                        Toast.makeText(LoginActivity.this,"Vous êtes connecté",Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(LoginActivity.this, HomeActivity.class);
                        startActivity(i);
                    }
                    else{
                        Toast.makeText(LoginActivity.this,"Connectez-vous",Toast.LENGTH_SHORT).show();
                    }
                }
            };

            btnSignIn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String email = emailId.getText().toString();
                    String pwd = password.getText().toString();
                    if(email.isEmpty()){
                        emailId.setError("Veillez entrer une adresse mail valide");
                        emailId.requestFocus();
                    }
                    else  if(pwd.isEmpty()){
                        password.setError("Veuillez entrer un mot de passe valide");
                        password.requestFocus();
                    }
                    else  if(email.isEmpty() && pwd.isEmpty()){
                        Toast.makeText(LoginActivity.this,"Les champs sont vides",Toast.LENGTH_SHORT).show();
                    }
                    else  if(!(email.isEmpty() && pwd.isEmpty())){
                        mFirebaseAuth.signInWithEmailAndPassword(email, pwd).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(!task.isSuccessful()){
                                    Toast.makeText(LoginActivity.this,"Une erreur est surevenue, réessayé",Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    Intent intToHome = new Intent(LoginActivity.this,HomeActivity.class);;
                                    startActivity(intToHome);

                                }
                            }
                        });
                    }
                    else{
                        Toast.makeText(LoginActivity.this,"Une erreur est survenue!",Toast.LENGTH_SHORT).show();

                    }

                }
            });

            tvSignUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intSignUp = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intSignUp);
                }
            });
        }

        @Override
        protected void onStart() {
            super.onStart();
            mFirebaseAuth.addAuthStateListener(mAuthStateListener);
        }
    }