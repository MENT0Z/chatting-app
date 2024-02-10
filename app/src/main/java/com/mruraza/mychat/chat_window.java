package com.mruraza.mychat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class chat_window extends AppCompatActivity {
    String reciverimage,receivername,senderUID,receiverUID;
    CircleImageView circlerview_profile;
    TextView recname;
    EditText typemessage;
    CardView sendbutton;
    FirebaseAuth auth;
    FirebaseDatabase database;

    public  static String senderImg;
    public  static String receiverImg;
    String senderRoom,ReceiverRoom;
    RecyclerView chattingwindowofrecyclerview;

    ArrayList<Message_model_class>arraylistformessage;
    messageAdaptor mmessageAdaptor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);
        getSupportActionBar().hide();

        auth = FirebaseAuth.getInstance();
        database =FirebaseDatabase.getInstance();

        receivername = getIntent().getStringExtra("nameeeeeee");
        reciverimage=getIntent().getStringExtra("receiverimageee");
        receiverUID=getIntent().getStringExtra("uidddd");

        typemessage = findViewById(R.id.typingspace);
        sendbutton = findViewById(R.id.sendbuttonid);
        circlerview_profile= findViewById(R.id.profile_picture_of_receiver);
        recname = findViewById(R.id.receivername);
        arraylistformessage = new ArrayList<>();

        chattingwindowofrecyclerview = findViewById(R.id.recycler_view_chat_win);



        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        chattingwindowofrecyclerview.setLayoutManager(linearLayoutManager);
        mmessageAdaptor = new messageAdaptor(chat_window.this,arraylistformessage);
        chattingwindowofrecyclerview.setAdapter(mmessageAdaptor);



        Picasso.get().load(reciverimage).into(circlerview_profile);
        recname.setText(receivername);
        senderUID = auth.getUid();

        senderRoom = senderUID + receiverUID;
        ReceiverRoom = receiverUID + senderUID;




        DatabaseReference reference = database.getReference().child("User").child(auth.getUid());
        DatabaseReference chatreference = database.getReference().child("chats").child(senderRoom).child("message");


        chatreference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arraylistformessage.clear();
                    for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                        Message_model_class messagggg = dataSnapshot.getValue(Message_model_class.class);
                        arraylistformessage.add(messagggg);

                    }
                mmessageAdaptor.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


         reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                senderImg = snapshot.child("profilepic").getValue().toString();
                receiverImg=reciverimage;

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        sendbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = typemessage.getText().toString();

                if(message.isEmpty()){
                    Toast.makeText(chat_window.this, "Pailea message type garnush!", Toast.LENGTH_SHORT).show();
                    return;
                }
                typemessage.setText("");
                Date date = new Date();
                Message_model_class messageee = new Message_model_class(message,senderUID,date.getTime());
                database = FirebaseDatabase.getInstance();
                database.getReference().child("chats").child(senderRoom).child("message").push().setValue(messageee).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        database.getReference().child("chats").child(ReceiverRoom).child("message").push().setValue(messageee).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                            }
                        });
                    }
                });
            }
        });

    }
}