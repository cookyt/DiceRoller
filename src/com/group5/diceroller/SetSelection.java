package com.group5.diceroller;

import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;

public class SetSelection
    implements Iterable<DiceSet> {

    /**
     * Set of dice in this selection.
     *
     * NOTE to the implementor, you don't have to use a Set to implement the
     * SetSelection. Having this here just made the class skeleton easier to
     * write.
     */
    Set<DiceSet> dice_sets;

    /**
     * The delimeter used between DiceSets in SetSelection.toString()
     */
    public static final String kDelimeter = "; ";

    /**
     * Constructs an empty SetSelection.
     * TODO implement. Current implementation for testing only.
     */
    public SetSelection() {
        // For hashset to be useable, boolean equals(Object) and int hashCode()
        // need to be implemented. TreeSet might be nicer for this job due to
        // the way it auto-sorts anything put in it.
        dice_sets = new HashSet<DiceSet>();
    }

    /**
     * Copys the given selection (deep copy).
     * TODO implement. Current implementation for testing only.
     */
    public SetSelection(SetSelection set_to_copy) {
        dice_sets = new HashSet<DiceSet>();
    }

    /**
     * Randomizes the dice in this SetSelection.
     * TODO implement
     */
    public void roll() {
    }

    /**
     * Adds the given DiceSet to this selection.
     * TODO implement Current implementation for testing only
     * 
     * @param set The DiceSet to add
     * @return True if the set was added, false otherwise.
     */
    public boolean add(DiceSet set) {
        dice_sets.add(set);
        return true;
    }

    /**
     * Removes the dice set with the given id from this selection.
     * TODO implement.
     *
     * @param set_id The id to search for.
     * @return True if the set was removed, false otherwise.
     */
    public boolean remove(int set_id) {
        return false;
    }

    /**
     * Returns a string of the DiceSet's contained in this selection.
     */
    public String toString() {
        StringBuilder ret = new StringBuilder();
        boolean first = true;
        for (DiceSet set : this) {
            if (first)
                first = false;
            else
                ret.append(kDelimeter); // seperate with semicolons
            ret.append(set.label());
        }
        return ret.toString();
    }

    /**
     * Returns an iterator over the DiceSets in this SetSelection.
     * NOTE to the implementor this function is the reason the dice_sets
     * attribute exists up above. It makes it easy to write the skeleton for
     * this method.
     * TODO implement
     */
    public Iterator<DiceSet> iterator() {
        return dice_sets.iterator();
    }
}
