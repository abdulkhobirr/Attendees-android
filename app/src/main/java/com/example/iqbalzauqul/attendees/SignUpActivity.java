package com.example.iqbalzauqul.attendees;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignUpActivity extends AppCompatActivity {

    EditText namaField;
    EditText usernameField;
    EditText emailField;
    EditText passwordField;
    Button signUp;
    ProgressDialog progressDialog;

    FirebaseAuth mAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        namaField = findViewById(R.id.nama_txt);
        usernameField = findViewById(R.id.username_txt);
        signUp = findViewById(R.id.signUpButtonRegister);
        emailField = findViewById(R.id.email_txt);
        passwordField = findViewById(R.id.password_txt);

        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }
        });


    }

    private void register() {


        final String email = emailField.getText().toString();
        String password = passwordField.getText().toString();
        final String nama = namaField.getText().toString();
        final String username = usernameField.getText().toString();

        if (TextUtils.isEmpty(email) && TextUtils.isEmpty(password) && TextUtils.isEmpty(nama) && TextUtils.isEmpty(username)) {

            Toast.makeText(SignUpActivity.this, "Harap isi data yang lengkap", Toast.LENGTH_LONG).show();


        } else {

            final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
            databaseReference.child("users").child(username).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        Toast.makeText(SignUpActivity.this, "Username tidak tersedia.", Toast.LENGTH_LONG).show();
                        return;
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            progressDialog.setMessage("Singning up...");
            progressDialog.show();


            Task task = mAuth.createUserWithEmailAndPassword(email, password);
            task.addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()) {
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setDisplayName(nama).build();


                        if (user != null) {
                            user.updateProfile(profileUpdates)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (!task.isSuccessful()) {
                                                Toast.makeText(SignUpActivity.this, "Something's wrong, please try again.", Toast.LENGTH_LONG).show();

                                            }
                                        }
                                    });
                        }
                        String uid = user.getUid();
                        DatabaseReference userRef = databaseReference.child("users").child(uid);
                        userRef.child("username").setValue(username);
                        userRef.child("nama").setValue(nama);
                        userRef.child("email").setValue(email);
                        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                        intent.putExtra("signup", true);
                        startActivity(intent);
                        progressDialog.dismiss();
                    }


                }



            });
            task.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    String error = e.getMessage();
                    Toast.makeText(SignUpActivity.this, "Terjadi Kesalahan: " + error, Toast.LENGTH_LONG).show();
                }
            });

        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }




}
