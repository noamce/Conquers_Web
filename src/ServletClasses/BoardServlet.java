package ServletClasses;

import GameEngine.GameEngine;
import com.google.gson.Gson;
import utils.GameDetails;
import utils.ServletUtils;
import utils.TerritoryMapToSend;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "BoardServlet")
public class BoardServlet extends HttpServlet {
    private ServletUtils utils = new ServletUtils();
    //private final String GAMES_URL = "Lobby/lobby.html";
    private List<Room> rooms = new ArrayList<>();

    protected void processRequest(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action;
        action = req.getParameter("action");
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
        currRoom = utils.getCurrentRoom(req, rooms);
        if (currRoom != null) {
            cols = currRoom.getGameEngine().getDescriptor().getColumns();
            rows = currRoom.getGameEngine().getDescriptor().getRows();
            engine=currRoom.getGameEngine();
            //out.println(gson.toJson(new BoardDetails(cols,rows, new TerritoryMapToSend(engine.getDescriptor().getTerritoryMap()))));

        }
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
