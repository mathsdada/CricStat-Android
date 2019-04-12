package com.mission.cricstat.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mission.cricstat.R;

import java.util.ArrayList;

public class StatsCategoryRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<String> mCategories;
    private StatsCategoryClickListener mListener;

    public StatsCategoryRecyclerViewAdapter(ArrayList<String> categories, StatsCategoryClickListener listener) {
        mCategories = categories;
        mListener = listener;
    }

    public interface StatsCategoryClickListener {
        void onStatsCategoryClickListener(int pos);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View matchView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.stats_category_card, viewGroup, false);
        return new MatchViewHolder(matchView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        ((MatchViewHolder)viewHolder).bindViews(mCategories.get(i));
    }

    @Override
    public int getItemCount() {
        return mCategories.size();
    }

    private class MatchViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mCategoryTv;

        MatchViewHolder(View matchView) {
            super(matchView);
            mCategoryTv = matchView.findViewById(R.id.stats_category_tv);
            matchView.setOnClickListener(this);
        }

        void bindViews(String category) {
            mCategoryTv.setText(category);
        }


        @Override
        public void onClick(View v) {
            if (mListener != null)
                mListener.onStatsCategoryClickListener(getAdapterPosition());
        }
    }
}
