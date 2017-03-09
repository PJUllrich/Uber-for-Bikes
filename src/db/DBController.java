package db;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;

import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class DBController {
    private static DBController instance = new DBController();
    MongoClient mongoClient;
    MongoDatabase db;

    private DBController() {
        // name depends on container name specified in docker-compose.yml
//        this.mongoClient = new MongoClient("db", 27017); // use new MongoClient() for running it in Intellij
//        this.mongoClient = new MongoClient();
        this.mongoClient = new MongoClient(new MongoClientURI("mongodb://192.168.10.20:27017,192.168.10.21:27017,192.168.10.22:27017/?replicaSet=ubobSet"));
        db = mongoClient.getDatabase("ubobDB");
        this.startListeningForChangeEvents();
    }

    public static DBController getInstance() {
        return instance;
    }

    /**
     * Inserts a Bike to the collection bikes
     *
     * @param bike The bike to be added
     */
    public Boolean insertBike(Bike bike) {
        if (this.getBikeById(bike.getId()) != null) {
            System.err.println("Bike with id already exists in Database.");
            return false;
        }

        MongoCollection collection = db.getCollection("bikes");
        collection.insertOne(Document.parse(bike.toJson()));
        return true;
    }

    public Boolean insertUser(User user) {
        if (this.getUserById(user.getId()) != null) {
            System.err.println("User with id already exists.");
            return false;
        } else if (this.getUserByName(user.getName()) != null) {
            System.err.println("User with name already exists.");
            return false;
        }
        MongoCollection collection = db.getCollection("users");
        collection.insertOne(Document.parse(user.toJson()));
        return true;
    }

    /**
     * deletes all documents from the bike collection
     */
    public void clearBikes() {
        db.getCollection("bikes").drop();
    }

    /**
     * deletes all documents from the user collection
     */
    public void clearUsers() {
        db.getCollection("users").drop();
    }

    /**
     * returns all Bike documents in the collection bikes as Bike objects
     *
     * @return ArrayList<Bike>
     */
    public ArrayList<Bike> getBikes() {
        MongoCollection collection = db.getCollection("bikes");
        MongoCursor<Document> cursor = collection.find().iterator();
        ArrayList<Bike> bikes = new ArrayList<>();
        Gson gson = new Gson();
        try {
            while (cursor.hasNext()) {
                String js = cursor.next().toJson();
                Bike bike = gson.fromJson(js, Bike.class);
                bikes.add(bike);
            }
        } catch (JsonSyntaxException e) {
            System.err.println("Bike could not be parsed from JSON");
        } finally {
            cursor.close();
        }
        return bikes;
    }

    /**
     * returns all User documents in the collection users as User objects
     *
     * @return ArrayList<Bike>
     */
    public ArrayList<User> getUsers() {
        MongoCollection collection = db.getCollection("users");
        MongoCursor<Document> cursor = collection.find().iterator();
        ArrayList<User> users = new ArrayList<>();
        Gson gson = new Gson();
        try {
            while (cursor.hasNext()) {
                String js = cursor.next().toJson();
                User user = gson.fromJson(js, User.class);
                users.add(user);
            }
        } catch (JsonSyntaxException e) {
            System.err.println("Bike could not be parsed from JSON");
        } finally {
            cursor.close();
        }
        return users;
    }

    public Document getDocumentById(String id, MongoCollection collection) {
        BasicDBObject query = new BasicDBObject("id", id);
        MongoCursor<Document> cursor = collection.find(query).iterator();
        if (cursor.hasNext()) {
            return cursor.next();
        }
        return null;
    }

    public Bike getBikeById(UUID identifier) {
        String id = identifier.toString();
        MongoCollection collection = db.getCollection("bikes");
        Document doc = getDocumentById(id, collection);
        Bike bike = null;
        if (doc != null) {
            Gson gson = new Gson();
            try {
                bike = gson.fromJson(doc.toJson(), Bike.class);
            } catch (JsonSyntaxException e) {
                System.err.println("Bike could not be parsed from JSON");
            }
        }
        return bike;
    }

    public User getUserById(UUID identifier) {
        String id = identifier.toString();
        MongoCollection collection = db.getCollection("users");
        Document doc = getDocumentById(id, collection);
        User user = null;
        if (doc != null) {
            Gson gson = new Gson();
            try {
                user = gson.fromJson(doc.toJson(), User.class);
            } catch (JsonSyntaxException e) {
                System.err.println("User could not be parsed from JSON");
            }
        }
        return user;
    }

    public User getUserByName(String name) {
        BasicDBObject query = new BasicDBObject("name", name);
        MongoCursor<Document> cursor = db.getCollection("users").find(query).iterator();
        if (cursor.hasNext()) {
            String json = cursor.next().toJson();
            return new Gson().fromJson(json, User.class);
        }

        return null;
    }

    public void updateUser(User user) {
        MongoCollection collection = db.getCollection("users");
        Document oldEntry = getDocumentById(user.getId().toString(), collection);
        collection.replaceOne(oldEntry, Document.parse(user.toJson()));
    }

    public void updateBike(Bike bike) {
        MongoCollection collection = db.getCollection("bikes");
        Document oldEntry = getDocumentById(bike.getId().toString(), collection);
        UpdateResult out = collection.replaceOne(oldEntry, Document.parse(bike.toJson()));
        System.out.println("break here");
    }

    public Bike insertRandomBike() {
        Double lon = ThreadLocalRandom.current().nextDouble(53.190, 53.260);
        Double lat = ThreadLocalRandom.current().nextDouble(6.450, 6.670);
        Double[] coordinates = {lon, lat};
        Bike newBike = new Bike(true, coordinates);

        this.insertBike(newBike);
        return newBike;
    }

    public Boolean removeUser(User user) {
        return removeObject(user.getId(), db.getCollection("users"));
    }

    public Boolean removeBike(Bike bike) {
        return removeObject(bike.getId(), db.getCollection("bikes"));
    }

    private Boolean removeObject(UUID id, MongoCollection collection) {
        BasicDBObject query = new BasicDBObject("id", id);
        return collection.deleteOne(query).wasAcknowledged();
    }

    private void startListeningForChangeEvents() {
        MongoCollection collection = db.getCollection("oplog");


    }
}