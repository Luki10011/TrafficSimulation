package com.example;

import java.util.List;
import java.util.Map;

import com.example.TrafficLight.LIGHT;

/**
 * Visualizes the state of a traffic intersection in the console.
 * Displays roads, traffic lights, and vehicles in a text-based format.
 */
public class IntersectionVisualizer {
    // Reference to the intersection being visualized
    private static Intersection intersection;
    
    // Constants for visualization layout
    private static final int MAX_CARS_ON_ROAD = 8;                              // Maximum cars to display per road
    private static final int DEFAULT_OFFSET_HORIZONTAL = MAX_CARS_ON_ROAD + 3;  // Horizontal spacing
    private static final int DEFAULT_OFFSET_VERTICAL = MAX_CARS_ON_ROAD;    // Vertical spacing

    
    public IntersectionVisualizer(Intersection intersection_) {
        IntersectionVisualizer.intersection = intersection_;
    }

    
    public void printIntersection() {
        // Get all roads from the intersection
        Map<String, Road> roads = intersection.getRoads();
        
        // Get traffic light status symbols for each direction
        String northLight = getLightSymbol(roads.get("north").getTrafficLight().getLightStatus());
        String southLight = getLightSymbol(roads.get("south").getTrafficLight().getLightStatus());
        String eastLight = getLightSymbol(roads.get("east").getTrafficLight().getLightStatus());
        String westLight = getLightSymbol(roads.get("west").getTrafficLight().getLightStatus());

        // Get vehicles on each road
        List<Vehicle> northCars = roads.get("north").getVehiclesOnRoad();
        List<Vehicle> southCars = roads.get("south").getVehiclesOnRoad();
        List<Vehicle> eastCars = roads.get("east").getVehiclesOnRoad();
        List<Vehicle> westCars = roads.get("west").getVehiclesOnRoad();
        
        // Calculate horizontal positioning based on westbound vehicles
        int horizontalOffset = 0;
        for (Vehicle westCar : westCars) {
            horizontalOffset += 1;
        }
        horizontalOffset += westCars.size() + DEFAULT_OFFSET_HORIZONTAL;
        
        // Constants for intersection layout
        int intersectionOffset = 6;  // Width of intersection area
        int roadWidth = intersectionOffset/2;  // Half of intersection width

        // Display remaining green light time
        System.out.println("Remaining green light time: "+ 
            (intersection.getCurrentGreenDuration() - intersection.getTimeInCurrentState()) + "s");
        
        // Draw northbound vehicles
        printVerticalCars(northCars, horizontalOffset + roadWidth, "south");
        
        // Draw north traffic light and horizontal road
        System.out.println(" ".repeat(horizontalOffset + roadWidth) +  "|  [" + northLight + "]  |" + 
            " ".repeat(horizontalOffset + roadWidth));
        System.out.println("=".repeat(horizontalOffset + roadWidth) +  "         " + 
            "=".repeat(horizontalOffset + roadWidth));

        // Draw eastbound vehicles, west/east lights, and westbound vehicles
        printHorizontalCars(eastCars, "west", horizontalOffset + 2*intersectionOffset + roadWidth);
        System.out.println("-".repeat(horizontalOffset + 1) + "[" + westLight + "]       [" + 
            eastLight + "]" + "-".repeat(horizontalOffset + 1));
        printHorizontalCars(westCars, "east", horizontalOffset);
        
        // Draw bottom horizontal road
        System.out.println("=".repeat(horizontalOffset + roadWidth) +  "         " + 
            "=".repeat(horizontalOffset + roadWidth));

        // Draw south traffic light and vehicles
        System.out.println(" ".repeat(horizontalOffset + roadWidth) +  "|  [" + southLight + "]  |");
        printVerticalCars(southCars, horizontalOffset + roadWidth, "north");
        System.out.println();
    }

    
    public void printVerticalCars(List<Vehicle> cars, int offset, String oppositeDirection) {
        if (!cars.isEmpty()) {
            StringBuilder output = new StringBuilder();
            int stop = Math.min(cars.size(), MAX_CARS_ON_ROAD);  // Limit displayed cars
            
            if (oppositeDirection.equals("south")) {
                // Northbound vehicles (moving down, shown as 'v')
                String emptyRoad = (" ".repeat(offset) + "|   |   |\n").repeat(DEFAULT_OFFSET_VERTICAL - stop);
                output.append(emptyRoad);
                
                for (int i = 0; i < stop; i++) {
                    output.append(" ".repeat(offset)).append("| v |   |\n");
                }
            } 
            else if (oppositeDirection.equals("north")) {
                // Southbound vehicles (up down, shown as '^')
                for (int i = 0; i < stop; i++) {
                    output.append(" ".repeat(offset)).append("|   | ^ |\n");
                }
                String emptyRoad = (" ".repeat(offset) + "|   |   |\n").repeat(DEFAULT_OFFSET_VERTICAL - stop);
                output.append(emptyRoad);
            }
            System.out.print(output.toString());
        } else {
            // Empty road case
            String emptyRoad = (" ".repeat(offset) + "|   |   |\n").repeat(DEFAULT_OFFSET_VERTICAL);
            System.out.print(emptyRoad);
        }
    }

    
    public void printHorizontalCars(List<Vehicle> cars, String oppositeDirection, int offset) {
        if (!cars.isEmpty()) {
            StringBuilder output = new StringBuilder();
            int stop = Math.min(cars.size(), MAX_CARS_ON_ROAD);  // Limit displayed cars
            
            if (oppositeDirection.equals("east")) {
                // Westbound vehicles (moving right, shown as '>')
                output.append(" ".repeat(offset - 2 * stop));
                for (int i = 0; i < stop; i++) {
                    output.append(" >");
                }
                output.append("\n");
            } 
            else if (oppositeDirection.equals("west")) {
                // Eastbound vehicles (moving left, shown as '<')
                output.append(" ".repeat(offset));
                for (int i = 0; i < stop; i++) {
                    output.append("< ");
                }
                output.append("\n");
            }
            System.out.print(output.toString());
        } else {
            System.out.println();  // Empty line for empty road
        }
    }

   
    private String getLightSymbol(LIGHT light) {
        switch (light) {
            case RED -> {
                return "R";
            }
            case YELLOW -> { 
                return "Y";
            }
            case GREEN -> {
                return "G";
            }
            default -> {
                System.out.println("Unknown light: " + light); 
                return "?";  // Unknown state
            }
        }
    }
}