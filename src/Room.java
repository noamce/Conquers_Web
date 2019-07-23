import GameEngine.GameEngine;
import GameObjects.Player;

import java.util.ArrayList;
import java.util.List;

public class Room {


    private String roomID="1";
    private List<Player> players = new ArrayList<>();
    private int numberOfOnlinePlayers;
    private int maxNumberOfOnlineUsers;
    private String roomCreator;
    private GameEngine engine;
    private int Rows;
    private int Columns;
    private int sizeofUnits;
    private boolean gameStarted=false;

    public Room(int maxNumberOfOnlineUsers, String gameCreator, GameEngine engine) {
        this.roomID=roomID + '0';
        this.maxNumberOfOnlineUsers = maxNumberOfOnlineUsers;
        this.numberOfOnlinePlayers = 0;
        this.roomCreator =gameCreator;
        this.engine=engine;
        this.Rows=engine.getDescriptor().getRows();
        this.Columns=engine.getDescriptor().getColumns();
        this.sizeofUnits=engine.getDescriptor().getUnitMap().size();
    }

    void addPlayer(Player player){
        players.add(player);
        numberOfOnlinePlayers++;
        if(numberOfOnlinePlayers==maxNumberOfOnlineUsers) {
            gameStarted = true;

        }
    }
    List<Player> getPlayers ()
    {
        return this.players;
    }
    void removePlayer(Player player){
        players.remove(player);
        numberOfOnlinePlayers--;
    }

public boolean isGameStarted()
{
    return gameStarted;
}
    public void setGameIsAlive(boolean bool){
        this.gameStarted = bool;
    }

    public String getRoomID() {
        return roomID;
    }
    public void removePlayer(String username){
        for(int i = 0 ; i< players.size(); i++){
            if(players.get(i).getPlayer_name().equals(username)){
                System.out.println("removed player" + players.get(i).getPlayer_name());
                players.remove(players.get(i));
                numberOfOnlinePlayers--;
            }
        }

    }

    public int getNumberOfOnlinePlayers() {
        return numberOfOnlinePlayers;
    }

    public int getMaxNumberOfOnlineUsers() {
        return maxNumberOfOnlineUsers;
    }


    public String getRoomCreator() {
        return roomCreator;
    }

    public GameEngine getGameEngine() {
        return this.engine;
    }

    public boolean isGameIsAlive() {
        return gameStarted;
    }

    public void setPlayers() {
        engine.getGameManager().loadPlayersIntoQueueOfTurns(players);
    }


    public boolean hasPlayer(String userName) {
        boolean[] result = new boolean[1];
        players.forEach(player -> {
            if(player.getPlayer_name().equals(userName)){
                result[0] = true;
            } else {
                result[0] = false;
            }
        });
        return result[0];
    }
}
