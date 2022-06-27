package com.example.galaxyalarmclock.ui.main;

import android.content.Context;
import android.widget.Switch;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.galaxyalarmclock.R;
import com.example.galaxyalarmclock.TabFragment1;
import com.example.galaxyalarmclock.TabFragment2;
import com.example.galaxyalarmclock.TabFragment3;

public class SectionsPagerAdapter extends FragmentPagerAdapter {

    private final Context mContext;

    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new TabFragment1();
                break;
            case 1:
                fragment = new TabFragment2();
                break;
            case 2:
                fragment = new TabFragment3();
                break;
        }
        return fragment;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Alarm";
            case 1:
                return "Stopwatch";
            case 2:
                return "Timer";
        }
        return null;
    }

    @Override
    public int getCount() {
        // Show 3 total pages.
        return 3;
    }
}