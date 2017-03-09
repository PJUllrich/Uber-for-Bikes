package db;

import com.google.gson.Gson;

import java.util.UUID;

public class Bike {
    private final UUID id;
    private Double[] location = new Double[]{53.217688, 6.566349};
    private Boolean available = false;

    public Bike(UUID id, Double[] location, Boolean available) {
        this(id, available);
        this.location = location;
    }

    public Bike(UUID id, Boolean available) {
        this.id = id;
        this.available = available;
    }

    public Bike(Boolean available, Double[] location) {
        this();
        this.location = location;
        this.available = available;
    }

    public Bike(Boolean available) {
        this();
        this.available = available;
    }

    public Bike() {
        this.id = UUID.randomUUID();
    }

    public UUID getId() {
        return id;
    }

    public Double[] getLocation() {
        return location;
    }

    public void setLocation(Double[] location) {
        this.location = location;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Bike bike = (Bike) o;

        return id != null ? id.equals(bike.id) : bike.id == null;
    }

    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
