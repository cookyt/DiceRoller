package com.group5.diceroller;

import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;

/**
 * Encapsulates the dice sets chosen to be rolled on the next roll.
 *
 * @Author Carlos Valera
 */
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
     */
    public SetSelection() {
        // For hashset to be useable, boolean equals(Object) and int hashCode()
        // need to be implemented. TreeSet might be nicer for this job due to
        // the way it auto-sorts anything put in it.
        dice_sets = new HashSet<DiceSet>();
    }

    /**
     * Copys the given selection (deep copy).
     */
    public SetSelection(SetSelection set_to_copy) {
        dice_sets = new HashSet<DiceSet>();
        for (DiceSet set : set_to_copy)
            add(new DiceSet(set));
    }

    /**
     * Randomizes the dice in this SetSelection.
     */
    public void roll() {
        for (DiceSet set : dice_sets)
            set.roll();
    }

    /**
     * Adds the given DiceSet to this selection.
     * 
     * @param set The DiceSet to add
     * @return True if the set was added, false otherwise.
     */
    public boolean add(DiceSet set) {
        return dice_sets.add(set);
    }

    /**
     * Removes the dice set with the given id from this selection.
     *
     * @param set The DiceSet to remove
     * @return True if the set was removed, false otherwise.
     */
    public boolean remove(DiceSet set) {
        return dice_sets.remove(set);
    }

    /**
     * True if the selection contains the given dice set.
     */
    public boolean contains(DiceSet set) {
        return dice_sets.contains(set);
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
     */
    public Iterator<DiceSet> iterator() {
        return dice_sets.iterator();
    }

    /**
     * Returns the number of sets in this selection.
     */
    public int size() {
        return dice_sets.size();
    }
}
