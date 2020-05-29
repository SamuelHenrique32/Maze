package com.trabalho02.maze;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.trabalho02.maze.R.layout;
import com.trabalho02.maze.view.GameScreen;

public class MainActivity extends AppCompatActivity {

    private Vibrator vib;
    private SensorManager sensorManager;
    private Sensor giroscopio;
    private SensorEventListener listener;
    GameScreen gameScreen;
    private static final int QUANTITY_SENSOR_READS = 20;
    private int currentRead = 0;

    int i=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        gameScreen = new GameScreen(MainActivity.this, null);

        vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        giroscopio = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        if (giroscopio == null) {
            Toast.makeText(this, "O dispositivo não possui giroscópio!", Toast.LENGTH_LONG).show();
            finish();
        }

        //alteracao
        listener = new SensorEventListener() {

            @Override
            public void onSensorChanged(SensorEvent event) {
                if(currentRead>=QUANTITY_SENSOR_READS) {
                    if(event.values[0] > -7f && event.values[0] < -3f){
                        gameScreen.move(GameScreen.MoveDirections.RIGHT);
                    }else if(event.values[0] < 7f && event.values[0] > 3f){
                        gameScreen.move(GameScreen.MoveDirections.LEFT);
                    }

                    if(event.values[1] < 7f && event.values[1] > 3f){
                        gameScreen.move(GameScreen.MoveDirections.BOTTOM);
                    }else if(event.values[1] > -7f && event.values[1] < -3f){
                        gameScreen.move(GameScreen.MoveDirections.TOP);
                    }

                    System.out.println(i);
                    i++;

                    currentRead = 0;
                } else{
                    currentRead++;
                }
            }


            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        sensorManager.registerListener(listener, giroscopio, 200000);
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        sensorManager.unregisterListener(listener);
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

    public void vibrar(View view)
    {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            vib.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
        }
        else
        {
            vib.vibrate(500);
        }
    }
}