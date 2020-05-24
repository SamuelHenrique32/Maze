package com.trabalho02.maze.utils;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class Utils extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public enum alertType{
        NEXT_LEVEL,
        LAST_LEVEL
    }

    public void showAlert(alertType type, int currentLevel) {

        AlertDialog alertDialog = new AlertDialog.Builder(this).create();

        if(type == alertType.NEXT_LEVEL){

            alertDialog.setTitle("Você Passou de Nível!");

            alertDialog.setMessage("Começar Nível " + currentLevel + "?");

            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Sim", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {dialog.dismiss();}
            });

            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Não", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {dialog.dismiss();}
            });
        }
        else if(type == alertType.LAST_LEVEL){

            alertDialog.setTitle("Você Venceu!");

            alertDialog.setMessage("Começar Novamente?");

            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Sim", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {dialog.dismiss();}
            });

            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Não", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {dialog.dismiss();}
            });
        }

        alertDialog.show();
    }
}
