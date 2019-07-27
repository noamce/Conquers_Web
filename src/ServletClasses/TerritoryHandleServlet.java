package ServletClasses;

import GameEngine.GameEngine;
import GameObjects.Territory;
import GameObjects.TerritoryDataTable;
import com.google.gson.Gson;
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

@WebServlet(name = "TerritoryHandleServlet")
public class TerritoryHandleServlet extends HttpServlet {
    private ServletUtils utils = new ServletUtils();
    //private final String GAMES_URL = "Lobby/lobby.html";
    private List<Room> rooms = new ArrayList<>();

    protected void processRequest(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        String action;
        action=req.getParameter("action");
       if(action.equals("territoryClicked")) {
        getTerritoryForButtonsInfo(req, resp);
       }else if(action.equals("territoryDataTable")) {
           getTerritoryDetailsForDataTable(req, resp);
       }




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
                playerDontHaveTerritories,
                (engine.gameManager.roundNumber == 1))
        ));


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
        if(Boolean.parseBoolean(req.getParameter("showFlag"))) {
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
