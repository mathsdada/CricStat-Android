package com.mission.cricstat.ui.tableView.holder;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder;
import com.mission.cricstat.R;
import com.mission.cricstat.ui.tableView.model.CellModel;

public class CellViewHolder extends AbstractViewHolder {
    public final TextView cell_textview;
    public final LinearLayout cell_container;

    public CellViewHolder(View itemView) {
        super(itemView);
        cell_textview = itemView.findViewById(R.id.cell_data);
        cell_container = itemView.findViewById(R.id.cell_container);
    }

    public void setCellModel(CellModel p_jModel, int pColumnPosition) {

        // Change textView align by column
        cell_textview.setGravity(Gravity.CENTER | Gravity.CENTER_VERTICAL);

        // Set text
        cell_textview.setText(String.valueOf(p_jModel.getData()));

        // It is necessary to remeasure itself.
        cell_container.getLayoutParams().width = 150;//LinearLayout.LayoutParams.WRAP_CONTENT;
        cell_textview.requestLayout();
    }

    @Override
    public void setSelected(SelectionState p_nSelectionState) {
        super.setSelected(p_nSelectionState);

        if (p_nSelectionState == SelectionState.SELECTED) {
            itemView.setBackgroundColor(ContextCompat.getColor(cell_textview.getContext(), R.color.colorSecondaryLight));
            cell_textview.setTextColor(ContextCompat.getColor(cell_textview.getContext(), android.R.color.white));
            cell_textview.setTypeface(null, Typeface.BOLD);
        } else {
            itemView.setBackgroundColor(ContextCompat.getColor(cell_textview.getContext(), android.R.color.white));
            cell_textview.setTextColor(ContextCompat.getColor(cell_textview.getContext(), android.R.color.black));
            cell_textview.setTypeface(null);
        }
    }
}
