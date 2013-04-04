package com.group5.diceroller;

import java.util.List;

public interface DiceRollerState {
    public List<DiceSet> diceSets();
    public SetSelection activeSelection();
    public List<SetSelection> rollHistory();
}
