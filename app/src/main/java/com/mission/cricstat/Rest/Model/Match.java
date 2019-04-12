package com.mission.cricstat.Rest.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Match {
    @SerializedName("title")
    @Expose
    private String mTitle;

    @SerializedName("format")
    @Expose
    private String mFormat;

    @SerializedName("venue")
    @Expose
    private String mVenue;

    @SerializedName("date")
    private Long mDateTime;

    @SerializedName("teams")
    @Expose
    private ArrayList<Team> mTeams;

    @SerializedName("series")
    @Expose
    private String mSeries;

    public Match(String title, String format, String venue, Long dateTime, ArrayList<Team> teams, String series) {
        mTitle = title;
        mFormat = format;
        mVenue = venue;
        mDateTime = dateTime;
        mTeams = teams;
        mSeries = series;
    }

    public Match copy() {
        return new Match(mTitle, mFormat, mVenue, mDateTime, mTeams, mSeries);
    }

    public String getTitle() {
        return mTitle;
    }

    public String getFormat() {
        return mFormat;
    }

    public String getVenue() {
        return mVenue;
    }

    public Long getDateTime() {
        return mDateTime;
    }

    public ArrayList<Team> getTeams() {
        return mTeams;
    }

    public String getSeries() {
        return mSeries;
    }
}
