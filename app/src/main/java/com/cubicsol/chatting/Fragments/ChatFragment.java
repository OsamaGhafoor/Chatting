package com.cubicsol.chatting.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.cubicsol.chatting.Adapter.UserAdapter;
import com.cubicsol.chatting.Models.User;
import com.cubicsol.chatting.R;
import com.cubicsol.chatting.databinding.FragmentChatBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class ChatFragment extends Fragment {



    public ChatFragment() {
        // Required empty public constructor
    }

   FragmentChatBinding binding;
    ArrayList<User> list = new ArrayList<>();
    FirebaseDatabase firebaseDatabase;

    DatabaseReference databaseReference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentChatBinding.inflate(inflater,container,false);
        firebaseDatabase = FirebaseDatabase.getInstance();

        UserAdapter adapter = new UserAdapter(list,getContext());

        binding.chatRecyclerView.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        binding.chatRecyclerView.setLayoutManager(layoutManager);

        firebaseDatabase.getReference().child("Users").addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {

               list.clear();
               for (DataSnapshot dataSnapshot :snapshot.getChildren()){
                   User users = dataSnapshot.getValue(User.class);
                   users.setUserId(dataSnapshot.getKey());
                   if (!users.getUserId().equals(FirebaseAuth.getInstance().getUid()))
                   {
                       list.add(users);
                   }


               }

               adapter.notifyDataSetChanged();
           }

           @Override
           public void onCancelled(@NonNull DatabaseError error) {

           }
       });

        // Inflate the layout for this fragment
        return binding.getRoot();
    }
}