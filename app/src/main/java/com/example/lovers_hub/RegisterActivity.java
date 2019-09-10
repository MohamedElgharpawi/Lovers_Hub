package com.example.lovers_hub;

import android.app.ProgressDialog;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
EditText Register_name,Register_email,Register_password;
Button CreateAccount_btn;
ProgressDialog progressDialog;
private FirebaseAuth mAuth;
private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    Register_name=findViewById(R.id.Register_name);
    Register_email=findViewById(R.id.Register_email);
    Register_password=findViewById(R.id.Register_password);
    CreateAccount_btn=findViewById(R.id.CreateAccount_btn);
    CreateAccount_btn.setOnClickListener(this);
    progressDialog=new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case  R.id.CreateAccount_btn:
                String name=Register_name.getText().toString().trim();
                String email=Register_email.getText().toString().trim();
                String password=Register_password.getText().toString().trim();

                SignUp(name,email,password);

           break;





        }
    }


    private  void SignUp(final String Name, String Email, String Password){

        if(Name.isEmpty()){
            Register_name.setError("name is required!");
            Register_name.requestFocus();
            return;
        }
        if(Email.isEmpty()){
            Register_email.setError("Email is required!");
            Register_email.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(Email).matches()){

            Register_email.setError("Enter a valid Email ");
            Register_email.requestFocus();
            return;

        }
        if(Password.isEmpty()){
            Register_password.setError("password is required!");
            Register_password.requestFocus();
            return;
        }
        if(Password.length()<6){

            Register_password.setError("Password must be more than 6 letters");
            Register_password.requestFocus();
            return;


        }

        progressDialog.setTitle("Signing Up !");
        progressDialog.setMessage("Please Wait .....");
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(true);
progressDialog.show();
        mAuth.createUserWithEmailAndPassword(Email, Password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                          FirebaseUser current_user=FirebaseAuth.getInstance().getCurrentUser();
                          String UserId=current_user.getUid();
                          mDatabase =FirebaseDatabase.getInstance().getReference().child("Users").child(UserId);
                            HashMap<String,String> userMap=new HashMap<>();
                            userMap.put("name",Name);
                            userMap.put("status","Hey,i am using Lovers Hub");
                            userMap.put("img","default");
                            userMap.put("thumb_image","default");
                            mDatabase.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){

                                        progressDialog.dismiss();
                                        Intent mainintent=new Intent(getApplicationContext(),MainActivity.class);
                                        startActivity(mainintent);
                                        finish();

                                    }
                                }
                            });



                        } else {
                            Toast.makeText(getApplicationContext(), "Authentication failed"+"->"+task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();

                        }

                        // ...
                    }
                });







    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent StartIntent=new Intent(this, StartActivity.class);
        startActivity(StartIntent);
        finish();
    }
}



