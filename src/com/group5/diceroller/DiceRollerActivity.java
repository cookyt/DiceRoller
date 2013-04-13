package com.group5.diceroller;

import java.util.List;
import java.util.Date;
import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.util.Log;

/**
 * Main activity for the application.
 *
 * @Author Carlos Valera
 */
public class DiceRollerActivity extends FragmentActivity
    implements CentralFragment.OnDiceRolledListener,
    SetChooserFragment.OnSelectionChangedListener {

    public static String kTag = "DiceRollerActivity";

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide fragments for each of the
     * sections. We use a {@link android.support.v4.app.FragmentPagerAdapter} derivative, which will
     * keep every loaded fragment in memory. If this becomes too memory intensive, it may be best
     * to switch to a {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    ToggleButton[] tabs;
    int selected_tab = 0;

    DiceRollerState state;
    SetChooserFragment chooser;
    CentralFragment central;
    StatisticsFragment statistics;

    @Override
    /**
     * Callback for creating the activity. It Creates the component fragments,
     * builds the view pager and its adapter, registers the listeners for
     * changing the current view with action bar tabs, and loads the dice sets
     * from the database.
     */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.i(kTag, "Creating Dice Roller");

        chooser    = new SetChooserFragment();
        central    = new CentralFragment();
        statistics = new StatisticsFragment();

        DiceDBOpenHelper.initialize(getApplicationContext());
        DiceRollerState.initialize();
        state = DiceRollerState.getState();


        // Create the adapter that will return a fragment for each of the three primary sections
        // of the app.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // When swiping between different sections, select the corresponding tab.
        // We can also use ActionBar.Tab#select() to do this if we have a reference to the
        // Tab.
        mViewPager.setOnPageChangeListener(new PageChangeListener());

        // Set up tabs
        int[] tab_ids = new int[] {R.id.chooser_button, R.id.central_button, R.id.statistics_button};
        tabs = new ToggleButton[3];
        for (int i=0; i<tabs.length; ++i)
        {
            tabs[i] = (ToggleButton) findViewById(tab_ids[i]);
            tabs[i].setOnClickListener(new TabButtonClickedListener(i));
        }
        setTabSelected(1);

        // Move the pager to the center item
        mViewPager.setCurrentItem(1, false);
    }

    /**
     * Callback for when the user rolls the dice in the selection. Currently,
     * it randomizes the dice in the active selection, adds it to the dice
     * history, and sets the view to the statistics.
     */
    public void onDiceRolled() {
        // play possible animations/sound here
        state.activeSelection().roll();

        state.rollHistory().add(0, new SetSelection(state.activeSelection()));
        state.rollDates().add(0, new Date());
        if (state.rollHistory().size() > DiceRollerState.kHistorySize) {
            state.rollHistory().remove(DiceRollerState.kHistorySize);
            state.rollDates().remove(DiceRollerState.kHistorySize);
        }

        statistics.update();
        mViewPager.setCurrentItem(2);
    }

    /**
     * Callback for when user changes the dice in the active selection.
     */
    public void onSelectionChanged() {
        central.updateSelectionText();
    }

    /**
     * Sets the tab in the given position to be selected.
     */
    public void setTabSelected(int position) {
        tabs[selected_tab].setChecked(false);
        tabs[position].setChecked(true);
        selected_tab = position;
    }


    /**
     * Click listener for tab buttons.
     */
    class TabButtonClickedListener implements View.OnClickListener {
        int position;
        public TabButtonClickedListener(int position) {
            this.position = position;
        }

        public void onClick(View v) {
            setTabSelected(position);
            mViewPager.setCurrentItem(position);
        }
    }

    /**
     * Handles setting the correct tab button selected when the page is changed
     * with a swipe.
     */
    class PageChangeListener extends ViewPager.SimpleOnPageChangeListener {
        @Override
        public void onPageSelected(int position) {
            setTabSelected(position);
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to one of the primary
     * sections of the app.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            switch (i)
            {
                case 0: return chooser;
                case 1: return central;
                case 2: return statistics;
            }
            return null;
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0: return getString(R.string.chooser_title).toUpperCase();
                case 1: return getString(R.string.central_title).toUpperCase();
                case 2: return getString(R.string.statistics_title).toUpperCase();
            }
            return null;
        }
    }
}
