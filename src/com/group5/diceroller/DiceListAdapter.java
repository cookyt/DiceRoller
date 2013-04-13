package com.group5.diceroller;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.EditText;
import android.text.TextWatcher;
import android.text.Editable;

class DiceListAdapter extends ArrayAdapter<Dice> {

    FragmentActivity activity;
    List<Dice> dice;

    // Keeps track of what text field uses what watcher because there's no way
    // to remove all watchers from a text field, and text fields are reused for
    // different dice in the dice list, so setting the text of one
    // programmatically might cause changes to the wrong dice object.
    HashMap<EditText, TextWatcher> registered_watchers;

    public DiceListAdapter(FragmentActivity activity, List<Dice> dice) {
        super(activity, R.layout.dice_row, dice);
        this.activity = activity;
        this.dice = dice;
        this.registered_watchers = new HashMap<EditText, TextWatcher>();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null)
        {
            LayoutInflater inflater = activity.getLayoutInflater();
            convertView = inflater.inflate(R.layout.dice_row, null);
            holder = new ViewHolder();
            holder.count = (EditText) convertView.findViewById(R.id.dice_count);
            holder.faces = (TextView) convertView.findViewById(R.id.face_count);
            holder.delete_button = (Button) convertView.findViewById(R.id.delete_button);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
            holder.count.removeTextChangedListener(registered_watchers.get(holder.count));
            registered_watchers.remove(holder.count);
        }

        final Dice represented_dice = dice.get(position);
        holder.faces.setText("D" + represented_dice.faces);
        holder.count.setText("" + represented_dice.count);

        TextWatcher watcher = new TextWatcher() {
            public void afterTextChanged(Editable s) {
                int count = AddDiceDialogFragment.getNumFromEditable(s);
                represented_dice.count = count;
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Intentionally emtpy
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Intentionally emtpy
            }
        };
        holder.count.addTextChangedListener(watcher);
        registered_watchers.put(holder.count, watcher);

        final DiceListAdapter dice_adapter = this;
        holder.delete_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dice.remove(position);
                dice_adapter.notifyDataSetChanged();
            }
        });

        return convertView;
    }

    class ViewHolder {
        public EditText count;
        public TextView faces;
        public Button delete_button;
    }
}
