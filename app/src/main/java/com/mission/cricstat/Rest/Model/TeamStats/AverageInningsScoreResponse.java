package com.mission.cricstat.Rest.Model.TeamStats;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class AverageInningsScoreResponse {
    @SerializedName("inningsNum")
    @Expose
    private int mInningsNum;

    @SerializedName("numInnings")
    @Expose
    private int mInnings;

    @SerializedName("runs")
    @Expose
    private int mRuns;

    @SerializedName("wickets")
    @Expose
    private int mWickets;

    @SerializedName("winPercentage")
    @Expose
    private int mWinPercentage;

    public int getInningsNum() {
        return mInningsNum;
    }

    public int getInnings() {
        return mInnings;
    }

    public int getRuns() {
        return mRuns;
    }

    public int getWickets() {
        return mWickets;
    }

    public int getWinPercentage() {
        return mWinPercentage;
    }

    public static ArrayList<Object> convertToObjectArray(ArrayList<AverageInningsScoreResponse> responses) {
        ArrayList<Object> objects = new ArrayList<>();
        objects.addAll(responses);
        return objects;
    }
}
