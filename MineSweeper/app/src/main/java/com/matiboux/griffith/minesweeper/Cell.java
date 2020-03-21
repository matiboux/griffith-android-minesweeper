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
    private boolean uncovered = false;
    public boolean minefield = false;

    /*
    public Cell() {

    }
    */

    public void setUncovered() {
        this.uncovered = true;
    }

    public boolean isUncovered() {
        return this.uncovered;
    }

    public void setMinefield() {
        this.minefield = true;
    }

    public boolean isMinefield() {
        return this.minefield;
    }
}
