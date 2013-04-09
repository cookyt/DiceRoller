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
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.graphics.Color;
import android.util.Log;
import android.os.Bundle;
import android.app.Activity;

public class StatisticsFragment extends Fragment {
    DiceRollerState state;

    ToggleButton change_view_button;
    TextView no_statistics_message;
    ListView all_rolls;
    HistoryAdapter all_rolls_adapter;
    View last_roll;
    View last_roll_scroller;

    public static final String kTag = "StatisticsFrag";
    public static final int kDicePerRow = 5;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        state = DiceRollerState.getState();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        Log.i(kTag, "Creating view");

        View layout = inflater.inflate(R.layout.statistics, container, false);

        no_statistics_message = (TextView) (layout.findViewById(R.id.no_statistics_message));

        change_view_button = (ToggleButton) (layout.findViewById(R.id.change_view_button));
        change_view_button.setOnClickListener(new ViewChangeOnClick());

        last_roll_scroller = layout.findViewById(R.id.last_roll_scroller);
        last_roll = layout.findViewById(R.id.last_roll_view);

        ViewHolder holder = new ViewHolder();
        holder.rolls = (LinearLayout) (last_roll.findViewById(R.id.dice_rolls_grid));
        last_roll.setTag(holder);

        all_rolls = (ListView) layout.findViewById(R.id.all_rolls_view);
        all_rolls_adapter = new HistoryAdapter();
        all_rolls.setAdapter(all_rolls_adapter);

        if (last_roll.getVisibility() == View.VISIBLE && state.rollHistory().size() > 0)
            populateStatsRow(last_roll, state.rollHistory().get(0));

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
        ViewHolder holder = (ViewHolder) view.getTag();

        holder.rolls.removeAllViews();
        LayoutInflater inflater = getActivity().getLayoutInflater();
        GridCreator creator = new GridCreator(holder.rolls, kDicePerRow);

        for (DiceSet set : selection) {
            TextView description = new TextView(getActivity().getApplicationContext());
            description.setText(String.format("%s: %d", set.label(), set.sum()));
            description.setTextColor(Color.BLACK);

            LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
            float screen_density = getActivity().getApplicationContext().getResources().getDisplayMetrics().density;
            params.setMargins(0, (int)(15*screen_density), 0, 0);

            creator.addDirect(description, params);

            for (Dice dice : set) {
                for (Integer val : dice) {
                    View die = inflater.inflate(R.layout.dice_box, holder.rolls, false);

                    TextView value_view = (TextView) (die.findViewById(R.id.die_value));
                    value_view.setText("" + val);

                    TextView face_view = (TextView) (die.findViewById(R.id.die_face_count));
                    face_view.setText("" + dice.faces);

                    creator.add(die);
                }
            }
            creator.newRow();
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
        all_rolls_adapter.notifyDataSetChanged();
    }

    /**
     * Class for automatically adding to a table like you would a grid. Useful
     * to create "static" grids. i.e. grids that don't scroll and which can be
     * put in a scrollable list.
     */
    class GridCreator {
        LinearLayout grid;
        int row;
        int col;
        int max_cols;
        boolean add_direct;

        LinearLayout cur_row;

        public GridCreator(LinearLayout grid, int max_cols) {
            this.grid = grid;
            this.row = 0;
            this.col = 0;
            this.add_direct = false;
            this.max_cols = max_cols;
        }

        public void add(View v) {
            if (col%max_cols == 0 || add_direct) { 
                newRow();
                add_direct = false;
            }
            cur_row.addView(v);
            ++col;
        }

        public void addDirect(View v) {
            add_direct = true;
            grid.addView(v);
        }

        public void addDirect(View v, LayoutParams params) {
            add_direct = true;
            grid.addView(v, params);
        }

        public void newRow() {
            ++row;
            cur_row = new LinearLayout(getActivity().getApplicationContext());
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

    class HistoryAdapter extends ArrayAdapter<SetSelection> {
        public HistoryAdapter() {
            super(getActivity(), R.layout.statistics_row, state.rollHistory());
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            View row = convertView;
            if (row == null) {
                LayoutInflater inflater = getActivity().getLayoutInflater();
                row = inflater.inflate(R.layout.statistics_row, parent, false);

                ViewHolder holder = new ViewHolder();
                holder.rolls = (LinearLayout) (row.findViewById(R.id.dice_rolls_grid));

                row.setTag(holder);
            }
            populateStatsRow(row, state.rollHistory().get(position));
            return row;
        }
    }

    static class ViewHolder {
        LinearLayout rolls;
    }
}
