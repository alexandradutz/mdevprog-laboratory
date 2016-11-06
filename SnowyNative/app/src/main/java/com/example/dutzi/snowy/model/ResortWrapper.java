package com.example.dutzi.snowy.model;

/**
 * Created by dutzi on 06.11.2016.
 */

public class ResortWrapper {
    private int id;
    private int resort_id;

    private boolean visited;
    private double distance;    //from your location to those coordinates

    public ResortWrapper() {
        this.resort_id = -1;
        this.visited = false;
        this.distance = 0;
    }

    public ResortWrapper(int resort_id, boolean visited, double distance) {
        this.resort_id = resort_id;
        this.visited = visited;
        this.distance = distance;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getResort_id() {
        return resort_id;
    }

    public void setResort_id(int resort_id) {
        this.resort_id = resort_id;
    }

    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }
}
