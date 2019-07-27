package utils;

public class PlayerModel {
    private String name;
    private int color;
    private int funds;
    private int numberOfTerritories;
    private int roundNumber;
    private int totalCycles;

    public PlayerModel(String name, int color, int funds, int numberOfTerritories, int roundNumber, int totalCycles) {
        this.name = name;
        this.color = color;
        this.funds = funds;
        this.numberOfTerritories = numberOfTerritories;
        this.roundNumber = roundNumber;
        this.totalCycles = totalCycles;
    }
}
