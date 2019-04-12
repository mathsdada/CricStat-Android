package com.mission.cricstat.Common;

public class StatsCategory {
    public final static String RECENT_MATCHES = "Recent Matches";
    public final static String PER_INN_AVG_SCORE = "Per Innings Average Score";
    public final static String BATTING_MOST_RUNS = "Most Runs Scored";
    public final static String BATTING_BEST_AVG = "Best Batting Average";
    public final static String BATTING_BEST_SR = "Best Batting Strike Rate";
    public final static String BATTING_MOST_4S = "Most Fours";
    public final static String BATTING_MOST_6S = "Most Sixes";
    public final static String BATTING_MOST_50S = "Most Fifties";
    public final static String BATTING_MOST_100S = "Most Hundreds";
    public final static String BATTING_MOST_DUCKS = "Most Ducks";
    public final static String BOWLING_MOST_WICKETS = "Most Wickets";
    public final static String BOWLING_BEST_ECONOMY = "Best Bowling Economy";
    public final static String BOWLING_BEST_AVERAGE = "Best Bowling Average";
    public final static String BOWLING_BEST_SR = "Best Bowling Strike Rate";
    public final static String BOWLING_MOST_MAIDENS = "Most Maidens";
    public final static String BOWLING_MOST_4_PLUS = "Most 4+ Wickets";
    public final static String BOWLING_MOST_5_PLUS = "Most 5+ Wickets";

    public static final String TEAM_STATS = "Team Stats";
    public static final String VENUE_STATS = "Venue Stats";
    public static String[] TEAM_STATS_CATEGORIES = {
            RECENT_MATCHES,
            BATTING_MOST_RUNS, BATTING_BEST_AVG, BATTING_BEST_SR,
            BATTING_MOST_4S, BATTING_MOST_6S, BATTING_MOST_50S, BATTING_MOST_100S, BATTING_MOST_DUCKS,
            BOWLING_MOST_WICKETS, BOWLING_BEST_AVERAGE, BOWLING_BEST_ECONOMY, BOWLING_BEST_SR,
            BOWLING_MOST_MAIDENS, BOWLING_MOST_4_PLUS, BOWLING_MOST_5_PLUS
    };

    public static String[] VENUE_STATS_CATEGORIES = {
            RECENT_MATCHES,
            PER_INN_AVG_SCORE,
            BATTING_MOST_RUNS, BATTING_BEST_AVG, BATTING_BEST_SR,
            BATTING_MOST_4S, BATTING_MOST_6S, BATTING_MOST_50S, BATTING_MOST_100S, BATTING_MOST_DUCKS,
            BOWLING_MOST_WICKETS, BOWLING_BEST_AVERAGE, BOWLING_BEST_ECONOMY, BOWLING_BEST_SR,
            BOWLING_MOST_MAIDENS, BOWLING_MOST_4_PLUS, BOWLING_MOST_5_PLUS
    };

}
