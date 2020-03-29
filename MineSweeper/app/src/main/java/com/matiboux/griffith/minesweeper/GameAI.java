package com.matiboux.griffith.minesweeper;

import android.util.Pair;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class GameAI {
    private static final int MOVE_DELAY = 500;

    private MineSweeperView mineSweeperView;
    private Timer timer;

    private int lastX = -1;
    private int lastY = -1;

    private GameAIMode mode = GameAIMode.Disabled;
    private OnModeChangeListener onModeChangeListener;

    public GameAI(MineSweeperView mineSweeperView) {
        this.mineSweeperView = mineSweeperView;

        this.mineSweeperView.setGameStateChangeListener(new OnGameStateChangeListener() {
            @Override
            public void onGameStateChange(GameState state) {
                if (state == GameState.Playing) return;
                reset();
            }
        });
    }

    public void reset() {
        lastX = lastY = -1;
        disable();
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

            //lastX = lastY = -1;

            for (int cellX = 0; cellX < gridSize; cellX++) {
                for (int cellY = 0; cellY < gridSize; cellY++) {
                    if (!cells[cellX][cellY].has(Cell.UNCOVERED)) continue;

                    int digit = cells[cellX][cellY].getDigit();
                    if (digit <= 0) continue;

                    int coveredCount = 0;
                    int markedCount = 0;

                    ArrayList<Pair<Integer, Integer>> coveredCells = new ArrayList<>();

                    int minX = Math.max(0, cellX - 1);
                    int maxX = Math.min(gridSize, cellX + 2);
                    int minY = Math.max(0, cellY - 1);
                    int maxY = Math.min(gridSize, cellY + 2);

                    for (int i = minX; i < maxX; i++) {
                        for (int j = minY; j < maxY; j++) {
                            if (cells[i][j].has(Cell.MARKED)) {
                                //if (markedCount >= digit) break zoneOuterFor; // Finished zone
                                markedCount++;
                            } else if (!cells[i][j].has(Cell.UNCOVERED)) {
                                //if (coveredCount >= digit) continue gridInnerFor; // Invalid zone
                                coveredCount++;
                                coveredCells.add(new Pair<>(i, j));
                            }
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
                    if (coveredCount == digit - markedCount) {
                        // The remaining cells in this zone should be marked
                        touchCell(pair.first, pair.second, MineSweeperMode.Marking);
                        return;
                    }
                }
            }

            touchCell(-1, -1);
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
            lastX = cellX;
            lastY = cellY;

            mineSweeperView.touchCell(cellX, cellY);
        }

        void touchCell(int cellX, int cellY) {
            touchCell(cellX, cellY, MineSweeperMode.Uncovering);
        }
    }
}
