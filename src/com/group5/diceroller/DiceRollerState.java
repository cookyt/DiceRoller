package com.group5.diceroller;

import java.util.List;
import java.util.LinkedList;
import java.util.Date;

/**
 * Singleton class representing the state of the dice in the dice roller. It
 * stores the active set, the selectable dice sets, and the roll history.
 *
 * @Author Carlos Valera
 */
public class DiceRollerState {
    List<DiceSet> dice_sets;
    SetSelection active_selection;
    List<SetSelection> roll_history;
    List<Date> roll_dates;

    SumCountPair dice_rolled;
    int num_rolls;

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
        num_rolls = 0;
        dice_rolled = new SumCountPair();
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

    public void updateRollHistory() {
        // update statistics
        num_rolls += 1;
        for (DiceSet s : activeSelection())
        {
            for (Dice d : s)
            {
                dice_rolled.count += d.count;
            }
            dice_rolled.sum += s.sum() - s.modifier;
        }

        // update history
        rollHistory().add(0, new SetSelection(activeSelection()));
        rollDates().add(0, new Date());
        if (rollHistory().size() > kHistorySize) {
            rollHistory().remove(kHistorySize);
            rollDates().remove(kHistorySize);
        }
    }

    public List<SetSelection> rollHistory() {
        return roll_history;
    }

    public List<Date> rollDates() {
        return roll_dates;
    }

    public double getAvgRolls() {
        return dice_rolled.avg();
    }

    public int getNumRolls() {
        return num_rolls;
    }
}

class SumCountPair {
    int sum;
    int count;

    SumCountPair() {
        sum = 0;
        count = 0;
    }

    double avg() {
        if (count == 0)
            return 0;
        return ((double) sum) / ((double) count);
    }
}
