package com.example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.TrafficLight.LIGHT;

public class Intersection {
    private final Map<String, Road> roads = new HashMap<>();
    private final List<String> leftVehicles = new ArrayList<>();
    private int timeInCurrentState = 0;
    private static final int YELLOW_DURATION = 1;
    private int currentGreenDuration = 1;
    private final int MIN_GREEN_DURATION = 3;
    private final int MAX_GREEN_DURATION = 10;
    private static String prevBusiestRoad = null;
    

    public Intersection() {
        roads.put("north", new Road("north", 1));
        roads.put("south", new Road("south", 1));
        roads.put("east", new Road("east", 1));
        roads.put("west", new Road("west", 1));

        // Inital state of traffic lights
        setLights("north", LIGHT.GREEN);
        setLights("south", LIGHT.RED);
        setLights("east", LIGHT.RED);
        setLights("west", LIGHT.RED);
        
        // Initial green light duration
        currentGreenDuration = calculateGreenDuration("north");
        
    }

    public void addVehicle(Vehicle vehicle) {
        roads.get(vehicle.getStartRoad()).addVehicleOnRoad(vehicle);
    }

    public String getBusiestRoad() {
        int totalVehicles = getTotalVehicles();
        String busiestRoad = null;
        
        // If there are no vehicles in the intersection, change lights sequentially
        if (totalVehicles == 0) {
            String[] roadList = roads.keySet().toArray(new String[0]);
            if (prevBusiestRoad == null) {
                prevBusiestRoad = roadList[0];
            }
    
            int index = 0;
            for (int i = 0; i < roadList.length; i++) {
                if (roadList[i].equals(prevBusiestRoad)) {
                    index = i;
                    break;
                }
            }
    
            prevBusiestRoad = roadList[(index + 1) % roadList.length];
            return prevBusiestRoad;
        }
    
        // Standard road choosing -> take priority, vehicles on the road and time at the red light 
        int maxWeight = -1;
        int maxWaitTime = -1;
    
        for (Map.Entry<String, Road> entry : roads.entrySet()) {
            Road road = entry.getValue();
            int vehiclesCount = road.getVehiclesOnRoad().size();
            int vehiclePrio = road.getPriority();                   
            int waitTime = road.getRedDuration();                   
            int weight = 2 * vehiclesCount + vehiclePrio;           
    
            if (weight > maxWeight) {
                maxWeight = weight;
                maxWaitTime = waitTime;
                busiestRoad = entry.getKey();
            } 
            // If the weight is equal to maxWeight, choose the road that has been waiting longer at the red light
            else if (weight == maxWeight && waitTime > maxWaitTime) {
                maxWaitTime = waitTime;
                busiestRoad = entry.getKey();
            }
        }
    
        prevBusiestRoad = busiestRoad;
        return busiestRoad;
    }
    public void step() {
        leftVehicles.clear();
        
        
        updateTrafficLight();
        timeInCurrentState++;
        
        for (Road road : roads.values()) {
            processVehicles(road);
        }
    }
    
    
    public void updateTrafficLight() {
        String activeDirection = getActiveDirection();
    
    
        LIGHT activeStatus = roads.get(activeDirection).getTrafficLight().getLightStatus();
        int requiredDuration = (activeStatus == LIGHT.GREEN) || (activeStatus == LIGHT.RED) ? currentGreenDuration : YELLOW_DURATION;
        
        changeRedLightTime();

        if (timeInCurrentState >= requiredDuration) {
            timeInCurrentState = 0;
            switchLightState(activeDirection, activeStatus);
        }
    }
    
    private void changeRedLightTime() {
        for (Map.Entry<String, Road> entry : roads.entrySet()) {
            Road road = entry.getValue();
            if (road.getTrafficLight().getLightStatus() == LIGHT.RED) {
                road.incrementRedDuration(road.getRedDuration()); 
            } else {
                road.setRedDuration(0); 
            }
        }
    }
    
    private String getActiveDirection() {
        for (Map.Entry<String, Road> entry : roads.entrySet()) {
            LIGHT status = entry.getValue().getTrafficLight().getLightStatus();
            if (status == LIGHT.GREEN || status == LIGHT.YELLOW) {
                return entry.getKey();
            }
        }
        return null;
    }

    private void switchLightState(String activeDirection, LIGHT currentStatus) {
        LIGHT prevLight = roads.get(activeDirection).getTrafficLight().getPrevLightStatus();
        if (currentStatus == LIGHT.GREEN) {
            // If the light is green, change it to yellow
            setLights(activeDirection, LIGHT.YELLOW);
            timeInCurrentState = 0;
            currentGreenDuration = YELLOW_DURATION;
        } 
        else if (currentStatus == LIGHT.YELLOW && prevLight == LIGHT.GREEN) {
            // If the light is yellow and was green, change it to yellow and choose new road
            setLights(activeDirection, LIGHT.RED);
            activateNewGreenLight();
        } else if (currentStatus == LIGHT.YELLOW && prevLight == LIGHT.RED) {
            // If the light is yellow and was red, update it green and calculate new time
            setLights(activeDirection, LIGHT.GREEN);
            currentGreenDuration = calculateGreenDuration(activeDirection);
        }
    }


    private void activateNewGreenLight() {
        String nextGreen = getBusiestRoad();
        if (roads.get(nextGreen).getTrafficLight().getLightStatus().equals(LIGHT.RED)){
            setLights(nextGreen, LIGHT.YELLOW);
            currentGreenDuration = YELLOW_DURATION;
            timeInCurrentState = 0;
        } else{
            setLights(nextGreen, LIGHT.GREEN);
            currentGreenDuration = calculateGreenDuration(nextGreen);
            timeInCurrentState = 0;
        }   
    }

    
    
    public int calculateGreenDuration(String direction) {
        int currentVehicles = roads.get(direction).getVehiclesOnRoad().size();
        int totalVehicles = getTotalVehicles();
        if (totalVehicles == 0) {
            return MIN_GREEN_DURATION;
        }
        
        // if optimization is need, calculate new time using ratio of currentVehicles and totalVehicles
        float ratio = (float)currentVehicles / totalVehicles;
        int calculatedDuration = MIN_GREEN_DURATION + 
                               (int)((MAX_GREEN_DURATION - MIN_GREEN_DURATION) * ratio);
        
        return  calculatedDuration;
    }
    
    private void setLights(String direction, LIGHT light) {
        roads.get(direction).getTrafficLight().setLightStatus(light);
    }

    private void processVehicles(Road road) {
        if (road.getTrafficLight().getLightStatus() == LIGHT.GREEN && 
            !road.getVehiclesOnRoad().isEmpty()) {
            Vehicle vehicle = road.getVehiclesOnRoad().remove(0);
            leftVehicles.add(vehicle.getVehicleId());
        }
    }

    // ------------------------------ Getters ------------------------------ 

    public int getMIN_GREEN_DURATION(){
        return MIN_GREEN_DURATION; 
    }

    public int getMAX_GREEN_DURATION(){
        return MAX_GREEN_DURATION;
    }

    public int getTotalVehicles() {
        return roads.values().stream()
                   .mapToInt(r -> r.getVehiclesOnRoad().size())
                   .sum();
    }


    public List<String> getLeftVehicles() {
        return leftVehicles;
    }

    public Map<String, Road> getRoads(){
        return roads;
    }

    public String getTrafficStatus() {
        StringBuilder output = new StringBuilder(); 
        output.append("Current status: ").append(currentGreenDuration).append(" ")
              .append(timeInCurrentState).append("\n");
        for (Road r : roads.values()) {
            output.append(r.getDirection()).append(" ")
                    .append(r.getTrafficLight().getLightStatus())
                  .append(" ").append(r.getRedDuration()).append("\n");
        }
        return output.toString();
    }
    public int getCurrentGreenDuration(){
        return  currentGreenDuration;
    }

    public int getTimeInCurrentState(){
        return timeInCurrentState;
    }
}