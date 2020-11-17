package com.example.app_client.Adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.app_client.View.CalendarFragment;

public class BookingPagerViewerAdapter extends FragmentStatePagerAdapter {

    public BookingPagerViewerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 1:
            case 2:
            case 0:
            default: return new CalendarFragment();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0: return "Scegli lo slot";
            case 1: return "scegli il Professore";
            case 2: return "Riepilogo";
            default: return "Error";
        }
    }
}
