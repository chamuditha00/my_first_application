package com.example.hegraholdings;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class MainActivity extends AppCompatActivity {
    EditText musername,
            mpassword;
    ProgressBar progressBar;
    FirebaseAuth fAuth;
    Button msignin;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        musername = findViewById(R.id.username);
        mpassword = findViewById(R.id.password);
        progressBar = findViewById(R.id.progressBar);
        fAuth = FirebaseAuth.getInstance();
        msignin = findViewById(R.id.signin);

        msignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = musername.getText().toString().trim();
                String password = mpassword.getText().toString().trim();

                if (TextUtils.isEmpty(email)){
                    musername.setError("username is required");
                    return;
                }
                if (TextUtils.isEmpty(password)){
                    mpassword.setError("password is required");
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);

                fAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(MainActivity.this,"login is successful",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),uploadimage.class));
                        }else {
                            Toast.makeText(MainActivity.this,"Error\n" + task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
            }

        });

    }

            }




