package com.example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.TrafficLight.LIGHT;

public class Intersection {
    private Map<String, Road> roads = new HashMap<>();
    private List<String> leftVehicles = new ArrayList<>();
    private int timeInCurrentState = 0;
    private static final int YELLOW_DURATION = 1;
    private int currentGreenDuration = 1;
    private final int MIN_GREEN_DURATION = 3;
    private final int MAX_GREEN_DURATION = 7;
    private static String prevBusiestDirection = null;
    

    public Intersection() {
        roads.put("north", new Road("north", 1));
        roads.put("south", new Road("south", 1));
        roads.put("east", new Road("east", 1));
        roads.put("west", new Road("west", 1));

        // Początkowy stan świateł
        setLights("north", LIGHT.GREEN);
        setLights("south", LIGHT.RED);
        setLights("east", LIGHT.RED);
        setLights("west", LIGHT.RED);
        
        // Oblicz początkowy czas zielonego
        currentGreenDuration = calculateGreenDuration("north");
        
    }

    public void addVehicle(Vehicle vehicle) {
        roads.get(vehicle.getStartRoad()).addVehicleOnRoad(vehicle);
    }

    private String getBusiestDirection() {
        int totalVehicles = getTotalVehicles();
        String busiestDirection = null;
        
        // Jeśli nie ma żadnych pojazdów, przełączaj sekwencyjnie
        if (totalVehicles == 0) {
            String[] roadList = roads.keySet().toArray(new String[0]);
            if (prevBusiestDirection == null) {
                prevBusiestDirection = roadList[0];
            }
    
            int index = 0;
            for (int i = 0; i < roadList.length; i++) {
                if (roadList[i].equals(prevBusiestDirection)) {
                    index = i;
                    break;
                }
            }
    
            prevBusiestDirection = roadList[(index + 1) % roadList.length];
            return prevBusiestDirection;
        }
    
        // Standardowe wybieranie drogi z uwzględnieniem priorytetu i czasu oczekiwania
        int maxWeight = -1;
        int maxWaitTime = -1;
    
        for (Map.Entry<String, Road> entry : roads.entrySet()) {
            Road road = entry.getValue();
            int vehiclesCount = road.getVehiclesOnRoad().size();
            int vehiclePrio = road.getPriority();                   //Pobranie priorytetu
            int waitTime = road.getRedDuration();                   //Pobieramy czas oczekiwania na czerwonym świetle
            int weight = 2 * vehiclesCount + vehiclePrio;           // Obliczamy wagę drogi
    
            if (weight > maxWeight) {
                maxWeight = weight;
                maxWaitTime = waitTime;
                busiestDirection = entry.getKey();
            } 
            // Jeśli waga jest równa, wybieramy drogę, która czekała dłużej na czerwonym
            else if (weight == maxWeight && waitTime > maxWaitTime) {
                maxWaitTime = waitTime;
                busiestDirection = entry.getKey();
            }
        }
    
        prevBusiestDirection = busiestDirection;
        return busiestDirection;
    }
    public void step() {
        leftVehicles.clear();
        
    
        
    
        // Zaktualizowanie statusu świateł
        updateTrafficLight();
        timeInCurrentState++;
        
        for (Road road : roads.values()) {
            processVehicles(road);
        }
    }
    
    
    private void updateTrafficLight() {
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
                road.incrementRedDuration(road.getRedDuration()); // Zwiększ licznik czerwonego światła
            } else {
                road.setRedDuration(0); // Zeruj licznik, jeśli światło nie jest czerwone
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
            // Jeśli światło jest zielone, zmieniamy na żółte
            setLights(activeDirection, LIGHT.YELLOW);
            timeInCurrentState = 0;
            currentGreenDuration = YELLOW_DURATION;
        } 
        else if (currentStatus == LIGHT.YELLOW && prevLight == LIGHT.GREEN) {
            // Jeśli było żółte, zmieniamy na czerwone i uruchamiamy nowe zielone
            setLights(activeDirection, LIGHT.RED);
            activateNewGreenLight();
        } else if (currentStatus == LIGHT.YELLOW && prevLight == LIGHT.RED) {
            // Jeśli było żółte, zmieniamy na czerwone i uruchamiamy nowe zielone
            setLights(activeDirection, LIGHT.GREEN);
            currentGreenDuration = calculateGreenDuration(activeDirection);
        }
    }


    private void activateNewGreenLight() {
        String nextGreen = getBusiestDirection();
        if (roads.get(nextGreen).getTrafficLight().getLightStatus().equals(LIGHT.RED)){
            setLights(nextGreen, LIGHT.YELLOW);
            currentGreenDuration = YELLOW_DURATION;
            timeInCurrentState = 0;
        } else{
            setLights(nextGreen, LIGHT.GREEN);
            currentGreenDuration = calculateGreenDuration(nextGreen);
            timeInCurrentState = 0;
            
            System.out.println("New green light for " + nextGreen + 
                            " for " + currentGreenDuration + " units");
        }
        
    }

    
    
    private int calculateGreenDuration(String direction) {
        int currentVehicles = roads.get(direction).getVehiclesOnRoad().size();
        int totalVehicles = getTotalVehicles();
        if (totalVehicles == 0) {
            return MIN_GREEN_DURATION;
        }
        
        float ratio = (float)currentVehicles / totalVehicles;
        int calculatedDuration = MIN_GREEN_DURATION + 
                               (int)((MAX_GREEN_DURATION - MIN_GREEN_DURATION) * ratio/2);
        
        // Zapewnij minimalny czas
        return  calculatedDuration;
    }
    
    private int getTotalVehicles() {
        return roads.values().stream()
                   .mapToInt(r -> r.getVehiclesOnRoad().size())
                   .sum();
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