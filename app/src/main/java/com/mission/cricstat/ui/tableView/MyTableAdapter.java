package com.mission.cricstat.ui.tableView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.evrencoskun.tableview.adapter.AbstractTableAdapter;
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractSorterViewHolder;
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder;
import com.mission.cricstat.Common.StatsCategory;
import com.mission.cricstat.R;
import com.mission.cricstat.ui.tableView.holder.CellViewHolder;
import com.mission.cricstat.ui.tableView.holder.ColumnHeaderViewHolder;
import com.mission.cricstat.ui.tableView.holder.RowHeaderViewHolder;
import com.mission.cricstat.ui.tableView.model.CellModel;
import com.mission.cricstat.ui.tableView.model.ColumnHeaderModel;
import com.mission.cricstat.ui.tableView.model.RowHeaderModel;

import java.util.ArrayList;

public class MyTableAdapter extends AbstractTableAdapter<ColumnHeaderModel, RowHeaderModel, CellModel> {

    private MyTableViewModel myTableViewModel;

    public MyTableAdapter(Context p_jContext, String statsCategory) {
        super(p_jContext);

        switch (statsCategory) {
            case StatsCategory.PER_INN_AVG_SCORE: {
                this.myTableViewModel = new AverageInningsScoreTableViewModel();
                break;
            }
            case StatsCategory.BATTING_MOST_RUNS:
            case StatsCategory.BATTING_BEST_AVG:
            case StatsCategory.BATTING_BEST_SR:
            case StatsCategory.BATTING_MOST_4S:
            case StatsCategory.BATTING_MOST_6S:
            case StatsCategory.BATTING_MOST_50S:
            case StatsCategory.BATTING_MOST_100S:
            case StatsCategory.BATTING_MOST_DUCKS: {
                this.myTableViewModel = new TeamBattingStatsTableViewModel();
                break;
            }
            case StatsCategory.BOWLING_MOST_WICKETS:
            case StatsCategory.BOWLING_MOST_MAIDENS:
            case StatsCategory.BOWLING_MOST_4_PLUS:
            case StatsCategory.BOWLING_MOST_5_PLUS:
            case StatsCategory.BOWLING_BEST_AVERAGE:
            case StatsCategory.BOWLING_BEST_SR:
            case StatsCategory.BOWLING_BEST_ECONOMY: {
                this.myTableViewModel = new TeamBowlingStatsTableViewModel();
                break;
            }
            default: {
                assert false;
                break;
            }
        }
    }


    @Override
    public AbstractViewHolder onCreateCellViewHolder(ViewGroup parent, int viewType) {
        View layout;
        // Get default Cell xml Layout
        layout = LayoutInflater.from(mContext).inflate(R.layout.tableview_cell_layout,
                parent, false);
        // Create a Cell ViewHolder
        return new CellViewHolder(layout);
    }

    @Override
    public void onBindCellViewHolder(AbstractViewHolder holder, Object p_jValue, int
            p_nXPosition, int p_nYPosition) {
        CellModel cell = (CellModel) p_jValue;
        // Get the holder to update cell item text
        ((CellViewHolder) holder).setCellModel(cell, p_nXPosition);
    }

    @Override
    public AbstractSorterViewHolder onCreateColumnHeaderViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(mContext).inflate(R.layout
                .tableview_column_header_layout, parent, false);

        return new ColumnHeaderViewHolder(layout, getTableView());
    }

    @Override
    public void onBindColumnHeaderViewHolder(AbstractViewHolder holder, Object p_jValue, int
            p_nXPosition) {
        ColumnHeaderModel columnHeader = (ColumnHeaderModel) p_jValue;

        // Get the holder to update cell item text
        ColumnHeaderViewHolder columnHeaderViewHolder = (ColumnHeaderViewHolder) holder;

        columnHeaderViewHolder.setColumnHeaderModel(columnHeader, p_nXPosition);
    }

    @Override
    public AbstractViewHolder onCreateRowHeaderViewHolder(ViewGroup parent, int viewType) {

        // Get Row Header xml Layout
        View layout = LayoutInflater.from(mContext).inflate(R.layout.tableview_row_header_layout,
                parent, false);

        // Create a Row Header ViewHolder
        return new RowHeaderViewHolder(layout);
    }

    @Override
    public void onBindRowHeaderViewHolder(AbstractViewHolder holder, Object p_jValue, int
            p_nYPosition) {

        RowHeaderModel rowHeaderModel = (RowHeaderModel) p_jValue;

        RowHeaderViewHolder rowHeaderViewHolder = (RowHeaderViewHolder) holder;
        rowHeaderViewHolder.row_header_textview.setText(rowHeaderModel.getData());

    }

    @Override
    public View onCreateCornerView() {
        return LayoutInflater.from(mContext).inflate(R.layout.tableview_corner_layout, null, false);
    }

    @Override
    public int getColumnHeaderItemViewType(int position) {
        return 0;
    }

    @Override
    public int getRowHeaderItemViewType(int position) {
        return 0;
    }

    @Override
    public int getCellItemViewType(int position) {
        return 0;
    }


    /**
     * This method is not a generic Adapter method. It helps to generate lists from single user
     * list for this adapter.
     */
    public void setResponseList(ArrayList<Object> responses) {
        // Generate the lists that are used to TableViewAdapter
        myTableViewModel.generateListForTableView(responses);

        // Now we got what we need to show on TableView.
        setAllItems(myTableViewModel.getColumHeaderModeList(), myTableViewModel
                .getRowHeaderModelList(), myTableViewModel.getCellModelList());
    }

}
