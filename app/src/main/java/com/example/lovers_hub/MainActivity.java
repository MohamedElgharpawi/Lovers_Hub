package com.example.lovers_hub;

import android.content.Intent;

import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private Toolbar mtoolbar;
    private ViewPager mviewPager;
    private mPagerAdapter mPagerAdapter ;
TabLayout MainTabs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mtoolbar=findViewById(R.id.MainPageToolbar);
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setTitle("Lovers Hub");
        mAuth = FirebaseAuth.getInstance();

        mviewPager=findViewById(R.id.MainViewPager);
mPagerAdapter=new mPagerAdapter(getSupportFragmentManager());
mviewPager.setAdapter(mPagerAdapter);
MainTabs=findViewById(R.id.MainTabs);
MainTabs.setupWithViewPager(mviewPager);

    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser==null){
            Intent StartIntent=new Intent(this, StartActivity.class);
            startActivity(StartIntent);
            finish();


        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

                super.onCreateOptionsMenu(menu);
getMenuInflater().inflate(R.menu.mainmenu,menu);



        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
         super.onOptionsItemSelected(item);
         if (item.getItemId()==R.id.logout_option){

             FirebaseAuth.getInstance().signOut();
             Intent startintent=new Intent(getApplicationContext(),StartActivity.class);
             startActivity(startintent);



         }

        if (item.getItemId()==R.id.main_settings_btn){


            Intent settingintent=new Intent(getApplicationContext(),SettingsActivity.class);
            startActivity(settingintent);



        }
        if (item.getItemId()==R.id.main_all_btn){


            Intent settingintent=new Intent(getApplicationContext(),UsersActivity.class);
            startActivity(settingintent);



        }




        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        moveTaskToBack(true);
        finish();





    }
}
