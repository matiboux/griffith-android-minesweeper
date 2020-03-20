package com.matiboux.griffith.minesweeper;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private MineSweeperView mineSweeperView;
    private Button btnReset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mineSweeperView = findViewById(R.id.mine_sweeper_view);
        btnReset = findViewById(R.id.button_reset);

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Reset the grid
                mineSweeperView.reset();
            }
        });
    }
}