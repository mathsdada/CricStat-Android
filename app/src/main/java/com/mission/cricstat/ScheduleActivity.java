package com.mission.cricstat;

import android.content.Intent;
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
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.mission.cricstat.Adapter.ScheduleRecyclerViewAdapter;
import com.mission.cricstat.Common.Constants;
import com.mission.cricstat.Common.StringUtil;
import com.mission.cricstat.Rest.Model.Match;
import com.mission.cricstat.Rest.Model.Team;
import com.mission.cricstat.Rest.Rest;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScheduleActivity extends AppCompatActivity implements ScheduleRecyclerViewAdapter.MatchCardItemClickListener {

    private static final String TAG = ScheduleActivity.class.getSimpleName();
    private ArrayList<Match> mMatches = new ArrayList<>();
    private ArrayList<Match> mScheduleAdapterDataSet = new ArrayList<>();
    private ScheduleRecyclerViewAdapter mScheduleRecyclerViewAdapter;
    private String[] mFormats = {Constants.FORMAT_ALL, Constants.FORMAT_T20, Constants.FORMAT_OD, Constants.FORMAT_TEST};
    private String mSelectedFormat = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setupFormatSpinner();

        RecyclerView scheduleRecyclerView = findViewById(R.id.rv_schedule);
        scheduleRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL));
        mScheduleRecyclerViewAdapter = new ScheduleRecyclerViewAdapter(mScheduleAdapterDataSet, this);
        scheduleRecyclerView.setAdapter(mScheduleRecyclerViewAdapter);
        scheduleRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        fetchListOfMatches();
    }

    private void fetchListOfMatches() {
        Rest.api().getSchedule().enqueue(new Callback<ArrayList<Match>>() {
            @Override
            public void onResponse(Call<ArrayList<Match>> call, Response<ArrayList<Match>> response) {
                if (response.body() == null) return;
                mMatches = response.body();
                mScheduleAdapterDataSet.clear();
                for (Match match: mMatches) {
                    for (Team team : match.getTeams()) {
                        team.setShortName(StringUtil.toShort(team.getName()));
                    }
                    if ((mSelectedFormat == null) ||
                            match.getFormat().equals(mSelectedFormat.toLowerCase())) {
                        mScheduleAdapterDataSet.add(match.copy());
                    }
                }
                mScheduleRecyclerViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<ArrayList<Match>> call, Throwable t) {
                Snackbar snackbar = Snackbar.make(findViewById(R.id.schedule_cordinator_layout), "Check Internet Connection", Snackbar.LENGTH_INDEFINITE);
                TextView textView = snackbar.getView().findViewById(android.support.design.R.id.snackbar_text);
                textView.setTextColor(Color.WHITE);
                snackbar.setAction("RETRY", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        fetchListOfMatches();
                    }
                });
                snackbar.show();
                Log.e(TAG, t.toString());
            }
        });
    }

    public void setupFormatSpinner() {
        Spinner spinner = findViewById(R.id.spinner_format);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                R.layout.spineer_item_layout, R.id.format, mFormats);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mSelectedFormat = mFormats[position];
                if (mSelectedFormat.equals(Constants.FORMAT_ALL)) mSelectedFormat = null;
                mScheduleAdapterDataSet.clear();
                for (Match match : mMatches) {
                    if ((mSelectedFormat == null) ||
                            match.getFormat().equals(mSelectedFormat.toLowerCase())) {
                        mScheduleAdapterDataSet.add(match.copy());
                    }
                }
                mScheduleRecyclerViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onMatchCardItemClick(int pos) {
        Intent intent = new Intent(this, MatchActivity.class);
        Gson gson = new Gson();
        intent.putExtra(MatchActivity.KEY_MATCH, gson.toJson(mScheduleAdapterDataSet.get(pos)));
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_schedule, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        fetchListOfMatches();
        return true;
    }
}