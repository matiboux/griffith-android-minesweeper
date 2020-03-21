package com.matiboux.griffith.minesweeper;

/*
enum CellState {
    Covered,
    Uncovered
}

enum CellContent {
    Empty,
    Mined
}
*/

public class Cell {
    public static final int COVERED = 0b000;
    public static final int UNCOVERED = 0b001;
    public static final int MARKED = 0b010;
    public static final int MINEFIELD = 0b100;

    private int state = COVERED;

    public void toggleMark() {
        // Toggle mark if not uncovered
        if (has(UNCOVERED)) return;
        state ^= MARKED;
    }

    public void uncover() {
        // Uncover if not marked
        if (has(MARKED)) return;
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
}
