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
    private List<DiceSet> dice_sets;

    public SetChooserFragment() {
        dice_sets = DiceSet.LoadAllFromDB();
        dice_sets.add(null); // add sentinal value to denote the "add a set" button
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        DiceSelectionAdapter adapter = new DiceSelectionAdapter();
        setListAdapter(adapter);
    }


    public class DiceSelectionAdapter extends ArrayAdapter<DiceSet> {

        public DiceSelectionAdapter() {
            super(getActivity(), R.layout.chooser_row, dice_sets);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row;

            // Last item in the list is the "add a set button"
            if (position == dice_sets.size()-1) {
                row = new Button(getActivity());
                ((Button) row).setText("Add a Set");
            } else {
                LayoutInflater inflater =  getActivity().getLayoutInflater();
                row = inflater.inflate(R.layout.chooser_row, parent, false);

                ToggleButton main_description = (ToggleButton) row.findViewById(R.id.main_description);
                main_description.setText(dice_sets.get(position).name());
                main_description.setTextOn(dice_sets.get(position).name());
                main_description.setTextOff(dice_sets.get(position).name());
            }
            return row;
        }
    }
}
