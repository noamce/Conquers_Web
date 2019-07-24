package ServletClasses;

import GameObjects.Player;
import com.google.gson.Gson;
import utils.ServletUtils;
import utils.UserManager;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/*@WebServlet(name = "ServletClasses.LoginServlet")
public class ServletClasses.LoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}*/


@WebServlet(name = "Login")
public class LoginServlet extends HttpServlet {
    private UserManager players = new UserManager();
    private final String GAMES_URL = "Lobby/lobby.html";
private int i=2;


    protected void processRequest(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {


        resp.setContentType("text/html");
        HttpSession httpSession = req.getSession();
        httpSession.setAttribute("username",req.getParameter("username"));
        String currUserName = req.getParameter("username");
        System.out.println("Current session name: " + currUserName);
        if (players.getUsers() == null) {
            players = new UserManager();
            submitPlayer(req,httpSession,resp);
        } else {
            if (isNameAvailable(currUserName)) {
                submitPlayer(req,httpSession,resp);
            } else {
                PrintWriter out = resp.getWriter();
                String errorMessage = "Username " + currUserName + " already exists. Please enter a different username.";
                        out.println("<script type=\"text/javascript\">");
                        out.println("alert('"+errorMessage+"');");
                        out.println("location='login.html';");
                        out.println("</script>");
            }
        }

    }

    private boolean isNameAvailable(String name) {

        for(String player : players.getUsers()){
            if(player.equals(name))
                return false;
        }
        return true;
    }
    private void submitPlayer(HttpServletRequest req, HttpSession httpSession,HttpServletResponse resp) throws IOException {
        addPlayer(req,httpSession,resp);
    }

    private void addPlayer(HttpServletRequest req,HttpSession httpSession,HttpServletResponse resp) throws IOException {
        players.addUser(req.getParameter("username"));
        httpSession.setAttribute("ThisSessionPlayer",req.getParameter("username"));
        getServletContext().setAttribute("players",players);

        resp.sendRedirect(GAMES_URL);


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
