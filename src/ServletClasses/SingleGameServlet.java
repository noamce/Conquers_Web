package ServletClasses;

import GameEngine.GameEngine;
import GameObjects.*;
import com.google.gson.Gson;
import javafx.scene.control.Alert;
import utils.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

@WebServlet(name = "ServletClasses.SingleGameServlet")
public class SingleGameServlet extends HttpServlet {


    private final String GAMES_URL = "Lobby/lobby.html";
    private List<Room> rooms = new ArrayList<>();
    private ServletUtils utils = new ServletUtils();

    protected void processRequest(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {


//        switch (req.getParameter("action")){
//            case "startGame":
//                startGame(req,resp);
//                break;
//            case "LeaveGame":
//                leaveGame(req,resp);
//                break;
//            case "dataTableDetails":
//                sendTableData(req,resp);
//                break;
//            case "getGameDetails":
//                returnGameDetails(req,resp);
//                break;
//            case "maintenance":
//                maintenanceData(req,resp);
//                break;
//            case "calculatedRisk":
//                calculatedRiskInfo(req,resp);
//                break;
//            case "wellOrchestrated":
//                wellOrchestratedInfo(req,resp);
//                break;
//            case "naturalTerritory":
//                naturalTerritoryInfo(req,resp);
//                break;
//            case "addArmy":
//                maintenanceData(req,resp);
//                break;
//            case "retire":
//                retireAndGoBack(req,resp);
//                break;
//            case "endTurn":
//                endTurnDetails(req,resp);
//                break;
//            case "territoryClicked":
//                getTerritoryForButtonsInfo(req,resp);
//                break;
//            case "territoryDataTable":
//                getTerritoryDetailsForDataTable(req,resp);
//                break;
//
//        }
        String action;
         action=req.getParameter("action");

        if (action.equals("clearGame"))
        {
            clearTheGame(req,resp);
        }
        if (action.equals("initCall"))
        {
            initGame(req,resp);
        }
//        else if (action.equals("isItThisPlayer"))
//        {
//            isItThisPlayer(req,resp);
//        }
//        else if (action.equals("allPlayersHere"))
//        {
//            allPlayersHere(req,resp);
//        }
        else if (action.equals("startGame"))
        {
            startGame(req,resp);
        }
        else if (action.equals("LeaveGame"))
        {
            leaveGame(req,resp);
        }
//        else if (action.equals("dataTableDetails"))
//        {
//           sendTableData(req,resp);
//        }
//        if (room.isGameIsAlive())
//        {
//            action = req.getParameter("action");
//
//        }
//        else if(action.equals("getGameDetails")) {
//            returnGameDetails(req, resp);
//        }
        else if(action.equals("maintenance")) {
            maintenanceData(req, resp);
        }
        else if(action.equals("calculatedRisk")) {
            calculatedRiskInfo(req, resp);
        }
        else if(action.equals("wellOrchestrated")) { /////
            wellOrchestratedInfo(req, resp);
        }
        else if(action.equals("naturalTerritory")) {
            naturalTerritoryInfo(req, resp);
        }
        else if(action.equals("addArmy")) { /////
            addArmyInfo(req, resp);
        }
        else if(action.equals("retire")) { ///////
            retireAndGoBack(req, resp);
        }
        else if(action.equals("endTurn")) {
            endTurnDetails(req, resp);
        }
//        else if(action.equals("territoryClicked")) {
//            getTerritoryForButtonsInfo(req, resp);
//        }
//        else if(action.equals("territoryDataTable")) {
//            getTerritoryDetailsForDataTable(req, resp);
//        }



    }

    private void clearTheGame(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        GameEngine engine;
        List<Room> rooms  = (ArrayList<Room>) getServletContext().getAttribute("rooms");
        Room currRoom = utils.getCurrentRoom(req, rooms);
        engine = currRoom.getGameEngine();




    }

//    private void isItThisPlayer(HttpServletRequest req, HttpServletResponse resp) throws IOException {
//        String whoIsThis="wait";
//        PrintWriter out = resp.getWriter();
//        resp.setContentType("application/json");
//        GameEngine engine;
//        List<Room> rooms  = (ArrayList<Room>) getServletContext().getAttribute("rooms");
//        Gson gson = new Gson();
//        Room currRoom = utils.getCurrentRoom(req, rooms);
//        engine = currRoom.getGameEngine();
//        if(currRoom.isGameStarted())
//             whoIsThis = engine.getGameManager().getCurrentPlayerTurn().getPlayer_name();
//        out.println(gson.toJson(whoIsThis));
//    }

//    private void allPlayersHere(HttpServletRequest req, HttpServletResponse resp) throws IOException {
//        resp.setContentType("application/json");
//        GameEngine engine;
//        Gson gson = new Gson();
//        List<Room> rooms  = (ArrayList<Room>) getServletContext().getAttribute("rooms");
//        PrintWriter out = resp.getWriter();
//        Room currRoom = utils.getCurrentRoom(req, rooms);
//        boolean allPlayershere = currRoom.isGameStarted();
//        out.println(gson.toJson(allPlayershere));
//    }


    private void naturalTerritoryInfo(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        GameEngine engine;
        List<Room> rooms  = (ArrayList<Room>) getServletContext().getAttribute("rooms");
        PrintWriter out = resp.getWriter();
        Room currRoom = utils.getCurrentRoom(req, rooms);
        engine = currRoom.getGameEngine();
        boolean haveEnoughTourings = false;
        Gson gson = new Gson();
        if (canBuildArmyFromData(req,resp,engine)) {
            Army army = buildArmyFromData(req,resp,engine);
            engine.getGameManager().setSelectedArmyForce(army);
            if (engine.getGameManager().conquerNeutralTerritory()) {

                int price = pricetobuy(req,resp,engine);
                if(price>-1) {
                     engine.getGameManager().getCurrentPlayerTurn().decrementFunds(price);
                    haveEnoughTourings=true;
                }

               // skip_turn.setDisable(false);
            }
        }

        out.println(gson.toJson(haveEnoughTourings));

    }

    private void addArmyInfo(HttpServletRequest req, HttpServletResponse resp) {
    }

    private void wellOrchestratedInfo(HttpServletRequest req, HttpServletResponse resp) {
    }

    private void calculatedRiskInfo(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Battle battle=new Battle();
        GameEngine engine;
        List<Room> rooms  = (ArrayList<Room>) getServletContext().getAttribute("rooms");
        PrintWriter out = resp.getWriter();
        resp.setContentType("application/json");
        Room currRoom = utils.getCurrentRoom(req, rooms);
        Gson gson = new Gson();
        engine = currRoom.getGameEngine();
        int territoryId=Integer.parseInt(req.getParameter("targetTerritory"));

        if (canBuildArmyFromData(req,resp,engine)) {
            Army PreattackerArmy = buildArmyFromData(req,resp,engine);
            Army PredefenceArmy = engine.getDescriptor().getTerritoryMap().get(territoryId).getConquerArmyForce();

            battle.preparedToBattle(PredefenceArmy, PreattackerArmy, engine.getDescriptor().getTerritoryMap().get(territoryId));
            //showWarStatus(PreattackerArmy, PredefenceArmy);

            if (battle.isAttackSucceed()) {

                battle.updateArmiesAfterAttackerVictory();
                engine.getDescriptor().getTerritoryMap().get(territoryId).getConquer().deleteTerritory(territoryId);
                engine.getDescriptor().getTerritoryMap().get(territoryId).setConquer(engine.getGameManager().getCurrentPlayerTurn());
                engine.getGameManager().getCurrentPlayerTurn().addTerritory(engine.getDescriptor().getTerritoryMap().get(territoryId));
                //print that attacker win.

            } else {

                battle.updateArmiesAfterAttackerDefeat();
                //print that attacker lose
            }
           // winner_war.setText("The Battle Winner: "+engine.getDescriptor().getTerritoryMap().get(territoryId).getConquer().getPlayer_name());
            String winner=engine.getDescriptor().getTerritoryMap().get(territoryId).getConquer().getPlayer_name();
            out.println(gson.toJson(new BattleInfo(PreattackerArmy,PredefenceArmy,winner,true)));
        }
        else{
            out.println(gson.toJson(new BattleInfo(null,null,null,false)));
        }
        if (battle.isWinnerArmyNotStrongEnoughToHoldTerritory()) {
            engine.getDescriptor().getTerritoryMap().get(territoryId).xChangeFundsForUnitsAndHold();
        }


    }

    private int pricetobuy(HttpServletRequest req, HttpServletResponse resp,GameEngine engine) {
        // int territoryId=Integer.parseInt(req.getParameter("targetTerritory"));
//        Map<String, String[]> map = req.getParameterMap();
//        int sumOfprice = 0;
//        String[] list = map.get("PreattackerArmy");
//        for (String paramName : list) {
//            String[] paramValues = map.get(paramName);
//            //Get Values of Param Name
//            for (String valueOfParam : paramValues) {
//                //Output the Values
//                int Amount = Integer.parseInt(valueOfParam);
//                int price = engine.getDescriptor().getUnitMap().get(paramName).getPurchase();
//                sumOfprice = sumOfprice + Amount * price;
//
//            }
//        }
        List<String> listOfUnits = new ArrayList<String>(engine.getDescriptor().getUnitMap().keySet());
        int sumOfprice = 0;
        for (int i = 0; i < engine.getDescriptor().getUnitMap().size(); i++) {
            String unitType=listOfUnits.get(i);
            int Amount = Integer.parseInt(req.getParameter(unitType));
            int price = engine.getDescriptor().getUnitMap().get(unitType).getPurchase();
            sumOfprice = sumOfprice + Amount * price;
        }
        if (sumOfprice <= engine.getGameManager().getCurrentPlayerTurn().getFunds())
            return sumOfprice;
        else {
            return -1;
        }


    }
    private Army buildArmyFromData(HttpServletRequest req, HttpServletResponse resp,GameEngine engine) {
//        Map<String, String[]> map = req.getParameterMap();


//        String[] list = map.get("PreattackerArmy");
//        for(String paramName:list) {
//            String[] paramValues = map.get(paramName);
//            //Get Values of Param Name
//            for (String amount : paramValues) {
//                //Output the Values
//                for (int j = 0; j <Integer.parseInt(amount); j++) {
//                    int rank = engine.getDescriptor().getUnitMap().get(paramName).getRank();
//                    int purchase = engine.getDescriptor().getUnitMap().get(paramName).getPurchase();
//                    int Maxfp = engine.getDescriptor().getUnitMap().get(paramName).getMaxFirePower();
//                    int competenceReduction = engine.getDescriptor().getUnitMap().get(paramName).getCompetenceReduction();
//                    Unit unit = new Unit(paramName, rank, purchase, Maxfp, competenceReduction);
//                    army.addUnit(unit);
//                }
//            }
//        }
        Army army = new Army();
        List<String> listOfUnits = new ArrayList<String>(engine.getDescriptor().getUnitMap().keySet());
        for (int i = 0; i < engine.getDescriptor().getUnitMap().size(); i++) {

            String unitType = listOfUnits.get(i);
            //Person person = (Person) tableData.getItems().get(i);
            //if (!person.getAmount().getText().isEmpty()) {
                int Amount = Integer.parseInt(req.getParameter(unitType));
                for (int j = 0; j < Amount; j++) {

                    int rank = engine.getDescriptor().getUnitMap().get(unitType).getRank();
                    int purchase = engine.getDescriptor().getUnitMap().get(unitType).getPurchase();
                    int Maxfp = engine.getDescriptor().getUnitMap().get(unitType).getMaxFirePower();
                    int competenceReduction = engine.getDescriptor().getUnitMap().get(unitType).getCompetenceReduction();
                    Unit unit = new Unit(unitType, rank, purchase, Maxfp, competenceReduction);
                    army.addUnit(unit);
                }
          //  }
        }
        return army;
    }

    private boolean canBuildArmyFromData(HttpServletRequest req, HttpServletResponse resp,GameEngine engine) {

        List<String> listOfUnits = new ArrayList<String>(engine.getDescriptor().getUnitMap().keySet());
        int sumOfprice = 0;
        for (int i = 0; i < engine.getDescriptor().getUnitMap().size(); i++) {


           // if (!person.getAmount().getText().isEmpty()) {
                String unitType=listOfUnits.get(i);
                int Amount = Integer.parseInt(req.getParameter(unitType));
                int price = engine.getDescriptor().getUnitMap().get(unitType).getPurchase();
                sumOfprice = sumOfprice + Amount * price;
           // }
        }
        if (sumOfprice <= engine.getGameManager().getCurrentPlayerTurn().getFunds())
            return true;
        else
            return false;



    }

    private void endTurnDetails(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        GameEngine engine;
        PrintWriter out = resp.getWriter();
        List<Room> rooms  = (ArrayList<Room>) getServletContext().getAttribute("rooms");
        resp.setContentType("application/json");
        Room currRoom = utils.getCurrentRoom(req, rooms);
        engine = currRoom.getGameEngine();
        Gson gson = new Gson();
        boolean isCycleOver=engine.getGameManager().isCycleOver();
        boolean gameOver=engine.gameManager.isGameOver();
        boolean thereIsWinner=(engine.gameManager.getWinnerPlayer()!=null);
        String winnerPlayerName="noWinner";
        boolean isItFirstRound=(engine.getGameManager().roundNumber == 1);

        if (isCycleOver) {
            engine.getGameManager().startOfRoundUpdates();
            engine.getGameManager().endOfRoundUpdates();
            engine.getGameManager().nextPlayerInTurn();
        } else {
            engine.gameManager.nextPlayerInTurn();
        }
        if (gameOver)
        {
            if (thereIsWinner)
            {
                winnerPlayerName=engine.gameManager.getWinnerPlayer().getPlayer_name();
            }
        }

        out.println(gson.toJson(new endTurnInfo(
                                isCycleOver,
                                gameOver,
                                thereIsWinner,
                                winnerPlayerName,
                                isItFirstRound)
        ));
    }

    private void maintenanceData(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        PrintWriter out = resp.getWriter();
        List<Room> rooms  = (ArrayList<Room>) getServletContext().getAttribute("rooms");
        GameEngine engine;
        boolean maintenaceSucseed;
        resp.setContentType("application/json");
        Room currRoom = utils.getCurrentRoom(req, rooms);
        engine = currRoom.getGameEngine();
        Gson gson = new Gson();
        int territoryId=Integer.parseInt(req.getParameter("targetTerritory"));
        Territory targetTerritory = engine.getDescriptor().getTerritoryMap().get(territoryId);
        Supplier<Integer> enoughMoney = () -> engine.gameManager.getRehabilitationArmyPriceInTerritory(targetTerritory);
        if (engine.getGameManager().isSelectedPlayerHasEnoughMoney(enoughMoney.get())) {
            int price = enoughMoney.get();
            engine.getGameManager().rehabilitateSelectedTerritoryArmy();
            engine.getGameManager().getCurrentPlayerTurn().decrementFunds((price));
            maintenaceSucseed=true;
        } else {
            maintenaceSucseed=false;

        }
        out.println(gson.toJson(maintenaceSucseed));
    }

//    private void getTerritoryDetailsForDataTable(HttpServletRequest req, HttpServletResponse resp) throws IOException {
//        List<Room> rooms  = (ArrayList<Room>) getServletContext().getAttribute("rooms");
//        GameEngine engine;
//        resp.setContentType("application/json");
//        PrintWriter out = resp.getWriter();
//        Room currRoom = utils.getCurrentRoom(req, rooms);
//        Gson gson = new Gson();
//        engine = currRoom.getGameEngine();
//        int maintenance,amount,rank,maintenanceCost,totalFirePower,fp;
//        String unitType="Not Available";
//        maintenance=amount=rank=maintenanceCost=totalFirePower=fp=0;
//        List<GameObjects.TerritoryDataTable> TerritoryDataTableList = new ArrayList<>();
//        if(Boolean.parseBoolean(req.getParameter("showFlag"))) {
//            List<String> unitMapp = new ArrayList<String>(engine.getDescriptor().getUnitMap().keySet());
//            int territoryID=Integer.parseInt(req.getParameter("territoryId"));
//            totalFirePower=engine.getDescriptor().getTerritoryMap().get(territoryID).getConquerArmyForce().getTotalPower();
//            maintenanceCost=engine.getDescriptor().getTerritoryMap().get(territoryID).getConquerArmyForce().calculateRehabilitationPrice();
//            for (int i = 0; i < unitMapp.size(); i++) {
//                unitType = unitMapp.get(i);
//                rank = engine.getDescriptor().getUnitMap().get(unitType).getRank();
//                maintenance=engine.getDescriptor().getTerritoryMap().get(territoryID).getConquerArmyForce().calculateRehabilitationPriceperUnit(unitType);
//                fp=engine.getDescriptor().getTerritoryMap().get(territoryID).getConquerArmyForce().getTotalPowerPerUnit(unitType);
//                amount=engine.getDescriptor().getTerritoryMap().get(territoryID).getConquerArmyForce().howMachFromThisUnitType(unitType);
//                TerritoryDataTableList.add(new TerritoryDataTable(unitType, maintenance, fp, amount, rank, maintenanceCost, totalFirePower));
//            }
//        }
//        else{
//            TerritoryDataTableList.add(new TerritoryDataTable(unitType, maintenance, fp, amount, rank, maintenanceCost, totalFirePower));
//        }
//
//
//        out.println(gson.toJson(TerritoryDataTableList));
//    }

//    private void getTerritoryForButtonsInfo(HttpServletRequest req, HttpServletResponse resp) throws IOException {
//        GameEngine engine;
//        resp.setContentType("application/json");
//        PrintWriter out = resp.getWriter();
//
//        List<Room> rooms  = (ArrayList<Room>) getServletContext().getAttribute("rooms");
//
//        Room currRoom = utils.getCurrentRoom(req, rooms);
//        Gson gson = new Gson();
//        engine = currRoom.getGameEngine();
//
//
//        int territoryId=Integer.parseInt(req.getParameter("targetTerritory"));
//        Territory targetTerritory = engine.getDescriptor().getTerritoryMap().get(territoryId);
//        engine.gameManager.setSelectedTerritoryForTurn(targetTerritory);
//        boolean territoryConquered=engine.gameManager.isConquered();
//        boolean gameOver=engine.getGameManager().isGameOver();
//        boolean onlyOnePlayer= (engine.getDescriptor().getPlayersList().size()==1);
//        boolean isTerritoryBelongsCurrentPlayer=engine.gameManager.isTerritoryBelongsCurrentPlayer();
//        boolean isTargetTerritoryValid=engine.gameManager.isTargetTerritoryValid();
//        boolean playerDontHaveTerritories=(engine.getGameManager().getCurrentPlayerTurn().getTerritoriesID().size() == 0);
//
//        out.println(gson.toJson(new TerritoryButtonsInfo(
//                                    territoryConquered,
//                                    gameOver,
//                                    onlyOnePlayer,
//                                    isTerritoryBelongsCurrentPlayer,
//                                    isTargetTerritoryValid,
//                                    playerDontHaveTerritories,
//                                    (engine.gameManager.roundNumber == 1))
//        ));
//
//
//    }

    private void initGame(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Gson gson = new Gson();
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        String userName = req.getSession(false).getAttribute("username").toString();
        int cols , rows;
        rooms = (ArrayList<Room>) getServletContext().getAttribute("rooms");
        Room currRoom = null;
        currRoom = utils.getCurrentRoom(req, rooms);
        GameEngine engine;
        if (currRoom != null) {
            cols = currRoom.getGameEngine().getDescriptor().getColumns();
            rows = currRoom.getGameEngine().getDescriptor().getRows();
            engine=currRoom.getGameEngine();
            out.println(gson.toJson(new GameDetails(cols, rows , userName,engine,currRoom.isGameStarted())));
        }
    }
//    private void returnGameDetails(HttpServletRequest req, HttpServletResponse resp) throws IOException {
//        resp.setContentType("application/json");
//        PrintWriter out = resp.getWriter();
//        Gson gson = new Gson();
//        int cols , rows;
//        String currentPlayer;
//        rooms = (ArrayList<Room>) getServletContext().getAttribute("rooms");
//        GameEngine engine;
//        Room currRoom = null;
//
//       currRoom = utils.getCurrentRoom(req, rooms);
//        if (currRoom != null) {
//            cols = currRoom.getGameEngine().getDescriptor().getColumns();
//            rows = currRoom.getGameEngine().getDescriptor().getRows();
//            engine=currRoom.getGameEngine();
//            out.println(gson.toJson(new GameDetails(cols, rows , "deatils",engine,currRoom.isGameStarted())));
//
//        }
//    }


    public void startGame(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        rooms = (ArrayList<Room>) getServletContext().getAttribute("rooms");
        Room currRoom = utils.getCurrentRoom(req, rooms);
        currRoom.getGameEngine().newGame();
        currRoom.getGameEngine().getDescriptor().setPlayersList(currRoom.getPlayers());
        currRoom.setPlayers();
        currRoom.getGameEngine().gameManager.nextPlayerInTurn();
        getServletContext().setAttribute("rooms",rooms);

    }
    private void retireAndGoBack(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        GameEngine engine;
        rooms = (ArrayList<Room>) getServletContext().getAttribute("rooms");
        Room currRoom = utils.getCurrentRoom(req, rooms);
        engine=currRoom.getGameEngine();
        engine.deletePlayer();
        leaveGame(req,resp);

    }

    private void leaveGame(HttpServletRequest req, HttpServletResponse resp) throws IOException {
//        resp.setContentType("application/json");
//        PrintWriter out = resp.getWriter();
//        Gson gson = new Gson();
        Room currRoom = utils.getCurrentRoom(req, rooms);
        String userName = req.getSession(false).getAttribute("username").toString();
        currRoom.removePlayer(userName);
       // currRoom.getGameEngine().deletePlayer();
        resp.sendRedirect(GAMES_URL);

    }


//    private void sendTableData(HttpServletRequest req, HttpServletResponse resp) throws IOException {
//        resp.setContentType("application/json");
//        PrintWriter out = resp.getWriter();
//        Gson gson = new Gson();
//        Room currRoom = utils.getCurrentRoom(req, rooms);
//        GameEngine engine=currRoom.getGameEngine();
//        List<String> unitMapp = new ArrayList<String>(engine.getDescriptor().getUnitMap().keySet());
//        List<GameObjects.unitDataTable> uniDataTable=new ArrayList<>();
//        for (int i = 0; i < unitMapp.size(); i++) {
//
//            String unitType = unitMapp.get(i);
//            int price1 = engine.getDescriptor().getUnitMap().get(unitType).getPurchase();
//            int subduction1 = engine.getDescriptor().getUnitMap().get(unitType).getCompetenceReduction();
//            int rank1 = engine.getDescriptor().getUnitMap().get(unitType).getRank();
//            uniDataTable.add(new unitDataTable(unitType, engine.getDescriptor().getUnitMap().get(unitType).getMaxFirePower(), price1, subduction1, rank1));
//
//
//        }
//          out.println(gson.toJson(uniDataTable));
//    }
// <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">

    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}