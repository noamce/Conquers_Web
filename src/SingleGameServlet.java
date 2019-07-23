import GameEngine.GameEngine;
import GameObjects.unitDataTable;
import GameObjects.Player;
import GameObjects.Territory;
import com.google.gson.Gson;
import utils.GameDetails;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "SingleGameServlet")
public class SingleGameServlet extends HttpServlet {
    String action;
    Room room;
    Player currentPlayer;
    Territory TargetTerritory;
    private final String GAMES_URL = "Lobby/lobby.html";
    private List<Room> rooms = new ArrayList<>();

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

       currRoom=getCurrentRoom(req);
        if (currRoom != null) {
            cols = currRoom.getGameEngine().getDescriptor().getColumns();
            rows = currRoom.getGameEngine().getDescriptor().getRows();
            engine=currRoom.getGameEngine();
            out.println(gson.toJson(new GameDetails(cols, rows , userName,engine,currRoom.isGameStarted())));
        }
    }
    public Room getCurrentRoom(HttpServletRequest req){
        int i=0;
        String userName = req.getSession(false).getAttribute("username").toString();
        Room currRoom=null;
        while(i<rooms.size())
        {
            if (rooms.get(i).hasPlayer(userName)) {
                currRoom = rooms.get(i);
            }
            i++;
        }

        return currRoom;
    }

    public void startGame(HttpServletRequest req, HttpServletResponse resp)
    {
        Room currRoom=getCurrentRoom(req);
        currRoom.getGameEngine().getDescriptor().setPlayersList(room.getPlayers());
        currRoom.getGameEngine().newGame();
        getServletContext().setAttribute("rooms",rooms);
    }

    private void leaveGame(HttpServletRequest req, HttpServletResponse resp) throws IOException {
//        resp.setContentType("application/json");
//        PrintWriter out = resp.getWriter();
//        Gson gson = new Gson();
        Room currRoom=getCurrentRoom(req);
        String userName = req.getSession(false).getAttribute("username").toString();
        currRoom.removePlayer(userName);
       // currRoom.getGameEngine().deletePlayer();
        resp.sendRedirect(GAMES_URL);

    }


    private void sendTableData(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        Gson gson = new Gson();
        Room currRoom=getCurrentRoom(req);
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