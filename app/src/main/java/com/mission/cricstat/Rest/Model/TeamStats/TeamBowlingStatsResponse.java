package com.mission.cricstat.Rest.Model.TeamStats;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;
import java.util.ArrayList;

public class TeamBowlingStatsResponse {
    @SerializedName("bowler")
    @Expose
    private final String mBowler;

    @SerializedName("innings")
    @Expose
    private final int mNumInnings;

    @SerializedName("runs")
    @Expose
    private final int mRuns;

    @SerializedName("balls")
    @Expose
    private final int mBalls;

    @SerializedName("average")
    @Expose
    private final BigDecimal mAverage;

    @SerializedName("strikeRate")
    private final BigDecimal mStrikeRate;

    @SerializedName("maidens")
    @Expose
    private final int mMaidens;

    @SerializedName("wickets")
    @Expose
    private final int mWickets;

    @SerializedName("economy")
    @Expose
    private final BigDecimal mEconomy;

    @SerializedName("aboveFourWickets")
    @Expose
    private final int mFourPlusWickets;

    @SerializedName("aboveFiveWickets")
    @Expose
    private final int mFivePlusWickets;

    public TeamBowlingStatsResponse(String bowler, int numInnings, int runs, int balls, BigDecimal average, BigDecimal strikeRate, int maidens, int wickets, BigDecimal economy, int fourPlusWickets, int fivePlusWickets) {
        mBowler = bowler;
        mNumInnings = numInnings;
        mRuns = runs;
        mBalls = balls;
        mAverage = average;
        mStrikeRate = strikeRate;
        mMaidens = maidens;
        mWickets = wickets;
        mEconomy = economy;
        mFourPlusWickets = fourPlusWickets;
        mFivePlusWickets = fivePlusWickets;
    }

    public String getBowler() {
        return mBowler;
    }

    public int getNumInnings() {
        return mNumInnings;
    }

    public int getRuns() {
        return mRuns;
    }

    public int getBalls() {
        return mBalls;
    }

    public BigDecimal getAverage() {
        return mAverage;
    }

    public BigDecimal getStrikeRate() {
        return mStrikeRate;
    }

    public int getMaidens() {
        return mMaidens;
    }

    public int getWickets() {
        return mWickets;
    }

    public BigDecimal getEconomy() {
        return mEconomy;
    }

    public int getFourPlusWickets() {
        return mFourPlusWickets;
    }

    public int getFivePlusWickets() {
        return mFivePlusWickets;
    }

    public static ArrayList<Object> convertToObjectArray(ArrayList<TeamBowlingStatsResponse> responses) {
        ArrayList<Object> objects = new ArrayList<>();
        objects.addAll(responses);
        return objects;
    }
}
