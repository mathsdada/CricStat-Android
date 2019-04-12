package com.mission.cricstat.ui.tableView.holder;

import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.evrencoskun.tableview.ITableView;
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractSorterViewHolder;
import com.mission.cricstat.R;
import com.mission.cricstat.ui.tableView.model.ColumnHeaderModel;

/**
 * Created by evrencoskun on 1.12.2017.
 */

public class ColumnHeaderViewHolder extends AbstractSorterViewHolder {
    final LinearLayout column_header_container;
    final TextView column_header_textview;
    final ITableView tableView;

    public ColumnHeaderViewHolder(View itemView, ITableView pTableView) {
        super(itemView);
        tableView = pTableView;
        column_header_textview = itemView.findViewById(R.id.column_header_textView);
        column_header_container = itemView.findViewById(R.id.column_header_container);
    }

    public void setColumnHeaderModel(ColumnHeaderModel pColumnHeaderModel, int pColumnPosition) {
        // Change alignment of textView
        column_header_textview.setGravity(Gravity.CENTER | Gravity.CENTER_VERTICAL);

        // Set text data
        column_header_textview.setText(pColumnHeaderModel.getData());

        // It is necessary to remeasure itself.
        column_header_container.getLayoutParams().width = LinearLayout.LayoutParams.WRAP_CONTENT;
        column_header_textview.requestLayout();
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

        column_header_container.setBackgroundColor(ContextCompat.getColor(column_header_container
                .getContext(), nBackgroundColorId));
        column_header_textview.setTextColor(ContextCompat.getColor(column_header_container
                .getContext(), nForegroundColorId));
        column_header_textview.setTypeface(null, typeFace);
    }
}
