package com.matiboux.griffith.minesweeper;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class MineSweeperView extends View {
    /*
    // private fields that are necessary for rendering the view
    // the colours of our squares
    private Paint red, green, blue;
    private Rect square; // the square itself
    private boolean touches[]; // which fingers providing input
    private float touchx[]; // x position of each touch
    private float touchy[]; // y position of each touch
    private int first; // the first touch to be rendered
    private boolean touch; // do we have at least on touch
    */

    private Paint coveredPaint, uncoveredPaint, markedPaint, minefieldPaint;
    private Paint gridPaint, textPaint;

    private Cell[][] cells;
    private int cellWidth, cellHeight;
    private int gridSize = 10;
    private int nbMines = 20;

    private MineSweeperMode mode;
    private OnModeChangeListener onModeChangeListener;

    private boolean gameOver;
    private OnGameOverListener onGameOverListener;

    // default constructor for the class that takes in a context
    public MineSweeperView(Context c) {
        super(c);
        if (!isInEditMode()) init();
    }

    // constructor that takes in a context and also a list of attributes
    // that were set through XML
    public MineSweeperView(Context c, AttributeSet as) {
        super(c, as);
        if (!isInEditMode()) init();
    }

    // constructor that take in a context, attribute set and also a default
    // style in case the view is to be styled in a certain way
    public MineSweeperView(Context c, AttributeSet as, int default_style) {
        super(c, as, default_style);
        if (!isInEditMode()) init();
    }

    // refactored init method as most of this code is shared by all the constructors
    private void init() {
        // Initialize the paint objects for each cell state
        coveredPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        coveredPaint.setColor(Color.BLACK);
        uncoveredPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        uncoveredPaint.setColor(Color.GRAY);
        markedPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        markedPaint.setColor(Color.YELLOW);
        minefieldPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        minefieldPaint.setColor(Color.RED);

        // Initialize the additional paint objects
        gridPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        gridPaint.setColor(Color.WHITE);
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(80);

        // Initialize the game board
        initializeGrid();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int size = 0;
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();

        size = Math.min(width, height);
        setMeasuredDimension(size, size);
    }

    @Override
    protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
        super.onSizeChanged(width, height, oldWidth, oldHeight);

        // Calculate the width & height of a cell
        cellWidth = getWidth() / gridSize;
        cellHeight = getHeight() / gridSize;

        invalidate();
    }

    // public method that needs to be overridden to draw the contents of this widget
    @Override
    public void onDraw(Canvas canvas) {
        // call the superclass method
        super.onDraw(canvas);

        // Black background
        //canvas.drawColor(Color.BLACK);

        int width = getWidth();
        int height = getHeight();

        Rect rect = new Rect();

        // Draw grid cells
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                // Determine which paint to use
                Paint fillPaint;
                if (cells[i][j].has(Cell.UNCOVERED)) {
                    if (cells[i][j].has(Cell.MINEFIELD)) fillPaint = minefieldPaint;
                    else fillPaint = uncoveredPaint;
                } else if (cells[i][j].has(Cell.MARKED)) fillPaint = markedPaint;
                else fillPaint = coveredPaint;

                // Set the rect for the cell
                rect.set(i * cellWidth, j * cellHeight,
                        (i + 1) * cellWidth, (j + 1) * cellHeight);

                // Draw the cell with the selected paint
                canvas.drawRect(rect, fillPaint);

                // Draw the text for the minefield if uncovered
                if (cells[i][j].has(Cell.UNCOVERED, Cell.MINEFIELD)) {
                    drawCenterText(canvas, "M", rect, textPaint);
                    gameOver = true;
                    onGameOverListener.onGameOver();
                }
            }
        }

        // Draw grid lines
        for (int i = 1; i < gridSize; i++) {
            canvas.drawLine(i * cellWidth, 0, i * cellWidth, height, gridPaint);
            canvas.drawLine(0, i * cellHeight, width, i * cellHeight, gridPaint);
        }

    }

    // public method that needs to be overridden to handle the touches from a user
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // Handle the touch event only if the game is not over
        if (!gameOver) {
            if (event.getActionMasked() == MotionEvent.ACTION_DOWN)
                return true; // Listen for the next Action Up event
            else if (event.getActionMasked() == MotionEvent.ACTION_UP) {
                // Uncover the cell
                int cellX = (int) event.getX() / cellWidth;
                int cellY = (int) event.getY() / cellHeight;

                if (mode == MineSweeperMode.Marking) cells[cellX][cellY].toggleMark();
                else cells[cellX][cellY].uncover();

                invalidate();
                return true;
            }
        }

        // If we have not handled the touch event, ask the system to do it
        return super.onTouchEvent(event);
    }

    public void initializeGrid() {
        // Initialize the grid matrix
        cells = new Cell[gridSize][gridSize];
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                cells[i][j] = new Cell();
            }
        }

        // Place mines
        for (int i = 0; i < nbMines; ) {
            // Generate random coordinates between 0 and (gridSize - 1)
            int randomX = (int) (Math.random() * gridSize);
            int randomY = (int) (Math.random() * gridSize);

            // Set the cell as a minefield if not already
            if (cells[randomX][randomY].has(Cell.MINEFIELD)) continue;
            cells[randomX][randomY].setMinefield();
            i++;
        }

        // Reset fields
        mode = MineSweeperMode.Uncovering;
        gameOver = false;

        // Trigger events
        if (onModeChangeListener != null) onModeChangeListener.onModeChange(mode);
    }

    public void reset() {
        // Reinitialize the grid
        initializeGrid();
        invalidate(); // Redraw
    }

    public void setModeChangeListener(OnModeChangeListener listener) {
        onModeChangeListener = listener;
    }

    public void switchMode() {
        mode = mode == MineSweeperMode.Uncovering
                ? MineSweeperMode.Marking : MineSweeperMode.Uncovering;
        onModeChangeListener.onModeChange(mode);
    }

    public MineSweeperMode getMode() {
        return mode;
    }

    public void setGameOverListener(OnGameOverListener listener) {
        onGameOverListener = listener;
    }

    private void drawCenterText(Canvas canvas, String text, Rect rect, Paint paint) {
        //paint.setTextAlign(Paint.Align.LEFT);

        // Get center of the space
        float centerX = rect.left + rect.width() / 2f;
        float centerY = rect.top + rect.height() / 2f;

        // Reuse rect to get the size of the text
        paint.getTextBounds(text, 0, text.length(), rect);

        // Compute coordinates for the text & Draw the text
        float x = centerX - rect.width() / 2f - rect.left;
        float y = centerY + rect.height() / 2f - rect.bottom;
        canvas.drawText(text, x, y, paint);
    }
}
