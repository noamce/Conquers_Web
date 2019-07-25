package utils;

public class TerritoryButtonsInfo {

    private boolean territoryConquered;
    private boolean gameOver;
    private boolean onlyOnePlayer;
    private boolean isTerritoryBelongsCurrentPlayer;
    private boolean isTargetTerritoryValid;
    private boolean playerDontHaveTerritories;

    public TerritoryButtonsInfo(boolean territoryConquered, boolean gameOver, boolean onlyOnePlayer, boolean isTerritoryBelongsCurrentPlayer, boolean isTargetTerritoryValid, boolean playerDontHaveTerritories) {
        this.territoryConquered = territoryConquered;
        this.gameOver = gameOver;
        this.onlyOnePlayer = onlyOnePlayer;
        this.isTerritoryBelongsCurrentPlayer = isTerritoryBelongsCurrentPlayer;
        this.isTargetTerritoryValid = isTargetTerritoryValid;
        this.playerDontHaveTerritories = playerDontHaveTerritories;
    }
}
