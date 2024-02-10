package com.mruraza.mychat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class login extends AppCompatActivity {
TextView txtview_for_signup;
EditText emaill,passwordd;
Button loginbuttton;
FirebaseAuth auth;

String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

android.app.ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Ek xin Kurnush \n Kati Hatar garya");
        progressDialog.setCancelable(false);
        getSupportActionBar().hide();
        txtview_for_signup = findViewById(R.id.id_for_signup);
        emaill = findViewById(R.id.email_enter);
        passwordd = findViewById(R.id.password_enter);
        loginbuttton = findViewById(R.id.button_for_login);
        auth=FirebaseAuth.getInstance();
        txtview_for_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(login.this,sign_up_registration.class);
                startActivity(intent);
            }
        });

loginbuttton.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        String email = emaill.getText().toString();
        String pass = passwordd.getText().toString();

        if (TextUtils.isEmpty(email)) {
            progressDialog.dismiss();
            Toast.makeText(login.this, "Please Enter Email address", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(pass)) {
            progressDialog.dismiss();
            Toast.makeText(login.this, "Please Enter password", Toast.LENGTH_SHORT).show();
        } else if (!email.matches(emailPattern)) {
            progressDialog.dismiss();
            emaill.setError("Enter valid Email");
            Toast.makeText(login.this, "Please Enter Valid Email", Toast.LENGTH_SHORT).show();
        } else if (pass.length() < 6) {
            progressDialog.dismiss();
            passwordd.setError("Password must be greater than 6 characters");
            Toast.makeText(login.this, "Please Enter Password Greater than 6 characters", Toast.LENGTH_SHORT).show();
        }
        else {

        auth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                    progressDialog.show();
                    try {
                        Intent intent = new Intent(login.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } catch (Exception e) {
                        Toast.makeText(login.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(login.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
    }
});
    }
}