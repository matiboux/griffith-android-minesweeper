package com.matiboux.griffith.minesweeper;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private MineSweeperView mineSweeperView;
    private Button btnMode;
    private Button btnReset;
    private TextView txvTotalMines;
    private TextView txvMarkedMines;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Components
        mineSweeperView = findViewById(R.id.mine_sweeper_view);
        btnMode = findViewById(R.id.button_mode);
        btnReset = findViewById(R.id.button_reset);
        txvTotalMines = findViewById(R.id.text_total_mines);
        txvMarkedMines = findViewById(R.id.text_marked_mines);

        updateFields(); // Update fields
        setEventListeners(); // Events
    }

    private void updateFields() {
        updateBtnModeText(mineSweeperView.getMode());
        txvTotalMines.setText(getString(R.string.text_total_mines, mineSweeperView.getMinesCount()));
        txvMarkedMines.setText(getString(R.string.text_marked_mines, mineSweeperView.getMarkedCount()));
    }

    private void setEventListeners() {
        mineSweeperView.setMarkedCountChangeListener(new OnMarkedCountChangeListener() {
            @Override
            public void onMarkedCountChange(int markedCount) {
                txvMarkedMines.setText(getString(R.string.text_marked_mines, markedCount));
            }
        });
        mineSweeperView.setModeChangeListener(new OnModeChangeListener() {
            @Override
            public void onModeChange(MineSweeperMode mode) {
                updateBtnModeText(mode);
            }
        });
        mineSweeperView.setGameStateChangeListener(new OnGameStateChangeListener() {
            @Override
            public void onGameStateChange(GameState state) {
                if (state == GameState.Lost) {
                    // Highlight the reset button when the game is over
                    btnReset.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
                } else if (state == GameState.Won) {
                    // Highlight the reset button when the game is over
                    btnReset.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
                } else {
                    btnReset.getBackground().clearColorFilter();
                }
            }
        });


        btnMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Switch between modes
                mineSweeperView.switchMode();
                updateBtnModeText(mineSweeperView.getMode());
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Reset the grid & the reset button color
                mineSweeperView.reset();
                btnReset.getBackground().clearColorFilter();
                updateFields();
            }
        });
    }

    private void updateBtnModeText(MineSweeperMode mode) {
        if (mode == MineSweeperMode.Marking) {
            btnMode.setText(R.string.button_mode_marking);
            btnMode.getBackground().setColorFilter(Color.YELLOW, PorterDuff.Mode.MULTIPLY);
        } else {
            btnMode.setText(R.string.button_mode_uncover);
            btnMode.getBackground().clearColorFilter();
        }
    }
}