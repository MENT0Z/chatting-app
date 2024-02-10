package com.mruraza.mychat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.ktx.Firebase;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {


    FirebaseAuth auth;
    RecyclerView id_for_recyclerview;

    UserAdaptor adaptor;
    FirebaseDatabase database;
    ArrayList<User> userArrayList ;
    ImageView btn_for_logout,settingimg,cam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();


        settingimg = findViewById(R.id.id_of_setting);
        cam = findViewById(R.id.id_of_camera_button);

        btn_for_logout = findViewById(R.id.id_of_logout);
        btn_for_logout.setOnClickListener(v -> {
            Dialog dialog = new Dialog(MainActivity.this,R.style.dialogue);
            dialog.setContentView(R.layout.dialogue_layout);
                Button yes,no;
                yes = dialog.findViewById(R.id.button_yes);
                no = dialog.findViewById(R.id.button_no);
                yes.setOnClickListener(v1 -> {
                    FirebaseAuth.getInstance().signOut();
                    Intent intent = new Intent(MainActivity.this,login.class);
                    startActivity(intent);
                    finish();
                });
                no.setOnClickListener(v12 ->
                        dialog.dismiss());
                dialog.show();
        });



        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        DatabaseReference reference = database.getReference().child("User");
     //   DatabaseReference reference = database.getReference().child("User");
        userArrayList = new ArrayList<>();
        id_for_recyclerview = findViewById(R.id.id_for_recyclerview);
        id_for_recyclerview.setLayoutManager(new LinearLayoutManager(this));
        adaptor = new UserAdaptor(MainActivity.this, userArrayList); // Initialize the adapter here

        id_for_recyclerview.setAdapter(adaptor); // Set the adapter after initializing and setting the data

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    User users = dataSnapshot.getValue(User.class);
                    userArrayList.add(users);
                }
                adaptor.notifyDataSetChanged(); // Notify the adapter after updating the list
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error if needed
                Toast.makeText(MainActivity.this, error.getMessage().toString().trim(), Toast.LENGTH_SHORT).show();
            }
        });


        if (auth.getCurrentUser() == null) {
            Intent intent = new Intent(MainActivity.this, login.class);
            startActivity(intent);
        }


        settingimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,settingactivity.class);
                startActivity(intent);
            }
        });

        cam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,10);
            }
        });

    }

}