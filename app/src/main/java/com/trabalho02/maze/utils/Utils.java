package com.trabalho02.maze.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class Utils extends AppCompatActivity {

    private Context currentContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        currentContext = getApplicationContext();
    }

    public enum alertType{
        NEXT_LEVEL,
        LAST_LEVEL
    }

    public void showAlert(alertType type, int currentLevel, Context context) {

        AlertDialog alertDialog = new AlertDialog.Builder(context).create();

        if(type == alertType.NEXT_LEVEL){

            alertDialog.setTitle("Você Passou de Nível!");

            alertDialog.setMessage("Póximo Nível: " + currentLevel);

            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            alertDialog.show();
        }
        else if(type == alertType.LAST_LEVEL){

            alertDialog.setTitle("Parabéns! Você venceu");

            alertDialog.setMessage("Recomeçar?");

            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            alertDialog.show();
        }

        //alertDialog.show();
    }
}