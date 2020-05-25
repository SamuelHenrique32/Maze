package com.trabalho02.maze;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import com.trabalho02.maze.R.layout;
import com.trabalho02.maze.view.GameScreen;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        GameScreen gameScreen = new GameScreen(MainActivity.this, null);
    }

    // Action bar button
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(layout.menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    // Handle button click
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int menuID = item.getItemId();

        // Restart
        if (menuID == R.id.mybutton) {
            GameScreen gameScreen = new GameScreen(MainActivity.this, null);

            setContentView(R.layout.activity_main);
        }
        return super.onOptionsItemSelected(item);
    }
}