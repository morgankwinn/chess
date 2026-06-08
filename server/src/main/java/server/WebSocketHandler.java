package server;

import com.google.gson.Gson;
import io.javalin.websocket.*;

import javax.swing.*;
import java.io.IOException;

public class WebSocketHandler implements WsConnectHandler, WsMessageHandler, WsCloseHandler {
    private final ConnectionManager connections = new ConnectionManager();

    @Override
    public void handleConnect(WsConnectContext ctx) {
        System.out.println("Websocket connected");
        ctx.enableAutomaticPings();
    }

    @Override
    public void handleMessage(WsMessageContext ctx) {
        try {
            Action action = new Gson().fromJson(ctx.message(), Action.class);
            switch (action.type()) {
                case  -> ;
                case  -> ;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void handleClose(WsCloseContext ctx) {
        System.out.println("Websocket closed");
    }


}
