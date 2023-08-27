package com.tonynguyen.game2048;

import java.util.Random;

public class BoardGame {
    private int boardSize;
    private int[][] boardGame;
    private int score;

    public BoardGame(int boardSize) {
        this.boardSize = boardSize;
        boardGame = new int[boardSize][boardSize];
        score = 0;
        init();
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int[][] getBoardGame() {
        return boardGame;
    }

    public void init() {
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                boardGame[i][j] = 0;
            }
        }
        //create 2 cell
        Random random = new Random();
        int numTilesFill = 2;
        while (numTilesFill > 0) {
            int x = random.nextInt(boardSize);
            int y = random.nextInt(boardSize);
            if (boardGame[x][y] == 0) {
                boardGame[x][y] = (random.nextInt(2) + 1) * 2;
                numTilesFill--;
            }
        }
    }

    private boolean canMoveUp() {
        for (int col = 0; col < boardSize; col++) {
            for (int row = 1; row < boardSize; row++) {
                if (boardGame[row][col] != 0 && (boardGame[row - 1][col] == 0 || boardGame[row - 1][col] == boardGame[row][col])) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean canMoveDown() {
        for (int col = 0; col < boardSize; col++) {
            for (int row = boardSize - 2; row >= 0; row--) {
                if (boardGame[row][col] != 0 && (boardGame[row + 1][col] == 0 || boardGame[row + 1][col] == boardGame[row][col])) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean canMoveLeft() {
        for (int row = 0; row < boardSize; row++) {
            for (int col = 1; col < boardSize; col++) {
                if (boardGame[row][col] != 0 && (boardGame[row][col - 1] == 0 || boardGame[row][col - 1] == boardGame[row][col])) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean canMoveRight() {
        for (int row = 0; row < boardSize; row++) {
            for (int col = boardSize - 2; col >= 0; col--) {
                if (boardGame[row][col] != 0 && (boardGame[row][col + 1] == 0 || boardGame[row][col + 1] == boardGame[row][col])) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isGameOver() {
        return !(canMoveRight() || canMoveLeft() || canMoveUp() || canMoveDown());
    }

    //Move
    public void move(Direction direction) {
        switch (direction) {
            case UP:
                moveUp();
                break;
            case DOWN:
                moveDown();
                break;
            case LEFT:
                moveLeft();
                break;
            case RIGHT:
                moveRight();
                break;
        }
    }

    private void moveDown() {
        if (!canMoveDown()) return;

        for (int col = 0; col < boardSize; col++) {
            int pointStop = boardSize-2;
            for (int row = boardSize - 2; row >=0; row--) {
                if (boardGame[row][col] == 0) continue;
                int pointStart = row;
                while(pointStart <= pointStop){
                    if(boardGame[pointStart][col] != boardGame[pointStart+1][col] && boardGame[pointStart+1][col] == 0){
                        boardGame[pointStart+1][col] = boardGame[pointStart][col];
                        boardGame[pointStart][col] = 0;
                    }
                    else if(boardGame[pointStart][col] == boardGame[pointStart+1][col] && boardGame[pointStart+1][col] != 0){
                        boardGame[pointStart+1][col] *=2;
                        boardGame[pointStart][col] = 0;
                        score+=boardGame[pointStart+1][col];
                        pointStop=pointStart;
                    }
                    pointStart++;
                }
            }
        }
        randomCell();
    }

    private void moveUp() {
        if (!canMoveUp()) return;

        for (int col = 0; col < boardSize; col++) {
            int pointStop = 1;
            for (int row = 1; row < boardSize; row++) {
                if (boardGame[row][col] == 0) continue;
                int pointStart = row;
                while(pointStart>=pointStop){
                    if(boardGame[pointStart][col] != boardGame[pointStart-1][col] && boardGame[pointStart-1][col] == 0){
                        boardGame[pointStart-1][col] = boardGame[pointStart][col];
                        boardGame[pointStart][col] = 0;
                    }
                    else if(boardGame[pointStart][col] == boardGame[pointStart-1][col] && boardGame[pointStart-1][col] != 0){
                        boardGame[pointStart-1][col] *= 2;
                        boardGame[pointStart][col] = 0;
                        score+=boardGame[pointStart-1][col];
                        pointStop = pointStart;
                    }
                    pointStart--;
                }
            }
        }
        randomCell();
    }

    private void moveLeft() {
        if (!canMoveLeft()) return;

        for (int row = 0; row < boardSize; row++) {
            int pointStop = 1;
            for (int col = 1; col < boardSize; col++) {
                if (boardGame[row][col] == 0) continue;
                int pointStart = col;
                while(pointStart>=pointStop){
                    if(boardGame[row][pointStart] != boardGame[row][pointStart-1] && boardGame[row][pointStart-1] == 0){
                        boardGame[row][pointStart-1] = boardGame[row][pointStart];
                        boardGame[row][pointStart] = 0;
                    }
                    else if(boardGame[row][pointStart] == boardGame[row][pointStart-1] && boardGame[row][pointStart-1] != 0){
                        boardGame[row][pointStart-1] *= 2;
                        boardGame[row][pointStart] = 0;
                        score+=boardGame[row][pointStart-1];
                        pointStop = pointStart;
                    }
                    pointStart--;
                }
            }
        }
        randomCell();
    }

    private void moveRight() {
        if (!canMoveRight()) return;

        for (int row = 0; row < boardSize; row++) {
            int pointStop = boardSize-2;
            for (int col = boardSize - 2; col >= 0; col--) {
                if (boardGame[row][col] == 0) continue;
                int pointStart = col;
                while(pointStart<=pointStop){
                    if(boardGame[row][pointStart] != boardGame[row][pointStart+1] && boardGame[row][pointStart+1] == 0){
                        boardGame[row][pointStart+1] = boardGame[row][pointStart];
                        boardGame[row][pointStart] = 0;
                    }
                    else if(boardGame[row][pointStart] == boardGame[row][pointStart+1] && boardGame[row][pointStart+1] != 0){
                        boardGame[row][pointStart+1] *= 2;
                        boardGame[row][pointStart] = 0;
                        score+=boardGame[row][pointStart+1];
                        pointStop = pointStart;
                    }
                    pointStart++;
                }
            }
        }
        randomCell();
    }

    public void randomCell() {
        Random random = new Random();
        int emptyCells = countEmptyCells();
        int cellIndex = random.nextInt(emptyCells)+1;
        int count = 0;
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (boardGame[i][j] == 0) {
                    count++;
                    if (count == cellIndex) {
                        boardGame[i][j] = getRandomNumber(random);
                        break;
                    }
                }
            }
            if (count == cellIndex) {
                break;
            }
        }
    }

    private int countEmptyCells() {
        int count = 0;
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (boardGame[i][j] == 0) {
                    count++;
                }
            }
        }
        return count;
    }

    //80% - 2 || 20% - 4
    private int getRandomNumber(Random random){
        int randomNumber = random.nextInt(10);
        if(randomNumber < 8) return 2;
        return 4;
    }
}
