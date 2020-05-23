package com.trabalho02.maze.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.trabalho02.maze.utils.Utils;

import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

import static android.app.PendingIntent.getActivity;

public class GameScreen extends View {

    private MazeSquareComponent[][] mazeSquareComponents;

    // Constants
    private static final int LINES_QUANTITY = 12;
    private static final int CULUMNS_QUANTITY = 8;
    private static final float SEPARATOR_SIZE = 8;
    private static final int INITIAL_LEVEL = 1;
    private static final int FINAL_LEVEL = 3;

    private int currentLevel = INITIAL_LEVEL;

    private float squareSize, verticalMargin, horizontalMargin;

    private Random rand;

    private MazeSquareComponent playerPosition;
    private MazeSquareComponent exitPosition;

    private Paint separatorPaintStyle;
    private Paint playerPaintStyle;
    private Paint exitPaintStyle;

    private Utils utils;

    // Directions
    private enum MoveDirections{
        TOP,
        BOTTOM,
        RIGHT,
        LEFT
    }

    private void move(MoveDirections dir){

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

    public GameScreen(Context context, @Nullable AttributeSet attributes){
        super(context, attributes);

        separatorPaintStyle = new Paint();
        separatorPaintStyle.setColor(Color.BLACK);
        separatorPaintStyle.setStrokeWidth(SEPARATOR_SIZE);

        rand = new Random();

        playerPaintStyle = new Paint();
        playerPaintStyle.setColor(Color.RED);

        exitPaintStyle = new Paint();
        exitPaintStyle.setColor(Color.BLUE);

        utils = new Utils();

        buildMazeScreen();
    }

    private int calculateSquareSize(int canvasWidth, int canvasHeight){

        if((canvasWidth/canvasHeight) < (CULUMNS_QUANTITY/LINES_QUANTITY)){
            return(canvasWidth/(CULUMNS_QUANTITY + 1));
        }

        // If reached here, first statement is false
        return(canvasHeight/(LINES_QUANTITY + 1));
    }

    private float calculateHorizontalMargin(int canvasWidth, float squareSize){
        return((canvasWidth-(CULUMNS_QUANTITY*squareSize))/2);
    }

    private float calculateVerticalMargin(int canvasHeight, float squareSize){
        return((canvasHeight-(LINES_QUANTITY*squareSize))/2);
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

        for(int i=0 ; i<CULUMNS_QUANTITY ; i++){
            for(int j = 0; j<LINES_QUANTITY ; j++){

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

            // Last level
            if(currentLevel == FINAL_LEVEL){
                utils.showAlert("Sucesso!", "Você Ganhou");
            }
            else{
                utils.showAlert("Você Passou de Nível!", "Começar Nível " + this.currentLevel + "?");

                buildMazeScreen();
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

        mazeSquareComponents = new MazeSquareComponent[CULUMNS_QUANTITY][LINES_QUANTITY];

        for(int i=0 ; i<CULUMNS_QUANTITY ; i++){
            for(int j = 0; j<LINES_QUANTITY ; j++){
                mazeSquareComponents[i][j] = new MazeSquareComponent(i,j);
            }
        }

        // Defines the inicial position to the player
        playerPosition = mazeSquareComponents[0][0];

        // Defines the exit position
        exitPosition = mazeSquareComponents[CULUMNS_QUANTITY-1][LINES_QUANTITY-1];

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
        if(mazeSquareComponent.lineIndex < (LINES_QUANTITY-1)){
            if(!(mazeSquareComponents[mazeSquareComponent.columnIndex][mazeSquareComponent.lineIndex+1].alreadyVisited)){
                squareBesides.add(mazeSquareComponents[mazeSquareComponent.columnIndex][mazeSquareComponent.lineIndex+1]);
            }
        }

        // Check right
        if(mazeSquareComponent.columnIndex < (CULUMNS_QUANTITY-1)){
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
