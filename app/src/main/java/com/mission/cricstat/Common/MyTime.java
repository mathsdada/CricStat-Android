package com.mission.cricstat.Common;

public class MyTime {
    private int mHours;
    private int mMinutes;
    private int mSeconds;

    public MyTime(Long seconds) {
        mHours = (int) (seconds/3600);
        int hoursRem = (int) (seconds - mHours*3600);
        mMinutes = hoursRem/60;
        mSeconds = hoursRem - mMinutes*60;
    }

    public void countDown() {
        if (mSeconds == 0) {
            if (mMinutes != 0) {
                mMinutes -= 1;
                mSeconds = 59;
            }
            else {
                if (mHours != 0) {
                    mHours -= 1;
                    mMinutes = 59;
                    mSeconds = 59;
                }
            }
        } else {
            mSeconds -= 1;
        }
    }

    @Override
    public String toString() {
        String output = "";
        if (mHours <= 9) output = "0";
        output += mHours + ":";
        if (mMinutes <= 9) output += "0";
        output += mMinutes + ":";
        if (mSeconds <=9) output += "0";
        output += mSeconds;
        return output;
    }

    public int getHours() {
        return mHours;
    }

    public int getMinutes() {
        return mMinutes;
    }

    public int getSeconds() {
        return mSeconds;
    }
}
