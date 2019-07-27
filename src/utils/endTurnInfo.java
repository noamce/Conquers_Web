package utils;

public class endTurnInfo {


    private boolean isCycleOver;
    private boolean gameOver;
    private boolean thereIsWinner;
    private String  winnerPlayerName;
    private boolean isItFirstRound;

    public endTurnInfo(boolean isCycleOver, boolean gameOver, boolean thereIsWinner, String winnerPlayerName, boolean isItFirstRound) {
        this.isCycleOver = isCycleOver;
        this.gameOver = gameOver;
        this.thereIsWinner = thereIsWinner;
        this.winnerPlayerName = winnerPlayerName;
        this.isItFirstRound = isItFirstRound;
    }
}
