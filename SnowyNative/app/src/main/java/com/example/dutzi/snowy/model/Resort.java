package com.example.dutzi.snowy.model;

/**
 * Created by dutzi on 06.11.2016.
 */

public class Resort {
    private int id; //generate unique using seed
    private String name;
    private String coordinates;
    private String country;
    private double slopes_km;

    public Resort(String name) {
        this.name = name;
    }

    public Resort(String name, String coordinates, String country, double slopes_km) {
        this.name = name;
        this.coordinates = coordinates;
        this.country = country;
        this.slopes_km = slopes_km;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(String coordinates) {
        this.coordinates = coordinates;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public double getSlopes_km() {
        return slopes_km;
    }

    public void setSlopes_km(double slopes_km) {
        this.slopes_km = slopes_km;
    }

    @Override
    public String toString() {
        return "Resort: " + name + '\'';
    }
}
