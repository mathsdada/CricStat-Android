package com.mission.cricstat.Rest.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Player {
    @SerializedName("name")
    @Expose
    private String mName;

    @SerializedName("role")
    @Expose
    private String mRole;

    @SerializedName("batting_style")
    @Expose
    private String mBattingStyle;

    @SerializedName("bowling_style")
    @Expose
    private String mBowlingStyle;

    public Player(String name, String role, String battingStyle, String bowlingStyle) {
        mName = name;
        mRole = role;
        mBattingStyle = battingStyle;
        mBowlingStyle = bowlingStyle;
    }

    public String getName() {
        return mName;
    }

    public String getRole() {
        return mRole;
    }

    public String getBattingStyle() {
        return mBattingStyle;
    }

    public String getBowlingStyle() {
        return mBowlingStyle;
    }
}
