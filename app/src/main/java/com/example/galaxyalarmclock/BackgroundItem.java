package com.example.galaxyalarmclock;

public class BackgroundItem {

    private final String mBackgroundName;
    private final int mBackgroundImage;

    public BackgroundItem(String backgroundName, int backgroundImage) {
        mBackgroundName = backgroundName;
        mBackgroundImage = backgroundImage;
    }

    public String getBackgroundName() {
        return mBackgroundName;
    }

    public int getBackgroundImage() {
        return mBackgroundImage;
    }
}
