package com.mission.cricstat.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mission.cricstat.Common.StringUtil;
import com.mission.cricstat.R;
import com.mission.cricstat.Rest.Model.TeamStats.InningsScore;
import com.mission.cricstat.Rest.Model.TeamStats.MatchScore;

import java.util.ArrayList;

public class TeamStatsRecentMatchesRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = TeamStatsRecentMatchesRecyclerViewAdapter.class.getSimpleName();
    private ArrayList<MatchScore> mMatchScores;

    public TeamStatsRecentMatchesRecyclerViewAdapter(ArrayList<MatchScore> matchScores) {
        mMatchScores = matchScores;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View matchView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.team_stats_match_score_card, viewGroup, false);
        return new MatchViewHolder(matchView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        ((MatchViewHolder)viewHolder).bindViews(mMatchScores.get(i));
    }

    @Override
    public int getItemCount() {
        return mMatchScores.size();
    }

    private class MatchViewHolder extends RecyclerView.ViewHolder {
        private TextView mTextViewOutcome, mTextViewMatchDate;
        private LinearLayout mLinearLayout1, mLinearLayout2, mLinearLayout3, mLinearLayout4;
        private TextView mTextViewInn1Team, mTextViewInn1Score;
        private TextView mTextViewInn2Team, mTextViewInn2Score;
        private TextView mTextViewInn3Team, mTextViewInn3Score;
        private TextView mTextViewInn4Team, mTextViewInn4Score;

        MatchViewHolder(View matchView) {
            super(matchView);

            mTextViewOutcome = matchView.findViewById(R.id.tv_match_outcome);
            mTextViewMatchDate = matchView.findViewById(R.id.tv_match_date);
            mLinearLayout1 = matchView.findViewById(R.id.layout_innings_1_score);
            mLinearLayout2 = matchView.findViewById(R.id.layout_innings_2_score);
            mLinearLayout3 = matchView.findViewById(R.id.layout_innings_3_score);
            mLinearLayout4 = matchView.findViewById(R.id.layout_innings_4_score);

            mTextViewInn1Team = matchView.findViewById(R.id.tv_innings_1_team_name);
            mTextViewInn2Team = matchView.findViewById(R.id.tv_innings_2_team_name);
            mTextViewInn3Team = matchView.findViewById(R.id.tv_innings_3_team_name);
            mTextViewInn4Team = matchView.findViewById(R.id.tv_innings_4_team_name);

            mTextViewInn1Score = matchView.findViewById(R.id.tv_innings_1_team_score);
            mTextViewInn2Score = matchView.findViewById(R.id.tv_innings_2_team_score);
            mTextViewInn3Score = matchView.findViewById(R.id.tv_innings_3_team_score);
            mTextViewInn4Score = matchView.findViewById(R.id.tv_innings_4_team_score);
        }

        void bindViews(MatchScore matchScore) {
            String outcome = matchScore.getOutcome();
            outcome = outcome.substring(0,1).toUpperCase() + outcome.substring(1);
            switch (matchScore.getStatus()) {
                case "l" : mTextViewOutcome.setBackgroundResource(R.color.colorSecondaryLight); break;
                case "w" : mTextViewOutcome.setBackgroundResource(R.color.colorPrimaryLight); break;
                case "d" : mTextViewOutcome.setBackgroundResource(android.R.color.darker_gray);
                case "nr": mTextViewOutcome.setBackgroundResource(android.R.color.holo_blue_light);
                default: mTextViewOutcome.setBackgroundResource(android.R.color.black);
            }
            if (matchScore.getStatus().equals("l")) mTextViewOutcome.setBackgroundResource(R.color.colorSecondaryLight);

            mTextViewOutcome.setText(outcome);
            mTextViewMatchDate.setText(StringUtil.epochToDate(matchScore.getDateEpoch()));
            for (InningsScore inningsScore: matchScore.getInningsScores()) {
                String teamName = StringUtil.toCamelCase(inningsScore.getBattingTeam());
                String teamScore =  inningsScore.getRuns() + "-" + inningsScore.getWickets() + " (" +
                        StringUtil.toOvers(inningsScore.getBalls()) + ") ";
                switch (inningsScore.getInningsNumber()) {
                    case 0: {
                        Log.e(TAG, "Ufffffff!!!....Innings number with 0..OMG!!!!");
                    }
                    case 1: {
                        mLinearLayout1.setVisibility(View.VISIBLE);
                        mLinearLayout2.setVisibility(View.GONE);
                        mLinearLayout3.setVisibility(View.GONE);
                        mLinearLayout4.setVisibility(View.GONE);
                        mTextViewInn1Team.setText(teamName);
                        mTextViewInn1Score.setText(teamScore);
                        break;
                    }
                    case 2: {
                        mLinearLayout2.setVisibility(View.VISIBLE);
                        mLinearLayout3.setVisibility(View.GONE);
                        mLinearLayout4.setVisibility(View.GONE);
                        mTextViewInn2Team.setText(teamName);
                        mTextViewInn2Score.setText(teamScore);
                        break;
                    }
                    case 3: {
                        mLinearLayout3.setVisibility(View.VISIBLE);
                        mLinearLayout4.setVisibility(View.GONE);
                        mTextViewInn3Team.setText(teamName);
                        mTextViewInn3Score.setText(teamScore);
                        break;
                    }
                    case 4: {
                        mLinearLayout4.setVisibility(View.VISIBLE);
                        mTextViewInn4Team.setText(teamName);
                        mTextViewInn4Score.setText(teamScore);
                        break;
                    }
                    default: {
                        Log.e(TAG,
                                "Looks like We are in Alternate Universe where More than 4 innings are being played in a Cricket Match");
                    }
                }
            }
        }
    }
}
