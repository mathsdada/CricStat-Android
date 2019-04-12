package com.mission.cricstat.ui.tableView.holder;

import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder;
import com.mission.cricstat.R;

/**
 * Created by evrencoskun on 1.12.2017.
 */

public class RowHeaderViewHolder extends AbstractViewHolder {
    public final TextView row_header_textview;

    public RowHeaderViewHolder(View p_jItemView) {
        super(p_jItemView);
        row_header_textview = p_jItemView.findViewById(R.id.row_header_textview);
    }

    @Override
    public void setSelected(SelectionState p_nSelectionState) {
        super.setSelected(p_nSelectionState);

        int nBackgroundColorId;
        int nForegroundColorId;
        int typeFace;

        if (p_nSelectionState == SelectionState.SELECTED) {
            nBackgroundColorId = android.R.color.white;
            nForegroundColorId = R.color.colorPrimaryLight;
            typeFace = Typeface.BOLD_ITALIC;

        } else if (p_nSelectionState == SelectionState.UNSELECTED) {
            nBackgroundColorId = R.color.colorPrimaryLight;
            nForegroundColorId = android.R.color.white;
            typeFace = Typeface.NORMAL;
        } else { // SelectionState.SHADOWED
            nBackgroundColorId = R.color.colorSecondaryLight;
            nForegroundColorId = android.R.color.white;
            typeFace = Typeface.BOLD;
        }

        itemView.setBackgroundColor(ContextCompat.getColor(itemView.getContext(),
                nBackgroundColorId));
        row_header_textview.setTextColor(ContextCompat.getColor(row_header_textview.getContext(),
                nForegroundColorId));
        row_header_textview.setTypeface(null, typeFace);
    }
}
