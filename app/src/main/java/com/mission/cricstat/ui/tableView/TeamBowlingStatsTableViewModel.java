package com.mission.cricstat.ui.tableView;

import com.mission.cricstat.Common.StringUtil;
import com.mission.cricstat.Rest.Model.TeamStats.TeamBattingStatsResponse;
import com.mission.cricstat.Rest.Model.TeamStats.TeamBowlingStatsResponse;
import com.mission.cricstat.ui.tableView.model.CellModel;
import com.mission.cricstat.ui.tableView.model.ColumnHeaderModel;
import com.mission.cricstat.ui.tableView.model.RowHeaderModel;

import java.util.ArrayList;
import java.util.List;

public class TeamBowlingStatsTableViewModel extends MyTableViewModel {
    @Override
    List<ColumnHeaderModel> createColumnHeaderModelList() {
        List<ColumnHeaderModel>list = new ArrayList<>();

        list.add(new ColumnHeaderModel("I"));
        list.add(new ColumnHeaderModel("W"));
        list.add(new ColumnHeaderModel("R"));
        list.add(new ColumnHeaderModel("B"));
        list.add(new ColumnHeaderModel("M"));
        list.add(new ColumnHeaderModel("Eco"));
        list.add(new ColumnHeaderModel("SR"));
        list.add(new ColumnHeaderModel("AVG"));
        list.add(new ColumnHeaderModel("4+ Wkts"));
        list.add(new ColumnHeaderModel("5+ Wkts"));
        return list;
    }

    @Override
    List<RowHeaderModel> createRowHeaderList(ArrayList<Object> responses) {
        List<RowHeaderModel> list = new ArrayList<>();

        for (Object response : responses) {
            list.add(new RowHeaderModel(StringUtil.toCamelCase(((TeamBowlingStatsResponse)response).getBowler())));
        }
        return list;
    }

    @Override
    List<List<CellModel>> createCellModelList(ArrayList<Object> responses) {
        List<List<CellModel>> lists = new ArrayList<>();

        for (int index = 0; index < responses.size(); index++) {
            TeamBowlingStatsResponse response = (TeamBowlingStatsResponse)responses.get(index);

            List<CellModel> list = new ArrayList<>();
            list.add(new CellModel("1-"+index, response.getNumInnings()));
            list.add(new CellModel("2-"+index, response.getWickets()));
            list.add(new CellModel("3-"+index, response.getRuns()));
            list.add(new CellModel("4-"+index, response.getBalls()));
            list.add(new CellModel("5-"+index, response.getMaidens()));
            list.add(new CellModel("6-"+index, response.getEconomy()));
            list.add(new CellModel("7-"+index, response.getStrikeRate()));
            list.add(new CellModel("8-"+index, response.getAverage()));
            list.add(new CellModel("9-"+index, response.getFourPlusWickets()));
            list.add(new CellModel("10-"+index, response.getFivePlusWickets()));
            lists.add(list);
        }
        return lists;
    }
}
