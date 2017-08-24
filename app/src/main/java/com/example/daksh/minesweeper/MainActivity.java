package com.example.daksh.minesweeper;

import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.content.ContextCompat;
import android.util.Log;


import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {

    TextView textView;
    static final int n=9;
    LinearLayout rowLayouts[];
    myButton buttons[][];
    LinearLayout mainLayout;
    LinearLayout.LayoutParams params;
    int arr[][]=new int[n][n];
    boolean gameOver=false;
    static int flags;
    final static int NO_CLICKED=0;
    final static int CLICKED=1;
    final static int INCOMPLETE=0;
    final static int COMPLETE=1;
    boolean first_clicked=false;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        flags=n;
        mainLayout=(LinearLayout)findViewById(R.id.mainLayout);

        for (int k = 0; k < flags; k++) {
        settingBombs();
        }

        setUpBoard();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.newgame)
        {
            resetBoard();
        }
        return true;
    }

    void resetBoard() {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                buttons[i][j].player = NO_CLICKED;
                buttons[i][j].setText("");
                arr[i][j]=0;
            }
        }

        flags=n;
        gameOver = false;
        // first_clicked=false;
        for (int k = 0; k < flags; k++) {
            settingBombs();
        }

        setUpBoard();
    }


    private void setUpBoard() {

       mainLayout.removeAllViews();


        textView=new TextView(this);
        params=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,0,1f);
        //params.setMargins(1,1,1,1);
        textView.setLayoutParams(params);
        textView.setText("Flags Left - "+flags);
        textView.setTextSize(20);
        textView.setBackgroundResource(R.drawable.text_color_boundary);
        textView.setTextColor(ContextCompat.getColor(this, R.color.lightgrey));
        textView.setTypeface(textView.getTypeface(), Typeface.BOLD);
        textView.setGravity(Gravity.CENTER);
        mainLayout.addView(textView);


        rowLayouts=new LinearLayout[n];
        for(int i=0;i<n;i++)
        {
            rowLayouts[i]=new LinearLayout(this);
            params=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,0,1f);
            params.setMargins(1,1,1,1);
            rowLayouts[i].setLayoutParams(params);
            rowLayouts[i].setOrientation(LinearLayout.HORIZONTAL);
            mainLayout.addView(rowLayouts[i]);
        }

        buttons=new myButton[n][n];
        for(int i=0;i<n;i++)
        {
            for(int j=0;j<n;j++)
            {
                buttons[i][j]=new myButton(this,i,j);
                params=new LinearLayout.LayoutParams(0,ViewGroup.LayoutParams.MATCH_PARENT,1f);
                params.setMargins(1,1,1,1);
                buttons[i][j].setLayoutParams(params);
                buttons[i][j].setOnClickListener(this);
                buttons[i][j].setOnLongClickListener(this);
                buttons[i][j].setTextSize(25);
                buttons[i][j].setBackgroundResource(R.drawable.button_color_background);
                rowLayouts[i].addView(buttons[i][j]);
            }

        }

    }

    @Override
    public void onClick(View v) {
        if(gameOver)
            return;


        myButton button = (myButton)v;
        if(button.player!=NO_CLICKED)
        {
            Toast.makeText(this, "Already Clicked ", Toast.LENGTH_SHORT).show();
            return;

        }
        int i=button.xCoordinate;
        int j=button.yCoordinate;

         if(arr[i][j]=='*')
        {
          //  button.setText("*");
            button.setBackgroundResource(R.drawable.mine_pic);
         //   button.setBackgroundColor(ContextCompat.getColor(this, R.color.red));
            for(int k=0;k<n;k++)
            {
                for(int w=0;w<n;w++)
                {
                    if(arr[k][w]=='*')
                    {
                       // buttons[k][w].setText("*");
                                   buttons[k][w].setBackgroundResource(R.drawable.mine_pic);
           //             buttons[k][w].setBackgroundColor(ContextCompat.getColor(this, R.color.red));
                    }
                }
            }
         //   button.setText("*");


//            button.setBackground(null);
            Toast.makeText(this, "You Lose", Toast.LENGTH_LONG).show();
            gameOver=true;
            return;
        }
        else{
            checkingAdjacentZeroes(button);
        }

        int status=gameStatus();
        if(status==COMPLETE){
            Toast.makeText(this, "You Won", Toast.LENGTH_LONG).show();
            gameOver=true;
        }

    }


    private int gameStatus() {

        for(int i=0;i<n;i++)
        {
            for(int j=0;j<n;j++)
            {
                if(buttons[i][j].player!=CLICKED)
                    return INCOMPLETE;
            }
        }
        if(flags==0)
        return COMPLETE;

        return INCOMPLETE;

    }


    public void settingBombs() {

        Random numberGenerator = new Random();
        while (true)
        {
                int i = numberGenerator.nextInt(n);
                int j = numberGenerator.nextInt(n);
                if (arr[i][j] != '*') {
                    arr[i][j] = '*';
                    arrangingDigits(i,j);
                    break;
                }

        }
    }

    public void arrangingDigits(int i,int j)
    {
        arr[i][j]='*';

            if (i - 1 >= 0 && arr[i - 1][j] != '*') {
                arr[i - 1][j] += 1;
            }
            if (i + 1 < n && j + 1 < n && arr[i + 1][j + 1] != '*') {
                arr[i + 1][j + 1] += 1;
            }
            if (i + 1 < n && j - 1 >= 0 && arr[i + 1][j - 1] != '*') {
                arr[i + 1][j - 1] += 1;
            }
            if (i + 1 < n && arr[i + 1][j] != '*') {
                arr[i + 1][j] += 1;
            }
            if (j + 1 < n && arr[i][j + 1] != '*') {
                arr[i][j + 1] += 1;
            }
            if (j - 1 >= 0 && arr[i][j - 1] != '*') {
                arr[i][j - 1] += 1;
            }
            if (i - 1 >= 0 && j + 1 < n && arr[i - 1][j + 1] != '*') {
                arr[i - 1][j + 1] += 1;
            }
            if (i - 1 >= 0 && j - 1 >= 0 && arr[i - 1][j - 1] != '*') {
                arr[i - 1][j - 1] += 1;
            }


        }




    @Override
    public boolean onLongClick(View v) {
       if(gameOver)
            return true;
        myButton button = (myButton)v;
        int i=button.xCoordinate;
        int j=button.yCoordinate;

        if(button.longClick)
        {
            if(flags>0) {
                button.player = CLICKED;
                textView.setText("Flags Left - " + (flags - 1));
                flags--;
               // button.setText("F");
                        button.setBackgroundResource(R.drawable.flag_image);
         //       button.setBackgroundColor(ContextCompat.getColor(this, R.color.pink));
                button.longClick = !button.longClick;
                int status=gameStatus();
                if(status==COMPLETE){
                    Toast.makeText(this, "You Won", Toast.LENGTH_LONG).show();
                    gameOver=true;
                }
            }
            else{
                Toast.makeText(this,"You can't use more flags ",Toast.LENGTH_SHORT).show();
            }
        }
        else if(!button.longClick)
        {
            button.player=NO_CLICKED;
            textView.setText("Flags Left - "+(flags+1));
            flags++;
            button.setText("");
            buttons[i][j].setBackgroundResource(R.drawable.button_color_background);
            button.longClick=!button.longClick;
        }
        return true;
    }


    public void checkingAdjacentZeroes(myButton button) {

        int i = button.xCoordinate;
        int j = button.yCoordinate;
        if (button.player != CLICKED) {

            if (arr[i][j] != 0) {
                button.setText(arr[i][j] + "");
                backgroundColorofText(arr[i][j],button);
                button.setEnabled(false);
                button.setBackgroundColor(ContextCompat.getColor(this, R.color.lightgrey));
                button.player = CLICKED;
                return;
            }

            button.setText("");
            button.setEnabled(false);
            button.setBackgroundColor(ContextCompat.getColor(this, R.color.lightgrey));
            button.player = CLICKED;
            if (i - 1 >= 0) {

                checkingAdjacentZeroes(buttons[i - 1][j]);
            }
            if (i + 1 < n && j + 1 < n) {


                checkingAdjacentZeroes(buttons[i + 1][j + 1]);
            }
            if (i + 1 < n && j - 1 >= 0) {


                checkingAdjacentZeroes(buttons[i + 1][j - 1]);

            }
            if (i + 1 < n) {

                checkingAdjacentZeroes(buttons[i + 1][j]);
            }
            if (j + 1 < n) {


                checkingAdjacentZeroes(buttons[i][j + 1]);

            }
            if (j - 1 >= 0) {


                checkingAdjacentZeroes(buttons[i][j - 1]);
            }
            if (i - 1 >= 0 && j + 1 < n) {

                checkingAdjacentZeroes(buttons[i - 1][j + 1]);
            }
            if (i - 1 >= 0 && j - 1 >= 0) {

                checkingAdjacentZeroes(buttons[i - 1][j - 1]);
            }

        }
    }

    private void backgroundColorofText(int i, myButton button) {
        if (i == 1)
            button.setTextColor(ContextCompat.getColor(this, R.color.blue));

        else if (i == 2) {
            button.setTextColor(ContextCompat.getColor(this, R.color.green));
        }
        else if(i==3)
        {
            button.setTextColor(ContextCompat.getColor(this, R.color.red));
        }
        else if(i==4)
        {
            button.setTextColor(ContextCompat.getColor(this, R.color.colorAccent));
        }
        else if(i==5)
        {
            button.setTextColor(ContextCompat.getColor(this, R.color.orange));
        }
    }

}
