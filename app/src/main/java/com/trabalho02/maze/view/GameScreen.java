package com.trabalho02.maze.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.trabalho02.maze.MainActivity;
import com.trabalho02.maze.R;
import com.trabalho02.maze.utils.Utils;

import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

import static android.app.PendingIntent.getActivity;

public class GameScreen extends View {

    private Context currentContext;

    private MazeSquareComponent[][] mazeSquareComponents;

    // Constants
    private static final int INITIAL_QUANTITY_OF_LINES = 8;
    private static final int INITIAL_QUANTITY_OF_COLUMNS = 6;
    private static final float SEPARATOR_SIZE = 8;
    private static final int INITIAL_LEVEL = 1;
    private static final int FINAL_LEVEL = 5;

    private int currentLevel;
    private int currentLinesQuantity, currentColumnsQuantity;

    private float squareSize, verticalMargin, horizontalMargin;

    private Random rand;

    private MazeSquareComponent playerPosition;
    private MazeSquareComponent exitPosition;

    private Paint separatorPaintStyle;
    private Paint playerPaintStyle;
    private Paint exitPaintStyle;

    private Utils utils;

    private MediaPlayer mp;

    public GameScreen(Context context, @Nullable AttributeSet attributes){

        super(context);

        this.currentContext = context;

        rand = new Random();

        utils = new Utils();

        separatorPaintStyle = new Paint();
        separatorPaintStyle.setColor(Color.BLACK);
        separatorPaintStyle.setStrokeWidth(SEPARATOR_SIZE);

        playerPaintStyle = new Paint();
        playerPaintStyle.setColor(Color.RED);

        exitPaintStyle = new Paint();
        exitPaintStyle.setColor(Color.BLUE);

        currentLevel = INITIAL_LEVEL;
        currentLinesQuantity = INITIAL_QUANTITY_OF_LINES;
        currentColumnsQuantity = INITIAL_QUANTITY_OF_COLUMNS;

        mp = MediaPlayer.create(currentContext, R.raw. start );
        mp.start();

        buildMazeScreen();
    }

    // Directions
    public enum MoveDirections{
        TOP,
        BOTTOM,
        RIGHT,
        LEFT
    }

    public void move(MoveDirections dir){

        // Depending of the current direction
        switch (dir){
            case TOP:
                // There is no top separator
                if(!playerPosition.topSeparator){
                    // Go one position to the top
                    playerPosition = mazeSquareComponents[playerPosition.columnIndex][playerPosition.lineIndex-1];
                }
            break;

            case BOTTOM:
                // There is no bottom separator
                if(!playerPosition.bottomSeparator) {
                    // Go one position to the bottom
                    playerPosition = mazeSquareComponents[playerPosition.columnIndex][playerPosition.lineIndex + 1];
                }
            break;

            case RIGHT:
                // There is no right separator
                if(!playerPosition.rightSeparator) {
                    // Go one position to the right
                    playerPosition = mazeSquareComponents[playerPosition.columnIndex+1][playerPosition.lineIndex];
                }
            break;

            case LEFT:
                // There is no left separator
                if(!playerPosition.leftSeparator) {
                    // Go one position to the left
                    playerPosition = mazeSquareComponents[playerPosition.columnIndex - 1][playerPosition.lineIndex];
                }
            break;
        }

        // Verify if the player founds the exit
        verifyEndOfCurrentMaze();

        // Set as old view
        invalidate();
    }

    private boolean updateLevel(){

        if(currentLevel<FINAL_LEVEL){

            currentLevel++;

            currentLinesQuantity+=2;

            currentColumnsQuantity++;

            return true;
        }

        return false;
    }

    private void restartMaze(){
        currentLevel = INITIAL_LEVEL;

        currentLinesQuantity = INITIAL_QUANTITY_OF_LINES;

        currentColumnsQuantity = INITIAL_QUANTITY_OF_COLUMNS;

        buildMazeScreen();
    }

    private int calculateSquareSize(int canvasWidth, int canvasHeight){

        if((canvasWidth/canvasHeight) < (currentColumnsQuantity / currentLinesQuantity)){
            return(canvasWidth/(currentColumnsQuantity + 1));
        }

        // If reached here, first statement is false
        return(canvasHeight/(currentLinesQuantity + 1));
    }

    private float calculateHorizontalMargin(int canvasWidth, float squareSize){
        return((canvasWidth-(currentColumnsQuantity *squareSize))/2);
    }

    private float calculateVerticalMargin(int canvasHeight, float squareSize){
        return((canvasHeight-(currentLinesQuantity *squareSize))/2);
    }

    @Override
    protected void onDraw(Canvas canvas){

        //super.onDraw((canvas));

        // Background color
        canvas.drawColor(Color.WHITE);

        // Values to the canvas
        int canvasWidth = getWidth();
        int canvasHeight = getHeight();

        // Calculates the square size
        squareSize = calculateSquareSize(canvasWidth, canvasHeight);

        // Calculates horizontal margin
        horizontalMargin = calculateHorizontalMargin(canvasWidth, squareSize);

        // Calculates vertical margin
        verticalMargin  = calculateVerticalMargin(canvasHeight, squareSize);

        canvas.translate(horizontalMargin, verticalMargin);

        drawCanvas(canvas, squareSize);
    }

    private void drawCanvas(Canvas canvas, float squareSize){

        for(int i = 0; i< currentColumnsQuantity; i++){
            for(int j = 0; j< currentLinesQuantity; j++){

                // Top line separator is present
                if(mazeSquareComponents[i][j].topSeparator){
                    canvas.drawLine(i*squareSize,j*squareSize, (i+1)*squareSize, j*squareSize, separatorPaintStyle);
                }

                // Bottom line separator is present
                if(mazeSquareComponents[i][j].bottomSeparator){
                    canvas.drawLine(i*squareSize,(j+1)*squareSize, (i+1)*squareSize, (j+1)*squareSize, separatorPaintStyle);
                }

                // Right line separator is present
                if(mazeSquareComponents[i][j].rightSeparator){
                    canvas.drawLine((i+1)*squareSize,j*squareSize, (i+1)*squareSize, (j+1)*squareSize, separatorPaintStyle);
                }

                // Left line separator is present
                if(mazeSquareComponents[i][j].leftSeparator){
                    canvas.drawLine(i*squareSize,j*squareSize, i*squareSize, (j+1)*squareSize, separatorPaintStyle);
                }
            }
        }

        float marginPlayerExit = calculateMarginForPlayerAndExit();

        // Shows the player
        canvas.drawRect((playerPosition.columnIndex*squareSize)+marginPlayerExit, (playerPosition.lineIndex*squareSize)+marginPlayerExit, ((playerPosition.columnIndex+1)*squareSize)-marginPlayerExit, ((playerPosition.lineIndex+1)*squareSize)-marginPlayerExit, playerPaintStyle);

        // Shows the exit
        canvas.drawRect((exitPosition.columnIndex*squareSize)+marginPlayerExit, (exitPosition.lineIndex*squareSize)+marginPlayerExit, ((exitPosition.columnIndex+1)*squareSize)-marginPlayerExit, ((exitPosition.lineIndex+1)*squareSize)-marginPlayerExit, exitPaintStyle);
    }

    private void verifyEndOfCurrentMaze(){
        // Go to the next level
        if(playerPosition == exitPosition){

            mp = MediaPlayer.create(currentContext, R.raw. found_exit );
            mp.start();

            if(updateLevel()){

                utils.showAlert(Utils.alertType.NEXT_LEVEL, currentLevel, this.currentContext);

                buildMazeScreen();
            } else {
                utils.showAlert(Utils.alertType.LAST_LEVEL, currentLevel, this.currentContext);

                restartMaze();
            }
        }
    }

    public float calculatePlayerCenteredXPosition(){
        return(horizontalMargin+(playerPosition.columnIndex+1/2f)*squareSize);
    }

    public float calculatePlayerCenteredYPosition(){
        return(verticalMargin+(playerPosition.lineIndex+1/2f)*squareSize);
    }

    private float calculateMarginForPlayerAndExit(){
        return squareSize/10;
    }

    private void buildMazeScreen(){

        mazeSquareComponents = new MazeSquareComponent[currentColumnsQuantity][currentLinesQuantity];

        for(int i = 0; i< currentColumnsQuantity; i++){
            for(int j = 0; j< currentLinesQuantity; j++){
                mazeSquareComponents[i][j] = new MazeSquareComponent(i,j);
            }
        }

        // Defines the inicial position to the player
        playerPosition = mazeSquareComponents[0][0];

        // Defines the exit position
        exitPosition = mazeSquareComponents[currentColumnsQuantity -1][currentLinesQuantity -1];

        backTrackingToGenerateRandomMaze();

    }

    // Backtracking by Cadinho
    private void backTrackingToGenerateRandomMaze(){

        // User for backtracking
        Stack<MazeSquareComponent> backtrackingStack = new Stack<>();

        MazeSquareComponent currentPosition;
        MazeSquareComponent nextPosition;

        // First position
        currentPosition = mazeSquareComponents[0][0];
        currentPosition.alreadyVisited = true;

        do{
            nextPosition = getNextPosition(currentPosition);

            if(nextPosition!=null){
                removeSeparator(currentPosition, nextPosition);

                backtrackingStack.push(currentPosition);

                currentPosition = nextPosition;

                currentPosition.alreadyVisited = true;
            } else {
                currentPosition = backtrackingStack.pop();
            }
        } while(!backtrackingStack.empty());
    }

    private void removeSeparator(MazeSquareComponent currentPosition, MazeSquareComponent nextPosition){

        // If the next element is above
        if((currentPosition.columnIndex == nextPosition.columnIndex) && (currentPosition.lineIndex == (nextPosition.lineIndex-1))){
            currentPosition.bottomSeparator = false;
            nextPosition.topSeparator = false;
        }

        // If the next element is below
        if((currentPosition.columnIndex == nextPosition.columnIndex) && (currentPosition.lineIndex == (nextPosition.lineIndex+1))){
            currentPosition.topSeparator = false;
            nextPosition.bottomSeparator = false;
        }

        // If the next element is to the right
        if((currentPosition.columnIndex == nextPosition.columnIndex+1) && (currentPosition.lineIndex == (nextPosition.lineIndex))){
            currentPosition.leftSeparator = false;
            nextPosition.rightSeparator = false;
        }

        // If the next element is to the left
        if((currentPosition.columnIndex == nextPosition.columnIndex-1) && (currentPosition.lineIndex == (nextPosition.lineIndex))){
            currentPosition.rightSeparator = false;
            nextPosition.leftSeparator = false;
        }
    }

    private MazeSquareComponent getNextPosition(MazeSquareComponent mazeSquareComponent){

        ArrayList<MazeSquareComponent> squareBesides = new ArrayList<>();

        // Check top
        if(mazeSquareComponent.lineIndex >= 1){
            if(!(mazeSquareComponents[mazeSquareComponent.columnIndex][mazeSquareComponent.lineIndex-1].alreadyVisited)){
                squareBesides.add(mazeSquareComponents[mazeSquareComponent.columnIndex][mazeSquareComponent.lineIndex-1]);
            }
        }

        // Check bottom
        if(mazeSquareComponent.lineIndex < (currentLinesQuantity -1)){
            if(!(mazeSquareComponents[mazeSquareComponent.columnIndex][mazeSquareComponent.lineIndex+1].alreadyVisited)){
                squareBesides.add(mazeSquareComponents[mazeSquareComponent.columnIndex][mazeSquareComponent.lineIndex+1]);
            }
        }

        // Check right
        if(mazeSquareComponent.columnIndex < (currentColumnsQuantity -1)){
            if(!(mazeSquareComponents[mazeSquareComponent.columnIndex+1][mazeSquareComponent.lineIndex].alreadyVisited)){
                squareBesides.add(mazeSquareComponents[mazeSquareComponent.columnIndex+1][mazeSquareComponent.lineIndex]);
            }
        }

        // Check left only if it isn't the first column
        if(mazeSquareComponent.columnIndex>=1){
            if(!(mazeSquareComponents[mazeSquareComponent.columnIndex-1][mazeSquareComponent.lineIndex].alreadyVisited)){
                squareBesides.add(mazeSquareComponents[mazeSquareComponent.columnIndex-1][mazeSquareComponent.lineIndex]);
            }
        }

        // If there is at least one element
        if(squareBesides.size() > 0){
            int randomElementIndex = rand.nextInt(squareBesides.size());

            return squareBesides.get(randomElementIndex);
        }

        // If reached here, there is no element to return
        return null;
    }

    // Unitary element of the maze
    private class MazeSquareComponent{

        int columnIndex;
        int lineIndex;
        boolean topSeparator = true;
        boolean bottomSeparator = true;
        boolean rightSeparator = true;
        boolean leftSeparator = true;
        boolean alreadyVisited = false;

        public MazeSquareComponent(int columnIndex, int lineIndex){

            this.lineIndex = lineIndex;
            this.columnIndex = columnIndex;
        }
    }
}