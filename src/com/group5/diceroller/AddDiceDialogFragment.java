package com.group5.diceroller;

import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.view.ViewGroup;
import android.view.View;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.content.DialogInterface;
import android.text.Editable;
import android.util.Log;

/**
 * Fragment for a context dialog which asks the user to select a face count for
 * a dice.
 *
 * @Author Carlos Valera
 */
public class AddDiceDialogFragment extends DialogFragment {
    public interface AddDiceListener {
        public void addDice(int num_faces);
    }
    
    public static final String kTag = "AddDiceDialog";
    AddDiceListener dice_adder;

    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            dice_adder = (AddDiceListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement AddDiceListener");
        }
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final EditText user_input = (EditText) (getActivity().getLayoutInflater().inflate(R.layout.add_dice, null));

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.add_dice_dialog_title);
        builder.setView(user_input);
        builder.setPositiveButton(R.string.add_dice_ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dice_adder.addDice(getNumFromEditable(user_input.getText()));
            }
        });
        builder.setNegativeButton(R.string.add_dice_cancel, null);
        return builder.create();
    }

    public static int getNumFromEditable(Editable e) {
        int num;
        String instr = e.toString();
        if (instr.length() == 0) {
            num = 0;
        } else {
            try {
                num = Integer.parseInt(instr);
            } catch(NumberFormatException ex) {
                Log.e(kTag, "Invalid number: " + instr);
                return 0;
            }
        }
        return num;
    }
}
