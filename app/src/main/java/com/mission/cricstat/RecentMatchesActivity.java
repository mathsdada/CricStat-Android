package com.mission.cricstat;

import android.graphics.Color;
import android.os.Bundle;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.mission.cricstat.Adapter.RecentMatchesRecyclerViewAdapter;
import com.mission.cricstat.Common.Constants;
import com.mission.cricstat.Common.StatsCategory;
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

import static com.mission.cricstat.Common.Constants.SPINNER_ITEM_ALL;

public class RecentMatchesActivity extends AppCompatActivity {

    private static final String SPINNER_ITEM_ALL = "ALL";
    private static final String TAG = RecentMatchesActivity.class.getSimpleName();

    /* Data */
    private RecentMatchesRecyclerViewAdapter mRecyclerViewAdapter;
    private ArrayList<MatchScore> mMatchScores = new ArrayList<>();

    /* local data containers */
    private Map<String, String> mQueryMap = null;
    private String mStatsType, mStatsSubType;

    private Filter mFilter;
    private String mSelectedTeam = null, mSelectedFormat = null, mSelectedVenue = null,
            mSelectedOpponent = null, mSelectedNumMatches = null;

    /*View Items */
    private RecyclerView mRecyclerView;
    private TextView mSummaryTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent_matches);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /* Extract Intent Data */
        mStatsType = getIntent().getExtras().getString(Constants.KEY_STATS_TYPE);
        mStatsSubType = getIntent().getExtras().getString(Constants.KEY_STATS_SUBTYPE);

        getSupportActionBar().setTitle(mStatsSubType);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mSummaryTextView = findViewById(R.id.tv_summary);
        /* Setup RecyclerView */
        mRecyclerView = findViewById(R.id.rv_team_stats_recent_matches);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL));
        mRecyclerViewAdapter = new RecentMatchesRecyclerViewAdapter(mMatchScores);
        mRecyclerView.setAdapter(mRecyclerViewAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        initializeFilter();
        fetchRecentMatchesStats(false);
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

    private void fetchRecentMatchesStats(boolean isRetry) {
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

        if (mStatsType.equals(StatsCategory.TEAM_STATS)) {
            Rest.api().getTeamRecentMatches(queryParams).enqueue(new Callback<ArrayList<MatchScore>>() {
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
                            fetchRecentMatchesStats(true);
                        }
                    });
                    snackbar.show();
                }
            });
        } else {
            Rest.api().getVenueRecentMatches(queryParams).enqueue(new Callback<ArrayList<MatchScore>>() {
                @Override
                public void onResponse(Call<ArrayList<MatchScore>> call, Response<ArrayList<MatchScore>> response) {
                    Log.e(TAG, ""+response.code());
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
                            fetchRecentMatchesStats(true);
                        }
                    });
                    snackbar.show();
                }
            });

        }
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
                fetchRecentMatchesStats(false);
            }
        });
        return true;
    }

    private String getSummaryText() {
        String summary = "";
        if (mSelectedTeam != null) summary += StringUtil.toShort(mSelectedTeam);
        if (mSelectedOpponent != null) summary += " vs " + StringUtil.toShort(mSelectedOpponent);
        if (mSelectedFormat != null) summary += " In " + mSelectedFormat.toUpperCase();
        if (mSelectedVenue != null) {
            String[] venueSplits = mSelectedVenue.split(Pattern.quote(","));
            summary += " At " + StringUtil.toCamelCase(venueSplits[venueSplits.length-1]);
        }
        return summary;
    }
}
