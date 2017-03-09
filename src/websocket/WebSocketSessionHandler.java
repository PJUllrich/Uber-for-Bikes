package websocket;

import com.google.gson.Gson;
import db.Bike;
import db.DBController;
import db.User;

import javax.json.JsonObject;
import javax.json.spi.JsonProvider;
import javax.websocket.Session;
import java.io.IOException;
import java.util.*;

public class WebSocketSessionHandler {
    private final Set<Session> sessions = new HashSet<>();
    private final Set<Bike> bikes = new HashSet<>();

    public void addSession(Session session) {
        sessions.add(session);
    }

    public void removeSession(Session session) {
        sessions.remove(session);
    }

    private JsonObject getBikeJSON(String action, Bike bike) {
        return JsonProvider.provider().createObjectBuilder()
                .add("action", action)
                .add("id", bike.getId().toString())
                .add("location", Arrays.toString(bike.getLocation()))
                .add("available", bike.getAvailable())
                .build();
    }

    private void sendToAllConnectedSessions(JsonObject message) {
        for (Session session : sessions) {
            sendToSession(session, message);
        }
    }

    private void sendToSession(Session session, JsonObject message) {
        try {
            session.getBasicRemote().sendText(message.toString());
        } catch (IOException e) {
            e.printStackTrace();
            sessions.remove(session);
        }
    }

    public void sendAllBikes(Session session) {
        ArrayList<Bike> allBikes = DBController.getInstance().getBikes();
        for (Bike bike : allBikes) {
            JsonObject addMessage = getBikeJSON("addBike", bike);
            this.sendToSession(session, addMessage);
        }
    }

    public void addRandomBike() {
        Bike newBike = DBController.getInstance().insertRandomBike();

        JsonObject addMessage = getBikeJSON("addBike", newBike);
        sendToAllConnectedSessions(addMessage);
    }

    public void changeBikeRental(JsonObject message, Session session) {
        Boolean rent = message.getBoolean("rent");

        rentBike(message, session);
    }

    private void rentBike(JsonObject message, Session session) {
        UUID userID = UUID.fromString(message.getString("userID"));
        UUID bikeID = UUID.fromString(message.getString("bikeID"));

        User user = DBController.getInstance().getUserById(userID);
        Bike bike = DBController.getInstance().getBikeById(bikeID);

        Boolean wantsToRent = message.getBoolean("rent");

        if (wantsToRent) {
            bike.setAvailable(false);
            user.addBikeRented(bike);
        } else {
            bike.setAvailable(true);
            user.removeBikeRented(bike);
        }

        this.updateUser(user, session);
        this.updateBike(bike);
    }

    private void updateUser(User user, Session session) {
        Gson gson = new Gson();

        JsonObject updateMessage = JsonProvider.provider().createObjectBuilder()
                .add("action", "updateUser")
                .add("id", user.getId().toString())
                .add("username", user.getName())
                .add("balance", user.getBalance())
                .add("rented", gson.toJson(user.getBikesRented()))
                .build();

        DBController.getInstance().updateUser(user);
        this.sendToSession(session, updateMessage);
    }

    private void updateBike(Bike bike) {
        JsonObject updateMessage = getBikeJSON("updateBike", bike);

        DBController.getInstance().updateBike(bike);
        this.sendToAllConnectedSessions(updateMessage);
    }

}
