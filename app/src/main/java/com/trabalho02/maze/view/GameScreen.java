package com.trabalho02.maze.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.view.View;
import androidx.annotation.Nullable;
import com.trabalho02.maze.Store;
import com.trabalho02.maze.R;
import com.trabalho02.maze.utils.Utils;
import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

public class GameScreen extends View {

    // Constants
    private static final int INITIAL_QUANTITY_OF_LINES = 8;
    private static final int INITIAL_QUANTITY_OF_COLUMNS = 6;
    private static final float SEPARATOR_SIZE = 8;
    private static final int INITIAL_LEVEL = 1;
    private static final int FINAL_LEVEL = 5;

    private Context currentContext;

    private Random rand;

    private Utils utils;

    public GameScreen(Context context, @Nullable AttributeSet attributes){

        super(context);

        this.currentContext = context;

        rand = new Random();

        utils = new Utils();

        Store.separatorPaintStyle = new Paint();
        Store.separatorPaintStyle.setColor(Color.BLACK);
        Store.separatorPaintStyle.setStrokeWidth(SEPARATOR_SIZE);

        Store.playerPaintStyle = new Paint();
        Store.playerPaintStyle.setColor(Color.RED);

        Store.exitPaintStyle = new Paint();
        Store.exitPaintStyle.setColor(Color.BLUE);

        Store.currentLevel = INITIAL_LEVEL;
        Store.currentLinesQuantity = INITIAL_QUANTITY_OF_LINES;
        Store.currentColumnsQuantity = INITIAL_QUANTITY_OF_COLUMNS;

        Store.mp = MediaPlayer.create(currentContext, R.raw. start );
        Store.mp.start();

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
                if(!Store.playerPosition.topSeparator){
                    // Go one position to the top
                    Store.playerPosition = Store.mazeSquareComponents[Store.playerPosition.columnIndex][Store.playerPosition.lineIndex-1];
                    //System.out.println("Move to the top");
                }
            break;

            case BOTTOM:
                // There is no bottom separator
                if(!Store.playerPosition.bottomSeparator) {
                    // Go one position to the bottom
                    Store.playerPosition = Store.mazeSquareComponents[Store.playerPosition.columnIndex][Store.playerPosition.lineIndex + 1];
                    //System.out.println("Move to the bottom");
                }
            break;

            case RIGHT:
                // There is no right separator
                if(!Store.playerPosition.rightSeparator) {
                    // Go one position to the right
                    Store.playerPosition = Store.mazeSquareComponents[Store.playerPosition.columnIndex+1][Store.playerPosition.lineIndex];
                    //System.out.println("Move to the right");
                }
            break;

            case LEFT:
                // There is no left separator
                if(!Store.playerPosition.leftSeparator) {
                    // Go one position to the left
                    Store.playerPosition = Store.mazeSquareComponents[Store.playerPosition.columnIndex - 1][Store.playerPosition.lineIndex];
                    //System.out.println("Move to the left");
                }
            break;
        }

        // Verify if the player founds the exit
        verifyEndOfCurrentMaze();

        // Set as old view
        invalidate();
    }

    private boolean updateLevel(){

        if(Store.currentLevel<FINAL_LEVEL){

            Store.currentLevel++;

            Store.currentLinesQuantity+=2;

            Store.currentColumnsQuantity++;

            return true;
        }

        return false;
    }

    private void restartMaze(){
        Store.currentLevel = INITIAL_LEVEL;

        Store.currentLinesQuantity = INITIAL_QUANTITY_OF_LINES;

        Store.currentColumnsQuantity = INITIAL_QUANTITY_OF_COLUMNS;

        buildMazeScreen();
    }

    private int calculateSquareSize(int canvasWidth, int canvasHeight){

        if((canvasWidth/canvasHeight) < (Store.currentColumnsQuantity / Store.currentLinesQuantity)){
            return(canvasWidth/(Store.currentColumnsQuantity + 1));
        }

        // If reached here, first statement is false
        return(canvasHeight/(Store.currentLinesQuantity + 1));
    }

    private float calculateHorizontalMargin(int canvasWidth, float squareSize){
        return((canvasWidth-(Store.currentColumnsQuantity *squareSize))/2);
    }

    private float calculateVerticalMargin(int canvasHeight, float squareSize){
        return((canvasHeight-(Store.currentLinesQuantity *squareSize))/2);
    }

    @Override
    protected void onDraw(Canvas canvas){

        // Background color
        canvas.drawColor(Color.WHITE);

        // Values to the canvas
        int canvasWidth = getWidth();
        int canvasHeight = getHeight();

        // Calculates the square size
        Store.squareSize = calculateSquareSize(canvasWidth, canvasHeight);

        // Calculates horizontal margin
        Store.horizontalMargin = calculateHorizontalMargin(canvasWidth, Store.squareSize);

        // Calculates vertical margin
        Store.verticalMargin  = calculateVerticalMargin(canvasHeight, Store.squareSize);

        canvas.translate(Store.horizontalMargin, Store.verticalMargin);

        drawCanvas(canvas, Store.squareSize);

        invalidate();
    }

    private void drawCanvas(Canvas canvas, float squareSize){

        for(int i = 0; i< Store.currentColumnsQuantity; i++){
            for(int j = 0; j< Store.currentLinesQuantity; j++){

                // Top line separator is present
                if(Store.mazeSquareComponents[i][j].topSeparator){
                    canvas.drawLine(i*squareSize,j*squareSize, (i+1)*squareSize, j*squareSize, Store.separatorPaintStyle);
                }

                // Bottom line separator is present
                if(Store.mazeSquareComponents[i][j].bottomSeparator){
                    canvas.drawLine(i*squareSize,(j+1)*squareSize, (i+1)*squareSize, (j+1)*squareSize, Store.separatorPaintStyle);
                }

                // Right line separator is present
                if(Store.mazeSquareComponents[i][j].rightSeparator){
                    canvas.drawLine((i+1)*squareSize,j*squareSize, (i+1)*squareSize, (j+1)*squareSize, Store.separatorPaintStyle);
                }

                // Left line separator is present
                if(Store.mazeSquareComponents[i][j].leftSeparator){
                    canvas.drawLine(i*squareSize,j*squareSize, i*squareSize, (j+1)*squareSize, Store.separatorPaintStyle);
                }
            }
        }

        float marginPlayerExit = calculateMarginForPlayerAndExit();

        // Shows the player
        canvas.drawRect((Store.playerPosition.columnIndex*squareSize)+marginPlayerExit, (Store.playerPosition.lineIndex*squareSize)+marginPlayerExit, ((Store.playerPosition.columnIndex+1)*squareSize)-marginPlayerExit, ((Store.playerPosition.lineIndex+1)*squareSize)-marginPlayerExit, Store.playerPaintStyle);

        // Shows the exit
        canvas.drawRect((Store.exitPosition.columnIndex*squareSize)+marginPlayerExit, (Store.exitPosition.lineIndex*squareSize)+marginPlayerExit, ((Store.exitPosition.columnIndex+1)*squareSize)-marginPlayerExit, ((Store.exitPosition.lineIndex+1)*squareSize)-marginPlayerExit, Store.exitPaintStyle);
    }

    private void verifyEndOfCurrentMaze(){
        // Go to the next level
        if(Store.playerPosition == Store.exitPosition){

            Store.mp = MediaPlayer.create(currentContext, R.raw. found_exit );
            Store.mp.start();

            if(updateLevel()){

                utils.showAlert(Utils.alertType.NEXT_LEVEL, Store.currentLevel, this.currentContext);

                buildMazeScreen();
            } else {
                utils.showAlert(Utils.alertType.LAST_LEVEL, Store.currentLevel, this.currentContext);

                restartMaze();
            }
        }
    }

    public float calculatePlayerCenteredXPosition(){
        return(Store.horizontalMargin+(Store.playerPosition.columnIndex+1/2f)* Store.squareSize);
    }

    public float calculatePlayerCenteredYPosition(){
        return(Store.verticalMargin+(Store.playerPosition.lineIndex+1/2f)* Store.squareSize);
    }

    private float calculateMarginForPlayerAndExit(){
        return Store.squareSize/10;
    }

    private void buildMazeScreen(){

        Store.mazeSquareComponents = new MazeSquareComponent[Store.currentColumnsQuantity][Store.currentLinesQuantity];

        for(int i = 0; i< Store.currentColumnsQuantity; i++){
            for(int j = 0; j< Store.currentLinesQuantity; j++){
                Store.mazeSquareComponents[i][j] = new MazeSquareComponent(i,j);
            }
        }

        // Defines the inicial position to the player
        Store.playerPosition = Store.mazeSquareComponents[0][0];

        // Defines the exit position
        Store.exitPosition = Store.mazeSquareComponents[Store.currentColumnsQuantity -1][Store.currentLinesQuantity -1];

        backTrackingToGenerateRandomMaze();

    }

    // Backtracking by Cadinho
    private void backTrackingToGenerateRandomMaze(){

        // User for backtracking
        Stack<MazeSquareComponent> backtrackingStack = new Stack<>();

        MazeSquareComponent currentPosition;
        MazeSquareComponent nextPosition;

        // First position
        currentPosition = Store.mazeSquareComponents[0][0];
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
            if(!(Store.mazeSquareComponents[mazeSquareComponent.columnIndex][mazeSquareComponent.lineIndex-1].alreadyVisited)){
                squareBesides.add(Store.mazeSquareComponents[mazeSquareComponent.columnIndex][mazeSquareComponent.lineIndex-1]);
            }
        }

        // Check bottom
        if(mazeSquareComponent.lineIndex < (Store.currentLinesQuantity -1)){
            if(!(Store.mazeSquareComponents[mazeSquareComponent.columnIndex][mazeSquareComponent.lineIndex+1].alreadyVisited)){
                squareBesides.add(Store.mazeSquareComponents[mazeSquareComponent.columnIndex][mazeSquareComponent.lineIndex+1]);
            }
        }

        // Check right
        if(mazeSquareComponent.columnIndex < (Store.currentColumnsQuantity -1)){
            if(!(Store.mazeSquareComponents[mazeSquareComponent.columnIndex+1][mazeSquareComponent.lineIndex].alreadyVisited)){
                squareBesides.add(Store.mazeSquareComponents[mazeSquareComponent.columnIndex+1][mazeSquareComponent.lineIndex]);
            }
        }

        // Check left only if it isn't the first column
        if(mazeSquareComponent.columnIndex>=1){
            if(!(Store.mazeSquareComponents[mazeSquareComponent.columnIndex-1][mazeSquareComponent.lineIndex].alreadyVisited)){
                squareBesides.add(Store.mazeSquareComponents[mazeSquareComponent.columnIndex-1][mazeSquareComponent.lineIndex]);
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
    public class MazeSquareComponent{

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