package com.group5.diceroller;

import java.util.List;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
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
        String[] values = { 
            "Android", "iPhone", "WindowsMobile", "Blackberry", "WebOS",
            "Ubuntu", "Windows7", "Max OS X", "Linux", "OS/2"
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
            android.R.layout.simple_list_item_1, values);
        setListAdapter(adapter);
    }

}
