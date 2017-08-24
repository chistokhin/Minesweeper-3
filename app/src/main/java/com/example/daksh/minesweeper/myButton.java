package com.example.daksh.minesweeper;

import android.content.Context;
import android.widget.Button;

/**
 * Created by Daksh Garg on 6/16/2017.
 */

public class myButton extends Button {

    int xCoordinate;
    int yCoordinate;
    int player;
    boolean longClick;
    public myButton(Context context,int x,int y) {
        super(context);
        xCoordinate=x;
        yCoordinate=y;
        player=MainActivity.NO_CLICKED;
        longClick=true;
    }
}
