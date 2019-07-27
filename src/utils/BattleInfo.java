package utils;

import GameEngine.GameEngine;
import GameObjects.Army;

import java.util.ArrayList;
import java.util.List;

public class BattleInfo {

    private List<OneBattleData> PreattackerArmy;
    private List<OneBattleData> PredefenceArmy;
    String winner;
    private boolean canBuildTheArmyFromTourings;

    public BattleInfo(GameEngine engine,Army preAttackerArmy, Army preDefenceArmy, String winner, boolean canBuildTheArmyFromTourrings) {
        this.PreattackerArmy = returnArmy(engine,preAttackerArmy);
        this.PredefenceArmy = returnArmy(engine,preDefenceArmy);
        this.winner = winner;
        this.canBuildTheArmyFromTourings=canBuildTheArmyFromTourrings;
    }

    private List<OneBattleData> returnArmy(GameEngine engine,Army army) {
        List<OneBattleData> preArmy=new ArrayList<>();
        List<String> unitMapp = new ArrayList<String>(engine.getDescriptor().getUnitMap().keySet());

        for (int i = 0; i < unitMapp.size(); i++) {

            String unitType = unitMapp.get(i);
            int preAttackAmount = army.howMachFromThisUnitType(unitType);
            int preAttackFp = army.getTotalPowerPerUnit(unitType);
            preArmy.add(new OneBattleData(unitType, preAttackAmount, preAttackFp));


        }
        return preArmy;
    }

}
