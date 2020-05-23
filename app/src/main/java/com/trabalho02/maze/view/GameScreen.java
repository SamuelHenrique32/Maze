package com.trabalho02.maze.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

public class GameScreen extends View {

    private MazeSquareComponent[][] mazeSquareComponents;

    // Constant that define the maze size
    private static final int LINES_QUANTITY = 12;
    private static final int CULUMNS_QUANTITY = 8;
    private static final float SEPARATOR_SIZE = 8;

    private float squareSize, verticalMargin, horizontalMargin;

    private Random rand;

    private Paint separator;

    public GameScreen(Context context, @Nullable AttributeSet attributes){
        super(context, attributes);

        separator = new Paint();
        separator.setColor(Color.BLACK);
        separator.setStrokeWidth(SEPARATOR_SIZE);

        rand = new Random();

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
                    canvas.drawLine(i*squareSize,j*squareSize, (i+1)*squareSize, j*squareSize, separator);
                }

                // Bottom line separator is present
                if(mazeSquareComponents[i][j].bottomSeparator){
                    canvas.drawLine(i*squareSize,(j+1)*squareSize, (i+1)*squareSize, (j+1)*squareSize, separator);
                }

                // Right line separator is present
                if(mazeSquareComponents[i][j].rightSeparator){
                    canvas.drawLine((i+1)*squareSize,j*squareSize, (i+1)*squareSize, (j+1)*squareSize, separator);
                }

                // Left line separator is present
                if(mazeSquareComponents[i][j].leftSeparator){
                    canvas.drawLine(i*squareSize,j*squareSize, i*squareSize, (j+1)*squareSize, separator);
                }
            }
        }
    }

    private void buildMazeScreen(){

        mazeSquareComponents = new MazeSquareComponent[CULUMNS_QUANTITY][LINES_QUANTITY];

        for(int i=0 ; i<CULUMNS_QUANTITY ; i++){
            for(int j = 0; j<LINES_QUANTITY ; j++){
                mazeSquareComponents[i][j] = new MazeSquareComponent(i,j);
            }
        }

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
