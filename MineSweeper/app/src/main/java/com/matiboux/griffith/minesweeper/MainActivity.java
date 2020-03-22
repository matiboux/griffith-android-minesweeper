package com.matiboux.griffith.minesweeper;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_about) {
            // Move to About Activity
            startActivity(new Intent(this, About.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
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