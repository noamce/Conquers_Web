package ServletClasses;

import GameEngine.GameEngine;
import ServletClasses.Room;
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

@WebServlet(name = "isItThisPlayerServlet")

public class isItThisPlayerServlet extends HttpServlet {
    private ServletUtils utils = new ServletUtils();
    //private final String GAMES_URL = "Lobby/lobby.html";
    private List<Room> rooms = new ArrayList<>();

    protected void processRequest(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        String action;
        action=req.getParameter("action");

        if (action.equals("isItThisPlayer"))
        {
            isItThisPlayer(req,resp);
        }




    }

    private void isItThisPlayer(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String whoIsThis="wait";
        PrintWriter out = resp.getWriter();
        resp.setContentType("application/json");
        GameEngine engine;
        List<Room> rooms  = (ArrayList<Room>) getServletContext().getAttribute("rooms");
        Gson gson = new Gson();
        Room currRoom = utils.getCurrentRoom(req, rooms);
        engine = currRoom.getGameEngine();
        if(currRoom.isGameStarted())
            whoIsThis = engine.getGameManager().getCurrentPlayerTurn().getPlayer_name();
        out.println(gson.toJson(whoIsThis));
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
