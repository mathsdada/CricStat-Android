package com.mission.cricstat;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.evrencoskun.tableview.TableView;
import com.mission.cricstat.Common.Constants;
import com.mission.cricstat.Common.StatsCategory;
import com.mission.cricstat.Common.StringUtil;
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

public class TableActivity extends AppCompatActivity {

    public static final String KEY_STATS_TYPE = "STATS_TYPE";
    public static final String KEY_PLAYING_TEAMS = "KEY_PLAYING_TEAMS";
    public static final String KEY_MATCH_FORMAT = "KEY_MATCH_FORMAT";
    public static final String KEY_MATCH_VENUE = "KEY_MATCH_VENUE";
    private static final String SPINNER_ITEM_ALL = "ALL";
    private static final String TAG = TableActivity.class.getSimpleName();

    private String mTitle;
    private String[] mTeamSpinnerItems;
    private String[] mFormatSpinnerItems = {Constants.FORMAT_ALL, Constants.FORMAT_T20, Constants.FORMAT_OD, Constants.FORMAT_TEST};
    private String[] mVenueSpinnerItems;
    private String[] mOpponentSpinnerItems;
    private String[] mNumMatchesSpinnerItems = {Constants.MATCHES_5, Constants.MATCHES_10, Constants.MATCHES_15};

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

        mTitle = getIntent().getExtras().getString(KEY_STATS_TYPE);
        mSelectedFormat = getIntent().getExtras().getString(KEY_MATCH_FORMAT);
        mTeamSpinnerItems = getIntent().getExtras().getStringArray(KEY_PLAYING_TEAMS);
        mVenueSpinnerItems = new String[]{SPINNER_ITEM_ALL, getIntent().getExtras().getString(KEY_MATCH_VENUE)};
        mOpponentSpinnerItems = new String[]{SPINNER_ITEM_ALL, mTeamSpinnerItems[1], mTeamSpinnerItems[0]};

        getSupportActionBar().setTitle(mTitle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mSelectedTeam = mTeamSpinnerItems[0];
        mSelectedNumMatches = mNumMatchesSpinnerItems[0];

        initializeTableView(mTableView);
        fetchStats(false);
    }

    private void initializeTableView(TableView tableView) {
        // Create TableView Adapter
        mTableAdapter = new MyTableAdapter(this, mTitle);
        tableView.setAdapter(mTableAdapter);
    }

    private void fetchStats(boolean isRetry) {
        switch (mTitle) {
            case StatsCategory.TEAM_STATS_BATTING_MOST_RUNS:
            case StatsCategory.TEAM_STATS_BATTING_BEST_AVG:
            case StatsCategory.TEAM_STATS_BATTING_BEST_SR:
            case StatsCategory.TEAM_STATS_BATTING_MOST_4S:
            case StatsCategory.TEAM_STATS_BATTING_MOST_6S:
            case StatsCategory.TEAM_STATS_BATTING_MOST_100S:
            case StatsCategory.TEAM_STATS_BATTING_MOST_50S:
            case StatsCategory.TEAM_STATS_BATTING_MOST_DUCKS:
            {
                fetchTeamBattingStats(isRetry);
                break;
            }
            case StatsCategory.TEAM_STATS_BOWLING_MOST_WICKETS:
            case StatsCategory.TEAM_STATS_BOWLING_MOST_MAIDENS:
            case StatsCategory.TEAM_STATS_BOWLING_MOST_4_PLUS:
            case StatsCategory.TEAM_STATS_BOWLING_MOST_5_PLUS:
            case StatsCategory.TEAM_STATS_BOWLING_BEST_AVERAGE:
            case StatsCategory.TEAM_STATS_BOWLING_BEST_SR:
            case StatsCategory.TEAM_STATS_BOWLING_BEST_ECONOMY:
            {
                Log.e(TAG, "Fetching " + mTitle);
                fetchTeamBowlingStats(isRetry);
                break;
            }
        }
    }

    private void fetchTeamBattingStats(boolean isRetry) {
        Map<String, String> queryParams = new HashMap<>();
        if (mSelectedTeam != null) queryParams.put("name", mSelectedTeam.toLowerCase());
        if (mSelectedFormat != null) queryParams.put("format", mSelectedFormat.toLowerCase());
        if (mSelectedVenue != null) queryParams.put("venue", mSelectedVenue.toLowerCase());
        if (mSelectedOpponent != null) queryParams.put("against_team", mSelectedOpponent.toLowerCase());
        if (mSelectedNumMatches != null) queryParams.put("num_matches", mSelectedNumMatches);

        if (!isRetry) {
            if (mQueryMap != null && mQueryMap.equals(queryParams)) return;
        }
        mQueryMap = queryParams;

        showProgressBar();
        switch (mTitle) {
            case StatsCategory.TEAM_STATS_BATTING_MOST_RUNS: {
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
            case StatsCategory.TEAM_STATS_BATTING_BEST_AVG: {
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
            case StatsCategory.TEAM_STATS_BATTING_BEST_SR: {
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
            case StatsCategory.TEAM_STATS_BATTING_MOST_4S: {
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
            case StatsCategory.TEAM_STATS_BATTING_MOST_6S: {
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
            case StatsCategory.TEAM_STATS_BATTING_MOST_50S: {
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
            case StatsCategory.TEAM_STATS_BATTING_MOST_100S: {
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
            case StatsCategory.TEAM_STATS_BATTING_MOST_DUCKS: {
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

    private void fetchTeamBowlingStats(boolean isRetry) {
        Map<String, String> queryParams = new HashMap<>();
        if (mSelectedTeam != null) queryParams.put("name", mSelectedTeam.toLowerCase());
        if (mSelectedFormat != null) queryParams.put("format", mSelectedFormat.toLowerCase());
        if (mSelectedVenue != null) queryParams.put("venue", mSelectedVenue.toLowerCase());
        if (mSelectedOpponent != null) queryParams.put("against_team", mSelectedOpponent.toLowerCase());
        if (mSelectedNumMatches != null) queryParams.put("num_matches", mSelectedNumMatches);

        if (!isRetry) {
            if (mQueryMap != null && mQueryMap.equals(queryParams)) return;
        }
        mQueryMap = queryParams;

        showProgressBar();
        switch (mTitle) {
            case StatsCategory.TEAM_STATS_BOWLING_MOST_WICKETS: {
                Log.e(TAG, queryParams.toString());
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
            case StatsCategory.TEAM_STATS_BOWLING_MOST_MAIDENS: {
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
            case StatsCategory.TEAM_STATS_BOWLING_MOST_4_PLUS: {
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
            case StatsCategory.TEAM_STATS_BOWLING_MOST_5_PLUS: {
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
            case StatsCategory.TEAM_STATS_BOWLING_BEST_AVERAGE: {
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
            case StatsCategory.TEAM_STATS_BOWLING_BEST_SR: {
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
            case StatsCategory.TEAM_STATS_BOWLING_BEST_ECONOMY: {
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

        final String[] teamSelected = new String[1];
        final String[] formatSelected = new String[1];
        final String[] venueSelected = new String[1];
        final String[] opponentSelected = new String[1];
        final String[] numMatchesSelected = new String[1];
        View view = this.getLayoutInflater().inflate(R.layout.layout_filter, null);
        Spinner teamSpinner = view.findViewById(R.id.team_spinner);
        Spinner formatSpinner = view.findViewById(R.id.format_spinner);
        Spinner venueSpinner = view.findViewById(R.id.venue_spinner);
        Spinner oppTeamSpinner = view.findViewById(R.id.opp_team_spinner);
        Spinner numMatchesSpinner = view.findViewById(R.id.num_matches_spinner);
        setupSpinner(teamSpinner, mTeamSpinnerItems, mSelectedTeam, true);
        setupSpinner(formatSpinner, mFormatSpinnerItems, mSelectedFormat, false);
        setupSpinner(venueSpinner, mVenueSpinnerItems, mSelectedVenue, true);
        setupSpinner(oppTeamSpinner, mOpponentSpinnerItems, mSelectedOpponent, true);
        setupSpinner(numMatchesSpinner, mNumMatchesSpinnerItems, mSelectedNumMatches, false);

        teamSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                teamSelected[0] = mTeamSpinnerItems[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                teamSelected[0] = mSelectedTeam;
            }
        });
        formatSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                formatSelected[0] = mFormatSpinnerItems[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                formatSelected[0] = mSelectedFormat;
            }
        });
        venueSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                venueSelected[0] = mVenueSpinnerItems[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                venueSelected[0] = mSelectedVenue;
            }
        });
        oppTeamSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                opponentSelected[0] = mOpponentSpinnerItems[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                opponentSelected[0] = mSelectedOpponent;
            }
        });
        numMatchesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                numMatchesSelected[0] = mNumMatchesSpinnerItems[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                numMatchesSelected[0] = mSelectedNumMatches;
            }
        });

        new AlertDialog.Builder(this).setView(view)
                .setPositiveButton("Apply", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mSelectedTeam = StringUtil.toCamelCase(teamSelected[0]);
                        mSelectedFormat = formatSelected[0].toLowerCase();
                        mSelectedVenue = StringUtil.toCamelCase(venueSelected[0]);
                        mSelectedOpponent = StringUtil.toCamelCase(opponentSelected[0]);
                        mSelectedNumMatches = numMatchesSelected[0].toLowerCase();

                        if (mSelectedFormat.toUpperCase().equals(SPINNER_ITEM_ALL)) mSelectedFormat = null;
                        if (mSelectedVenue.toUpperCase().equals(SPINNER_ITEM_ALL)) mSelectedVenue = null;
                        if (mSelectedOpponent.toUpperCase().equals(SPINNER_ITEM_ALL)) mSelectedOpponent = null;

                        fetchStats(false);
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
        return true;
    }

    private void setupSpinner(Spinner spinner, String[] data, String defaultItem, boolean camelCaseSearch) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spineer_item_layout,
                R.id.format, data);
        spinner.setAdapter(adapter);
        if (defaultItem != null) {
            if (camelCaseSearch) defaultItem = StringUtil.toCamelCase(defaultItem);
            else defaultItem = defaultItem.toUpperCase();
            spinner.setSelection(adapter.getPosition(defaultItem));
        }

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
