package server;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class ConnectionManager {
    public final HashMap<Integer, ArrayList<Session>> connections = new HashMap<>();

    public void add(Integer gameID, Session session) {
        ArrayList<Session> sessions = connections.get(gameID);
        if (sessions == null) {
            sessions = new ArrayList<>();
        }
        sessions.add(session);
        connections.put(gameID, sessions);
    }

    public void remove(Integer gameID, Session session) {
        ArrayList<Session> sessions = connections.get(gameID);
        sessions.remove(session);
        connections.put(gameID, sessions);
    }

    public void broadcast(Session excludeSession, ServerMessage notification, Integer gameID) throws IOException {
        String notificationJson = new Gson().toJson(notification);
        for (Session c : connections.get(gameID)) {
            if (c.isOpen()) {
                if (!c.equals(excludeSession)) {
                    c.getRemote().sendString(notificationJson);
                }
            }
        }
    }
}
