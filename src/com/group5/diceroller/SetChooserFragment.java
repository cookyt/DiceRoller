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
import android.widget.ListView;
import android.os.Bundle;
import android.util.Log;
import android.content.Intent;

public class SetChooserFragment extends ListFragment {
    public static final String kTag = "SetChooserFrag";
    public static final int kUpdateSets = 0;
    public static final String kIntentItemPosition = "ItemPosition";
    DiceRollerState state;
    OnSelectionChangedListener selection_changed_listener;
    DiceSelectionAdapter adapter;

    @Override
    /**
     * Creates an adapter for this list and attaches it when the activity is
     * created. Also adds the "add a set" button to the bottom of the list.
     */
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        adapter = new DiceSelectionAdapter();

        Button add_a_set = new Button(getActivity());
        add_a_set.setText("Add a Set");
        add_a_set.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivityForResult(new Intent(getActivity(), SetCreatorActivity.class), kUpdateSets);
            }
        });
        getListView().addFooterView(add_a_set);

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
            selection_changed_listener = (OnSelectionChangedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnSelectionChangedListener");
        }
        state = DiceRollerState.getState();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode != kUpdateSets)
            return;
        if (resultCode != Activity.RESULT_OK)
            return;
        Log.i(kTag, "notifying adapter explicitly");
        adapter.notifyDataSetChanged();
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
         * resources.
         *
         * @param position The position in the list being requested.
         * @return The view for the requested row.
         */
        public View getView(final int position, View convertView, ViewGroup parent) {
            Log.i(kTag, "Creating chooser item, pos:" + position);
            // Last item in the list is the "add a set button"

            View row = convertView;
            ToggleButton main_description;
            Button edit;
            if (row == null)
            {
                LayoutInflater inflater =  getActivity().getLayoutInflater();
                row = inflater.inflate(R.layout.chooser_row, parent, false);

                // ViewHolder trick
                // see: http://www.youtube.com/watch?v=N6YdwzAvwOA
                main_description = (ToggleButton) row.findViewById(R.id.main_description);
                edit = (Button) row.findViewById(R.id.edit_button);

                ViewHolder holder = new ViewHolder(main_description, edit);
                row.setTag(holder);
            }
            else
            {
                ViewHolder holder = (ViewHolder) row.getTag();
                main_description = holder.main_description;
                edit = holder.edit;
            }

            DiceSet cur_set = state.diceSets().get(position);
            String label = cur_set.label();

            main_description.setText(label);
            main_description.setTextOn(label);
            main_description.setTextOff(label);
            main_description.setChecked(state.activeSelection().contains(cur_set));
            main_description.setOnClickListener(new ToggleOnClickListener(position));

            edit.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), EditSetActivity.class);
                    intent.putExtra(kIntentItemPosition, position);
                    startActivityForResult(intent, kUpdateSets);
                }
            });
            return row;
        }

        class ViewHolder {
            public ViewHolder(ToggleButton main_description, Button edit) {
                this.main_description = main_description;
                this.edit = edit;
            }
            ToggleButton main_description;
            Button edit;
        }
    }

    /**
     * This listens for a click event on one of the set buttons to add/remove a
     * set from the current selection.
     */
    class ToggleOnClickListener implements View.OnClickListener {
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
                state.activeSelection().remove(described_set);
            }
            selection_changed_listener.onSelectionChanged();
        }
    }
}
