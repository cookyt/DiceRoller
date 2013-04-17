package com.group5.diceroller;

import java.util.List;
import java.util.LinkedList;
import java.util.TreeMap;
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
    TreeMap<Integer, SumCountPair> per_dice_stats;

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
        per_dice_stats = new TreeMap<Integer, SumCountPair>();
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
                int dsum = d.sum();
                int dcount = d.count;

                dice_rolled.count += dcount;
                dice_rolled.sum += dsum;

                Integer key = new Integer(d.faces);
                SumCountPair dice_stat = per_dice_stats.get(key);
                if (dice_stat == null)
                {
                    dice_stat = new SumCountPair();
                    dice_stat.faces = d.faces;
                    per_dice_stats.put(key, dice_stat);
                }
                dice_stat.count += dcount;
                dice_stat.sum += dsum;
            }
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

    public TreeMap<Integer, SumCountPair> perDiceStats() {
        return per_dice_stats;
    }

    public static class SumCountPair {
        int faces;
        int sum;
        int count;

        SumCountPair() {
            sum = 0;
            count = 0;
            faces = 0;
        }

        double avg() {
            if (count == 0)
                return 0;
            return ((double) sum) / ((double) count);
        }
    }
}

