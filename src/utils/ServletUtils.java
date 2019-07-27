package utils;

import GameEngine.GameEngine;
import ServletClasses.Room;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class ServletUtils {
    private static final Object userManagerLock = new Object();
    public static UserManager getUserManager(ServletContext servletContext) {

		synchronized (userManagerLock) {
			if (servletContext.getAttribute("userManager") == null) {
				servletContext.setAttribute("userManager", new UserManager());
			}
		}
		return (UserManager) servletContext.getAttribute("userManager");
	}

	public Room getCurrentRoom(HttpServletRequest req, List<Room> rooms){
		int i=0;
		String userName = req.getSession(false).getAttribute("username").toString();
		Room currRoom=null;
		while(i<rooms.size())
		{
			if (rooms.get(i).hasPlayer(userName)) {
				currRoom = rooms.get(i);
				break;
			}
			i++;
		}

		return currRoom;
	}

  //  public static void responseJson(Game room, PrintWriter out, boolean withWords) {
//        StringBuilder gameJson = new StringBuilder();
//        gameJson.append("{\n\t\"boadprint\": ");
//        gameJson.append(room.getController().getBaordPrint());
//        gameJson.append((",\"boardsize\":"+room.getEngine().getBoardSize()));
//        if(withWords) {
//            gameJson.append(",\"wordcomposed\": [");
//            List<PlayerModel> players = room.getController().getEngine().getPlayers();
//
//            PlayerModel currentPlayer = players.get(room.getController().getEngine().getTurnOf());
//            for (int i = 0; i < currentPlayer.wordComposedList().size(); i++) {
//                gameJson.append("\"" + currentPlayer.wordComposedList().get(i) + "\"");
//                if (i != currentPlayer.wordComposedList().size() - 1)
//                    gameJson.append(",");
//            }
//            gameJson.append("]");
//            gameJson.append(",\"players\":[");
//            for (int i = 0; i < players.size(); i++) {
//                gameJson.append("\"" + players.get(i).getName() +" Points:"+players.get(i).getPoints()+ "\"");
//                if (i != players.size() - 1)
//                    gameJson.append(",");
//            }
//            gameJson.append("]");
//            gameJson.append(",\"massage\":\"" + room.getController().getMassage() + "\"");
//        }
//        gameJson.append("}");
//        out.println(gameJson.toString());
//        out.flush();
//    }
}
