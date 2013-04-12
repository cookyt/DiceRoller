package com.group5.diceroller;

import java.util.List;
import java.util.LinkedList;
import java.util.Date;

/**
 * Singleton class representing the state of the dice in the dice roller. It
 * stores the active set, the selectable dice sets, and the roll history.
 */
public class DiceRollerState {
    List<DiceSet> dice_sets;
    SetSelection active_selection;
    List<SetSelection> roll_history;
    List<Date> roll_dates;

    public static final int kHistorySize = 20;

    static DiceRollerState state = null;

    private DiceRollerState() {
        // have to add/remove from 2 ends, so a linked list is better here.
        // (TODO fixed size history would suggest ring buffered array for
        // speed, not critical for now)
        roll_history = new LinkedList<SetSelection>();
        roll_dates = new LinkedList<Date>();
        dice_sets = DiceSet.LoadAllFromDB();
        active_selection = new SetSelection();
    }

    public static void initialize() {
        state = new DiceRollerState();
    }

    /**
     * Used to access the singleton instance of the dice roller state. The
     * application should call initialize() first to populate the list of dice
     * sets from the database.
     */
    public static DiceRollerState getState() {
        return state;
    }

    public List<DiceSet> diceSets() {
        return dice_sets;
    }

    public SetSelection activeSelection() {
        return active_selection;
    }

    public List<SetSelection> rollHistory() {
        return roll_history;
    }

    public List<Date> rollDates() {
        return roll_dates;
    }
}
