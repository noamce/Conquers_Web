package ServletClasses;

import GameEngine.GameEngine;
import GameObjects.Player;
import com.google.gson.Gson;
import utils.PlayerModel;
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

@WebServlet(name = "ServletClasses.PlayerInfo")
public class PlayerInfo extends HttpServlet {
    private ServletUtils utils = new ServletUtils();

    private void processRequest(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        //System.out.println("inside the loop");
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        Gson gson = new Gson();
        List<Room> rooms  = (ArrayList<Room>) getServletContext().getAttribute("rooms");
        GameEngine engine;
        Room currRoom = utils.getCurrentRoom(req, rooms);
        engine = currRoom.getGameEngine();
        Player currentPlayer = engine.getGameManager().getCurrentPlayerTurn();


        out.println(gson.toJson(new PlayerModel(
                currentPlayer.getPlayer_name(),
                currentPlayer.getColor(),
                currentPlayer.getFunds(),
                engine.getGameManager().roundNumber,
                engine.getDescriptor().getTotalCycles())
        ));
    }

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
