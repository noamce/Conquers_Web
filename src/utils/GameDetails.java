package utils;

import GameEngine.GameEngine;
public class GameDetails {
    int cols, rows;
    String userName;
    GameEngine gameEngine;
    boolean started;

    public GameDetails(int cols, int rows, String userName,GameEngine engine,boolean started) {
        this.cols = cols;
        this.rows = rows;
        this.userName = userName;
        this.gameEngine=engine;
        this.started=started;
    }
}
