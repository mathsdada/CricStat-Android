package com.mission.cricstat.Rest.Model.TeamStats;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;
import java.util.ArrayList;

public class TeamBattingStatsResponse {
    @SerializedName("batsman")
    @Expose
    private final String mBatsman;

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

    @SerializedName("highScore")
    @Expose
    private final int mHighScore;

    @SerializedName("fours")
    @Expose
    private final int mFours;

    @SerializedName("sixes")
    @Expose
    private final int mSixes;

    @SerializedName("ducks")
    @Expose
    private final int mDucks;

    @SerializedName("fifties")
    @Expose
    private final int mFifties;

    @SerializedName("hundreds")
    @Expose
    private final int mHundreds;

    @SerializedName("notOuts")
    @Expose
    private final int mNotOuts;

    public TeamBattingStatsResponse(String batsman, int numInnings, int runs, int balls, BigDecimal average, BigDecimal strikeRate, int highScore, int fours, int sixes, int ducks, int fifties, int hundreds, int notOuts) {
        mBatsman = batsman;
        mNumInnings = numInnings;
        mRuns = runs;
        mBalls = balls;
        mAverage = average;
        mStrikeRate = strikeRate;
        mHighScore = highScore;
        mFours = fours;
        mSixes = sixes;
        mDucks = ducks;
        mFifties = fifties;
        mHundreds = hundreds;
        mNotOuts = notOuts;
    }

    public String getBatsman() {
        return mBatsman;
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

    public int getHighScore() {
        return mHighScore;
    }

    public int getFours() {
        return mFours;
    }

    public int getSixes() {
        return mSixes;
    }

    public int getDucks() {
        return mDucks;
    }

    public int getFifties() {
        return mFifties;
    }

    public int getHundreds() {
        return mHundreds;
    }

    public int getNotOuts() {
        return mNotOuts;
    }

    public static ArrayList<Object> convertToObjectArray(ArrayList<TeamBattingStatsResponse> responses) {
        ArrayList<Object> objects = new ArrayList<>();
        objects.addAll(responses);
        return objects;
    }
}
