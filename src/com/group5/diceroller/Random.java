package com.group5.diceroller;

import org.random.rjgodoy.trng.MH_SecureRandom;
import java.security.SecureRandom;


/**
 * Encapsulates random number generation for the application.
 *
 * @Author Ryan Pearce
 */
public class Random {
    
///@TODO - Use MH_SecureRandom with correct HTTP settings to access random.org
	
    int min_value; // Lowest value face
    int max_value = 6; // Highest value face
    Boolean useRandomOrg = false; // not yet implemented
    
    SecureRandom roller = new SecureRandom();
    
    // Constructor for the Random object
    public Random() {
        this.min_value = 1; // default die faces
        this.max_value = 6; // default die faces
    }
    
    public void range(int minvalue, int maxvalue) {
        this.min_value = minvalue;
        this.max_value = maxvalue;
    }
    
    public int roll() {
        if (useRandomOrg){
            throw new UnsupportedOperationException(
                    "Using Random.org is not implemented yet.");
        }
        else {
            if (min_value > max_value) {
                throw new IllegalArgumentException(
                        "min value cannot be greater than max value");
            }
            if ((min_value < 0 )|| (max_value < 0)) {
                throw new IllegalArgumentException(
                        "The die cannot have negative valued sides.");
            }
            return (roller.nextInt(this.max_value - this.min_value) + min_value);
        }
    }

    public int getMin_value() {
        return min_value;
    }

    public void setMin_value(int min_value) {
        this.min_value = min_value;
    }

    public int getMax_value() {
        return max_value;
    }

    public void setMax_value(int max_value) {
        this.max_value = max_value;
    }

    public Boolean getUseRandomOrg() {
        return useRandomOrg;
    }

    public void setUseRandomOrg(Boolean useRandomOrg) {
        this.useRandomOrg = useRandomOrg;
    }
}
