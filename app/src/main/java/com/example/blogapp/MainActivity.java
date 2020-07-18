package com.example.blogapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

public class MainActivity extends AppCompatActivity {
    Button b1,b2;
    EditText et1,et2;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        b1=findViewById(R.id.btn_login);
        b2=findViewById(R.id.btn_signup);
        et1=findViewById(R.id.username_login);
        et2=findViewById(R.id.password_login);
        mAuth = FirebaseAuth.getInstance();
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in= new Intent(MainActivity.this,signup.class);
                startActivity(in);

            }
        });
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email= et1.getText().toString().trim();
                String password = et2.getText().toString().trim();

                if(TextUtils.isEmpty(email)){
                    et1.setError("Email is required");
                    return;
                }

                if(TextUtils.isEmpty(password)){
                    et2.setError("Password is required");
                    return;
                }

                if(password.length() < 6){
                    et2.setError("Password must be greater than 6 characters");
                    return;
                }

                //Authenticate the user
                mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(MainActivity.this, "Logged In Successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),Dashboard.class));

                        }
                        else{
                            Toast.makeText(MainActivity.this, "Error Occured" + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}
