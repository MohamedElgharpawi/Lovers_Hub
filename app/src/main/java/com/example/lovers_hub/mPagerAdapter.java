package com.example.lovers_hub;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class mPagerAdapter extends FragmentPagerAdapter {

    public mPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
      switch (i){
          case 0:
              return new RequestFragment();


          case 1:
              return new ChatsFragment();

          case 2:
              return new FriendsFragment();


              default:
                  return new RequestFragment();



      }
    }

    @Override
    public int getCount() {
        return 3;
    }
    public CharSequence getPageTitle(int i){

        switch (i){
            case 0:
                return "Requests";


            case 1:
                return "Chats";

            case 2:
                return "Friends";


            default:
                return null;



        }



    }
}
