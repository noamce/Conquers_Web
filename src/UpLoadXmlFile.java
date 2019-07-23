
import GameEngine.GameDescriptor;
import GameEngine.GameEngine;
import GameObjects.Player;
import com.google.gson.Gson;
import utils.UserManager;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.*;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


@WebServlet(name = "UpLoadXmlFile")
    public class UpLoadXmlFile extends HttpServlet {
    private final static String XML_PATH = "/resources/ex3-medium.xml";
    private List<Room> rooms = new ArrayList<>();
    private ArrayList <String> gameNames=new ArrayList<>();
    private GameEngine engine;
    private StringBuilder errorText = new StringBuilder();
    private final String GAMES_URL = "Lobby/lobby.html";
    private final String ROOMS_ERROR_URL = "Lobby/RoomsErr.html";
    int count = 0;
    String error="ok";

    protected void processRequest(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, InterruptedException {
        String action = req.getParameter("action");
        if(action != null && action.equals("getErrorStatus"))
            if(!error.equals("ok"))
                getErrorStatus(req,resp);
            else
               getErrorStatus(req,resp);
        else
            {
                if (action!=null && action.equals("addUserToRoom"))
                {
                    addUserToRoom(req,resp);
                }
        if (action != null && action.equals("getRoomslist"))
            getRoomsListAction(req, resp);
        else  if (action != null && action.equals("gameDetails")) {
            GetTargetRoom(req,resp);
        }
        else
            {
            engine = new GameEngine();
            rooms = (ArrayList<Room>) getServletContext().getAttribute("rooms");
            resp.setContentType("text/html;charset=UTF-8");
            PrintWriter out = resp.getWriter();
            HttpSession httpSession = req.getSession();
            InputStream xmlConnection = req.getPart("xml").getInputStream();
            String currUserName = req.getParameter("username");
            try {
                engine.setDescriptor(engine.createDescriptor1(xmlConnection));
                error = "ok";
            } catch (GameDescriptor.NotWorkingException e) {
                error = "THERE WAS AN ERROR \n One of the XML details is not valid";
                resp.sendRedirect(GAMES_URL);
            } catch (GameDescriptor.colandRowException e) {
                error = "THERE WAS AN ERROR Loading THE XML \n number of columns or rows is not valid";
                resp.sendRedirect(GAMES_URL);//
            } catch (GameDescriptor.noDefaultProfitException e) {
                error = "No default profit in XML";
                resp.sendRedirect(GAMES_URL);//
            } catch (GameDescriptor.IllegalunitdetailsorNamedubbException e) {
                error = "Error in the unit details of the XML \n ID or Name is showing more than 1";
                resp.sendRedirect(GAMES_URL);//
            } catch (GameDescriptor.IllegalDupprankortypeException e) {
                error = "Error in the unit details of the XML \n There are duplicate of Rank or Type to the units";
                resp.sendRedirect(GAMES_URL);//
            } catch (GameDescriptor.dubbIDException e) {
                error = "THERE WAS AN ERROR Loading THE XML \n Double ID in xml detected";
                resp.sendRedirect(GAMES_URL);//
            } catch (GameDescriptor.dubbPlayerIdException e) {
                error = "Error in the Players details of the XML \n There are at least one ID that showing twice";
                resp.sendRedirect(GAMES_URL);//
            } catch (GameDescriptor.IllegalunitdetailsorderException e) {
                error = "Error in the unit details of the XML \n Id's order are not right";
                resp.sendRedirect(GAMES_URL);

//
            } catch (GameDescriptor.noDefaultThresholdException e) {
                error = "No default Army Threshold in XML";
                resp.sendRedirect(GAMES_URL);
//
            }
            if (error.equals("ok")) {
                System.out.println("Current session name: " + currUserName);
                if (rooms == null) {
                    rooms = new ArrayList<>();
                    submitRoom(req, httpSession, resp);

                } else if (isNameAvailable(engine.getDescriptor().getGameTitle()) == true) {
                    submitRoom(req, httpSession, resp);
                } else {
                    resp.setContentType("text/html");
                    PrintWriter pw = resp.getWriter();
                    pw.println("<script type=\"text/javascript\">");
                    pw.println("alert('The game you wanted to upload already exists');");
                    pw.println("location='Lobby/lobby.html';");
                    pw.println("</script>");
                }
            }
        }
        }
    }

    private void getErrorStatus(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        PrintWriter out = resp.getWriter();
        Gson gson = new Gson();
        resp.setContentType("application/json");
        out.println(gson.toJson(error));


    }

    private void addUserToRoom(HttpServletRequest req, HttpServletResponse resp)
    {
        String CurrentUsername = req.getSession(false).getAttribute("username").toString();
        String CurrentRoom=req.getParameter("selectedroom");
        Room room = null;
        for(int i=0;i<rooms.size();i++)
        {
            if (rooms.get(i).getGameEngine().getDescriptor().getGameTitle().equals(CurrentRoom))
                room=rooms.get(i);
        }
            if (room != null) {
                if (room.isGameIsAlive() == false) {

                    Player currentPlayer = new Player(CurrentUsername);
                    room.addPlayer(currentPlayer);

                }

            }
    }

    private void GetTargetRoom(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        rooms=(ArrayList<Room>) getServletContext().getAttribute("rooms");
        String string=req.getParameter("key");
        PrintWriter out = resp.getWriter();

        int i=0;
        while(i<rooms.size())
        {
            if (rooms.get(i).getGameEngine().getDescriptor().getGameTitle().equals(string))
            {
                Gson gson = new Gson();
                String creator=rooms.get(i).getRoomCreator();
                int numPlayers=rooms.get(i).getMaxNumberOfOnlineUsers();
                GameEngine engine=rooms.get(i).getGameEngine();
                Room room=new Room(numPlayers,creator,engine);
                resp.setContentType("application/json");
                out.println(gson.toJson(room));
            }
            i++;
        }
    }

    private void submitRoom(HttpServletRequest req, HttpSession httpSession,HttpServletResponse resp) throws IOException {
        addRoom(req,httpSession,resp);

    }



    private void addRoom(HttpServletRequest req,HttpSession httpSession,HttpServletResponse resp) throws IOException {
        String name=(String) req.getSession().getAttribute("username");
        if(getServletContext().getAttribute("rooms")!=null)
        rooms=(ArrayList<Room>) getServletContext().getAttribute("rooms");
        else
            rooms=new ArrayList<>();
        rooms.add(new Room(engine.getDescriptor().getMaxNumberOfPlayersPlaying(),name,engine));
        getServletContext().setAttribute("rooms", rooms);
        //need to see why the request is not sent good
        resp.sendRedirect(GAMES_URL);
    }



    private void getRoomsListAction(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        Gson gson = new Gson();
        rooms = (ArrayList<Room>) getServletContext().getAttribute("rooms");
        gameNames.clear();
        for(int i=0;i<rooms.size();i++)
            gameNames.add(rooms.get(i).getGameEngine().getDescriptor().getGameTitle());
        String json = gson.toJson(gameNames);
        out.println(json);
        out.flush();
    }

    private boolean isNameAvailable(String name)
    {

        for(String gameTitle : gameNames){
            if(gameTitle.equals(name))
                return false;
        }
        return true;
    }

    private String printHTML(String input) {
        return "<html>\n" +
                "<head>\n" +
                "<title>Conquers</title>\n" +
                "<script src=\"Lobby/jquery-3.4.1.js\"></script>\n"+
                "<link rel=\"stylesheet\" href=\"Lobby/bootstrap.min.css\">\n" +
                "<link rel=\"stylesheet\" href=\"Rooms.css\">\n" +
                "</head>\n" +
                "<body background=\"images/Professional-Website-Background-White-106363.jpg\">\n" +
                "<header>\n" +
                "<form action=\"/Logout\" method=\"get\">\n"+
                "<div class=\"logout\">\n"+
                "<Button  name=\"btn\" value=\"quitbtn\"> Logout </Button>\n"+
                "</div>\n"+

                "</form>\n"+
                "<span class=\"userNameSpan\"></span>\n"+
                "</header>\n" +
                "<form accept-charset=\"UTF-8\" method=\"post\" action=\"/UpLoadXmlFile\" autocomplete=\"off\" enctype=\"multipart/form-data\">\n"+
                "<div class=\"container\">\n"+
                "<h3>Enter your own game:</h3>\n"+
                "Choose XML File: <input type=\"file\" name=\"xml\" title=\"\" ><br />\n"+
                "<button type=\"submit\" value=\"Submit\">Submit</button>\n"+
                "</div>\n"+
                "<label class=\"red\">"+input+"</label>\n"+
                "</form>\n"+
                "<span class=\"errorXml\"></span>\n"+

                "<div class=\"left\">\n"+
                "<img src=\"/images/Player List.png\"/>\n"+
                "<div class=\"sidebar\">\n"+
                "<div id=\"userslist\" class=\"left\">\n"+
                "</div>\n"+
                "</div>\n"+
                "</div>\n"+
                "<div class=\"mainDiv\">\n"+
                "<div class=\"dialogDiv\">\n"+
                "<div class=\"dialogContent\">\n"+
                "<div class=\"details\">\n"+
                "<div class=\"gametitle\"></div>\n"+
                "<div class=\"creatorName\"></div>\n"+
                "<div class=\"boardSize\"></div>\n"+
                "<div class=\"playerNumber\"></div>\n"+
                "<div class=\"status\"></div>\n"+
                "<div class=\"detailsOfBoard\"></div>\n"+
                "<div class=\"detailsOfUnits\"></div>\n"+

                "<form action=\"/GameRoom/GameRoom.html\" method=\"get\">\n"+
                 "<Button  name=\"btn\" value=\"quitbtn\"> start </Button>\n"+

                "</form>\n"+
                "<Button  name=\"btn\" value=\"quitbtn\"onclick=\"hideRoom()\"> return to lobby </Button>\n"+

                "</div>\n"+
                "</div>\n"+
                "</div>\n"+
                "</div>\n"+
                "<div class=\"right\">\n"+
                "<img src=\"/images/Open Rooms.png\" style=\"margin: initial\"/>\n"+
                "<div id=\"roomsarea\" class=\"span6\"></div>\n"+
                "</div>\n"+
                "<script type=\"text/javascript\" src=\"LobbyPageControl.js\"></script>\n"+
                "</body>\n" +
                "</html>";
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
        try {
            processRequest(request, response);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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
        try {
            processRequest(request, response);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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
