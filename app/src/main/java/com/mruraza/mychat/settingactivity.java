package com.mruraza.mychat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.net.URI;

public class settingactivity extends AppCompatActivity {

    Button done_button;
    EditText namebar,statusbar;
    ImageView setprofilepic;
    FirebaseDatabase database;
    FirebaseAuth auth;
    FirebaseStorage storage;
    Uri setprofileuri;
    String email,passwordd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settingactivity);
        getSupportActionBar().hide();

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();

        done_button = findViewById(R.id.save_setting);
        namebar = findViewById(R.id.name_setting);
        statusbar = findViewById(R.id.status_setting);
        setprofilepic = findViewById(R.id.choose_photo_setting);


        Dialog loadingdialog;
        loadingdialog = new Dialog(settingactivity.this);
        loadingdialog.setContentView(R.layout.progressbar);
        loadingdialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.roundedbtn));
        loadingdialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        loadingdialog.setCancelable(false);

        DatabaseReference reference = database.getReference().child("User").child(auth.getUid());
        StorageReference storageReference = storage.getReference().child("Upload").child(auth.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                email = snapshot.child("emailadd").getValue().toString();
                String namee = snapshot.child("userName").getValue().toString();
                 passwordd = snapshot.child("passone").getValue().toString();;
                String Status = snapshot.child("status").getValue().toString();
                String profile = snapshot.child("profilepic").getValue().toString();

                namebar.setText(namee);
                statusbar.setText(Status);
                Picasso.get().load(profile).into(setprofilepic);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(settingactivity.this, error.getMessage().toString().trim(), Toast.LENGTH_SHORT).show();
            }
        });
        setprofilepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 101);
            }
        });

        done_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = namebar.getText().toString();
                String statusssss = statusbar.getText().toString();
                if(setprofileuri!=null){
                    storageReference.putFile(setprofileuri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String finalimageuri = uri.toString();
                                    User user = new User(finalimageuri,name,email,passwordd,auth.getUid(),statusssss);
                                    reference.setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){

                                                loadingdialog.show();
                                                Toast.makeText(settingactivity.this, "Tapai ko data Sucessfully Save vayo", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(settingactivity.this,MainActivity.class);
                                                startActivity(intent);
                                                finish();
                                            }else {
                                                Toast.makeText(settingactivity.this, "Kehi Kura Milena", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

                                }
                            });
                        }
                    });
                }else {
                        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String finaluri = uri.toString();
                                User user = new User(finaluri,name,email,passwordd,auth.getUid(),statusssss);

                                reference.setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            loadingdialog.show();
                                            Toast.makeText(settingactivity.this, "Tapai ko data Sucessfully Save vayo", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(settingactivity.this,MainActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }else {
                                            Toast.makeText(settingactivity.this, "Kehi Kura Milena", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });


                            }
                        });

                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 101){
            if(data!=null){
               setprofileuri = data.getData();
               setprofilepic.setImageURI(setprofileuri);

            }
        }
    }
}