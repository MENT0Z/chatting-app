package com.mruraza.mychat;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdaptor extends RecyclerView.Adapter<UserAdaptor.viewholder> {
    Context mainActivity;
    ArrayList<User> userArrayList;
    public UserAdaptor(MainActivity mainActivity, ArrayList<User> userArrayList) {
        this.mainActivity = mainActivity;
        this.userArrayList = userArrayList;
    }

    @NonNull
    @Override
    public UserAdaptor.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mainActivity).inflate(R.layout.user_resource_items,parent,false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdaptor.viewholder holder, int position) {
        User users = userArrayList.get(position);

        if(FirebaseAuth.getInstance().getCurrentUser().getUid().equals(users.getUserId()))
        {
            holder.itemView.setVisibility(View.GONE);
            holder.itemView.getLayoutParams().height = 0;
        }

        holder.user_name.setText(users.userName);//changed from getUsername()
        holder.user_status.setText(users.status);//changed from getStatus()
        Picasso.get().load(users.profilepic).into(holder.profile_pic);//changed to getprofilepuc

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mainActivity,chat_window.class);
                intent.putExtra("nameeeeeee",users.getUserName());
                intent.putExtra("receiverimageee",users.getProfilepic());
                intent.putExtra("uidddd",users.getUserId());
                mainActivity.startActivity(intent);
            }
        });



    }


    @Override
    public int getItemCount()
     {
        return userArrayList.size();
    }

    public static class viewholder extends RecyclerView.ViewHolder {
        CircleImageView profile_pic;
        TextView user_name,user_status;
        public viewholder(@NonNull View itemView) {
            super(itemView);

            profile_pic = itemView.findViewById(R.id.profile_picc);
            user_name = itemView.findViewById(R.id.user_nameec);
            user_status = itemView.findViewById(R.id.user_statusc);
        }
    }
}
