package com.mission.cricstat.Rest.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mission.cricstat.Common.StringUtil;

import java.util.ArrayList;

public class Team {
    @SerializedName("name")
    @Expose
    private String mName;

    private String mShortName;

    @SerializedName("squad")
    @Expose
    private ArrayList<Player> mSquad;

    public Team(String name, ArrayList<Player> squad) {
        mName = name;
        mSquad = squad;
    }

    public String getName() {
        return mName;
    }

    public ArrayList<Player> getSquad() {
        return mSquad;
    }

    public String getShortName() {
        return mShortName;
    }

    public void setShortName(String shortName) {
        mShortName = shortName;
    }
}
