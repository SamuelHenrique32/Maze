package com.trabalho02.maze.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import java.text.AttributedCharacterIterator;

public class GameScreen extends View {

    private MazeSquareComponent[][] mazeSquareComponents;

    // Constant that define the maze size
    private static final int LINES_QUANTITY = 10;
    private static final int CULUMNS_QUANTITY = 7;
    private static final float SEPARATOR_SIZE = 4;

    private float squareSize, verticalMargin, horizontalMargin;

    private Paint separator;

    public GameScreen(Context context, @Nullable AttributeSet attributes){
        super(context, attributes);

        separator = new Paint();
        separator.setColor(Color.BLACK);
        separator.setStrokeWidth(SEPARATOR_SIZE);

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
        canvas.drawColor(Color.GREEN);

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

        /*for(int i=0 ; i<LINES_QUANTITY ; i++){
            for(int j = 0; j<CULUMNS_QUANTITY ; j++){
                if(mazeSquareComponents[i][j].topLine){
                    canvas.drawLine(i*squareSize,j*squareSize, (i+1)*squareSize, j*squareSize, separator);
                }
            }
        }*/

        drawCanvas(canvas, squareSize);
    }

    private void drawCanvas(Canvas canvas, float squareSize){

        for(int i=0 ; i<CULUMNS_QUANTITY ; i++){
            for(int j = 0; j<LINES_QUANTITY ; j++){

                // Top line separator is present
                if(mazeSquareComponents[j][i].topLine){
                    canvas.drawLine(i*squareSize,j*squareSize, (i+1)*squareSize, j*squareSize, separator);
                }

                // Bottom line separator is present
                if(mazeSquareComponents[j][i].bottomLine){
                    canvas.drawLine(i*squareSize,(j+1)*squareSize, (i+1)*squareSize, (j+1)*squareSize, separator);
                }

                // Right line separator is present
                if(mazeSquareComponents[j][i].rightLine){
                    canvas.drawLine((i+1)*squareSize,j*squareSize, (i+1)*squareSize, (j+1)*squareSize, separator);
                }

                // Left line separator is present
                if(mazeSquareComponents[j][i].leftLine){
                    canvas.drawLine(i*squareSize,j*squareSize, i*squareSize, (j+1)*squareSize, separator);
                }
            }
        }
    }

    private void buildMazeScreen(){
        mazeSquareComponents = new MazeSquareComponent[LINES_QUANTITY][CULUMNS_QUANTITY];

        for(int i=0 ; i<LINES_QUANTITY ; i++){
            for(int j = 0; j<CULUMNS_QUANTITY ; j++){
                mazeSquareComponents[i][j] = new MazeSquareComponent(i,j);
            }
        }
    }

    // Unitary element of the maze
    private class MazeSquareComponent{

        int columnsQuantity;
        int linesQuantity;
        boolean topLine = true;
        boolean bottomLine = true;
        boolean rightLine = true;
        boolean leftLine = true;

        public MazeSquareComponent(int lineIndex, int columnIndex){

            this.linesQuantity = lineIndex;
            this.columnsQuantity = columnIndex;
        }
    }
}
