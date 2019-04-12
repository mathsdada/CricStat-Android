package com.mission.cricstat;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.mission.cricstat.Adapter.TeamStatsRecentMatchesRecyclerViewAdapter;
import com.mission.cricstat.Common.Constants;
import com.mission.cricstat.Common.StringUtil;
import com.mission.cricstat.Rest.Model.TeamStats.MatchScore;
import com.mission.cricstat.Rest.Rest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TeamStatsRecentMatchesActivity extends AppCompatActivity {

    public static final String KEY_STATS_TYPE = "STATS_TYPE";
    public static final String KEY_PLAYING_TEAMS = "KEY_PLAYING_TEAMS";
    public static final String KEY_MATCH_FORMAT = "KEY_MATCH_FORMAT";
    public static final String KEY_MATCH_VENUE = "KEY_MATCH_VENUE";
    private static final String SPINNER_ITEM_ALL = "ALL";
    private static final String TAG = TeamStatsRecentMatchesActivity.class.getSimpleName();

    /* Data */
    private TeamStatsRecentMatchesRecyclerViewAdapter mRecyclerViewAdapter;
    private ArrayList<MatchScore> mMatchScores = new ArrayList<>();

    /* local data containers */
    private Map<String, String> mQueryMap = null;
    private String mTitle;
    private String mMatchFormat;
    private String[] mTeamSpinnerItems;
    private String[] mFormatSpinnerItems = {Constants.FORMAT_ALL, Constants.FORMAT_T20, Constants.FORMAT_OD, Constants.FORMAT_TEST};
    private String[] mVenueSpinnerItems;
    private String[] mOpponentSpinnerItems;
    private String[] mNumMatchesSpinnerItems = {Constants.MATCHES_5, Constants.MATCHES_10, Constants.MATCHES_15};

    private String mSelectedTeam, mSelectedFormat, mSelectedVenue = null, mSelectedOpponent = null, mSelectedNumMatches;

    /*View Items */
    private RecyclerView mRecyclerView;
    private TextView mSummaryTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_stats_recent_matches);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mSummaryTextView = findViewById(R.id.tv_summary);
        /* Extract Intent Data */
        mTitle = getIntent().getExtras().getString(KEY_STATS_TYPE);
        mTeamSpinnerItems = getIntent().getExtras().getStringArray(KEY_PLAYING_TEAMS);
        mMatchFormat = getIntent().getExtras().getString(KEY_MATCH_FORMAT);
        mVenueSpinnerItems = new String[]{SPINNER_ITEM_ALL, getIntent().getExtras().getString(KEY_MATCH_VENUE)};
        mOpponentSpinnerItems = new String[]{SPINNER_ITEM_ALL, mTeamSpinnerItems[1], mTeamSpinnerItems[0]};

        getSupportActionBar().setTitle(mTitle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        /* Setup RecyclerView */
        mRecyclerView = findViewById(R.id.rv_team_stats_recent_matches);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL));
        mRecyclerViewAdapter = new TeamStatsRecentMatchesRecyclerViewAdapter(mMatchScores);
        mRecyclerView.setAdapter(mRecyclerViewAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mSelectedTeam = mTeamSpinnerItems[0];
        mSelectedFormat = mMatchFormat;
        mSelectedNumMatches = mNumMatchesSpinnerItems[0];

        fetchRecentMatchesStats(mSelectedTeam, mSelectedFormat, mSelectedVenue, mSelectedOpponent, mSelectedNumMatches);
    }

    private void fetchRecentMatchesStats(String teamName, String format, String venue,
                                         String oppTeam, String numMatches) {
        Map<String, String> queryParams = new HashMap<>();
        if (teamName != null) queryParams.put("name", teamName.toLowerCase());
        if (format != null) queryParams.put("format", format.toLowerCase());
        if (venue != null) queryParams.put("venue", venue.toLowerCase());
        if (oppTeam != null) queryParams.put("against_team", oppTeam.toLowerCase());
        if (numMatches != null) queryParams.put("num_matches", numMatches);

        Log.e(TAG, queryParams.toString());
        if (mQueryMap != null && mQueryMap.equals(queryParams)) return;
        mQueryMap = queryParams;

        Rest.api().getRecentMatches(queryParams).enqueue(new Callback<ArrayList<MatchScore>>() {
            @Override
            public void onResponse(Call<ArrayList<MatchScore>> call, Response<ArrayList<MatchScore>> response) {
                mMatchScores.clear();
                mMatchScores.addAll(response.body());
                mRecyclerViewAdapter.notifyDataSetChanged();
                mSummaryTextView.setText(getSummaryText());
            }

            @Override
            public void onFailure(Call<ArrayList<MatchScore>> call, Throwable t) {
                Snackbar snackbar = Snackbar.make(findViewById(R.id.schedule_cordinator_layout), "Check Internet Connection", Snackbar.LENGTH_INDEFINITE);
                TextView textView = snackbar.getView().findViewById(android.support.design.R.id.snackbar_text);
                textView.setTextColor(Color.WHITE);
                snackbar.setAction("RETRY", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        fetchRecentMatchesStats(mSelectedTeam, mSelectedFormat, mSelectedVenue, mSelectedOpponent, mSelectedNumMatches);
                    }
                });
                snackbar.show();
            }
        });
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

                        fetchRecentMatchesStats(mSelectedTeam, mSelectedFormat, mSelectedVenue, mSelectedOpponent, mSelectedNumMatches);
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

    private String getSummaryText() {
        String summary = StringUtil.toShort(mSelectedTeam);
        if (mSelectedOpponent != null) summary += " vs " + StringUtil.toShort(mSelectedOpponent) + " ";
        if (mSelectedFormat != null) summary += " in " + mSelectedFormat.toUpperCase() + " ";
        if (mSelectedVenue != null) {
            String[] venueSplits = mSelectedVenue.split(Pattern.quote(","));
            summary += " at " + StringUtil.toCamelCase(venueSplits[venueSplits.length-1]);
        }
        return summary;
    }
}
