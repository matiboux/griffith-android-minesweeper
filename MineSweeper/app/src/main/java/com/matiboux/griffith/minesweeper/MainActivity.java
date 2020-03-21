package com.matiboux.griffith.minesweeper;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private MineSweeperView mineSweeperView;
    private Button btnMode;
    private Button btnReset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Components
        mineSweeperView = findViewById(R.id.mine_sweeper_view);
        btnMode = findViewById(R.id.button_mode);
        btnReset = findViewById(R.id.button_reset);

        updateBtnModeText(mineSweeperView.getMode());

        setEventListeners(); // Events
    }

    private void setEventListeners() {
        mineSweeperView.setGameOverListener(new OnGameOverListener() {
            @Override
            public void onGameOver() {
                // Highlight the reset button when the game is over
                btnReset.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
            }
        });
        mineSweeperView.setModeChangeListener(new OnModeChangeListener() {
            @Override
            public void onModeChange(MineSweeperMode mode) {
                updateBtnModeText(mode);
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
            }
        });
    }

    private void updateBtnModeText(MineSweeperMode mode) {
        if (mode == MineSweeperMode.Marking) btnMode.setText(R.string.button_mode_marking);
        else btnMode.setText(R.string.button_mode_uncover);
    }
}