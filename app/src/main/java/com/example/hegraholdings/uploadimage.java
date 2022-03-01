package com.example.hegraholdings;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class uploadimage extends AppCompatActivity {
    //initialize variable
    private StorageReference mStroageref;
    ImageView imageView;
    Button button;
    private FirebaseAuth authprofile;
    private StorageReference storageReference;
    private FirebaseUser firebaseUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uploadimage);
        //assign variable
        mStroageref = FirebaseStorage.getInstance().getReference();
        imageView = findViewById(R.id.imageView);
        button = findViewById(R.id.button);
        authprofile = FirebaseAuth.getInstance();
        firebaseUser = authprofile.getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference("meter_readings");





        if(ContextCompat.checkSelfPermission(uploadimage.this,
                Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(uploadimage.this,
                    new String[]{Manifest.permission.CAMERA} ,101);
        }


        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 101);

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();

            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
            byte bb[] = bytes.toByteArray();
            imageView.setImageBitmap(bitmap);

            uploadToFirebase(bb);
        }
    }

    private void uploadToFirebase(byte[] bb) {
        String time = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss").format(new Date());

        String imagename = time + "JPEG" ;
        StorageReference sr = mStroageref.child(authprofile.getCurrentUser().getEmail() + "__" + imagename);
        sr.putBytes(bb).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(uploadimage.this, "successful", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(uploadimage.this,"unsuccessful",Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
        finish();
    }
}