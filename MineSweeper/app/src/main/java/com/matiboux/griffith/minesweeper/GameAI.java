package com.matiboux.griffith.minesweeper;

import android.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class GameAI {
    private static final int MOVE_DELAY = 500;

    private MineSweeperView mineSweeperView;
    private Timer timer;

    private GameAIMode mode = GameAIMode.Disabled;
    private OnModeChangeListener onModeChangeListener;

    public GameAI(MineSweeperView mineSweeperView) {
        this.mineSweeperView = mineSweeperView;

        this.mineSweeperView.setGameStateChangeListener(new OnGameStateChangeListener() {
            @Override
            public void onGameStateChange(GameState state) {
                if (state != GameState.Playing) disable();
            }
        });
    }

    public void disable() {
        mode = GameAIMode.Disabled; // Disable AI
        if (timer != null) timer.cancel(); // Stops processing the next moves
        if (onModeChangeListener != null) onModeChangeListener.onModeChange(mode); // Trigger event
    }

    public void setModeChangeListener(OnModeChangeListener listener) {
        onModeChangeListener = listener;
    }

    public void switchMode() {
        if (mode == GameAIMode.Enabled) disable(); // Disable AI
        else if (mineSweeperView.getState() == GameState.Playing) {
            // Process the next move every second
            timer = new Timer();
            timer.schedule(new MoveProcessor(), MOVE_DELAY, MOVE_DELAY);

            mode = GameAIMode.Enabled; // Switch Mode
            onModeChangeListener.onModeChange(mode); // Trigger event
        }
    }

    public GameAIMode getMode() {
        return mode;
    }

    class MoveProcessor extends TimerTask {
        private Cell[][] cells;
        private int gridSize;

        public void run() {
            cells = mineSweeperView.getCells();
            gridSize = mineSweeperView.getGridSize();

            for (int cellX = 0; cellX < gridSize; cellX++) {
                for (int cellY = 0; cellY < gridSize; cellY++) {
                    if (!cells[cellX][cellY].has(Cell.UNCOVERED)) continue;

                    int digit = cells[cellX][cellY].getDigit();
                    if (digit <= 0) continue;

                    List<Pair<Integer, Integer>> coveredCells = new ArrayList<>();
                    List<Pair<Integer, List<Pair<Integer, Integer>>>> neighborCoveredCellsList = new ArrayList<>();
                    int markedCount = 0;

                    int minX = Math.max(0, cellX - 1);
                    int maxX = Math.min(gridSize, cellX + 2);
                    int minY = Math.max(0, cellY - 1);
                    int maxY = Math.min(gridSize, cellY + 2);

                    for (int i = minX; i < maxX; i++)
                        for (int j = minY; j < maxY; j++) {
                            if (cells[i][j].has(Cell.MARKED))
                                markedCount++;
                            else if (!cells[i][j].has(Cell.UNCOVERED))
                                coveredCells.add(new Pair<>(i, j));
                            else if (i != cellX || j != cellY) {
                                int marksLeft = cells[i][j].getDigit();
                                if (marksLeft <= 0) continue;

                                List<Pair<Integer, Integer>> neighborCoveredCells = new ArrayList<>();
                                int neighborMarkedCount = 0;
                                //int neighborCoveredCount = 0;

                                int minI = Math.max(0, i - 1);
                                int maxI = Math.min(gridSize, i + 2);
                                int minJ = Math.max(0, j - 1);
                                int maxJ = Math.min(gridSize, j + 2);

                                for (int x = minI; x < maxI; x++)
                                    for (int y = minJ; y < maxJ; y++) {
                                        if (cells[x][y].has(Cell.MARKED))
                                            neighborMarkedCount++;
                                        else if (!cells[x][y].has(Cell.UNCOVERED))
                                            neighborCoveredCells.add(new Pair<>(x, y));
                                    }

                                marksLeft -= neighborMarkedCount;
                                neighborCoveredCellsList.add(new Pair<>(marksLeft, neighborCoveredCells));
                            }
                        }

                    if (coveredCells.isEmpty()) continue;

                    Pair<Integer, Integer> pair = coveredCells.get(0);

                    if (markedCount == digit) {
                        // This zone is finished
                        // Uncover remaining cells of this zone
                        touchCell(pair.first, pair.second);
                        return;
                    }
                    if (coveredCells.size() == digit - markedCount) {
                        // The remaining cells in this zone should be marked
                        touchCell(pair.first, pair.second, MineSweeperMode.Marking);
                        return;
                    }

                    System.out.println("neighbors of " + cellX + ", " + cellY + ": " + neighborCoveredCellsList.size());
                    for (Pair<Integer, List<Pair<Integer, Integer>>> neighborInfo : neighborCoveredCellsList)
                        System.out.print(" " + neighborInfo.first);
                    System.out.println();

                    for (Pair<Integer, List<Pair<Integer, Integer>>> neighborInfo : neighborCoveredCellsList) {
                        List<Pair<Integer, Integer>> neighborCoveredCells = neighborInfo.second;

                        int i;
                        int size = neighborCoveredCells.size();
                        System.out.print(size);
                        for (i = 0; i < size; i++)
                            if (!coveredCells.contains(neighborCoveredCells.get(i))) break;
                        if (i < size) continue; // Doesn't contain all neighbor's covered cells

                        System.out.println("neighbor info " + neighborInfo.first);

                        for (i = 0; i < size; i++)
                            coveredCells.remove(neighborCoveredCells.get(i));

                        markedCount += neighborInfo.first; // Number of the neighbour cell's unmarked mines
                    }

                    if (coveredCells.isEmpty()) continue;

                    pair = coveredCells.get(0);

                    if (markedCount == digit) {
                        // This zone is finished
                        // Uncover remaining cells of this zone
                        touchCell(pair.first, pair.second);
                        return;
                    }
                    if (coveredCells.size() == digit - markedCount) {
                        // The remaining cells in this zone should be marked
                        touchCell(pair.first, pair.second, MineSweeperMode.Marking);
                        return;
                    }
                }
            }

            touchCell(-1, -1); // Random move
        }

        void touchCell(int cellX, int cellY, MineSweeperMode mode) {
            int gridSize = mineSweeperView.getGridSize();

            if (cellX >= 0 && cellY >= 0)
                mineSweeperView.setMode(mode); // Set mode
            else {
                Cell[][] cells = mineSweeperView.getCells();

                do {
                    cellX = (int) (Math.random() * gridSize);
                    cellY = (int) (Math.random() * gridSize);
                }
                while (cells[cellX][cellY].has(Cell.UNCOVERED)
                        || cells[cellX][cellY].has(Cell.MARKED));

                mineSweeperView.setMode(MineSweeperMode.Uncovering);
                System.out.println("random touch");
            }

            // Set last cell coordinates

            mineSweeperView.touchCell(cellX, cellY);
        }

        void touchCell(int cellX, int cellY) {
            touchCell(cellX, cellY, MineSweeperMode.Uncovering);
        }
    }
}
