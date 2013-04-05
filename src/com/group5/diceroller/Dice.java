package com.group5.diceroller;

import java.util.List;
import java.util.Iterator;

public class Dice
    implements Comparable<Dice>, Iterable<Integer> {

    /**
     * The number of faces each dice in this object has.
     */
    int faces;

    /**
     * The number of like dice in this object.
     */
    int count;

    /**
     * ID of the set this dice belongs to.
     */
    int set_id;

    /**
     * List of rolled values.
     */
    List<Integer> last_roll;

    /**
     * Randomizes the values of the dice in this object.
     * TODO implement:Ryan
     */
    public void roll() {
    }

    /**
     * Saves this dice in the database with the associated set id.
     * TODO implement:Padraic
     *
     * @param set_id The ID of the set this dice belongs to.
     */
    public void save(int set_id) {
    }

    /**
     * Returns true iff the two dice have the same count, number of faces, and
     * set id.
     * TODO impelement:Ryan
     *
     * @param other The Dice to compare against
     */
    public boolean equals(Dice other) {
        return false;
    }

    /**
     * Compares two dice based on their face value and count.
     * TODO implement:Ryan
     *
     * @param other The dice to compare against.
     */
    public int compareTo(Dice other) {
        return 0;
    }

    /**
     * Returns an iterator over the rolled values in this Dice object.
     */
    public Iterator<Integer> iterator() {
        return last_roll.iterator();
    }
}
