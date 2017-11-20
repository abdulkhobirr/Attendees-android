package com.example.iqbalzauqul.attendees;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUp extends AppCompatActivity {

    EditText nama;
    EditText username;
    EditText emailField;
    EditText passwordField;
    Button signUp;

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

            Toast.makeText(SignUp.this, "Harap isi email dan password", Toast.LENGTH_LONG).show();


        } else {

            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (!task.isSuccessful()) {

                        Toast.makeText(SignUp.this, "Register gagal", Toast.LENGTH_LONG).show();
                    } else {

                        Intent intent = new Intent(SignUp.this, LoginActivity.class);
                        intent.putExtra("signup", true);
                        startActivity(intent);

                    }


                }
            });

        }


    }




}
