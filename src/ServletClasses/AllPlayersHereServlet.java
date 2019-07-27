package ServletClasses;

import GameEngine.GameEngine;
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

@WebServlet(name = "AllPlayersHereServlet")
public class AllPlayersHereServlet extends HttpServlet {
    private ServletUtils utils = new ServletUtils();
    //private final String GAMES_URL = "Lobby/lobby.html";
    private List<Room> rooms = new ArrayList<>();

    protected void processRequest(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action;
        action = req.getParameter("action");

     if (action.equals("allPlayersHere"))
        {
            allPlayersHere(req,resp);
        }

    }

    private void allPlayersHere(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        GameEngine engine;
        Gson gson = new Gson();
        List<Room> rooms  = (ArrayList<Room>) getServletContext().getAttribute("rooms");
        PrintWriter out = resp.getWriter();
        Room currRoom = utils.getCurrentRoom(req, rooms);
        boolean allPlayershere = currRoom.isGameStarted();
        out.println(gson.toJson(allPlayershere));
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
