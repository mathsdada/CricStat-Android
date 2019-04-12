package com.mission.cricstat.ui.tableView;

import com.mission.cricstat.ui.tableView.model.CellModel;
import com.mission.cricstat.ui.tableView.model.ColumnHeaderModel;
import com.mission.cricstat.ui.tableView.model.RowHeaderModel;

import java.util.ArrayList;
import java.util.List;

public abstract class MyTableViewModel {
    private List<ColumnHeaderModel> mColumnHeaderModelList;
    private List<RowHeaderModel> mRowHeaderModelList;
    private List<List<CellModel>> mCellModelList;

    abstract List<ColumnHeaderModel> createColumnHeaderModelList();
    abstract List<RowHeaderModel> createRowHeaderList(ArrayList<Object> responses);
    abstract List<List<CellModel>> createCellModelList(ArrayList<Object> responses);

    public List<ColumnHeaderModel> getColumHeaderModeList() {
        return mColumnHeaderModelList;
    }

    public List<RowHeaderModel> getRowHeaderModelList() {
        return mRowHeaderModelList;
    }

    public List<List<CellModel>> getCellModelList() {
        return mCellModelList;
    }


    public void generateListForTableView(ArrayList<Object> responses) {
        mColumnHeaderModelList = createColumnHeaderModelList();
        mCellModelList = createCellModelList(responses);
        mRowHeaderModelList = createRowHeaderList(responses);
    }
}
