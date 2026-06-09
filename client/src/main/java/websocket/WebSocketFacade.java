package websocket;

import com.google.gson.Gson;
import jakarta.websocket.*;
import ui.PreLoginClient;

import java.net.URI;

public class WebSocketFacade extends Endpoint {

    Session session;

    public WebSocketFacade(String url) throws RuntimeException {
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/ws");

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            //set message handler
            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    String notification = new Gson().fromJson(message, String.class);
                    PreLoginClient.notify(notification);
                }
            });
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    //Endpoint requires this method, but you don't have to do anything
    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }

    private void connect() {
    }
    
}
