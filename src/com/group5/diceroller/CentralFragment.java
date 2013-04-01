package com.group5.diceroller;

import android.support.v4.app.Fragment;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.os.Bundle;

public class CentralFragment extends Fragment {
    OnDiceRolledListener rolled_listener;
    DiceRollerState state;
    TextView selection_text;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            rolled_listener = (OnDiceRolledListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnDiceRolledListener");
        }

        try {
            state = (DiceRollerState) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement DiceRollerState");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.central, container, false);
        Button roll_button = (Button) layout.findViewById(R.id.roll_button);
        selection_text = (TextView) layout.findViewById(R.id.selection_text);
        roll_button.setOnClickListener(new RollEvent());
        return layout;
    }

    /**
     * Re-evaluates the dice in the active selection and sets the label for the
     * central view accordingly. If there are no dice in the selection, it
     * defaults to the global empty_selection string in the resources.
     */
    public void updateSelectionText() {
        String des = state.activeSelection().toString();
        if (des.length() > 0)
            selection_text.setText(des);
        else
            selection_text.setText(R.string.empty_selection);
    }

    public class RollEvent implements View.OnClickListener {
        public void onClick(View v) {
            state.activeSelection().roll();
            rolled_listener.onDiceRolled();
        }
    }

}
