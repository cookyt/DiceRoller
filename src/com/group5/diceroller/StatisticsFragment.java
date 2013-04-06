package com.group5.diceroller;

import java.util.List;
import java.util.ArrayList;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ToggleButton;
import android.widget.TextView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.ListView;
import android.widget.GridView;
import android.widget.BaseAdapter;
import android.util.Log;
import android.os.Bundle;
import android.app.Activity;

public class StatisticsFragment extends Fragment {
    DiceRollerState state;

    ToggleButton change_view_button;
    TextView no_statistics_message;
    View all_rolls;
    View last_roll;
    View last_roll_scroller;

    public static final String kTag = "StatisticsFrag";

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            state = (DiceRollerState) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement DiceRollerState");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        Log.i(kTag, "Creating view");

        View layout = inflater.inflate(R.layout.statistics, container, false);

        no_statistics_message = (TextView) (layout.findViewById(R.id.no_statistics_message));

        change_view_button = (ToggleButton) (layout.findViewById(R.id.change_view_button));
        change_view_button.setOnClickListener(new ViewChangeOnClick());

        all_rolls = layout.findViewById(R.id.all_rolls_view);
        last_roll = layout.findViewById(R.id.last_roll_view);
        last_roll_scroller = layout.findViewById(R.id.last_roll_scroller);

        chooseChangeButtonEnable();
        return layout;
    }

    /**
     * Takes a view of the form described by statistics_row.xml, and a
     * SetSelection, and creates/attaches the proper adapters to its internal
     * listviews.
     *
     * @param view The view to populate.
     * @param selection The selection from which to pull data.
     */
    private void populateStatsRow(View view, SetSelection selection) {
        Log.i(kTag, "Populating last roll view");

        LinearLayout sumsview = (LinearLayout) view.findViewById(R.id.dice_set_sums_list);
        for (DiceSet set : selection) {
            TextView description = new TextView(getActivity().getApplicationContext());
            description.setText(String.format("%s: %d", set.label(), set.sum()));
            sumsview.addView(description);
        }

        TableLayout diceview = (TableLayout) view.findViewById(R.id.dice_rolls_grid);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        GridCreator creator = new GridCreator(diceview, 3);
        for (DiceSet set : selection) {
            for (Dice dice : set) {
                for (Integer val : dice) {
                    View die = inflater.inflate(R.layout.dice_box, diceview, false);

                    TextView value_view = (TextView) (die.findViewById(R.id.die_value));
                    value_view.setText("" + val);

                    TextView face_view = (TextView) (die.findViewById(R.id.die_face_count));
                    face_view.setText(dice.faces);

                    creator.add(die);
                }
            }
        }
    }

    private void chooseChangeButtonEnable() {
        boolean enable_button;

        if (state.rollHistory().size() > 0) {
            no_statistics_message.setVisibility(View.GONE);
            change_view_button.setEnabled(true);

            if (change_view_button.isChecked()) {
                all_rolls.setVisibility(View.GONE);
                last_roll_scroller.setVisibility(View.VISIBLE);
            } else {
                all_rolls.setVisibility(View.VISIBLE);
                last_roll_scroller.setVisibility(View.GONE);
            }

        } else {
            no_statistics_message.setVisibility(View.VISIBLE);
            change_view_button.setEnabled(false);

            all_rolls.setVisibility(View.GONE);
            last_roll_scroller.setVisibility(View.GONE);
        }
    }

    /**
     * Updates all views related to dice statistics.
     */
    public void update() {
        chooseChangeButtonEnable();
        populateStatsRow(last_roll, state.rollHistory().get(0));
    }

    /**
     * Class for automatically adding to a table like you would a grid. Useful
     * to create "static" grids. i.e. grids that don't scroll and which can be
     * put in a scrollable list.
     */
    class GridCreator {
        TableLayout grid;
        int row;
        int col;
        int max_cols;

        TableRow cur_row;

        public GridCreator(TableLayout grid, int max_cols) {
            this.grid = grid;
            this.row = 0;
            this.col = 0;
            this.max_cols = max_cols;
        }

        public void add(View v) {
            if (col%max_cols == 0) { 
                newRow();
            }
            cur_row.addView(v);
            ++col;
        }

        public void newRow() {
            ++row;
            TableRow cur_row = new TableRow(getActivity().getApplicationContext());
            grid.addView(cur_row);
        }
    }

    class ViewChangeOnClick implements View.OnClickListener {
        public void onClick(View v) {
            ToggleButton btn = (ToggleButton) v;
            if (btn.isChecked())
            {
                last_roll_scroller.setVisibility(View.VISIBLE);
                all_rolls.setVisibility(View.GONE);
            }
            else
            {
                last_roll_scroller.setVisibility(View.GONE);
                all_rolls.setVisibility(View.VISIBLE);
            }
        }
    }
}
