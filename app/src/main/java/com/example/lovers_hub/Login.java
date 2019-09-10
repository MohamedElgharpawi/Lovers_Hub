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

public class Login extends AppCompatActivity implements View.OnClickListener {
EditText Login_email,Login_password;
Button Login_btn;
ProgressDialog progressDialog;
 private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Login_email=findViewById(R.id.Login_email);
        Login_password=findViewById(R.id.Login_password);
        Login_btn=findViewById(R.id.Login_btn);
        mAuth = FirebaseAuth.getInstance();
        progressDialog=new ProgressDialog(this);
        Login_btn.setOnClickListener(this);
    }

    public void login(String Email,String Password){


        if(Email.isEmpty()){
            Login_email.setError("Email is required!");
            Login_email.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(Email).matches()){

            Login_email.setError("Enter a valid Email ");
            Login_email.requestFocus();
            return;

        }
        if(Password.isEmpty()){
            Login_password.setError("password is required!");
            Login_password.requestFocus();
            return;
        }
        if(Password.length()<6){

            Login_password.setError("Password must be more than 6 letters");
            Login_password.requestFocus();
            return;


        }

        progressDialog.setTitle("Logging in ..!");
        progressDialog.setMessage("Please Wait .....");
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(true);
        progressDialog.show();
        mAuth.signInWithEmailAndPassword(Email, Password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent mainintent =new Intent(getApplicationContext(),MainActivity.class);
                            startActivity(mainintent);
                            progressDialog.dismiss();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(getApplicationContext(), "Authentication failed."+" "+task.getException().getMessage(),
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

    @Override
    public void onClick(View v) {
switch (v.getId()){
    case R.id.Login_btn:
        String email = Login_email.getText().toString().trim();
        String password =Login_password.getText().toString().trim();
        login(email,password);
        break;




}
    }
}
