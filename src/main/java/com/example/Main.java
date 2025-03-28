package com.example;

import java.util.Scanner;

public class Main {

    /**
         * Performing simulation of traffic lights from given input file.
         * The output is saved into given output file name.
         * 
         * @param args[0]: input file
         * @param args[1]: ouput file
    */
    public static void main(String[] args) {
        if (args.length == 2){
            String inputFile = args[0];
            String outputFIle = args[1];

            // Checking if given files have .json extension
            if (isJsonFile(inputFile) && isJsonFile(outputFIle)){
                greetUser();
                Simulation simulation = new Simulation();
                System.out.println("\n");
                System.out.println("\n");
                simulation.performSimulationFromJSON(inputFile, outputFIle);
            } else{
                throw new AssertionError("Given files names don't have json extension!");
            }
        } else {
            throw new AssertionError("Argument parsing error. Please check given inputs!");
        }
        
    }

    public static void greetUser(){
        System.out.println("Welcome to AVS (Adaptive Vehicle System) Simulation\n");
        System.out.println("Here are some informations you need to know:");
        System.out.println("- you will be acknowledged about current command and remaining green light time");
        System.out.println("- only step command will trigger time counter");
        System.out.println("- for each command you will be shown current intersection state\n");

        System.out.print("Press enter to continue: ");
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
    }

    public static boolean isJsonFile(String fileName) {
        return fileName.toLowerCase().endsWith(".json");
    }
}