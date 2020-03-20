package com.matiboux.griffith.minesweeper;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
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

    private Paint coveredPaint, uncoveredPaint, gridPaint;

    private boolean[][] cells;
    private int gridSize = 10;
    private int cellWidth, cellHeight;

    // default constructor for the class that takes in a context
    public MineSweeperView(Context c) {
        super(c);
        if (!isInEditMode())
            init();
    }

    // constructor that takes in a context and also a list of attributes
    // that were set through XML
    public MineSweeperView(Context c, AttributeSet as) {
        super(c, as);
        if (!isInEditMode())
            init();
    }

    // constructor that take in a context, attribute set and also a default
    // style in case the view is to be styled in a certain way
    public MineSweeperView(Context c, AttributeSet as, int default_style) {
        super(c, as, default_style);
        if (!isInEditMode())
            init();
    }

    // refactored init method as most of this code is shared by all the
    // constructors
    private void init() {

        // Paint objects
        coveredPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        coveredPaint.setColor(Color.BLACK);
        uncoveredPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        uncoveredPaint.setColor(Color.GRAY);
        gridPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        gridPaint.setColor(Color.WHITE);

        /*
        // create the paint objects for rendering our rectangles
        red = new Paint(Paint.ANTI_ALIAS_FLAG);
        green = new Paint(Paint.ANTI_ALIAS_FLAG);
        blue = new Paint(Paint.ANTI_ALIAS_FLAG);
        red.setColor(0xFFFF0000);
        green.setColor(0xFF00FF00);
        blue.setColor(0xFF0000FF);

        // initialise all the touch arrays to have 16 elements as we know no way to
        // accurately determine how many pointers the device will handle. 16 is an
        // overkill value for phones and tablets as it would require a minimum of
        // four hands on a device to hit that pointer limit
        touches = new boolean[16];
        touchx = new float[16];
        touchy = new float[16];

        // initialise the first square that will be shown at all times
        touchx[0] = 200.f;
        touchy[0] = 200.f;

        // initialise the rectangle
        square = new Rect(-100, -100, 100, 100);

        // we start off with nothing touching the view
        touch = false;
        */
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
        updateGridDimensions();
    }

    private void updateGridDimensions() {
        if (gridSize < 1) gridSize = 1;

        // Calculate the width & height of a cell
        cellWidth = getWidth() / gridSize;
        cellHeight = getHeight() / gridSize;

        // Initialize the grid of cells for the game: gridSize × gridSize
        // According to the Java specifications,
        // The array is filled with a default value:
        // For type boolean, the default value is false.
        // https://docs.oracle.com/javase/specs/jls/se7/html/jls-4.html#jls-4.12.5
        cells = new boolean[gridSize][gridSize];

        invalidate();
    }

    // public method that needs to be overridden to draw the contents of this
    // widget
    @Override
    public void onDraw(Canvas canvas) {
        // call the superclass method
        super.onDraw(canvas);

        // Black background
        //canvas.drawColor(Color.BLACK);

        int width = getWidth();
        int height = getHeight();

        // Draw grid cells
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                // Determine which paint to use
                Paint fillPaint = cells[i][j] ? uncoveredPaint : coveredPaint;

                // Draw the cell with the selected paint
                canvas.drawRect(i * cellWidth, j * cellHeight,
                        (i + 1) * cellWidth, (j + 1) * cellHeight,
                        fillPaint);
            }
        }

        // Draw grid lines
        for (int i = 1; i < gridSize; i++) {
            canvas.drawLine(i * cellWidth, 0, i * cellWidth, height, gridPaint);
            canvas.drawLine(0, i * cellHeight, width, i * cellHeight, gridPaint);
        }

        /*
        // draw the rest of the squares in green to indicate multitouch
        for (int i = 0; i < 16; i++) {
            if (touches[i]) {
                canvas.save();
                canvas.translate(touchx[i], touchy[i]);
                if (first == i)
                    canvas.drawRect(square, red);
                else
                    canvas.drawRect(square, green);
                canvas.restore();
            }
        }

        // if there is no touches then just draw a single blue square in the last place
        if (!touch) {
            canvas.save();
            canvas.translate(touchx[first], touchy[first]);
            //canvas.drawRect(square, blue);
            canvas.drawCircle(
                    square.left + ((square.right - square.left) >> 1),
                    square.top + ((square.bottom - square.top) >> 1),
                    (square.right - square.left) >> 1, blue);
            canvas.restore();
        }
        */
    }

    // public method that needs to be overridden to handle the touches from a
    // user
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        /*
        if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
            for (int i = 0; i < gridSize; i++) {
                for (int j = 0; j < gridSize; j++) {
                    // Randomly discover a cell
                    if (Math.random() >= 0.9) cells[i][j] = true;
                }
            }

            invalidate();
        }
        */

        if (event.getActionMasked() == MotionEvent.ACTION_DOWN)
            return true;
        else if (event.getActionMasked() == MotionEvent.ACTION_UP) {
            int cellX = (int) event.getX() / cellWidth;
            int cellY = (int) event.getY() / cellHeight;

            System.out.println(cellX);
            System.out.println(cellY);

            cells[cellX][cellY] = true;
            invalidate();
            return true;
        }

        /*
        // determine what kind of touch event we have
        if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
            // this indicates that the user has placed the first finger on the
            // screen what we will do here is enable the pointer, track its location
            // and indicate that the user is touching the screen right now
            // we also take a copy of the pointer id as the initial pointer for this
            // touch
            int pointer_id = event.getPointerId(event.getActionIndex());
            touches[pointer_id] = true;
            touchx[pointer_id] = event.getX();
            touchy[pointer_id] = event.getY();
            touch = true;
            first = pointer_id;
            invalidate();
            return true;
        } else if (event.getActionMasked() == MotionEvent.ACTION_UP) {
            // this indicates that the user has removed the last finger from the
            // screen and has ended all touch events. here we just disable the
            // last touch.
            int pointer_id = event.getPointerId(event.getActionIndex());
            touches[pointer_id] = false;
            first = pointer_id;
            touch = false;
            invalidate();
            return true;
        } else if (event.getActionMasked() == MotionEvent.ACTION_MOVE) {
            // indicates that one or more pointers has been moved. Android for
            // efficiency will batch multiple move events into one. thus you
            // have to check to see if all pointers have been moved.
            for (int i = 0; i < 16; i++) {
                int pointer_index = event.findPointerIndex(i);
                if (pointer_index != -1) {
                    touchx[i] = event.getX(pointer_index);
                    touchy[i] = event.getY(pointer_index);
                }
            }
            invalidate();
            return true;
        } else if (event.getActionMasked() == MotionEvent.ACTION_POINTER_DOWN) {
            // indicates that a new pointer has been added to the list
            // here we enable the new pointer and keep track of its position
            int pointer_id = event.getPointerId(event.getActionIndex());
            touches[pointer_id] = true;
            touchx[pointer_id] = event.getX(pointer_id);
            touchy[pointer_id] = event.getY(pointer_id);
            invalidate();
            return true;
        } else if (event.getActionMasked() == MotionEvent.ACTION_POINTER_UP) {
            // indicates that a pointer has been removed from the list
            // note that is is possible for us to lose our initial pointer
            // in order to maintain some semblance of an active pointer
            // (this may be needed depending on the application) we set
            // the earliest pointer to be the new first pointer.
            int pointer_id = event.getPointerId(event.getActionIndex());
            touches[pointer_id] = false;
            if (pointer_id == first) {
                for (int i = 0; i < 16; i++)
                    if (touches[i]) {
                        first = i;
                        break;
                    }
            }
            invalidate();
            return true;
        }
        */

        // if we get to this point they we have not handled the touch
        // ask the system to handle it instead
        return super.onTouchEvent(event);
    }

    public void reset() {
        // Reset the grid
        cells = new boolean[gridSize][gridSize];
        invalidate();
    }
}
