package com.group5.diceroller;

import java.util.List;
import java.util.ArrayList;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ToggleButton;
import android.os.Bundle;
import android.app.Activity;

public class StatisticsFragment extends Fragment {
    DiceRollerState state;

    ToggleButton change_view_button;
    View all_rolls;
    View last_roll;

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
        change_view_button = (ToggleButton)layout.findViewById(R.id.change_view_button);
        change_view_button.setOnClickListener(new ViewChangeOnClick());

        all_rolls = layout.findViewById(R.id.all_rolls_view);
        last_roll = layout.findViewById(R.id.last_roll_view);
        return layout;
    }

    /**
     * Updates all views related to dice statistics.
     */
    public void update() {
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
