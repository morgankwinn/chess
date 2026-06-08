package server;

import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;
import java.util.HashMap;

public class ConnectionManager {
    public final HashMap<Session, Session> connections = new HashMap<>();

    public void add(Session session) {
        connections.put(session, session);
    }

    public void remove(Session session) {
        connections.remove(session);
    }

    public void broadcast(Session excludeSession, String notification) throws IOException {
        for (Session c : connections.values()) {
            if (c.isOpen()) {
                if (!c.equals(excludeSession)) {
                    c.getRemote().sendString(notification);
                }
            }
        }
    }
}
