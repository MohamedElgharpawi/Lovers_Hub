package com.example.lovers_hub;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerAdapter_LifecycleAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.NotNull;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class UsersActivity extends AppCompatActivity {
Toolbar toolbar;
    RecyclerView UsersRecycler;
    private DatabaseReference UsersDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);
        toolbar=findViewById(R.id.users_bar);
        UsersRecycler=findViewById(R.id.UsersRecycler);
        UsersDatabase= FirebaseDatabase.getInstance().getReference().child("Users");
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("All Users");
UsersRecycler.setHasFixedSize(true);
UsersRecycler.setLayoutManager(new LinearLayoutManager(this));



    }


    @Override
    protected void onStart() {
        super.onStart();
FirebaseRecyclerOptions<Users>options=new FirebaseRecyclerOptions.Builder<Users>()
        .setQuery(UsersDatabase, Users.class).build();
FirebaseRecyclerAdapter adapter=new FirebaseRecyclerAdapter<Users,UsersViewHolder>(options) {


    @Override
    protected void onBindViewHolder(@NonNull UsersViewHolder usersViewHolder, int i, @NonNull Users users) {
        usersViewHolder.setName(users.getName());
        usersViewHolder.setStatus(users.getStatus());

        usersViewHolder.setImg(users.getThumb_image());
        String User_Id=getRef(i).getKey();

        usersViewHolder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ProfileIntent=new Intent(getApplicationContext(),ProfileActivity.class);
                ProfileIntent.putExtra("User_Id",User_Id);
                startActivity(ProfileIntent);
            }
        });



    }



    @NonNull
    @Override
    public UsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.users_row,parent,false);
       return new UsersViewHolder(v);
    }

};

UsersRecycler.setAdapter(adapter);
adapter.startListening();

    }


    @Override
    protected void onStop() {
        super.onStop();
    }
    public static class UsersViewHolder extends RecyclerView.ViewHolder{
View view;

        public UsersViewHolder(@NonNull View itemView) {
            super(itemView);
            view=itemView;
        }

        public void setName(String name){
            TextView user_name=view.findViewById(R.id.user_name);
            user_name.setText(name);


        }

        public void setStatus(String status) {
            TextView user_stuts= view.findViewById(R.id.user_status);
            user_stuts.setText(status);
        }



            public void setImg(String thumb_image){
            CircleImageView user_img =view.findViewById(R.id.user_img);


            Picasso.get().load(thumb_image).placeholder(R.mipmap.avatar).into(user_img);}







    }







}
