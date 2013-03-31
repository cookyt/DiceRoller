package com.group5.diceroller;

import java.util.List;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ArrayAdapter;
import android.os.Bundle;

public class SetChooserFragment extends ListFragment {
    private List<DiceSet> dice_sets;

    public SetChooserFragment() {
        dice_sets = DiceSet.LoadAllFromDB();
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
            LayoutInflater inflater =  getActivity().getLayoutInflater();
            View row = inflater.inflate(R.layout.chooser_row, parent, false);
            TextView main_description = (TextView) row.findViewById(R.id.main_description);
            main_description.setText(dice_sets.get(position).name());
            return row;
        }
    }
}
