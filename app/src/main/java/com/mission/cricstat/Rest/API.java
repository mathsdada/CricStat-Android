package com.mission.cricstat.Rest;

import com.mission.cricstat.Rest.Model.Match;
import com.mission.cricstat.Rest.Model.TeamStats.MatchScore;
import com.mission.cricstat.Rest.Model.TeamStats.TeamBattingStatsResponse;
import com.mission.cricstat.Rest.Model.TeamStats.TeamBowlingStatsResponse;

import java.util.ArrayList;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public interface API {
    @GET("/schedule")
    Call<ArrayList<Match>> getSchedule();

    @GET("/team_stats/recent_matches")
    Call<ArrayList<MatchScore>> getTeamRecentMatches(@QueryMap Map<String, String> params);

    @GET("/team_stats/batting/most_runs")
    Call<ArrayList<TeamBattingStatsResponse>> getTeamBattingMostRuns(@QueryMap Map<String, String> params);

    @GET("/team_stats/batting/highest_average")
    Call<ArrayList<TeamBattingStatsResponse>> getTeamBattingBestAverage(@QueryMap Map<String, String> params);

    @GET("/team_stats/batting/highest_strike_rate")
    Call<ArrayList<TeamBattingStatsResponse>> getTeamBattingBestStrikeRate(@QueryMap Map<String, String> params);

    @GET("/team_stats/batting/most_fours")
    Call<ArrayList<TeamBattingStatsResponse>> getTeamBattingMostFours(@QueryMap Map<String, String> params);

    @GET("/team_stats/batting/most_sixes")
    Call<ArrayList<TeamBattingStatsResponse>> getTeamBattingMostSixes(@QueryMap Map<String, String> params);

    @GET("/team_stats/batting/most_fifties")
    Call<ArrayList<TeamBattingStatsResponse>> getTeamBattingMostFifties(@QueryMap Map<String, String> params);

    @GET("/team_stats/batting/most_hundreds")
    Call<ArrayList<TeamBattingStatsResponse>> getTeamBattingMostHundreds(@QueryMap Map<String, String> params);

    @GET("/team_stats/batting/most_ducks")
    Call<ArrayList<TeamBattingStatsResponse>> getTeamBattingMostDucks(@QueryMap Map<String, String> params);

    @GET("/team_stats/bowling/most_wickets")
    Call<ArrayList<TeamBowlingStatsResponse>> getTeamBowlingMostWickets(@QueryMap Map<String, String> params);

    @GET("/team_stats/bowling/most_maidens")
    Call<ArrayList<TeamBowlingStatsResponse>> getTeamBowlingMostMaidens(@QueryMap Map<String, String> params);

    @GET("/team_stats/bowling/most_four_plus_wkts")
    Call<ArrayList<TeamBowlingStatsResponse>> getTeamBowlingMostFourPlusWkts(@QueryMap Map<String, String> params);

    @GET("/team_stats/bowling/most_five_plus_wkts")
    Call<ArrayList<TeamBowlingStatsResponse>> getTeamBowlingMostFivePlusWkts(@QueryMap Map<String, String> params);

    @GET("/team_stats/bowling/best_average")
    Call<ArrayList<TeamBowlingStatsResponse>> getTeamBowlingBestAverage(@QueryMap Map<String, String> params);

    @GET("/team_stats/bowling/best_strike_rate")
    Call<ArrayList<TeamBowlingStatsResponse>> getTeamBowlingBestStrikeRate(@QueryMap Map<String, String> params);

    @GET("/team_stats/bowling/best_economy")
    Call<ArrayList<TeamBowlingStatsResponse>> getTeamBowlingBestEconomy(@QueryMap Map<String, String> params);

    @GET("/venue_stats/recent_matches")
    Call<ArrayList<MatchScore>> getVenueRecentMatches(@QueryMap Map<String, String> params);

    @GET("/venue_stats/batting/most_runs")
    Call<ArrayList<TeamBattingStatsResponse>> getVenueBattingMostRuns(@QueryMap Map<String, String> params);

    @GET("/venue_stats/batting/highest_average")
    Call<ArrayList<TeamBattingStatsResponse>> getVenueBattingBestAverage(@QueryMap Map<String, String> params);

    @GET("/venue_stats/batting/highest_strike_rate")
    Call<ArrayList<TeamBattingStatsResponse>> getVenueBattingBestStrikeRate(@QueryMap Map<String, String> params);

    @GET("/venue_stats/batting/most_fours")
    Call<ArrayList<TeamBattingStatsResponse>> getVenueBattingMostFours(@QueryMap Map<String, String> params);

    @GET("/venue_stats/batting/most_sixes")
    Call<ArrayList<TeamBattingStatsResponse>> getVenueBattingMostSixes(@QueryMap Map<String, String> params);

    @GET("/venue_stats/batting/most_fifties")
    Call<ArrayList<TeamBattingStatsResponse>> getVenueBattingMostFifties(@QueryMap Map<String, String> params);

    @GET("/venue_stats/batting/most_hundreds")
    Call<ArrayList<TeamBattingStatsResponse>> getVenueBattingMostHundreds(@QueryMap Map<String, String> params);

    @GET("/venue_stats/batting/most_ducks")
    Call<ArrayList<TeamBattingStatsResponse>> getVenueBattingMostDucks(@QueryMap Map<String, String> params);

    @GET("/venue_stats/bowling/most_wickets")
    Call<ArrayList<TeamBowlingStatsResponse>> getVenueBowlingMostWickets(@QueryMap Map<String, String> params);

    @GET("/venue_stats/bowling/most_maidens")
    Call<ArrayList<TeamBowlingStatsResponse>> getVenueBowlingMostMaidens(@QueryMap Map<String, String> params);

    @GET("/venue_stats/bowling/most_four_plus_wkts")
    Call<ArrayList<TeamBowlingStatsResponse>> getVenueBowlingMostFourPlusWkts(@QueryMap Map<String, String> params);

    @GET("/venue_stats/bowling/most_five_plus_wkts")
    Call<ArrayList<TeamBowlingStatsResponse>> getVenueBowlingMostFivePlusWkts(@QueryMap Map<String, String> params);

    @GET("/venue_stats/bowling/best_average")
    Call<ArrayList<TeamBowlingStatsResponse>> getVenueBowlingBestAverage(@QueryMap Map<String, String> params);

    @GET("/venue_stats/bowling/best_strike_rate")
    Call<ArrayList<TeamBowlingStatsResponse>> getVenueBowlingBestStrikeRate(@QueryMap Map<String, String> params);

    @GET("/venue_stats/bowling/best_economy")
    Call<ArrayList<TeamBowlingStatsResponse>> getVenueBowlingBestEconomy(@QueryMap Map<String, String> params);
}
