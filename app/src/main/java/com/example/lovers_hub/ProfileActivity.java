package com.example.lovers_hub;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.annotation.StyleableRes;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.icu.text.DateFormat;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Date;

public class ProfileActivity extends AppCompatActivity {

    private ImageView profileImage;
    private TextView profileUserName,profileStatus,profileFriendCount;
    private Button SendFriendRequest,CancleFriendRequest;
    DatabaseReference UsersDB;
    DatabaseReference FriendRequestDataBase , FriendsDatabase;
    FirebaseUser CurrentUser;
    String Name,Status,Img;
    private ProgressDialog progressDialog;
    private String CurrenrState;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        String User_Id=getIntent().getStringExtra("User_Id");
        profileImage=findViewById(R.id.profileImage);
        profileUserName=findViewById(R.id.profileUserName);
        profileStatus=findViewById(R.id.profileStatus);
        profileFriendCount=findViewById(R.id.profileFriendCount);
        SendFriendRequest=findViewById(R.id.SendFriendRequest);
        CancleFriendRequest=findViewById(R.id.CancleFriendRequest);
        UsersDB= FirebaseDatabase.getInstance().getReference().child("Users").child(User_Id);
        FriendRequestDataBase=FirebaseDatabase.getInstance().getReference().child("Friend_req");
        FriendsDatabase=FirebaseDatabase.getInstance().getReference().child("Friends");
        CurrentUser= FirebaseAuth.getInstance().getCurrentUser();
        CurrenrState="not_friend";



        progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Loading The Profile");
        progressDialog.setMessage("Please,wait while Loading your profile....");
        progressDialog.setCancelable(false);
        progressDialog.show();

        UsersDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

           Name=dataSnapshot.child("name").getValue().toString();
           Status=dataSnapshot.child("status").getValue().toString();
           Img=dataSnapshot.child("img").getValue().toString();
           profileUserName.setText(Name);
           profileStatus.setText(Status);
                Picasso.get().load(Img).placeholder(R.mipmap.avatar).into(profileImage);
// Request related code
                FriendRequestDataBase.child(CurrentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                       if(dataSnapshot.hasChild(User_Id)){

                           String requset_status=dataSnapshot.child(User_Id).child("request_type").getValue().toString();

                           if(requset_status.equals("received")){
                               CurrenrState="Request_received";
                               SendFriendRequest.setText("Accept Friend Request");
                               SendFriendRequest.setBackgroundColor(getResources().getColor(R.color.green));

                           }
                           else if(requset_status.equals("sent")){
                               CurrenrState="Request_Sent";
                               SendFriendRequest.setText("Cancel Friend Request");
                               SendFriendRequest.setBackgroundColor(getResources().getColor(R.color.red));

                           }


                       }
                       else{
                           FriendsDatabase.child(CurrentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                               @Override
                               public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                   if(dataSnapshot.hasChild(User_Id)){
                                       CurrenrState="friends";
                                       SendFriendRequest.setText("Un friend");



                                   }
                               }

                               @Override
                               public void onCancelled(@NonNull DatabaseError databaseError) {

                               }
                           });

                       }



                        progressDialog.dismiss();



                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });




            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

SendFriendRequest.setOnClickListener(new View.OnClickListener() {
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onClick(View v) {

        SendFriendRequest.setEnabled(false);
        //if the status is that the friend request is not sent !!

        if (CurrenrState.equals("not_friend")){
            FriendRequestDataBase.child(CurrentUser.getUid()).child(User_Id).child("request_type").setValue("sent").addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if(task.isSuccessful()){

                        CurrenrState="Request_Sent";
                        SendFriendRequest.setText("Cancel Friend Request");
                        SendFriendRequest.setBackgroundColor(getResources().getColor(R.color.red));

                        FriendRequestDataBase.child(User_Id).child(CurrentUser.getUid()).child("request_type").setValue("received").addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getApplicationContext(),"Friend Request Sent ,Successfully" ,Toast.LENGTH_LONG).show();


                            }
                        });

                    }else {

                        Toast.makeText(getApplicationContext(),"Failed To send Request",Toast.LENGTH_LONG).show();


                    }
                    SendFriendRequest.setEnabled(true);

                }
            });


        }
//if the status is that the friend request is already sent !!

        if(CurrenrState.equals("Request_Sent")){

            FriendRequestDataBase.child(CurrentUser.getUid()).child(User_Id).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
    @Override
    public void onSuccess(Void aVoid) {

         FriendRequestDataBase.child(User_Id).child(CurrentUser.getUid()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
       @Override
       public void onSuccess(Void aVoid) {

           SendFriendRequest.setEnabled(true);
           CurrenrState="not_friend";
           SendFriendRequest.setText("Send Friend Request");
           SendFriendRequest.setBackgroundColor(getResources().getColor(R.color.white));
           Toast.makeText(getApplicationContext(),"Friend Request is Cancelled !",Toast.LENGTH_LONG).show();




       }
   });


    }
});


        }

if (CurrenrState.equals("Request_received")){
    String currentDate= DateFormat.getDateTimeInstance().format(new Date());
    FriendsDatabase.child(CurrentUser.getUid()).child(User_Id).setValue(currentDate).addOnSuccessListener(new OnSuccessListener<Void>() {
        @Override
        public void onSuccess(Void aVoid) {
            FriendsDatabase.child(User_Id).child(CurrentUser.getUid()).setValue(currentDate).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    FriendRequestDataBase.child(CurrentUser.getUid()).child(User_Id).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            FriendRequestDataBase.child(User_Id).child(CurrentUser.getUid()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                    SendFriendRequest.setEnabled(true);
                                    CurrenrState="friends";
                                    SendFriendRequest.setText("Un friend ");
                                }
                            });
                        }
                    });
                }
            });

        }
    });


}
if (CurrenrState.equals("friends")){

    FriendsDatabase.child(CurrentUser.getUid()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
        @Override
        public void onSuccess(Void aVoid) {
       FriendsDatabase.child(User_Id).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
           @Override
           public void onSuccess(Void aVoid) {

               CurrenrState="not_friend";
               SendFriendRequest.setText("");


           }
       });
        }
    });











}


    }
});
    }
}
