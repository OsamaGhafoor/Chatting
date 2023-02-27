package com.cubicsol.chatting;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.cubicsol.chatting.Models.User;
import com.cubicsol.chatting.databinding.ActivitySettingsBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class SettingsActivity extends AppCompatActivity {

    ActivitySettingsBinding binding;

    FirebaseAuth mAuth;

    FirebaseDatabase firebaseDatabase;

    FirebaseStorage firebaseStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseStorage = FirebaseStorage.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();

        getSupportActionBar().hide();

        binding.backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(SettingsActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });

        binding.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,25);

            }
        });

        binding.saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!binding.txtUserAbout.getText().toString().equals("") && !binding.txtUserName.getText().toString().equals(""))
                {

                    String status = binding.txtUserAbout.getText().toString();
                    String userName = binding.txtUserName.getText().toString();

                    HashMap<String,Object> obj = new HashMap<>();
                    obj.put("userName",userName);
                    obj.put("status",status);

                    firebaseDatabase.getReference().child("Users").child(FirebaseAuth.getInstance().getUid())
                            .updateChildren(obj);

                    Toast.makeText(SettingsActivity.this, "Profile Updated", Toast.LENGTH_SHORT).show();

                }else {

                    Toast.makeText(SettingsActivity.this, "Please Enter Username and Status", Toast.LENGTH_SHORT).show();

                }


            }
        });

        binding.txtView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(SettingsActivity.this, "Privacy Policy is clicked", Toast.LENGTH_SHORT).show();
            }
        });

        binding.txtView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(SettingsActivity.this, "About Us is clicked", Toast.LENGTH_SHORT).show();
            }
        });

        binding.txtView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(SettingsActivity.this, "Invite a Friend is clicked", Toast.LENGTH_SHORT).show();
            }
        });

        binding.txtView5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(SettingsActivity.this, "Notification is clicked", Toast.LENGTH_SHORT).show();
            }
        });

        binding.txtView6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(SettingsActivity.this, "Help is clicked", Toast.LENGTH_SHORT).show();
            }
        });


        firebaseDatabase.getReference().child("Users").child(FirebaseAuth.getInstance().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        User user = snapshot.getValue(User.class);

                        Picasso.get()
                                .load(user.getProfilePic())
                                .placeholder(R.drawable.avatar)
                                .into(binding.profileImage);

                        binding.txtUserAbout.setText(user.getStatus());
                        binding.txtUserName.setText(user.getUserName());

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);




            Uri file = data.getData();
            binding.profileImage.setImageURI(file);


            final StorageReference storageReference = firebaseStorage.getReference().child("profile_pic")
                    .child(FirebaseAuth.getInstance().getUid());

            storageReference.putFile(file).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                 storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                     @Override
                     public void onSuccess(Uri uri) {

                         firebaseDatabase.getReference().child("Users").child(FirebaseAuth.getInstance().getUid())
                                 .child("profilePic").setValue(uri.toString());

                     }
                 });

                }
            });
        }
    }
