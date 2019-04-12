package com.mission.cricstat.Adapter;

import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mission.cricstat.Common.MyTime;
import com.mission.cricstat.Common.StringUtil;
import com.mission.cricstat.R;
import com.mission.cricstat.Rest.Model.Match;

import java.util.ArrayList;

public class ScheduleRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<Match> mMatches;
    private MatchCardItemClickListener mListener;

    public ScheduleRecyclerViewAdapter(ArrayList<Match> matches, MatchCardItemClickListener listener) {
        mMatches = matches;
        mListener = listener;
    }

    public interface MatchCardItemClickListener {
        void onMatchCardItemClick(int pos);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View matchView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.schedule_match_card, viewGroup, false);
        return new MatchViewHolder(matchView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        ((MatchViewHolder)viewHolder).bindViews(mMatches.get(i));
    }

    @Override
    public int getItemCount() {
        return mMatches.size();
    }

    private class MatchViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mSeriesTv, mTeamOneTv, mTeamTwoTv, mFormatTv, mTimeTv;

        MatchViewHolder(View matchView) {
            super(matchView);
            mSeriesTv = matchView.findViewById(R.id.series_tv);
            mTeamOneTv = matchView.findViewById(R.id.team_1_tv);
            mTeamTwoTv = matchView.findViewById(R.id.team_2_tv);
            mFormatTv = matchView.findViewById(R.id.format_tv);
            mTimeTv = matchView.findViewById(R.id.time_tv);
            matchView.setOnClickListener(this);
        }

        void bindViews(Match match) {
            mSeriesTv.setText(match.getSeries());
            mTeamOneTv.setText(match.getTeams().get(0).getShortName());
            mTeamTwoTv.setText(match.getTeams().get(1).getShortName());
            mFormatTv.setText(match.getFormat());

            long timeRemaining =  match.getDateTime() - System.currentTimeMillis();
            final MyTime myTime = new MyTime(timeRemaining/1000);
            new CountDownTimer(timeRemaining, 1000) {

                @Override
                public void onTick(long millisUntilFinished) {
                    myTime.countDown();
                    mTimeTv.setText(myTime.toString());
                }

                @Override
                public void onFinish() {
                    mTimeTv.setText("In Progress");
                }
            }.start();
        }


        @Override
        public void onClick(View v) {
            if (mListener != null)
                mListener.onMatchCardItemClick(getAdapterPosition());
        }
    }
}
