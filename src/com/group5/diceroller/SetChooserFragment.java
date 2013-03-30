package com.group5.diceroller;

import java.util.List;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.os.Bundle;

public class SetChooserFragment extends Fragment {
    private List<DiceSet> dice_sets;

    public SetChooserFragment() {
        dice_sets = DiceSet.LoadAllFromDB();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.chooser, container, false);

        String[] values = { 
            "Android", "iPhone", "WindowsMobile", "Blackberry", "WebOS",
            "Ubuntu", "Windows7", "Max OS X", "Linux", "OS/2"
        };
        ListView mListView = (ListView) mView.findViewById(R.id.chooser_list);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
            android.R.layout.simple_list_item_1, values);
        mListView.setAdapter(adapter);

        return mView;
    }
}
