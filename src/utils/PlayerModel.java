package utils;

public class PlayerModel {
    private String name;
    private int color;
    private int funds;
    private int territoriesSize;
    private int roundNumber;
    private int totalCycles;

    public PlayerModel(String name, int color, int funds, int territoriesSize, int roundNumber, int totalCycles) {
        this.name = name;
        this.color = color;
        this.funds = funds;
        this.territoriesSize = territoriesSize;
        this.roundNumber = roundNumber;
        this.totalCycles = totalCycles;
    }
}
