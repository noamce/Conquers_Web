import GameEngine.GameDescriptor;
import GameEngine.GameEngine;
import com.google.gson.Gson;
import utils.ServletUtils;
import utils.UserManager;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@WebServlet(name = "LobbyServlet")
public class LobbyServlet extends HttpServlet {
    UserManager userManager;
    private GameEngine engine;
    private List<Room> rooms = new ArrayList<>();
    private ArrayList<String> gameNames = new ArrayList<>();

    int i = 0;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String action = request.getParameter("action");
        int actionValue = -1;

        if (action.equals("getLoggedUsername")) {
            actionValue = 0;
        }
        if (action.equals("getLoggedUsers")) {
            actionValue = 1;
        }


        switch (actionValue) {
            case 0:
                this.getLoggedUsername(request, response);
                break;
            case 1:
                this.getUsersListAction(request, response);
                break;

        }

    }


    private void getUsersListAction(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        Gson gson = new Gson();
        userManager = (UserManager) getServletContext().getAttribute("players");

        ArrayList<String> usersList = userManager.getUsers();
        String json = gson.toJson(usersList);
        out.println(json);
        out.flush();
    }

    private void getLoggedUsername(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        Gson gson = new Gson();
        String CurrentUsername = request.getSession(false).getAttribute("username").toString();
        out.println(gson.toJson(CurrentUsername));
        out.println();
    }





    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
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
