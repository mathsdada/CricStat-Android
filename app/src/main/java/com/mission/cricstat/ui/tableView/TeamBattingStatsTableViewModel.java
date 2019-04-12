package com.mission.cricstat.ui.tableView;

import com.mission.cricstat.Common.StringUtil;
import com.mission.cricstat.Rest.Model.TeamStats.TeamBattingStatsResponse;
import com.mission.cricstat.ui.tableView.model.CellModel;
import com.mission.cricstat.ui.tableView.model.ColumnHeaderModel;
import com.mission.cricstat.ui.tableView.model.RowHeaderModel;

import java.util.ArrayList;
import java.util.List;

public class TeamBattingStatsTableViewModel extends MyTableViewModel {
    @Override
    List<ColumnHeaderModel> createColumnHeaderModelList() {
        List<ColumnHeaderModel>list = new ArrayList<>();

        list.add(new ColumnHeaderModel("I"));
        list.add(new ColumnHeaderModel("R"));
        list.add(new ColumnHeaderModel("B"));
        list.add(new ColumnHeaderModel("AVG"));
        list.add(new ColumnHeaderModel("SR"));
        list.add(new ColumnHeaderModel("HS"));
        list.add(new ColumnHeaderModel("4s"));
        list.add(new ColumnHeaderModel("6s"));
        list.add(new ColumnHeaderModel("Ducks"));
        list.add(new ColumnHeaderModel("50s"));
        list.add(new ColumnHeaderModel("100s"));
        list.add(new ColumnHeaderModel("NO"));
        return list;
    }

    @Override
    List<RowHeaderModel> createRowHeaderList(ArrayList<Object> responses) {
        List<RowHeaderModel> list = new ArrayList<>();

        for (Object response : responses) {
            list.add(new RowHeaderModel(StringUtil.toCamelCase(((TeamBattingStatsResponse)response).getBatsman())));
        }
        return list;
    }

    @Override
    List<List<CellModel>> createCellModelList(ArrayList<Object> responses) {
        List<List<CellModel>> lists = new ArrayList<>();

        for (int index = 0; index < responses.size(); index++) {
            TeamBattingStatsResponse response = (TeamBattingStatsResponse)responses.get(index);

            List<CellModel> list = new ArrayList<>();
            list.add(new CellModel("1-"+index, response.getNumInnings()));
            list.add(new CellModel("2-"+index, response.getRuns()));
            list.add(new CellModel("3-"+index, response.getBalls()));
            list.add(new CellModel("4-"+index, response.getAverage()));
            list.add(new CellModel("5-"+index, response.getStrikeRate()));
            list.add(new CellModel("6-"+index, response.getHighScore()));
            list.add(new CellModel("7-"+index, response.getFours()));
            list.add(new CellModel("8-"+index, response.getSixes()));
            list.add(new CellModel("9-"+index, response.getDucks()));
            list.add(new CellModel("10-"+index, response.getFifties()));
            list.add(new CellModel("11-"+index, response.getHundreds()));
            list.add(new CellModel("12-"+index, response.getNotOuts()));
            lists.add(list);
        }
        return lists;
    }
}
