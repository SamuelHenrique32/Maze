package com.trabalho02.maze;

import android.graphics.Paint;
import android.media.MediaPlayer;

import com.trabalho02.maze.view.GameScreen;

public class Store {

    public static int currentLevel;

    public static int currentLinesQuantity, currentColumnsQuantity;

    public static float squareSize, verticalMargin, horizontalMargin;

    public static volatile GameScreen.MazeSquareComponent playerPosition;

    public static GameScreen.MazeSquareComponent exitPosition;

    public static GameScreen.MazeSquareComponent[][] mazeSquareComponents;

    public static Paint separatorPaintStyle;
    public static Paint playerPaintStyle;
    public static Paint exitPaintStyle;

    public static MediaPlayer mp;
}