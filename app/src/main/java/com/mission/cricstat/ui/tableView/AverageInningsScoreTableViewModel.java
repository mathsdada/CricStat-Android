package com.mission.cricstat.ui.tableView;

import com.mission.cricstat.Common.StringUtil;
import com.mission.cricstat.Rest.Model.TeamStats.AverageInningsScoreResponse;
import com.mission.cricstat.ui.tableView.model.CellModel;
import com.mission.cricstat.ui.tableView.model.ColumnHeaderModel;
import com.mission.cricstat.ui.tableView.model.RowHeaderModel;

import java.util.ArrayList;
import java.util.List;

public class AverageInningsScoreTableViewModel extends MyTableViewModel {
    @Override
    List<ColumnHeaderModel> createColumnHeaderModelList() {
        List<ColumnHeaderModel>list = new ArrayList<>();

        list.add(new ColumnHeaderModel("I"));
        list.add(new ColumnHeaderModel("R"));
        list.add(new ColumnHeaderModel("Wk"));
        list.add(new ColumnHeaderModel("Win %"));
        return list;
    }

    @Override
    List<RowHeaderModel> createRowHeaderList(ArrayList<Object> responses) {
        List<RowHeaderModel> list = new ArrayList<>();

        for (Object response : responses) {
            list.add(new RowHeaderModel(StringUtil.toCamelCase("Innings-" + ((AverageInningsScoreResponse)response).getInningsNum())));
        }
        return list;
    }

    @Override
    List<List<CellModel>> createCellModelList(ArrayList<Object> responses) {
        List<List<CellModel>> lists = new ArrayList<>();

        for (int index = 0; index < responses.size(); index++) {
            AverageInningsScoreResponse response = (AverageInningsScoreResponse) responses.get(index);

            List<CellModel> list = new ArrayList<>();
            list.add(new CellModel("1-"+index, response.getInnings()));
            list.add(new CellModel("2-"+index, response.getRuns()));
            list.add(new CellModel("3-"+index, response.getWickets()));
            list.add(new CellModel("4-"+index, response.getWinPercentage()));
            lists.add(list);
        }
        return lists;
    }
}
