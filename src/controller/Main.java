package controller;

import db.DBController;

public class Main {
    static public void main(String[] args) {
        DBController db = DBController.getInstance();

//        User u1 = new User("Billy", "pw1");
//        db.insertUser(u1);
//        User u2 = db.getUserByName("Billy");
//        System.out.println(u2.getName());

//
//        // test bikes
//        System.out.println("Inserting test bikes");
//        Bike b1 = new Bike();
//        Bike b2 = new Bike();
//        System.out.println(b1);
//        System.out.println(b2);
//        db.insertBike(b1);
//        db.insertBike(b2);
//
//        System.out.println();
//
//        System.out.println("Retrieving test bikes");
//        ArrayList<Bike> bikes = db.getBikes();
//        System.out.println(bikes.get(0));
//        System.out.println(bikes.get(1));
//        System.out.println("Success!");
//
//        System.out.println();
//
//        // try to get bike by id
//        System.out.println("Get bike by id");
//        Bike b3 = db.getBikeById(b1.getId());
//        System.out.println(b3);
//        Bike b4 = db.getBikeById(b2.getId());
//
//        Bike b5 = new Bike();
//        Bike b6 = db.getBikeById(b5.getId()); // should not be found ( = null)
//        System.out.println("Should be null: " + b6);
//
//        System.out.println();
//
//        System.out.println("Updating test bikes");
//        System.out.println("Previous Setting: " + db.getBikeById(b1.getId()) + " " + db.getBikeById(b1.getId()).getAvailable());
//        b1.toggleAvailable();
//        DBController.getInstance().updateBike(b1);
//        System.out.println("Updated Setting: " + db.getBikeById(b1.getId()) + " " + db.getBikeById(b1.getId()).getAvailable());
//
//        db.clearBikes();    // else they pile up
//
//        System.out.println();
//
//
//        System.out.println("Testing insertion");
//
//
//        System.out.println("Testing retrieval");
//        ArrayList<User> users = db.getUsers();
//        System.out.println(users.get(0));
//        System.out.println(users.get(1));
//
//        // try to get user by id
//        System.out.println("Get user by id");
//        System.out.println(db.getUserById(u1.getId()));
//        System.out.println(db.getUserById(u2.getId()));
//        User u5 = new User("John", "pw3");
//        System.out.println("This should be null: " + db.getUserById(u5.getId()));
//        System.out.println("Success!");
//        db.clearUsers();
    }
}