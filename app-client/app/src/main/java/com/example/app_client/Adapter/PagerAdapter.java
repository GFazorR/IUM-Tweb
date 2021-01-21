package com.example.app_client.Adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.app_client.Utils.LoginManager;
import com.example.app_client.View.SubjectFragment;
import com.example.app_client.View.DashBoardFragment;

public class PagerAdapter extends FragmentStatePagerAdapter {

    private boolean isLoggedIn;


    public PagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        isLoggedIn = LoginManager.isLoggedIn();
    }


    @NonNull
    @Override
    public Fragment getItem(int position) {
        return position == 0 ? new SubjectFragment() : new DashBoardFragment();
    }



    @Override
    public int getCount() { return isLoggedIn ? 2 : 1; }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return position == 0 ? "Area Utente" : "Prenota";
    }
}
