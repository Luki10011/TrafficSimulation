package com.example;


public class Vehicle {
    private final String vehicleId;
    private final String startRoad;
    private final String endRoad;
    private String currentRoad;


    public Vehicle(String vehicleId, String startRoad, String endRoad) {
        this.vehicleId = vehicleId;
        this.startRoad = startRoad;
        if (startRoad.equals(endRoad)){
            throw new AssertionError("Start road and end road can't be the same!");
        }else{
            this.endRoad = endRoad;
            this.currentRoad = startRoad;
        }

    }
    
    public String getEndRoad() {return endRoad;}
    public String getCurrentRoad() {return currentRoad;}
    public String getStartRoad() {return startRoad;}
    public String getVehicleId() {return vehicleId;}


    @Override
    public String toString(){
        StringBuilder vehicleString = new StringBuilder();
        vehicleString.append("Id: ").append(this.vehicleId).append("\n");
        vehicleString.append("StartRoad: ").append(this.startRoad).append("\n");
        vehicleString.append("EndRoad: ").append(this.endRoad).append("\n");
        return vehicleString.toString();
    }
    
}
