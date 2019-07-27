package utils;

import GameObjects.Army;

public class BattleInfo {

    private Army PreAttackerArmy;
    private Army PreDefenceArmy;
    private String winner;
    private boolean canBuildTheArmyFromTourings;

    public BattleInfo(Army preAttackerArmy, Army preDefenceArmy, String winner,boolean canBuildTheArmyFromTourrings) {
        PreAttackerArmy = preAttackerArmy;
        PreDefenceArmy = preDefenceArmy;
        this.winner = winner;
        this.canBuildTheArmyFromTourings=canBuildTheArmyFromTourrings;
    }
}
