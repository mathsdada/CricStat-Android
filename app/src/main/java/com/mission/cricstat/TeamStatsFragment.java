package com.mission.cricstat;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mission.cricstat.Adapter.StatsCategoryRecyclerViewAdapter;
import com.mission.cricstat.Common.StatsCategory;
import com.mission.cricstat.Common.StringUtil;
import com.mission.cricstat.Rest.Model.Match;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;

public class TeamStatsFragment extends Fragment {
    private Match mMatch;
    public static String KEY_MATCH = "KEY_MATCH";
    private ArrayList<String> mCategories;
    private StatsCategoryRecyclerViewAdapter mStatsCategoryRecyclerViewAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Gson gson = new Gson();
        Type type = new TypeToken<Match>(){}.getType();
        mMatch = gson.fromJson(getArguments().getString(KEY_MATCH), type);
        mCategories = new ArrayList<>(Arrays.asList(StatsCategory.TEAM_STATS_CATEGORIES));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_team_stats, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView = view.findViewById(R.id.rv_team_stats);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.HORIZONTAL));
        mStatsCategoryRecyclerViewAdapter = new StatsCategoryRecyclerViewAdapter(mCategories, new StatsCategoryRecyclerViewAdapter.StatsCategoryClickListener() {
            @Override
            public void onStatsCategoryClickListener(int pos) {
                String[] teams = new String[] {StringUtil.toCamelCase(mMatch.getTeams().get(0).getName()),
                        StringUtil.toCamelCase(mMatch.getTeams().get(1).getName())};
                Bundle bundle = new Bundle();
                bundle.putString(TeamStatsRecentMatchesActivity.KEY_STATS_TYPE, mCategories.get(pos));
                bundle.putStringArray(TeamStatsRecentMatchesActivity.KEY_PLAYING_TEAMS, teams);
                bundle.putString(TeamStatsRecentMatchesActivity.KEY_MATCH_FORMAT, mMatch.getFormat());
                bundle.putString(TeamStatsRecentMatchesActivity.KEY_MATCH_VENUE, StringUtil.toCamelCase(mMatch.getVenue()));
                switch (mCategories.get(pos)) {
                    case StatsCategory.TEAM_STATS_RECENT_MATCHES: {
                        Intent intent = new Intent(getActivity(), TeamStatsRecentMatchesActivity.class);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        break;
                    }
                    case StatsCategory.TEAM_STATS_BATTING_MOST_RUNS:
                    case StatsCategory.TEAM_STATS_BATTING_BEST_AVG:
                    case StatsCategory.TEAM_STATS_BATTING_BEST_SR:
                    case StatsCategory.TEAM_STATS_BATTING_MOST_4S:
                    case StatsCategory.TEAM_STATS_BATTING_MOST_6S:
                    case StatsCategory.TEAM_STATS_BATTING_MOST_50S:
                    case StatsCategory.TEAM_STATS_BATTING_MOST_100S:
                    case StatsCategory.TEAM_STATS_BATTING_MOST_DUCKS:
                    case StatsCategory.TEAM_STATS_BOWLING_MOST_WICKETS:
                    case StatsCategory.TEAM_STATS_BOWLING_MOST_MAIDENS:
                    case StatsCategory.TEAM_STATS_BOWLING_MOST_4_PLUS:
                    case StatsCategory.TEAM_STATS_BOWLING_MOST_5_PLUS:
                    case StatsCategory.TEAM_STATS_BOWLING_BEST_AVERAGE:
                    case StatsCategory.TEAM_STATS_BOWLING_BEST_SR:
                    case StatsCategory.TEAM_STATS_BOWLING_BEST_ECONOMY:
                    {
                        Intent intent = new Intent(getActivity(), TableActivity.class);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        break;
                    }
                    default:
                        break;
                }
            }
        });
        recyclerView.setAdapter(mStatsCategoryRecyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }
}
