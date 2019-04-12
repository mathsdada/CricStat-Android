package com.mission.cricstat;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.mission.cricstat.Common.Constants;
import com.mission.cricstat.Common.StringUtil;

public class Filter {
    private Activity mContext;

    private String mTeam;
    private String mFormat;
    private String mVenue;
    private String mOpponent;
    private String mNumMatches;

    private String[] mTeamArray;
    private String[] mFormatArray;
    private String[] mVenueArray;
    private String[] mOpponentArray;
    private String[] mNumMatchesArray;


    public Filter(Activity context) {
        mContext = context;
    }

    public interface SelectionListener {
        void onSelectionListener(String team, String format, String venue, String opponent, String numMatches);
    }
    public void setDefaultSelection(String team, String format, String venue, String opponent, String numMatches) {
        mTeam = team;
        mFormat = format;
        mVenue = venue;
        mOpponent = opponent;
        mNumMatches = numMatches;
    }

    public void setSelectionArray(String[] teamArray, String[] formatArray, String[] venueArray,
                                  String[] opponentArray, String[] numMatchesArray) {
        mTeamArray = teamArray;
        mFormatArray = formatArray;
        mVenueArray = venueArray;
        mOpponentArray = opponentArray;
        mNumMatchesArray = numMatchesArray;
    }

    public void getSelection(final SelectionListener listener) {
        final String[] teamSelected = new String[1];
        final String[] formatSelected = new String[1];
        final String[] venueSelected = new String[1];
        final String[] opponentSelected = new String[1];
        final String[] numMatchesSelected = new String[1];
        View view = mContext.getLayoutInflater().inflate(R.layout.layout_filter, null);
        Spinner teamSpinner = view.findViewById(R.id.team_spinner);
        Spinner formatSpinner = view.findViewById(R.id.format_spinner);
        Spinner venueSpinner = view.findViewById(R.id.venue_spinner);
        Spinner oppTeamSpinner = view.findViewById(R.id.opp_team_spinner);
        Spinner numMatchesSpinner = view.findViewById(R.id.num_matches_spinner);

        if (mTeamArray != null) {
            setupSpinner(teamSpinner, mTeamArray, mTeam, true);
        }
        if (mFormatArray != null) {
            setupSpinner(formatSpinner, mFormatArray, mFormat, false);
        }
        if (mVenueArray != null) {
            setupSpinner(venueSpinner, mVenueArray, mVenue, true);
        }
        if (mOpponentArray != null) {
            setupSpinner(oppTeamSpinner, mOpponentArray, mOpponent, true);
        }
        if (mNumMatchesArray != null) {
            setupSpinner(numMatchesSpinner, mNumMatchesArray, mNumMatches, false);
        }

        teamSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                teamSelected[0] = mTeamArray[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                teamSelected[0] = mTeam;
            }
        });
        formatSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                formatSelected[0] = mFormatArray[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                formatSelected[0] = mFormat;
            }
        });
        venueSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                venueSelected[0] = mVenueArray[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                venueSelected[0] = mVenue;
            }
        });
        oppTeamSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                opponentSelected[0] = mOpponentArray[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                opponentSelected[0] = mOpponent;
            }
        });
        numMatchesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                numMatchesSelected[0] = mNumMatchesArray[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                numMatchesSelected[0] = mNumMatches;
            }
        });

        new AlertDialog.Builder(mContext).setView(view)
                .setPositiveButton("Apply", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (mTeamArray != null) {
                            mTeam = StringUtil.toCamelCase(teamSelected[0]);
                        }
                        if (mVenueArray != null) {
                            mVenue = StringUtil.toCamelCase(venueSelected[0]);
                            if (mVenue.toUpperCase().equals(Constants.SPINNER_ITEM_ALL)) mVenue = null;
                        }
                        if (mOpponentArray != null) {
                            mOpponent = StringUtil.toCamelCase(opponentSelected[0]);
                            if (mOpponent.toUpperCase().equals(Constants.SPINNER_ITEM_ALL)) mOpponent = null;
                        }
                        if (mFormatArray != null) {
                            mFormat = formatSelected[0].toLowerCase();
                            if (mFormat.toUpperCase().equals(Constants.SPINNER_ITEM_ALL)) mFormat = null;
                        }
                        if (mNumMatchesArray != null) {
                            mNumMatches = numMatchesSelected[0].toLowerCase();
                        }
                        if (listener != null) {
                            listener.onSelectionListener(mTeam, mFormat, mVenue, mOpponent, mNumMatches);
                        }
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void setupSpinner(Spinner spinner, String[] data, String defaultItem, boolean camelCaseSearch) {
        if (data == null) return;
        ArrayAdapter<String> adapter = new ArrayAdapter<>(mContext, R.layout.spineer_item_layout,
                R.id.format, data);
        spinner.setAdapter(adapter);
        if (defaultItem != null) {
            if (camelCaseSearch) defaultItem = StringUtil.toCamelCase(defaultItem);
            else defaultItem = defaultItem.toUpperCase();
            spinner.setSelection(adapter.getPosition(defaultItem));
        }

    }
}
