package com.example;

import java.util.ArrayList;
import java.util.List;

public class Road {
    private String roadId;
    private List<Vehicle> vehiclesOnRoad;
    private TrafficLight trafficLight;
    private int priority = 0;
    private int numberOfVehicles = 0;
    private int redDuration = 0;

    public Road(String roadId_, int priority_){
        this.roadId = roadId_;
        this.vehiclesOnRoad = new ArrayList<>();
        this.trafficLight = new TrafficLight();
        this.numberOfVehicles = 0;
        this.priority = priority_;
    }

    


    public void addVehicleOnRoad(Vehicle vehicle){
        if (vehicle != null){
            vehiclesOnRoad.add(vehicle);
            this.numberOfVehicles++;
        }
    }
    public void deleteVehicleFromRoad(){
        if (!vehiclesOnRoad.isEmpty()){
            vehiclesOnRoad.remove(0);
            this.numberOfVehicles--;
        }
    }

    public void incrementRedDuration(int redDuration) {
        this.redDuration = redDuration + 1;
    }

    @Override
    public String toString(){
        List<Vehicle> vehicles = getVehiclesOnRoad(); 
        StringBuilder roadString = new StringBuilder();
        for (Vehicle vehicle : vehicles){
            roadString.append(vehicle.toString()).append("\n");
        }
        return roadString.toString();
    }

    // ------------------------------ Getters ------------------------------ 

    public List<Vehicle> getVehiclesOnRoad() {
        return vehiclesOnRoad;
    }

    public String getDirection() {return 
        roadId;
    }

    public TrafficLight getTrafficLight() {
        return trafficLight;
    }

    public int getPriority(){
        return priority;
    }

    public int getRedDuration() {
        return redDuration;
    }

    // ------------------------------ Setters ------------------------------ 

    public void setTrafficLight(TrafficLight trafficLight) {
        this.trafficLight = trafficLight;
    }

    public void setRedDuration(int redDuration) {
        this.redDuration = redDuration;
    }

    

    
}
