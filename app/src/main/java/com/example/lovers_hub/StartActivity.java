package com.example.lovers_hub;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class StartActivity extends AppCompatActivity {
Button Create_new_Button;
TextView AlreadyHasAccount;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        Create_new_Button=findViewById(R.id.Create_new_Button);
        AlreadyHasAccount=findViewById(R.id.AlreadyHasAccount);

        Create_new_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent RegisterIntent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(RegisterIntent);




            }
        });
        AlreadyHasAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
Intent startintent=new Intent(getApplicationContext(),Login.class);
startActivity(startintent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        moveTaskToBack(true);
        finish();
    }


}
