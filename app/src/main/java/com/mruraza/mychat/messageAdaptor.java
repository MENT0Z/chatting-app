package com.mruraza.mychat;

import static com.mruraza.mychat.chat_window.receiverImg;
import static com.mruraza.mychat.chat_window.senderImg;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class messageAdaptor extends RecyclerView.Adapter{
                Context context;
                ArrayList<Message_model_class> messageAdapterArraylist;
                int itemsend=1;
                int itemreceive=2;



    public messageAdaptor(Context context, ArrayList<Message_model_class> messageAdapterArraylist) {
        this.context = context;
        this.messageAdapterArraylist = messageAdapterArraylist;
    }

    @NonNull
    @Override

    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if(viewType==itemsend)
        {
            View view = LayoutInflater.from(context).inflate(R.layout.sender_layout,parent,false);
            return new senderViewHolder(view);
        }
        else {
            View view = LayoutInflater.from(context).inflate(R.layout.receiver_layout,parent,false);
            return new receiverViewHolder(view);
        }
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message_model_class messaggee = messageAdapterArraylist.get(position);

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                new AlertDialog.Builder(context).setTitle("Delete")
                        .setMessage("Are you sure you want to delete this message?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }).show();

                return false;
            }
        });


        if (holder.getClass() == senderViewHolder.class) {
            senderViewHolder viewHolder = (senderViewHolder) holder;
            viewHolder.msgtxt.setText(messaggee.getMessage());
            Picasso.get().load(senderImg).into(viewHolder.circleImageView);
        } else {
            receiverViewHolder viewHolder = (receiverViewHolder) holder;
            viewHolder.msgtxt.setText(messaggee.getMessage());
            Picasso.get().load(receiverImg).into(viewHolder.circleImageView);
        }
    }



    @Override
    public int getItemCount() {
        return messageAdapterArraylist.size();
    }

    @Override
    public int getItemViewType(int position) {
        Message_model_class message = messageAdapterArraylist.get(position);
        if(FirebaseAuth.getInstance().getCurrentUser().getUid().equals(message.getSenderid())){
            return itemsend;
        }
        else {
            return itemreceive;
        }
    }


     class senderViewHolder extends RecyclerView.ViewHolder{
            CircleImageView circleImageView;
            TextView msgtxt;

        public senderViewHolder(@NonNull View itemView)
        {
            super(itemView);
            circleImageView = itemView.findViewById(R.id.profilerggg);
            msgtxt = itemView.findViewById(R.id.messageTextView);
        }
    }


    public class receiverViewHolder extends RecyclerView.ViewHolder {
        CircleImageView circleImageView;
        TextView msgtxt;
        public receiverViewHolder(@NonNull View itemView)
        {
            super(itemView);
            circleImageView = itemView.findViewById(R.id.pro);
            msgtxt = itemView.findViewById(R.id.messageTextView1);
        }
    }


}
