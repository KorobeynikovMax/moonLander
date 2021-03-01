package com.javarush.games.moonlander;

import com.javarush.engine.cell.*;

public class MoonLanderGame extends Game {

    public static final int WIDTH = 64;
    public static final int HEIGHT = 64;
    private Rocket rocket;
    private GameObject platform;
    private GameObject landscape;
    private boolean isGameStopped;
    private boolean isUpPressed;
    private boolean isLeftPressed;
    private boolean isRightPressed;
    private int score;


    @Override
    public void initialize() {
        setScreenSize(WIDTH, HEIGHT);
        showGrid(false);
        createGame();
    }

    private void createGame() {
        isGameStopped = false;
        isLeftPressed = false;
        isRightPressed = false;
        isUpPressed = false;
        score = 1000;
        createGameObjects();
        drawScene();
        setTurnTimer(50);
    }

    private void createGameObjects() {
        rocket = new Rocket(WIDTH / 2, 0);
        platform = new GameObject(23, MoonLanderGame.HEIGHT - 1,
                ShapeMatrix.PLATFORM);
        landscape = new GameObject(0, 25, ShapeMatrix.LANDSCAPE);
    }
    
    private void drawScene() {
        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                setCellColor(j, i, Color.DARKGRAY);
            }
        }
        rocket.draw(this);
        landscape.draw(this);
    }

    @Override
    public void onTurn(int step) {
        rocket.move(isUpPressed, isLeftPressed, isRightPressed);
        check();
        if (score > 0) score--;
        setScore(score);
        drawScene();
    }

    @Override
    public void setCellColor(int x, int y, Color color) {
        if (x >= 0 && y >= 0 && x < WIDTH && y < HEIGHT) {
            super.setCellColor(x, y, color);
        }
    }

    @Override
    public void onKeyPress(Key key) {
        if (Key.SPACE.equals(key) && isGameStopped) {
            createGame();
            return;
        }
        if (Key.UP.equals(key)) {
            isUpPressed = true;
        }
        if (Key.LEFT.equals(key)) {
            isLeftPressed = true;
            isRightPressed = false;
        }
        if (Key.RIGHT.equals(key)) {
            isLeftPressed = false;
            isRightPressed = true;
        }
    }

    @Override
    public void onKeyReleased(Key key) {
        if (Key.UP.equals(key)) {
            isUpPressed = false;
        }
        if (Key.LEFT.equals(key)) {
            isLeftPressed = false;
        }
        if (Key.RIGHT.equals(key)) {
            isRightPressed = false;
        }
    }

    private void check() {
        if (rocket.isCollision(landscape) && !(rocket.isCollision(platform)
                && rocket.isStopped())) {
            gameOver();
        }
        if (rocket.isCollision(platform) && rocket.isStopped()) {
            win();
        }
    }

    private void win() {
        isGameStopped = true;
        rocket.land();
        showMessageDialog(Color.BISQUE, "You did it!", Color.GREEN, 75);
        stopTurnTimer();
    }

    private void gameOver() {
        isGameStopped = true;
        rocket.crash();
        showMessageDialog(Color.BISQUE, "You lose it!", Color.RED, 75);
        stopTurnTimer();
        score = 0;
    }
}
