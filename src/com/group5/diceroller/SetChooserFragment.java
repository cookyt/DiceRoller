package com.group5.diceroller;

import java.util.List;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ToggleButton;
import android.widget.Button;
import android.widget.ArrayAdapter;
import android.os.Bundle;

public class SetChooserFragment extends ListFragment {
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        DiceSelectionAdapter adapter = new DiceSelectionAdapter();
        setListAdapter(adapter);
    }


    public class DiceSelectionAdapter extends ArrayAdapter<DiceSet> {

        public DiceSelectionAdapter() {
            super(getActivity(), R.layout.chooser_row, ((DiceRollerActivity) getActivity()).dice_sets);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row;
            DiceRollerActivity mActivity = (DiceRollerActivity) getActivity();
            List<DiceSet> dice_sets = mActivity.dice_sets;

            // Last item in the list is the "add a set button"
            if (position == dice_sets.size()-1) {
                row = new Button(mActivity);
                ((Button) row).setText("Add a Set");
            } else {
                LayoutInflater inflater =  mActivity.getLayoutInflater();
                row = inflater.inflate(R.layout.chooser_row, parent, false);

                ToggleButton main_description = (ToggleButton) row.findViewById(R.id.main_description);
                main_description.setText(dice_sets.get(position).name());
                main_description.setTextOn(dice_sets.get(position).name());
                main_description.setTextOff(dice_sets.get(position).name());

                main_description.setOnClickListener(new ToggleOnClickListener(position));
            }
            return row;
        }
    }

    public class ToggleOnClickListener implements View.OnClickListener {
        DiceSet described_set;
        DiceRollerActivity activity;

        public ToggleOnClickListener(int position) {
            activity = (DiceRollerActivity) getActivity();
            described_set = activity.dice_sets.get(position);
        }

        public void onClick(View v) {
            ToggleButton btn = (ToggleButton) v;
            if (btn.isChecked()) {
                activity.active_selection.add(described_set);
            } else {
                activity.active_selection.remove(described_set.id);
            }
        }
    }
}
