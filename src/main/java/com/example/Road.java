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

    public Road(String roadId_, int priority_ ){
        this.roadId = roadId_;
        this.vehiclesOnRoad = new ArrayList<>();
        this.trafficLight = new TrafficLight();
        this.numberOfVehicles = priority_;
    }


    public List<Vehicle> getVehiclesOnRoad() {return vehiclesOnRoad;}
    public String getDirection() {return roadId;}

    public void addVehicleOnRoad(Vehicle vehicle){
        vehiclesOnRoad.add(vehicle);
        this.numberOfVehicles++;
    }
    public void deleteVehicleFromRoad(){
        if (!vehiclesOnRoad.isEmpty()){
            vehiclesOnRoad.remove(0);
            this.numberOfVehicles--;
        }else{
            System.out.println("There are no vehicles");
        }
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

    public TrafficLight getTrafficLight() {
        return trafficLight;
    }

    public void setTrafficLight(TrafficLight trafficLight) {
        this.trafficLight = trafficLight;
    }

    public int getPriority(){
        return priority;
    }

    public int getRedDuration() {
        return redDuration;
    }

    public void setRedDuration(int redDuration) {
        this.redDuration = redDuration;
    }

    public void incrementRedDuration(int redDuration) {
        this.redDuration = redDuration + 1;
    }

    
}
