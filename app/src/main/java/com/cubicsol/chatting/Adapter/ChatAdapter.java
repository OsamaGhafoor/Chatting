package com.cubicsol.chatting.Adapter;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cubicsol.chatting.Models.MessageModel;
import com.cubicsol.chatting.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ChatAdapter extends RecyclerView.Adapter{


    ArrayList<MessageModel> messageModels;
    Context context;
    String receiverId;

    int Sender_View_Type = 1;

    int Receiver_View_Type = 2;

    public ChatAdapter(ArrayList<MessageModel> messageModels, Context context) {
        this.messageModels = messageModels;
        this.context = context;
    }

    public ChatAdapter(ArrayList<MessageModel> messageModels, Context context, String receiverId) {
        this.messageModels = messageModels;
        this.context = context;
        this.receiverId = receiverId;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

      if (viewType ==Sender_View_Type)
      {
          View view = LayoutInflater.from(context).inflate(R.layout.sender_chat_resource,parent,false);

          return  new SenderViewHolder(view);
      }else {

          View view = LayoutInflater.from(context).inflate(R.layout.receiver_chat_resource,parent,false);

          return  new RecieverViewHolder(view);
      }
    }

    @Override
    public int getItemViewType(int position) {

        if (messageModels.get(position).getuId().equals(FirebaseAuth.getInstance().getUid()))
        {
            return Sender_View_Type;

        }else {

            return Receiver_View_Type;
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        MessageModel messageModel = messageModels.get(position);

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                new AlertDialog.Builder(context)
                        .setTitle("Delete")
                        .setMessage("Are you sure you want to delete this message?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                String senderRoom = FirebaseAuth.getInstance().getUid()+receiverId;
                                database.getReference().child("chats").child(senderRoom)
                                        .setValue(null);

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


        if (holder.getClass() == SenderViewHolder.class)
        {

            ((SenderViewHolder) holder).senderMsg.setText(messageModel.getMessage());

            Date date = new Date(messageModel.getTimeStamp());
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("h:mm:a");
            String strDate = simpleDateFormat.format(date);

            ((SenderViewHolder) holder).senderTime.setText(strDate);

        }else {

            ((RecieverViewHolder) holder).recieverMsg.setText(messageModel.getMessage());

            Date date = new Date(messageModel.getTimeStamp());
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("h:mm:a");
            String strDate = simpleDateFormat.format(date);
            ((RecieverViewHolder) holder).recieverTime.setText(strDate);
        }

    }

    @Override
    public int getItemCount() {
        return messageModels.size();
    }

    public class RecieverViewHolder extends RecyclerView.ViewHolder{

        TextView recieverMsg,recieverTime;

        public RecieverViewHolder(@NonNull View itemView) {
            super(itemView);

            recieverMsg = itemView.findViewById(R.id.receiverText);
            recieverTime = itemView.findViewById(R.id.receiverTime);

        }
    }


    public class SenderViewHolder extends RecyclerView.ViewHolder{

        TextView senderMsg,senderTime;

        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);

            senderMsg = itemView.findViewById(R.id.senderText);
            senderTime = itemView.findViewById(R.id.senderTime);
        }
    }
}
