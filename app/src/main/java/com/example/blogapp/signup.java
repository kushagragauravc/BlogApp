package com.example.blogapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class signup extends AppCompatActivity {
    public static final String TAG = "TAG";
    private FirebaseAuth mAuth;
    Button bs1;
    EditText et_reg_name,et_reg_email,et_reg_username,et_reg_password,et_reg_pno;
    FirebaseFirestore fStore;
    String userID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        bs1=findViewById(R.id.btn_register);
        et_reg_name=findViewById(R.id.name_signup);
        et_reg_email=findViewById(R.id.email_signup);
        et_reg_username=findViewById(R.id.username_signup);
        et_reg_password=findViewById(R.id.pass_signup);
        et_reg_pno=findViewById(R.id.mobile_signup);


        mAuth = FirebaseAuth.getInstance();
        fStore= FirebaseFirestore.getInstance();
        if(mAuth.getCurrentUser() !=null){
            startActivity(new Intent(getApplicationContext(),Dashboard.class));
            finish();
        }
        bs1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email= et_reg_email.getText().toString().trim();
                String password = et_reg_password.getText().toString().trim();
                final String fullName = et_reg_name.getText().toString().trim();
                final String username = et_reg_username.getText().toString().trim();
                final String phone = et_reg_pno.getText().toString().trim();

                if(TextUtils.isEmpty(email)){
                    et_reg_email.setError("Email is required");
                    return;
                }

                if(TextUtils.isEmpty(password)){
                    et_reg_password.setError("Password is required");
                    return;
                }

                if(password.length() < 6){
                    et_reg_password.setError("Password must be greater than 6 characters");
                    return;
                }

                mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(signup.this, "User Created", Toast.LENGTH_SHORT).show();
                            userID = mAuth.getCurrentUser().getUid();
                            DocumentReference documentReference = fStore.collection("users").document(userID);
                            Map<String,Object> user = new HashMap<>();
                            user.put("fullName",fullName);
                            user.put("email",email);
                            user.put("username",username);
                            user.put("phone",phone);
                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "onSuccess: user profile is created for " + userID);

                                }
                            });
                            startActivity(new Intent(getApplicationContext(),Dashboard.class));
                        }
                        else{
                            Toast.makeText(signup.this, "Error Occured", Toast.LENGTH_SHORT).show();

                        }
                    }
                });


            }
        });

    }
}
