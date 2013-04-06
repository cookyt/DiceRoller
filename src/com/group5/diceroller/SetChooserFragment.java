package com.group5.diceroller;

import java.util.List;
import android.support.v4.app.ListFragment;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ToggleButton;
import android.widget.Button;
import android.widget.ArrayAdapter;
import android.os.Bundle;
import android.util.Log;

public class SetChooserFragment extends ListFragment {
    DiceRollerState state;
    OnSelectionChangedListener selection_changed_listener;
    public static final String kTag = "SetChooserFrag";

    @Override
    /**
     * Creates an adapter for this list and attaches it when the activity is
     * created.
     */
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        DiceSelectionAdapter adapter = new DiceSelectionAdapter();
        setListAdapter(adapter);
    }

    @Override
    /**
     * Assures that the containing activity implements the DiceRollerState and
     * OnSelectionChangedListener interfaces.
     */
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            state = (DiceRollerState) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement DiceRollerState");
        }

        try {
            selection_changed_listener = (OnSelectionChangedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnSelectionChangedListener");
        }
    }

    /**
     * Adapter for the list fragment. It adapts elements from the list of
     * available DiceSets from the containing activity.
     */
    public class DiceSelectionAdapter extends ArrayAdapter<DiceSet> {

        public DiceSelectionAdapter() {
            super(getActivity(), R.layout.chooser_row, state.diceSets());
        }

        @Override
        /**
         * Returns the view to be used for the position'th value in the list.
         * For common element, it uses the chooser_row layout defined in global
         * resources. For the last element, it generates a large button with
         * "Add a Set" in it. For this reason, the last element in the list of
         * DiceSets should be some sort of unused sentinal (null works nicely).
         *
         * TODO implementation inflates a new view on every call (ineffecient),
         * should check for convertView==null
         *
         * @param position The position in the list being requested.
         * @return The view for the requested row.
         */
        public View getView(int position, View convertView, ViewGroup parent) {
            View row;

            Log.i(kTag, "Creating chooser item, pos:" + position);
            // Last item in the list is the "add a set button"
            if (position == state.diceSets().size()-1) {
                row = new Button(getActivity());
                ((Button) row).setText("Add a Set");
            } else {
                LayoutInflater inflater =  getActivity().getLayoutInflater();
                row = inflater.inflate(R.layout.chooser_row, parent, false);

                ToggleButton main_description = (ToggleButton) row.findViewById(R.id.main_description);

                DiceSet cur_set = state.diceSets().get(position);
                String label = cur_set.label();

                main_description.setText(label);
                main_description.setTextOn(label);
                main_description.setTextOff(label);
                main_description.setChecked(state.activeSelection().contains(cur_set));

                main_description.setOnClickListener(new ToggleOnClickListener(position));
            }
            return row;
        }
    }

    /**
     * This listens for a click event on one of the set buttons to add/remove a
     * set from the current selection.
     */
    public class ToggleOnClickListener implements View.OnClickListener {
        DiceSet described_set;

        public ToggleOnClickListener(int position) {
            described_set = state.diceSets().get(position);
        }

        /**
         * Adds or removes a set from the selection (depending on whether the
         * button is being checked or unchecked), and calls the
         * onSelectionChanged callback of the containing activity for the list
         * fragment
         *
         * @param v The button being pressed.
         */
        public void onClick(View v) {
            ToggleButton btn = (ToggleButton) v;
            if (btn.isChecked()) {
                state.activeSelection().add(described_set);
            } else {
                state.activeSelection().remove(described_set.id);
            }
            selection_changed_listener.onSelectionChanged();
        }
    }
}
