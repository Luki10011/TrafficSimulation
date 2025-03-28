package com.example;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

public class Simulation {

    private final Intersection intersection;
    private final IntersectionVisualizer iVisualizer;
    private final List<Vehicle> allVehicles;
    
    public Simulation() {
        this.intersection = new Intersection();
        this.allVehicles = new ArrayList<>();
        this.iVisualizer = new IntersectionVisualizer(intersection);
    }

    private static String getStringFromFile(String filePath) {
        File file = new File(filePath);
        StringBuilder data = new StringBuilder();

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                data.append(scanner.nextLine()).append("\n");
            }
        } catch (FileNotFoundException e) {
            System.out.println("Couldn't find file: " + e.getMessage());
        }

        return data.toString();
    }

    public void performSimulationFromJSON(String inputFile, String outputFile) {
        String JSONString = getStringFromFile(inputFile);   // Input json
        JSONObject jsonObject = new JSONObject(JSONString);
        JSONArray commands = jsonObject.getJSONArray("commands");
        
        
        JSONArray stepStatuses = new JSONArray(); // Output json
        
        for (int i = 0; i < commands.length(); i++) {
            JSONObject command = commands.getJSONObject(i);
            String type = command.getString("type");

            // Keep track of current command, it can be usefull during debugging
            System.out.print("Command: " + type + "\t" + (i+1) +"/" + commands.length() + "\t".repeat(3));

            switch (type) {
                case "addVehicle" -> {
                    // Getting data from json
                    String vehicleId = command.getString("vehicleId");
                    String startRoad = command.getString("startRoad");
                    String endRoad = command.getString("endRoad");
                    // Check if there is a car that already has same id
                    for (Vehicle car : allVehicles) {
                        if (car.getVehicleId().equals(vehicleId)) {
                            throw new AssertionError("Car of this ID is already in simulation!");
                        }
                    }
                    Vehicle car = new Vehicle(vehicleId, startRoad, endRoad);
                    allVehicles.add(car);
                    intersection.addVehicle(car);
                }
                case "step" -> {
                    intersection.step();
                    
                    // For each step save left vehicles
                    JSONObject stepStatus = new JSONObject();
                    JSONArray leftVehicles = new JSONArray();
                    
                    for (String vehicle : intersection.getLeftVehicles()) {
                        leftVehicles.put(vehicle);
                    }
                    
                    stepStatus.put("leftVehicles", leftVehicles);
                    stepStatuses.put(stepStatus);
                }
                default -> throw new AssertionError("Unknown command, please check input file!");
            }
            
            iVisualizer.printIntersection();
            waitForEnter();
        }
        
        
        
        // Zapisujemy cały wynik do pliku
        saveDataToJSONFile(stepStatuses, outputFile);
        
    }

    public static void saveDataToJSONFile(JSONArray outputJSON, String outputFile){
        JSONObject output = new JSONObject();
        output.put("stepStatuses", outputJSON);
        
        try (FileWriter file = new FileWriter(outputFile)) {
            file.write(output.toString(2)); 
            System.out.println("Successfully saved simulation results to: " + outputFile);
        } catch (IOException e) {
            System.err.println("Error while saving to file: " + e.getMessage());
        }

    }
    
    public static void waitForEnter() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please press enter to continue...");
        scanner.nextLine();
        clearConsole();
    }

    public static void clearConsole() {
        String os = System.getProperty("os.name").toLowerCase();
        ProcessBuilder processBuilder;
        
        try {
            if (os.contains("win")) {
                // Windows:
                processBuilder = new ProcessBuilder("cmd", "/c", "cls");
            } else {
                // Linux/macOS
                processBuilder = new ProcessBuilder("clear");
            }
            processBuilder.inheritIO().start().waitFor(); // Wykonanie komendy i oczekiwanie na zakończenie
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public List<Vehicle> getAllVehicles() {
        return allVehicles;
    }

    public Intersection getIntersection() {
        return intersection;
    }

    
}