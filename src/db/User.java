package db;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

public class User {
    private UUID id;
    private String name;
    private String password;
    private Double balance = 0.0;
    private Collection<Bike> bikesRented = new ArrayList<>();

    public User(String name, String password) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.password = password;
    }

    public User(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.balance = user.getBalance();
        this.bikesRented = user.getBikesRented();
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public Collection<Bike> getBikesRented() {
        return bikesRented;
    }

    public void addBikeRented(Bike bike) {
        this.bikesRented.add(bike);
    }

    public Boolean checkPassword(String password) {
        return this.password.equals(password);
    }

    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public void removeBikeRented(Bike bike) {
        this.bikesRented.remove(bike);
    }
}