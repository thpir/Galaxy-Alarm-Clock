package com.example.galaxyalarmclock;
/**
 * The adapter-layout manager pattern lets you provide different screens of content within an Activity:
 *   - Use an adapter to fill the content screen to show in the Activity.
 *   - Use a layout manager that changes the content screens depending on which tab is selected.
 */

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public PagerAdapter(FragmentManager fragmentManager, int NumOfTabs) {
        super(fragmentManager);
        this.mNumOfTabs = NumOfTabs;
    }

    // Return the Fragment associated with a specified position
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: return new TabFragment1();
            case 1: return new TabFragment2();
            case 2: return new TabFragment3();
            default: return null;
        }
    }

    // Return the number of views available
    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
