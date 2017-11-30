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

public class SignUpActivity extends AppCompatActivity {

    EditText nama;
    EditText username;
    EditText emailField;
    EditText passwordField;
    Button signUp;
    ProgressDialog progressDialog;

    FirebaseAuth mAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        nama = findViewById(R.id.nama_txt);
        username = findViewById(R.id.username_txt);
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


        String email = emailField.getText().toString();
        String password = passwordField.getText().toString();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {

            Toast.makeText(SignUpActivity.this, "Harap isi email dan password", Toast.LENGTH_LONG).show();


        } else {
            progressDialog.setMessage("Singning up...");
            progressDialog.show();


            Task task = mAuth.createUserWithEmailAndPassword(email, password);
            task.addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    progressDialog.dismiss();
                    if (task.isSuccessful()) {
                        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                        intent.putExtra("signup", true);
                        startActivity(intent);
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
