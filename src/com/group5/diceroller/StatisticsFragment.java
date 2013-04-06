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
import android.widget.ListView;
import android.widget.GridView;
import android.widget.BaseAdapter;
import android.os.Bundle;
import android.app.Activity;

public class StatisticsFragment extends Fragment {
    DiceRollerState state;

    ToggleButton change_view_button;
    View all_rolls;
    View last_roll;

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
        View layout = inflater.inflate(R.layout.statistics, container, false);

        change_view_button = (ToggleButton) (layout.findViewById(R.id.change_view_button));
        change_view_button.setOnClickListener(new ViewChangeOnClick());

        all_rolls = layout.findViewById(R.id.all_rolls_view);
        last_roll = layout.findViewById(R.id.last_roll_view);

        populateListStats(last_roll, state.activeSelection());

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
    private void populateListStats(View view, SetSelection selection) {
        ListView sumsview = (ListView) view.findViewById(R.id.dice_set_sums_list);
        GridView diceview = (GridView) view.findViewById(R.id.dice_rolls_grid);
        diceview.setAdapter(new DiceAdapter(selection));
    }

    /**
     * Updates all views related to dice statistics.
     */
    public void update() {
    }

    /**
     * Adapter for the dice in a selection.
     */
    class DiceAdapter extends BaseAdapter {
        ArrayList<Integer> values;
        ArrayList<Integer> faces;

        public DiceAdapter(SetSelection selection) {
            values = new ArrayList<Integer>();
            faces = new ArrayList<Integer>();
            for (DiceSet set : selection)
                for (Dice dice : set)
                    for (Integer val : dice)
                    {
                        values.add(val);
                        faces.add(dice.faces);
                    }
        }

        @Override
        public int getCount() {
            return values.size();
        }

        @Override
        public Object getItem(int position) {
            return values.get(position);
        }

        @Override
        public long getItemId(int position) {
            return (long)position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getActivity().getLayoutInflater();
            View layout = inflater.inflate(R.layout.dice_box, parent, false);

            TextView value_view = (TextView)layout.findViewById(R.id.die_value);
            value_view.setText(values.get(position).toString());

            TextView face_view = (TextView)layout.findViewById(R.id.die_face_count);
            face_view.setText(faces.get(position).toString());
            return layout;
        }
    }

    class ViewChangeOnClick implements View.OnClickListener {
        public void onClick(View v) {
            ToggleButton btn = (ToggleButton) v;
            if (btn.isChecked())
            {
                last_roll.setVisibility(View.VISIBLE);
                all_rolls.setVisibility(View.GONE);
            }
            else
            {
                last_roll.setVisibility(View.GONE);
                all_rolls.setVisibility(View.VISIBLE);
            }
        }
    }
}
