package com.mission.cricstat.Rest.Model.TeamStats;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class MatchScore {
    @SerializedName("title")
    @Expose
    private String mTitle;

    @SerializedName("outcome")
    @Expose
    private String mOutcome;

    @SerializedName("format")
    @Expose
    private String mFormat;

    @SerializedName("inningsScores")
    @Expose
    private ArrayList<InningsScore> mInningsScores;

    @SerializedName("date")
    @Expose
    private Long mDateEpoch;

    @SerializedName("status")
    @Expose
    private String mStatus;

    public String getOutcome() {
        return mOutcome;
    }

    public ArrayList<InningsScore> getInningsScores() {
        return mInningsScores;
    }

    public Long getDateEpoch() {
        return mDateEpoch;
    }

    public String getStatus() {
        return mStatus;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getFormat() {
        return mFormat;
    }
}
