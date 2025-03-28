package com.example;

import java.util.List;
import java.util.Map;

import com.example.TrafficLight.LIGHT;

public class IntersectionVisualizer {
    private static Intersection intersection;
    private static final int MAX_CARS_ON_ROAD = 4;
    private static final int DEFAULT_OFFSET_HORIZONTAL = MAX_CARS_ON_ROAD + 3;
    private static final int DEFAULT_OFFSET_VERTICAL = MAX_CARS_ON_ROAD;

    public IntersectionVisualizer(Intersection intersection_){
        IntersectionVisualizer.intersection = intersection_;
    }

    public void printIntersection() {
        Map<String, Road> roads = intersection.getRoads();
        String northLight = getLightSymbol(roads.get("north").getTrafficLight().getLightStatus());
        String southLight = getLightSymbol(roads.get("south").getTrafficLight().getLightStatus());
        String eastLight = getLightSymbol(roads.get("east").getTrafficLight().getLightStatus());
        String westLight = getLightSymbol(roads.get("west").getTrafficLight().getLightStatus());

        List<Vehicle> northCars = roads.get("north").getVehiclesOnRoad();
        List<Vehicle> southCars = roads.get("south").getVehiclesOnRoad();
        List<Vehicle> eastCars = roads.get("east").getVehiclesOnRoad();
        List<Vehicle> westCars = roads.get("west").getVehiclesOnRoad();
        
        int horizontalOffset = 0;
        for (Vehicle westCar : westCars) {
            horizontalOffset  += 1;
        }
        horizontalOffset += westCars.size() + DEFAULT_OFFSET_HORIZONTAL;
        int intersectionOffset = 6;
        int roadWidth = intersectionOffset/2;

        //Informacja o pozostalym czasie zielonego swiatla
        System.out.println("Green light time left: "+ (intersection.getCurrentGreenDuration() - intersection.getTimeInCurrentState()) + "s");
        
        //Wyrysuj polnocne samochody
        printVerticalCars(northCars, horizontalOffset + roadWidth, "south");
        //Wyrysuj polnocne swiatlo
        System.out.println(" ".repeat(horizontalOffset + roadWidth) +  "|  [" + northLight + "]  |" + " ".repeat(horizontalOffset+ roadWidth));
        System.out.println("=".repeat(horizontalOffset + roadWidth) +  "         " + "=".repeat(horizontalOffset+ roadWidth));

        //przygotowanie pod wysweitlenie samochodow ze wschodu
        
        //wyswietlenie swiatel ze wschodu, zachodu i samochodow
        printHorizontalCars(eastCars, "west", horizontalOffset + 2*intersectionOffset + roadWidth);
        System.out.println("-".repeat(horizontalOffset + 1) +"[" + westLight +"]       [" + eastLight + "]" + "-".repeat(horizontalOffset + 1));
        printHorizontalCars(westCars, "east", horizontalOffset);
        System.out.println("=".repeat(horizontalOffset + roadWidth) +  "         " + "=".repeat(horizontalOffset+ roadWidth));

        //wyswietlenie samochodow z poludnia
        System.out.println(" ".repeat(horizontalOffset + roadWidth) +  "|  [" + southLight + "]  |");
        printVerticalCars(southCars, horizontalOffset + roadWidth, "north");
        System.out.println();
        
    }

    public void printVerticalCars(List<Vehicle> cars, int offset, String oppositeDirection){
        if (!cars.isEmpty()){
            StringBuilder output = new StringBuilder();
            int stop = cars.size() > MAX_CARS_ON_ROAD ?  MAX_CARS_ON_ROAD : cars.size();
            if (oppositeDirection.equals("south")){
                String s1 = (" ".repeat(offset) +  "|   |   |\n").repeat(DEFAULT_OFFSET_VERTICAL - stop);
                output.append(s1);
                for (int i = 0; i < stop ; i++){
                    String s = " ".repeat(offset);
                    output.append(s).append("| v |   |").append("\n");
                }
            } else if (oppositeDirection.equals("north")){
                for (int i = 0; i < stop ; i++){
                    String s = " ".repeat(offset);
                    output.append(s).append("| ^ |   |").append("\n");
                }
                String s1 = (" ".repeat(offset) +  "|   |   |\n").repeat(DEFAULT_OFFSET_VERTICAL - stop);
                output.append(s1);
            }
            System.out.print(output.toString());
        }else{
            StringBuilder output = new StringBuilder();
            String s1 = (" ".repeat(offset) +  "|   |   |\n").repeat(DEFAULT_OFFSET_VERTICAL);
            output.append(s1);
            
            System.out.print(output.toString());
        }
        
    }

    public void printHorizontalCars(List<Vehicle> cars, String oppositeDirection, int offset){
        if (!cars.isEmpty()){
            StringBuilder output = new StringBuilder();
            int stop = cars.size() > MAX_CARS_ON_ROAD ?  MAX_CARS_ON_ROAD : cars.size();
            if (oppositeDirection.equals("east")){
                output.append(" ".repeat(offset- 2 * stop));
                for (int i = 0; i < stop ; i++){
                    output.append(" >");
                }
                output.append("\n");
            } else if (oppositeDirection.equals("west")){
                output.append(" ".repeat(offset));
                for (int i = 0; i < stop ; i++){
                    output.append("<").append(" ");
                }
                output.append("\n");
            }
            System.out.print(output.toString());
        }else{
            System.out.println(); 
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
                System.out.println("Unknown light: " + light); // Sprawdź, co się dzieje
                return "?";
            }
        }
    }
    
}
