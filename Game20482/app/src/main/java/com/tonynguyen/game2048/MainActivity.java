package com.tonynguyen.game2048;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.GridLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private int[][] board = new int[4][4];
    private TextView[][] cells = new TextView[4][4];
    private GridLayout gridLayout;
    private TextView scoreView, bestView;
    AppCompatButton buttonNew, buttonUndo;
    private BoardGame boardGame;
    private GestureDetector gestureDetector;
    private int score, bestScore;
    private AlertDialog gameOverDialog;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gridLayout = findViewById(R.id.boardGame);
        scoreView = findViewById(R.id.score);
        bestView = findViewById(R.id.best);
        gestureDetector = new GestureDetector(this, new SwipeGestureListener());
        init();
        updateUI();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event) || super.onTouchEvent(event);
    }

    public void init(){
        //Boardgame
        boardGame = new BoardGame(4);
        board = boardGame.getBoardGame();
        //Score
        this.score = boardGame.getScore();
        //Dialog game over
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_game_over, null);
        builder.setView(dialogView);
        builder.setCancelable(false);
        gameOverDialog = builder.create();
        //bestScore
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        int bestScore = sharedPreferences.getInt("SCORE", 0);
        bestView.setText(String.valueOf(bestScore));

    }
    private void updateUI(){
        score = boardGame.getScore();
        if(boardGame.isGameOver()){
            //show Dialog game over
            gameOverDialog.show();
            //save best score
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("SCORE", score);
            editor.apply();
        }
        for(int i=0; i<4; i++){
            for(int j=0; j<4; j++){
                if(cells[i][j]==null){
                    cells[i][j] = new TextView(this);
                    GridLayout.Spec rowSpec = GridLayout.spec(i, 1f);
                    GridLayout.Spec colSpec = GridLayout.spec(j, 1f);
                    GridLayout.LayoutParams params = new GridLayout.LayoutParams(rowSpec, colSpec);
                    params.height = 0;
                    params.width = 0;
                    params.setMargins(12,12,12,12);
                    cells[i][j].setLayoutParams(params);
                    gridLayout.addView(cells[i][j]);
                }
                int value = board[i][j];
                cells[i][j].setBackgroundResource(getCellBackground(value));
                cells[i][j].setText(value>0 ? String.valueOf(value) : "");
                cells[i][j].setTypeface(null, Typeface.BOLD);
                if(value < 5){
                    cells[i][j].setTextColor(getColor(R.color.cellSmall));
                }else {
                    cells[i][j].setTextColor(getColor(R.color.cellBig));
                }

                if(value <100){
                    cells[i][j].setTextSize(38);
                }
                else if(value < 1000){
                    cells[i][j].setTextSize(30);
                }
                else if(value<10000){
                    cells[i][j].setTextSize(24);
                }
                else{
                    cells[i][j].setTextSize(18);
                }
                cells[i][j].setGravity(Gravity.CENTER);
            }
        }
        //Score
        scoreView.setText(String.valueOf(score));
        //bestScore
        if(score>=bestScore){
            bestView.setText(String.valueOf(score));
        }
    }
    private int getCellBackground(int value){
        switch(value){
            case 0: return R.drawable.background_empty;
            case 2: return R.drawable.background_2;
            case 4: return R.drawable.background_4;
            case 8: return R.drawable.background_8;
            case 16: return R.drawable.background_16;
            case 32: return R.drawable.background_32;
            case 64: return R.drawable.background_64;
            case 128: return R.drawable.background_128;
            case 256: return R.drawable.background_256;
            case 512: return R.drawable.background_512;
            case 1024: return R.drawable.background_1024;
            case 2048: return R.drawable.background_2048;
            case 4096: return R.drawable.background_4096;
            case 8192: return R.drawable.background_8192;
            case 16384: return R.drawable.background_16384;
            case 32768: return R.drawable.background_32768;
            case 65536: return R.drawable.background_65536;
            default: return R.drawable.background_131072;
        }
    }

    private void moveTile(int fromRow, int fromColumn, int toRow, int toColumn) {
        if (cells[fromRow][fromColumn] != null) {
            TextView tile = cells[fromRow][fromColumn];
            cells[toRow][toColumn] = tile;
            cells[fromRow][fromColumn] = null;

            ObjectAnimator animatorX = ObjectAnimator.ofFloat(tile, "translationX", 0f, tile.getWidth() * (toColumn - fromColumn));
            ObjectAnimator animatorY = ObjectAnimator.ofFloat(tile, "translationY", 0f, tile.getHeight() * (toRow - fromRow));

            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.playTogether(animatorX, animatorY);
            animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
            animatorSet.setDuration(1000); // Thời gian di chuyển (milliseconds)
            animatorSet.start();
        }
    }
    private class SwipeGestureListener extends GestureDetector.SimpleOnGestureListener {
        private static final int SWIPE_THRESHOLD = 100;
        private static final int SWIPE_VELOCITY_THRESHOLD = 100;

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            float diffX = e2.getX() - e1.getX();
            float diffY = e2.getY() - e1.getY();

            if (Math.abs(diffX) > Math.abs(diffY)) {
                if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffX > 0) {

                        boardGame.move(Direction.RIGHT);
                    } else {

                        boardGame.move(Direction.LEFT);
                    }
                }
            } else {
                if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffY > 0) {

                        boardGame.move(Direction.DOWN);
                    } else {

                        boardGame.move(Direction.UP);
                    }
                }
            }
            updateUI();
            return super.onFling(e1, e2, velocityX, velocityY);
        }
    }


}