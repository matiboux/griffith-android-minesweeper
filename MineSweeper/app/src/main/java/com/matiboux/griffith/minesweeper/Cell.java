package com.matiboux.griffith.minesweeper;

import androidx.annotation.NonNull;

public class Cell {
    public static final int COVERED = 0b000;
    public static final int UNCOVERED = 0b001;
    public static final int MARKED = 0b010;
    public static final int MINEFIELD = 0b100;

    private int state = COVERED;
    private int digit = 0;

    public void toggleMark() {
        state ^= MARKED;
    }

    public void uncover() {
        state |= UNCOVERED;
    }

    public void setMinefield() {
        state |= MINEFIELD;
    }

    public boolean has(int... states) {
        for (int stateFlag : states)
            if ((state & stateFlag) != stateFlag) return false;
        return true;
    }

    public int getDigit() {
        return digit;
    }

    public void registerNeighbourMine() {
        digit++;
    }
}
