package com.example.lovers_hub;

import android.app.ProgressDialog;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ActivityStatus extends AppCompatActivity implements View.OnClickListener {
ImageButton close_btn;
EditText Status_Et;
Button SaveStatus_btn;
String NewStatus;
DatabaseReference databaseReference;
FirebaseUser firebaseUser;
String CurrentStatus;
private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);
        Status_Et=findViewById(R.id.Status_Et);
        SaveStatus_btn=findViewById(R.id.SaveStatus_btn);
        SaveStatus_btn.setOnClickListener(this);
        close_btn=findViewById(R.id.close_btn);
        close_btn.setOnClickListener(this);
        progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Status Update");
        progressDialog.setMessage("Please Wait ....");
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
       // GetCurrentStatus();
Status_Et.setText(getIntent().getStringExtra("status"));



    }

    @Override
    public void onClick(View v) {
switch (v.getId()){

    case R.id.close_btn:
        Intent SettingIntent=new Intent(getApplicationContext(),SettingsActivity.class);
        startActivity(SettingIntent);
        break;
    case R.id.SaveStatus_btn:
        progressDialog.show();
        NewStatus = Status_Et.getText().toString().trim();

        SetNewStatus(NewStatus);

        break;



}
    }
    /*private  void GetCurrentStatus(){
      firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
      String CurrentUserId=firebaseUser.getUid();
      databaseReference=FirebaseDatabase.getInstance().getReference().child("Users").child(CurrentUserId);
      databaseReference.addValueEventListener(new ValueEventListener() {
          @Override
          public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
              CurrentStatus=dataSnapshot.child("status").getValue().toString();
              Status_Et.setText(CurrentStatus);



          }

          @Override
          public void onCancelled(@NonNull DatabaseError databaseError) {

          }
      });

    }*/
    private void SetNewStatus(String newStatus){

        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        String CurrentUserId=firebaseUser.getUid();
        databaseReference=FirebaseDatabase.getInstance().getReference().child("Users").child(CurrentUserId);
        databaseReference.child("status").setValue(newStatus).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    progressDialog.dismiss();
                    Intent SettingIntent =new Intent(getApplicationContext(),SettingsActivity.class);
                    startActivity(SettingIntent);


                }else {
                    Toast.makeText(getApplicationContext(),"Something Went Wrong ,Please try again !",Toast.LENGTH_LONG).show();
                }
            }
        });


    }



}
