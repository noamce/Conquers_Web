package ServletClasses;

import GameEngine.GameEngine;
import GameObjects.TerritoryDataTable;
import GameObjects.unitDataTable;
import GameObjects.Player;
import GameObjects.Territory;
import com.google.gson.Gson;
import utils.GameDetails;
import utils.ServletUtils;
import utils.TerritoryButtonsInfo;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "ServletClasses.SingleGameServlet")
public class SingleGameServlet extends HttpServlet {
    String action;
    Room room;
    Player currentPlayer;
    Territory TargetTerritory;
    private final String GAMES_URL = "Lobby/lobby.html";
    private List<Room> rooms = new ArrayList<>();
    private ServletUtils utils = new ServletUtils();

    protected void processRequest(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {

        action=req.getParameter("action");
        if (action.equals("startGame"))
        {
            startGame(req,resp);
        }
        if (action.equals("LeaveGame"))
        {
            leaveGame(req,resp);
        }
        if (action.equals("dataTableDetails"))
        {
           sendTableData(req,resp);
        }
//        if (room.isGameIsAlive())
//        {
//            action = req.getParameter("action");
//
//        }
        if(action.equals("getGameDetails")) {
            returnGameDetails(req, resp);
        }
        if(action.equals("maintenance")) {
            returnGameDetails(req, resp);
        }
        if(action.equals("calculatedRisk")) {
            returnGameDetails(req, resp);
        }
        if(action.equals("wellOrchestrated")) {
            returnGameDetails(req, resp);
        }
        if(action.equals("naturalTerritory")) {
            returnGameDetails(req, resp);
        }
        if(action.equals("addArmy")) {
            returnGameDetails(req, resp);
        }
        if(action.equals("retire")) {
            returnGameDetails(req, resp);
        }
        if(action.equals("endTurn")) {
            returnGameDetails(req, resp);
        }
        if(action.equals("territoryClicked")) {
            getTerritoryForButtonsInfo(req, resp);
        }
        if(action.equals("territoryDataTable")) {
            getTerritoryDetailsForDataTable(req, resp);
        }


    }

    private void getTerritoryDetailsForDataTable(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        List<Room> rooms  = (ArrayList<Room>) getServletContext().getAttribute("rooms");
        GameEngine engine;
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        Room currRoom = utils.getCurrentRoom(req, rooms);
        Gson gson = new Gson();
        engine = currRoom.getGameEngine();
        int maintenance,amount,rank,maintenanceCost,totalFirePower,fp;
        String unitType="Not Available";
        maintenance=amount=rank=maintenanceCost=totalFirePower=fp=0;
        List<GameObjects.TerritoryDataTable> TerritoryDataTableList = new ArrayList<>();
        if(Boolean.parseBoolean(req.getParameter("showTerritoryInfoFlag"))) {
            List<String> unitMapp = new ArrayList<String>(engine.getDescriptor().getUnitMap().keySet());
            int territoryID=Integer.parseInt(req.getParameter("territoryId"));
            totalFirePower=engine.getDescriptor().getTerritoryMap().get(territoryID).getConquerArmyForce().getTotalPower();
            maintenanceCost=engine.getDescriptor().getTerritoryMap().get(territoryID).getConquerArmyForce().calculateRehabilitationPrice();
            for (int i = 0; i < unitMapp.size(); i++) {
                unitType = unitMapp.get(i);
                rank = engine.getDescriptor().getUnitMap().get(unitType).getRank();
                maintenance=engine.getDescriptor().getTerritoryMap().get(territoryID).getConquerArmyForce().calculateRehabilitationPriceperUnit(unitType);
                fp=engine.getDescriptor().getTerritoryMap().get(territoryID).getConquerArmyForce().getTotalPowerPerUnit(unitType);
                amount=engine.getDescriptor().getTerritoryMap().get(territoryID).getConquerArmyForce().howMachFromThisUnitType(unitType);
                TerritoryDataTableList.add(new TerritoryDataTable(unitType, maintenance, fp, amount, rank, maintenanceCost, totalFirePower));
            }
        }
        else{
            TerritoryDataTableList.add(new TerritoryDataTable(unitType, maintenance, fp, amount, rank, maintenanceCost, totalFirePower));
        }


        out.println(gson.toJson(TerritoryDataTableList));
    }

    private void getTerritoryForButtonsInfo(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        GameEngine engine;
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();

        List<Room> rooms  = (ArrayList<Room>) getServletContext().getAttribute("rooms");

        Room currRoom = utils.getCurrentRoom(req, rooms);
        Gson gson = new Gson();
        engine = currRoom.getGameEngine();


        int territoryId=Integer.parseInt(req.getParameter("targetTerritory"));
        Territory targetTerritory = engine.getDescriptor().getTerritoryMap().get(territoryId);
        engine.gameManager.setSelectedTerritoryForTurn(targetTerritory);
        boolean territoryConquered=engine.gameManager.isConquered();
        boolean gameOver=engine.getGameManager().isGameOver();
        boolean onlyOnePlayer= (engine.getDescriptor().getPlayersList().size()==1);
        boolean isTerritoryBelongsCurrentPlayer=engine.gameManager.isTerritoryBelongsCurrentPlayer();
        boolean isTargetTerritoryValid=engine.gameManager.isTargetTerritoryValid();
        boolean playerDontHaveTerritories=(engine.getGameManager().getCurrentPlayerTurn().getTerritoriesID().size() == 0);

        out.println(gson.toJson(new TerritoryButtonsInfo(
                                    territoryConquered,
                                    gameOver,
                                    onlyOnePlayer,
                                    isTerritoryBelongsCurrentPlayer,
                                    isTargetTerritoryValid,
                                    playerDontHaveTerritories)
        ));


    }

    private void returnGameDetails(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        Gson gson = new Gson();
        int cols , rows;
        String currentPlayer;
        rooms = (ArrayList<Room>) getServletContext().getAttribute("rooms");
        GameEngine engine;
        Room currRoom = null;

        String userName = req.getSession(false).getAttribute("username").toString();

       currRoom = utils.getCurrentRoom(req, rooms);
        if (currRoom != null) {
            cols = currRoom.getGameEngine().getDescriptor().getColumns();
            rows = currRoom.getGameEngine().getDescriptor().getRows();
            engine=currRoom.getGameEngine();
            out.println(gson.toJson(new GameDetails(cols, rows , userName,engine,currRoom.isGameStarted())));
        }
    }


    public void startGame(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        Room currRoom = utils.getCurrentRoom(req, rooms);
        currRoom.getGameEngine().newGame();
        currRoom.getGameEngine().getDescriptor().setPlayersList(currRoom.getPlayers());
        currRoom.setPlayers();
        currRoom.getGameEngine().gameManager.nextPlayerInTurn();
        getServletContext().setAttribute("rooms",rooms);

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


    private void sendTableData(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        Gson gson = new Gson();
        Room currRoom = utils.getCurrentRoom(req, rooms);
        GameEngine engine=currRoom.getGameEngine();
        List<String> unitMapp = new ArrayList<String>(engine.getDescriptor().getUnitMap().keySet());
        List<GameObjects.unitDataTable> uniDataTable=new ArrayList<>();
        for (int i = 0; i < unitMapp.size(); i++) {

            String unitType = unitMapp.get(i);
            int price1 = engine.getDescriptor().getUnitMap().get(unitType).getPurchase();
            int subduction1 = engine.getDescriptor().getUnitMap().get(unitType).getCompetenceReduction();
            int rank1 = engine.getDescriptor().getUnitMap().get(unitType).getRank();
            uniDataTable.add(new unitDataTable(unitType, engine.getDescriptor().getUnitMap().get(unitType).getMaxFirePower(), price1, subduction1, rank1));


        }
          out.println(gson.toJson(uniDataTable));
    }
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