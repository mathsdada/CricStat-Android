package com.mission.cricstat.Rest.Model.TeamStats;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class InningsScore {
    @SerializedName("inningsNumber")
    @Expose
    private int mInningsNumber;

    @SerializedName("battingTeam")
    @Expose
    private String mBattingTeam;

    @SerializedName("runs")
    @Expose
    private int mRuns;

    @SerializedName("wickets")
    @Expose
    private int mWickets;

    @SerializedName("balls")
    @Expose
    private int mBalls;

    public InningsScore(int inningsNumber, String battingTeam, int runs, int wickets, int balls) {
        mInningsNumber = inningsNumber;
        mBattingTeam = battingTeam;
        mRuns = runs;
        mWickets = wickets;
        mBalls = balls;
    }

    public int getInningsNumber() {
        return mInningsNumber;
    }

    public String getBattingTeam() {
        return mBattingTeam;
    }

    public int getRuns() {
        return mRuns;
    }

    public int getWickets() {
        return mWickets;
    }

    public int getBalls() {
        return mBalls;
    }
}
