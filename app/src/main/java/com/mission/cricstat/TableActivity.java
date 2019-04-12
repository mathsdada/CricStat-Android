package com.mission.cricstat;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.evrencoskun.tableview.TableView;
import com.mission.cricstat.Common.Constants;
import com.mission.cricstat.Common.StatsCategory;
import com.mission.cricstat.Rest.Model.TeamStats.TeamBattingStatsResponse;
import com.mission.cricstat.Rest.Model.TeamStats.TeamBowlingStatsResponse;
import com.mission.cricstat.Rest.Rest;
import com.mission.cricstat.ui.tableView.MyTableAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.mission.cricstat.Common.Constants.SPINNER_ITEM_ALL;

public class TableActivity extends AppCompatActivity {
    private static final String TAG = TableActivity.class.getSimpleName();

    private Filter mFilter;
    private String mStatsType, mStatsSubType;

    private String mSelectedTeam = null, mSelectedFormat = null, mSelectedVenue = null,
                    mSelectedOpponent = null, mSelectedNumMatches = null;
    private Map<String, String> mQueryMap = null;

    private TableView mTableView;
    private MyTableAdapter mTableAdapter;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mProgressBar = findViewById(R.id.progressBar);
        mTableView = findViewById(R.id.my_TableView);

        mStatsType = getIntent().getExtras().getString(Constants.KEY_STATS_TYPE);
        mStatsSubType = getIntent().getExtras().getString(Constants.KEY_STATS_SUBTYPE);

        getSupportActionBar().setTitle(mStatsSubType);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initializeTableView(mTableView);
        initializeFilter();
        fetchStats(false);
    }

    private void initializeFilter() {
        String[] teamArray = null, formatArray = null, venueArray = null, opponentArray = null, numMatchesArray = null;
        if (mStatsType.equals(StatsCategory.TEAM_STATS)) {
            teamArray = getIntent().getExtras().getStringArray(Constants.KEY_PLAYING_TEAMS);
            venueArray = new String[]{SPINNER_ITEM_ALL, getIntent().getExtras().getString(Constants.KEY_MATCH_VENUE)};
            opponentArray = new String[]{SPINNER_ITEM_ALL, teamArray[1], teamArray[0]};

            mSelectedTeam = teamArray[0];
        } else {
            mSelectedVenue = getIntent().getExtras().getString(Constants.KEY_MATCH_VENUE);
        }
        formatArray = new String[]{Constants.FORMAT_ALL, Constants.FORMAT_T20, Constants.FORMAT_OD, Constants.FORMAT_TEST};
        numMatchesArray = new String[]{Constants.MATCHES_5, Constants.MATCHES_10, Constants.MATCHES_15};

        mSelectedFormat = getIntent().getExtras().getString(Constants.KEY_MATCH_FORMAT);
        mSelectedNumMatches = numMatchesArray[0];

        mFilter = new Filter(this);
        mFilter.setSelectionArray(teamArray, formatArray, venueArray, opponentArray, numMatchesArray);
        mFilter.setDefaultSelection(mSelectedTeam, mSelectedFormat, mSelectedVenue, mSelectedOpponent, mSelectedNumMatches);
    }

    private void initializeTableView(TableView tableView) {
        // Create TableView Adapter
        mTableAdapter = new MyTableAdapter(this, mStatsSubType);
        tableView.setAdapter(mTableAdapter);
    }

    private void fetchStats(boolean isRetry) {
        Map<String, String> queryParams = new HashMap<>();
        if (mSelectedTeam != null) queryParams.put("name", mSelectedTeam.toLowerCase());
        if (mSelectedFormat != null) queryParams.put("format", mSelectedFormat.toLowerCase());
        if (mSelectedVenue != null) {
            if (mStatsType.equals(StatsCategory.VENUE_STATS)) {
                queryParams.put("name", mSelectedVenue.toLowerCase());
            } else {
                queryParams.put("venue", mSelectedVenue.toLowerCase());
            }
        }
        if (mSelectedOpponent != null) queryParams.put("against_team", mSelectedOpponent.toLowerCase());
        if (mSelectedNumMatches != null) queryParams.put("num_matches", mSelectedNumMatches);

        if (!isRetry) {
            if (mQueryMap != null && mQueryMap.equals(queryParams)) return;
        }
        mQueryMap = queryParams;

        switch (mStatsSubType) {
            case StatsCategory.BATTING_MOST_RUNS:
            case StatsCategory.BATTING_BEST_AVG:
            case StatsCategory.BATTING_BEST_SR:
            case StatsCategory.BATTING_MOST_4S:
            case StatsCategory.BATTING_MOST_6S:
            case StatsCategory.BATTING_MOST_100S:
            case StatsCategory.BATTING_MOST_50S:
            case StatsCategory.BATTING_MOST_DUCKS:
            {
                if (mStatsType.equals(StatsCategory.TEAM_STATS)) {
                    fetchTeamBattingStats();
                } else {
                    fetchVenueBattingStats();
                }
                break;
            }
            case StatsCategory.BOWLING_MOST_WICKETS:
            case StatsCategory.BOWLING_MOST_MAIDENS:
            case StatsCategory.BOWLING_MOST_4_PLUS:
            case StatsCategory.BOWLING_MOST_5_PLUS:
            case StatsCategory.BOWLING_BEST_AVERAGE:
            case StatsCategory.BOWLING_BEST_SR:
            case StatsCategory.BOWLING_BEST_ECONOMY:
            {
                if (mStatsType.equals(StatsCategory.TEAM_STATS)) {
                    fetchTeamBowlingStats();
                } else {
                    fetchVenueBowlingStats();
                }
                break;
            }
        }
    }

    private void fetchTeamBattingStats() {
        showProgressBar();
        switch (mStatsSubType) {
            case StatsCategory.BATTING_MOST_RUNS: {
                Rest.api().getTeamBattingMostRuns(mQueryMap).enqueue(new Callback<ArrayList<TeamBattingStatsResponse>>() {
                    @Override
                    public void onResponse(Call<ArrayList<TeamBattingStatsResponse>> call, Response<ArrayList<TeamBattingStatsResponse>> response) {
                        updateTeamBattingStatsTableAdapter(response.body());
                        hideProgressBar();
                    }

                    @Override
                    public void onFailure(Call<ArrayList<TeamBattingStatsResponse>> call, Throwable t) {
                        showRetrySnackBar();
                    }
                });
                break;
            }
            case StatsCategory.BATTING_BEST_AVG: {
                Rest.api().getTeamBattingBestAverage(mQueryMap).enqueue(new Callback<ArrayList<TeamBattingStatsResponse>>() {
                    @Override
                    public void onResponse(Call<ArrayList<TeamBattingStatsResponse>> call, Response<ArrayList<TeamBattingStatsResponse>> response) {
                        updateTeamBattingStatsTableAdapter(response.body());
                        hideProgressBar();
                    }

                    @Override
                    public void onFailure(Call<ArrayList<TeamBattingStatsResponse>> call, Throwable t) {
                        showRetrySnackBar();
                    }
                });
                break;
            }
            case StatsCategory.BATTING_BEST_SR: {
                Rest.api().getTeamBattingBestStrikeRate(mQueryMap).enqueue(new Callback<ArrayList<TeamBattingStatsResponse>>() {
                    @Override
                    public void onResponse(Call<ArrayList<TeamBattingStatsResponse>> call, Response<ArrayList<TeamBattingStatsResponse>> response) {
                        updateTeamBattingStatsTableAdapter(response.body());
                        hideProgressBar();
                    }

                    @Override
                    public void onFailure(Call<ArrayList<TeamBattingStatsResponse>> call, Throwable t) {
                        showRetrySnackBar();
                    }
                });
                break;
            }
            case StatsCategory.BATTING_MOST_4S: {
                Rest.api().getTeamBattingMostFours(mQueryMap).enqueue(new Callback<ArrayList<TeamBattingStatsResponse>>() {
                    @Override
                    public void onResponse(Call<ArrayList<TeamBattingStatsResponse>> call, Response<ArrayList<TeamBattingStatsResponse>> response) {
                        updateTeamBattingStatsTableAdapter(response.body());
                        hideProgressBar();
                    }

                    @Override
                    public void onFailure(Call<ArrayList<TeamBattingStatsResponse>> call, Throwable t) {
                        showRetrySnackBar();
                    }
                });
                break;
            }
            case StatsCategory.BATTING_MOST_6S: {
                Rest.api().getTeamBattingMostSixes(mQueryMap).enqueue(new Callback<ArrayList<TeamBattingStatsResponse>>() {
                    @Override
                    public void onResponse(Call<ArrayList<TeamBattingStatsResponse>> call, Response<ArrayList<TeamBattingStatsResponse>> response) {
                        updateTeamBattingStatsTableAdapter(response.body());
                        hideProgressBar();
                    }

                    @Override
                    public void onFailure(Call<ArrayList<TeamBattingStatsResponse>> call, Throwable t) {
                        showRetrySnackBar();
                    }
                });
                break;
            }
            case StatsCategory.BATTING_MOST_50S: {
                Rest.api().getTeamBattingMostFifties(mQueryMap).enqueue(new Callback<ArrayList<TeamBattingStatsResponse>>() {
                    @Override
                    public void onResponse(Call<ArrayList<TeamBattingStatsResponse>> call, Response<ArrayList<TeamBattingStatsResponse>> response) {
                        updateTeamBattingStatsTableAdapter(response.body());
                        hideProgressBar();
                    }

                    @Override
                    public void onFailure(Call<ArrayList<TeamBattingStatsResponse>> call, Throwable t) {
                        showRetrySnackBar();
                    }
                });
                break;
            }
            case StatsCategory.BATTING_MOST_100S: {
                Rest.api().getTeamBattingMostHundreds(mQueryMap).enqueue(new Callback<ArrayList<TeamBattingStatsResponse>>() {
                    @Override
                    public void onResponse(Call<ArrayList<TeamBattingStatsResponse>> call, Response<ArrayList<TeamBattingStatsResponse>> response) {
                        updateTeamBattingStatsTableAdapter(response.body());
                        hideProgressBar();
                    }

                    @Override
                    public void onFailure(Call<ArrayList<TeamBattingStatsResponse>> call, Throwable t) {
                        showRetrySnackBar();
                    }
                });
                break;
            }
            case StatsCategory.BATTING_MOST_DUCKS: {
                Rest.api().getTeamBattingMostDucks(mQueryMap).enqueue(new Callback<ArrayList<TeamBattingStatsResponse>>() {
                    @Override
                    public void onResponse(Call<ArrayList<TeamBattingStatsResponse>> call, Response<ArrayList<TeamBattingStatsResponse>> response) {
                        updateTeamBattingStatsTableAdapter(response.body());
                        hideProgressBar();
                    }

                    @Override
                    public void onFailure(Call<ArrayList<TeamBattingStatsResponse>> call, Throwable t) {
                        showRetrySnackBar();
                    }
                });
                break;
            }
            default:{
                hideProgressBar();
                break;
            }
        }
    }

    private void fetchVenueBattingStats() {
        showProgressBar();
        switch (mStatsSubType) {
            case StatsCategory.BATTING_MOST_RUNS: {
                Rest.api().getVenueBattingMostRuns(mQueryMap).enqueue(new Callback<ArrayList<TeamBattingStatsResponse>>() {
                    @Override
                    public void onResponse(Call<ArrayList<TeamBattingStatsResponse>> call, Response<ArrayList<TeamBattingStatsResponse>> response) {
                        updateTeamBattingStatsTableAdapter(response.body());
                        hideProgressBar();
                    }

                    @Override
                    public void onFailure(Call<ArrayList<TeamBattingStatsResponse>> call, Throwable t) {
                        showRetrySnackBar();
                    }
                });
                break;
            }
            case StatsCategory.BATTING_BEST_AVG: {
                Rest.api().getVenueBattingBestAverage(mQueryMap).enqueue(new Callback<ArrayList<TeamBattingStatsResponse>>() {
                    @Override
                    public void onResponse(Call<ArrayList<TeamBattingStatsResponse>> call, Response<ArrayList<TeamBattingStatsResponse>> response) {
                        updateTeamBattingStatsTableAdapter(response.body());
                        hideProgressBar();
                    }

                    @Override
                    public void onFailure(Call<ArrayList<TeamBattingStatsResponse>> call, Throwable t) {
                        showRetrySnackBar();
                    }
                });
                break;
            }
            case StatsCategory.BATTING_BEST_SR: {
                Rest.api().getVenueBattingBestStrikeRate(mQueryMap).enqueue(new Callback<ArrayList<TeamBattingStatsResponse>>() {
                    @Override
                    public void onResponse(Call<ArrayList<TeamBattingStatsResponse>> call, Response<ArrayList<TeamBattingStatsResponse>> response) {
                        updateTeamBattingStatsTableAdapter(response.body());
                        hideProgressBar();
                    }

                    @Override
                    public void onFailure(Call<ArrayList<TeamBattingStatsResponse>> call, Throwable t) {
                        showRetrySnackBar();
                    }
                });
                break;
            }
            case StatsCategory.BATTING_MOST_4S: {
                Rest.api().getVenueBattingMostFours(mQueryMap).enqueue(new Callback<ArrayList<TeamBattingStatsResponse>>() {
                    @Override
                    public void onResponse(Call<ArrayList<TeamBattingStatsResponse>> call, Response<ArrayList<TeamBattingStatsResponse>> response) {
                        updateTeamBattingStatsTableAdapter(response.body());
                        hideProgressBar();
                    }

                    @Override
                    public void onFailure(Call<ArrayList<TeamBattingStatsResponse>> call, Throwable t) {
                        showRetrySnackBar();
                    }
                });
                break;
            }
            case StatsCategory.BATTING_MOST_6S: {
                Rest.api().getVenueBattingMostSixes(mQueryMap).enqueue(new Callback<ArrayList<TeamBattingStatsResponse>>() {
                    @Override
                    public void onResponse(Call<ArrayList<TeamBattingStatsResponse>> call, Response<ArrayList<TeamBattingStatsResponse>> response) {
                        updateTeamBattingStatsTableAdapter(response.body());
                        hideProgressBar();
                    }

                    @Override
                    public void onFailure(Call<ArrayList<TeamBattingStatsResponse>> call, Throwable t) {
                        showRetrySnackBar();
                    }
                });
                break;
            }
            case StatsCategory.BATTING_MOST_50S: {
                Rest.api().getVenueBattingMostFifties(mQueryMap).enqueue(new Callback<ArrayList<TeamBattingStatsResponse>>() {
                    @Override
                    public void onResponse(Call<ArrayList<TeamBattingStatsResponse>> call, Response<ArrayList<TeamBattingStatsResponse>> response) {
                        updateTeamBattingStatsTableAdapter(response.body());
                        hideProgressBar();
                    }

                    @Override
                    public void onFailure(Call<ArrayList<TeamBattingStatsResponse>> call, Throwable t) {
                        showRetrySnackBar();
                    }
                });
                break;
            }
            case StatsCategory.BATTING_MOST_100S: {
                Rest.api().getVenueBattingMostHundreds(mQueryMap).enqueue(new Callback<ArrayList<TeamBattingStatsResponse>>() {
                    @Override
                    public void onResponse(Call<ArrayList<TeamBattingStatsResponse>> call, Response<ArrayList<TeamBattingStatsResponse>> response) {
                        updateTeamBattingStatsTableAdapter(response.body());
                        hideProgressBar();
                    }

                    @Override
                    public void onFailure(Call<ArrayList<TeamBattingStatsResponse>> call, Throwable t) {
                        showRetrySnackBar();
                    }
                });
                break;
            }
            case StatsCategory.BATTING_MOST_DUCKS: {
                Rest.api().getVenueBattingMostDucks(mQueryMap).enqueue(new Callback<ArrayList<TeamBattingStatsResponse>>() {
                    @Override
                    public void onResponse(Call<ArrayList<TeamBattingStatsResponse>> call, Response<ArrayList<TeamBattingStatsResponse>> response) {
                        updateTeamBattingStatsTableAdapter(response.body());
                        hideProgressBar();
                    }

                    @Override
                    public void onFailure(Call<ArrayList<TeamBattingStatsResponse>> call, Throwable t) {
                        showRetrySnackBar();
                    }
                });
                break;
            }
            default:{
                hideProgressBar();
                break;
            }
        }
    }

    private void fetchTeamBowlingStats() {
        showProgressBar();
        switch (mStatsSubType) {
            case StatsCategory.BOWLING_MOST_WICKETS: {
                Rest.api().getTeamBowlingMostWickets(mQueryMap).enqueue(new Callback<ArrayList<TeamBowlingStatsResponse>>() {
                    @Override
                    public void onResponse(Call<ArrayList<TeamBowlingStatsResponse>> call, Response<ArrayList<TeamBowlingStatsResponse>> response) {
                        updateTeamBowlingStatsTableAdapter(response.body());
                        hideProgressBar();
                    }

                    @Override
                    public void onFailure(Call<ArrayList<TeamBowlingStatsResponse>> call, Throwable t) {
                        Log.e(TAG, t.getMessage());
                        showRetrySnackBar();
                    }
                });
                break;
            }
            case StatsCategory.BOWLING_MOST_MAIDENS: {
                Rest.api().getTeamBowlingMostMaidens(mQueryMap).enqueue(new Callback<ArrayList<TeamBowlingStatsResponse>>() {
                    @Override
                    public void onResponse(Call<ArrayList<TeamBowlingStatsResponse>> call, Response<ArrayList<TeamBowlingStatsResponse>> response) {
                        updateTeamBowlingStatsTableAdapter(response.body());
                        hideProgressBar();
                    }

                    @Override
                    public void onFailure(Call<ArrayList<TeamBowlingStatsResponse>> call, Throwable t) {
                        showRetrySnackBar();
                    }
                });
                break;
            }
            case StatsCategory.BOWLING_MOST_4_PLUS: {
                Rest.api().getTeamBowlingMostFourPlusWkts(mQueryMap).enqueue(new Callback<ArrayList<TeamBowlingStatsResponse>>() {
                    @Override
                    public void onResponse(Call<ArrayList<TeamBowlingStatsResponse>> call, Response<ArrayList<TeamBowlingStatsResponse>> response) {
                        updateTeamBowlingStatsTableAdapter(response.body());
                        hideProgressBar();
                    }

                    @Override
                    public void onFailure(Call<ArrayList<TeamBowlingStatsResponse>> call, Throwable t) {
                        showRetrySnackBar();
                    }
                });
                break;
            }
            case StatsCategory.BOWLING_MOST_5_PLUS: {
                Rest.api().getTeamBowlingMostFivePlusWkts(mQueryMap).enqueue(new Callback<ArrayList<TeamBowlingStatsResponse>>() {
                    @Override
                    public void onResponse(Call<ArrayList<TeamBowlingStatsResponse>> call, Response<ArrayList<TeamBowlingStatsResponse>> response) {
                        updateTeamBowlingStatsTableAdapter(response.body());
                        hideProgressBar();
                    }

                    @Override
                    public void onFailure(Call<ArrayList<TeamBowlingStatsResponse>> call, Throwable t) {
                        showRetrySnackBar();
                    }
                });
                break;
            }
            case StatsCategory.BOWLING_BEST_AVERAGE: {
                Rest.api().getTeamBowlingBestAverage(mQueryMap).enqueue(new Callback<ArrayList<TeamBowlingStatsResponse>>() {
                    @Override
                    public void onResponse(Call<ArrayList<TeamBowlingStatsResponse>> call, Response<ArrayList<TeamBowlingStatsResponse>> response) {
                        updateTeamBowlingStatsTableAdapter(response.body());
                        hideProgressBar();
                    }

                    @Override
                    public void onFailure(Call<ArrayList<TeamBowlingStatsResponse>> call, Throwable t) {
                        showRetrySnackBar();
                    }
                });
                break;
            }
            case StatsCategory.BOWLING_BEST_SR: {
                Rest.api().getTeamBowlingBestStrikeRate(mQueryMap).enqueue(new Callback<ArrayList<TeamBowlingStatsResponse>>() {
                    @Override
                    public void onResponse(Call<ArrayList<TeamBowlingStatsResponse>> call, Response<ArrayList<TeamBowlingStatsResponse>> response) {
                        updateTeamBowlingStatsTableAdapter(response.body());
                        hideProgressBar();
                    }

                    @Override
                    public void onFailure(Call<ArrayList<TeamBowlingStatsResponse>> call, Throwable t) {
                        showRetrySnackBar();
                    }
                });
                break;
            }
            case StatsCategory.BOWLING_BEST_ECONOMY: {
                Rest.api().getTeamBowlingBestEconomy(mQueryMap).enqueue(new Callback<ArrayList<TeamBowlingStatsResponse>>() {
                    @Override
                    public void onResponse(Call<ArrayList<TeamBowlingStatsResponse>> call, Response<ArrayList<TeamBowlingStatsResponse>> response) {
                        updateTeamBowlingStatsTableAdapter(response.body());
                        hideProgressBar();
                    }

                    @Override
                    public void onFailure(Call<ArrayList<TeamBowlingStatsResponse>> call, Throwable t) {
                        showRetrySnackBar();
                    }
                });
                break;
            }
            default: {
                break;
            }

        }
    }

    private void fetchVenueBowlingStats() {
        showProgressBar();
        switch (mStatsSubType) {
            case StatsCategory.BOWLING_MOST_WICKETS: {
                Rest.api().getVenueBowlingMostWickets(mQueryMap).enqueue(new Callback<ArrayList<TeamBowlingStatsResponse>>() {
                    @Override
                    public void onResponse(Call<ArrayList<TeamBowlingStatsResponse>> call, Response<ArrayList<TeamBowlingStatsResponse>> response) {
                        updateTeamBowlingStatsTableAdapter(response.body());
                        hideProgressBar();
                    }

                    @Override
                    public void onFailure(Call<ArrayList<TeamBowlingStatsResponse>> call, Throwable t) {
                        Log.e(TAG, t.getMessage());
                        showRetrySnackBar();
                    }
                });
                break;
            }
            case StatsCategory.BOWLING_MOST_MAIDENS: {
                Rest.api().getVenueBowlingMostMaidens(mQueryMap).enqueue(new Callback<ArrayList<TeamBowlingStatsResponse>>() {
                    @Override
                    public void onResponse(Call<ArrayList<TeamBowlingStatsResponse>> call, Response<ArrayList<TeamBowlingStatsResponse>> response) {
                        updateTeamBowlingStatsTableAdapter(response.body());
                        hideProgressBar();
                    }

                    @Override
                    public void onFailure(Call<ArrayList<TeamBowlingStatsResponse>> call, Throwable t) {
                        showRetrySnackBar();
                    }
                });
                break;
            }
            case StatsCategory.BOWLING_MOST_4_PLUS: {
                Rest.api().getVenueBowlingMostFourPlusWkts(mQueryMap).enqueue(new Callback<ArrayList<TeamBowlingStatsResponse>>() {
                    @Override
                    public void onResponse(Call<ArrayList<TeamBowlingStatsResponse>> call, Response<ArrayList<TeamBowlingStatsResponse>> response) {
                        updateTeamBowlingStatsTableAdapter(response.body());
                        hideProgressBar();
                    }

                    @Override
                    public void onFailure(Call<ArrayList<TeamBowlingStatsResponse>> call, Throwable t) {
                        showRetrySnackBar();
                    }
                });
                break;
            }
            case StatsCategory.BOWLING_MOST_5_PLUS: {
                Rest.api().getVenueBowlingMostFivePlusWkts(mQueryMap).enqueue(new Callback<ArrayList<TeamBowlingStatsResponse>>() {
                    @Override
                    public void onResponse(Call<ArrayList<TeamBowlingStatsResponse>> call, Response<ArrayList<TeamBowlingStatsResponse>> response) {
                        updateTeamBowlingStatsTableAdapter(response.body());
                        hideProgressBar();
                    }

                    @Override
                    public void onFailure(Call<ArrayList<TeamBowlingStatsResponse>> call, Throwable t) {
                        showRetrySnackBar();
                    }
                });
                break;
            }
            case StatsCategory.BOWLING_BEST_AVERAGE: {
                Rest.api().getVenueBowlingBestAverage(mQueryMap).enqueue(new Callback<ArrayList<TeamBowlingStatsResponse>>() {
                    @Override
                    public void onResponse(Call<ArrayList<TeamBowlingStatsResponse>> call, Response<ArrayList<TeamBowlingStatsResponse>> response) {
                        updateTeamBowlingStatsTableAdapter(response.body());
                        hideProgressBar();
                    }

                    @Override
                    public void onFailure(Call<ArrayList<TeamBowlingStatsResponse>> call, Throwable t) {
                        showRetrySnackBar();
                    }
                });
                break;
            }
            case StatsCategory.BOWLING_BEST_SR: {
                Rest.api().getVenueBowlingBestStrikeRate(mQueryMap).enqueue(new Callback<ArrayList<TeamBowlingStatsResponse>>() {
                    @Override
                    public void onResponse(Call<ArrayList<TeamBowlingStatsResponse>> call, Response<ArrayList<TeamBowlingStatsResponse>> response) {
                        updateTeamBowlingStatsTableAdapter(response.body());
                        hideProgressBar();
                    }

                    @Override
                    public void onFailure(Call<ArrayList<TeamBowlingStatsResponse>> call, Throwable t) {
                        showRetrySnackBar();
                    }
                });
                break;
            }
            case StatsCategory.BOWLING_BEST_ECONOMY: {
                Rest.api().getVenueBowlingBestEconomy(mQueryMap).enqueue(new Callback<ArrayList<TeamBowlingStatsResponse>>() {
                    @Override
                    public void onResponse(Call<ArrayList<TeamBowlingStatsResponse>> call, Response<ArrayList<TeamBowlingStatsResponse>> response) {
                        updateTeamBowlingStatsTableAdapter(response.body());
                        hideProgressBar();
                    }

                    @Override
                    public void onFailure(Call<ArrayList<TeamBowlingStatsResponse>> call, Throwable t) {
                        showRetrySnackBar();
                    }
                });
                break;
            }
            default: {
                break;
            }

        }
    }

    private void updateTeamBattingStatsTableAdapter(ArrayList<TeamBattingStatsResponse> responses) {
        if (responses == null) return;
        mTableAdapter.setResponseList(TeamBattingStatsResponse.convertToObjectArray(responses));
    }

    private void updateTeamBowlingStatsTableAdapter(ArrayList<TeamBowlingStatsResponse> responses) {
        if (responses == null) return;
        mTableAdapter.setResponseList(TeamBowlingStatsResponse.convertToObjectArray(responses));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_team_stats_recent_matches, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() != R.id.action_filter) return super.onOptionsItemSelected(item);
        mFilter.getSelection(new Filter.SelectionListener() {
            @Override
            public void onSelectionListener(String team, String format, String venue, String opponent, String numMatches) {
                mSelectedTeam = team;
                mSelectedFormat = format;
                mSelectedVenue = venue;
                mSelectedOpponent = opponent;
                mSelectedNumMatches = numMatches;
                fetchStats(false);
            }
        });
        return true;
    }

    public void showProgressBar() {
        mProgressBar.setVisibility(View.VISIBLE);
        mTableView.setVisibility(View.INVISIBLE);
    }

    public void hideProgressBar() {
        mProgressBar.setVisibility(View.INVISIBLE);
        mTableView.setVisibility(View.VISIBLE);
    }

    private void showRetrySnackBar() {
        Snackbar snackbar = Snackbar.make(findViewById(R.id.schedule_cordinator_layout), "Check Internet Connection", Snackbar.LENGTH_INDEFINITE);
        TextView textView = snackbar.getView().findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        snackbar.setAction("RETRY", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchStats(true);
            }
        });
        snackbar.show();
    }
}
