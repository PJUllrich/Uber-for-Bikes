package websocket;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.StringReader;

@ServerEndpoint("/actions")
public class WebSocketServer {
    private static WebSocketSessionHandler sessionHandler = new WebSocketSessionHandler();

    @OnOpen
    public void open(Session session) {
        System.out.println("Connection opened!");
        sessionHandler.addSession(session);
    }

    @OnClose
    public void close(Session session) {
        sessionHandler.removeSession(session);
    }

    @OnError
    public void onError(Throwable error) {
        System.err.println("This error occurred in the Websocket: " + error.getMessage());
    }

    @OnMessage
    public void handleMessage(String message, Session session) {
        try (JsonReader reader = Json.createReader(new StringReader(message))) {
            JsonObject jsonMessage = reader.readObject();

            String action = jsonMessage.getString("action");

            switch (action) {

            }

            if ("addBike".equals(jsonMessage.getString("action"))) {
                sessionHandler.addRandomBike();
            } else if ("getAllBikes".equals(jsonMessage.getString("action"))) {
                sessionHandler.sendAllBikes(session);
            } else if ("changeBikeRental".equals(jsonMessage.getString("action"))) {
                sessionHandler.changeBikeRental(jsonMessage, session);
            }
        }
    }
}