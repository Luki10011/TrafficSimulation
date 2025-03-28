package com.example;

public class Main {
    public static void main(String[] args) {
        if (args.length == 2){
            String inputFile = "input.json";//args[0];
            String outputFIle = "output.json";//args[1];
            if (isJsonFile(inputFile) && isJsonFile(outputFIle)){
                Simulation simulation = new Simulation();
                System.out.println("\n");
                simulation.performSimulationFromJSON(inputFile, outputFIle);
            } else{
                throw new AssertionError("Given files names don't have right extension!");
            }
        } else {
            throw new AssertionError("It looks like you haven't given the input and output file name .");
        }
        
    }

    private void greetUserPositive(){
        System.out.println("Welcome to AVS (Adaptive Vehicle System) Simulation!");
        System.out.println("");
        System.out.println("Here are the files names you have given:");
    }

    private void greetUserNegative(){
        System.out.println("Welcome to AVS (Adaptive Vehicle System) Simulation!");
        System.out.println("");
        System.out.println("Here are the files names you have given:");
    }

    public static boolean isJsonFile(String fileName) {
        return fileName.toLowerCase().endsWith(".json");
    }
}