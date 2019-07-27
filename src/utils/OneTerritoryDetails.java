package utils;

import GameObjects.Player;

public class OneTerritoryDetails

{
    int id;
    int Threshold;
    int profit;
    Player conquer;

    public OneTerritoryDetails(int id, int threshold, int profit, Player conquer) {
        this.id = id;
        Threshold = threshold;
        this.profit = profit;
        this.conquer = conquer;
    }
}
