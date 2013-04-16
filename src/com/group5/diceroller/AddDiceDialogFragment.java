package com.group5.diceroller;

import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.view.ViewGroup;
import android.view.View;
import android.view.LayoutInflater;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.text.Editable;
import android.util.Log;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnShowListener;

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

        final Dialog dialog = builder.create();
        dialog.setOnShowListener(new OnShowListener() {
            public void onShow(DialogInterface d) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(user_input, InputMethodManager.SHOW_IMPLICIT);
            }
        });
        return dialog;
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
