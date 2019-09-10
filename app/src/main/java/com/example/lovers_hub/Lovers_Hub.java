package com.example.lovers_hub;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

public class Lovers_Hub extends Application {

    @Override
    public void onCreate() {
        super.onCreate();


        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
