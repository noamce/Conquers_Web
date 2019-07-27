package ServletClasses;

import GameEngine.GameEngine;
import GameObjects.unitDataTable;
import com.google.gson.Gson;
import utils.ServletUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "DataTableServlet")
public class DataTableServlet extends HttpServlet {


    private ServletUtils utils = new ServletUtils();
    //private final String GAMES_URL = "Lobby/lobby.html";
    private List<Room> rooms = new ArrayList<>();

    protected void processRequest(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action;
        action = req.getParameter("action");
        if (action.equals("dataTableDetails"))
        {
            sendTableData(req,resp);
        }
    }

    private void sendTableData(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        List<Room> rooms  = (ArrayList<Room>) getServletContext().getAttribute("rooms");
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
