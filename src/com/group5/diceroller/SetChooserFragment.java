package com.group5.diceroller;

import java.util.List;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.os.Bundle;

public class SetChooserFragment extends Fragment {
    private List<DiceSet> dice_sets;

    public SetChooserFragment() {
        dice_sets = DiceSet.LoadAllFromDB();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        return inflater.inflate(R.layout.chooser, container, false);
    }
}
