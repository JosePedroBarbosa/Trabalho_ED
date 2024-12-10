package game.menus;

import dataStructures.implementations.ArrayOrderedList;
import dataStructures.implementations.ArrayUnorderedList;
import game.data.ExportData;
import game.data.ImportData;
import game.data.SimulationResults;
import game.exceptions.NoMissionInstantiated;
import game.mission.AutoSimulation;
import game.mission.Mission;
import game.mission.ManualSimulation;
import game.settings.GameSettings;

import java.io.File;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * The Menus class handles the user interface for the game, providing menus for navigation,
 * settings management, and simulation operations.
 */
public class Menus {
    /**
     * Displays the welcome menu, imports game data, and transitions to the mission menu.
     */
    public static void welcomeMenu() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("================");
        System.out.println("  WELCOME BACK  ");
        System.out.println("================");
        System.out.print("\nPRESS ENTER...");

        scanner.nextLine();
        System.out.println();

        ImportData importer = new ImportData();

        System.out.println("================ \n");
        System.out.println("Importing All Data...");
        importer.importGameSettingsData();
        importer.importCurrentMissionData();
        System.out.println("\n================ \n");
        missionMenu(scanner);
    }

    /**
     * Displays the mission menu, allowing the user to start simulations, change settings,
     * or view manual simulation results.
     *
     * @param scanner the scanner instance for user input
     */
    public static void missionMenu(Scanner scanner) {
        boolean running = true;

        while (running) {
            System.out.println("===== IMF Simulator =====");
            System.out.println("1. Start Simulation");
            System.out.println("2. Settings");
            System.out.println("3. Manual Simulation Results");
            System.out.println("0. Exit");
            System.out.print("Choose an option: ");

            try{
                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1:
                        try{
                            Mission mission = Mission.getInstance();
                            startSimulationMenu(scanner, mission);
                        }catch(NoMissionInstantiated e){
                            System.out.println("No mission has been imported yet.");
                            System.out.println("Please go to 'Game Settings' and select 'Import Mission'.");
                        }
                        break;
                    case 2:
                        manageSettingsMenu(scanner);
                        break;
                    case 3:
                        manageManualSimulationsMenu(scanner);
                        break;
                    case 0:
                        running = false;
                        System.out.println("Exiting IMF Simulator.");
                        break;
                    default:
                        System.out.println("Invalid option. Please try again.");
                }
                System.out.println();

            }catch(InputMismatchException ex){
                System.out.println("Invalid option. Please try again.");
                scanner.nextLine();
            }

        }
        scanner.close();
    }

    /**
     * Displays the simulation menu, providing options for manual or automatic simulations.
     *
     * @param scanner the scanner instance for user input
     * @param mission the instantiated mission object for simulation
     */
    private static void startSimulationMenu(Scanner scanner, Mission mission) {
        int choice = -1;

        while(choice != 0) {
            System.out.println("===== Simulation Menu =====");
            System.out.println("1. Manual Simulation");
            System.out.println("2. Automatic Simulation");
            System.out.println("0. Back");
            System.out.print("Choose an option: ");

            try{
                choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1:
                        System.out.println("Starting Manual Simulation...");
                        ManualSimulation.manualSimulation(mission, scanner);
                        break;
                    case 2:
                        System.out.println("Starting Automatic Simulation...");
                        AutoSimulation.autoSimulation(mission);
                        break;
                    case 0:
                        break;
                    default:
                        System.out.println("Invalid option. Returning to Main Menu...");
                }

            }catch(InputMismatchException | NoMissionInstantiated ex){
                System.out.println("Invalid option. Please try again.");
                scanner.nextLine();
            }

        }
    }

    /**
     * Manages game settings, allowing the user to adjust various parameters.
     *
     * @param scanner the scanner instance for user input
     */
    public static void manageSettingsMenu(Scanner scanner) {
        int choice = -1;

        while(choice != 0) {
            System.out.println("===== Settings =====");
            System.out.println("1. Game Settings");
            System.out.println("2. Import Mission");
            System.out.println("0. Back");
            System.out.print("Choose an option: ");

            try{
                choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1:
                        manageGameSettingsMenu(scanner);
                        break;
                    case 2:
                        importNewMission(scanner);
                    case 0:
                        break;
                    default:
                        System.out.println("Invalid option. Returning to Main Menu...");
                }

            }catch(InputMismatchException ex){
                System.out.println("Invalid option. Please try again.");
                scanner.nextLine();
            }

        }
    }

    /**
     * Displays and manages game settings options, including capacity and power adjustments.
     *
     * @param scanner the scanner instance for user input
     */
    private static void manageGameSettingsMenu(Scanner scanner) {

        int choice = -1;
        int modifications = 0;

        while(choice != 0) {
            System.out.println("===== Game Settings =====");
            System.out.println("1. Change Backpack Capacity");
            System.out.println("2. Change Max Enemy Moves (1-2)");
            System.out.println("3. Change The Cruz Power");
            System.out.println("4. Save Changes To JSON");
            System.out.println("0. Back");
            System.out.print("Choose an option: ");

            try{
                choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1:
                        if(changeBackpackCapacity(scanner)){
                            modifications++;
                        }
                        break;
                    case 2:
                        if(changeEmemyMoves(scanner)){
                            modifications++;
                        }
                        break;
                    case 3:
                        if(changePlayerPower(scanner)){
                            modifications++;
                        }
                        break;
                    case 4:
                        ExportData.saveGameSettingsToJson();
                        modifications = 0;
                        break;
                    case 0:
                        if(modifications != 0) {
                            saveGameSettingsToJsonMenu(scanner);
                        }
                        break;
                    default:
                        System.out.println("Invalid option. Please try again.");
                }

            }catch(InputMismatchException ex){
                System.out.println("Invalid option. Please try again.");
                scanner.nextLine();
            }

        }
    }

    /**
     * Manages manual simulation results, allowing the user to view previously imported simulations.
     *
     * @param scanner the scanner instance for user input
     */
    public static void manageManualSimulationsMenu(Scanner scanner) {
        int choice = -1;

        ArrayUnorderedList<String> importedCodes = ImportData.importSimulationCodesResults();

        while(choice != 0) {
            System.out.println("\n===== Available Manual Simulations =====");

            if(importedCodes.isEmpty()){
                System.out.println("No simulations available.");
            }else {
                int codesCounter = 1;
                for(String code : importedCodes) {
                    if(code != null){
                        System.out.println(codesCounter + ". " + code);
                        codesCounter++;
                    }
                }
            }

            System.out.println("0. Back");
            System.out.print("Choose an option: ");

            try{
                choice = scanner.nextInt();
                scanner.nextLine();

                if (choice == 0) {
                    System.out.println("Returning to Main Menu...");
                } else if (choice > 0 && choice <= importedCodes.size()) {
                    String selectedCode = importedCodes.getByIndex(choice - 1);
                    System.out.println("You selected the simulation with code: " + selectedCode);
                    ArrayOrderedList<SimulationResults> simulationResults = ImportData.importSimulationResultsByCode(selectedCode);

                    System.out.println(simulationResults.toString());
                } else {
                    System.out.println("Invalid option. Please try again.");
                }

            }catch(InputMismatchException ex){
                System.out.println("Invalid option. Please try again.");
                scanner.nextLine();
            }

        }
    }

    /**
     * Changes the backpack capacity in game settings.
     *
     * @param scanner the scanner instance for user input
     * @return true if the change was successful, false otherwise
     */
    private static boolean changeBackpackCapacity(Scanner scanner) {
        System.out.print("Insert Backpack Capacity: ");

        try{
            int backpackCapacity = scanner.nextInt();
            scanner.nextLine();

            if(backpackCapacity > 0){
                GameSettings.setMaxBackpackCapacity(backpackCapacity);
                System.out.println("\nBackpack Capacity Changed To: " + backpackCapacity);
                return true;
            }else{
                System.out.println("Invalid Capacity.");
                return false;
            }
        }catch(InputMismatchException ex){
            System.out.println("Invalid Capacity.");
            scanner.nextLine();
            return false;
        }
    }

    /**
     * Changes the maximum enemy moves in game settings.
     *
     * @param scanner the scanner instance for user input
     * @return true if the change was successful, false otherwise
     */
    private static boolean changeEmemyMoves(Scanner scanner) {
        System.out.print("Insert Max Enemy Moves (1 - 2): ");

        try{
            int maxMoves = scanner.nextInt();
            scanner.nextLine();

            if(maxMoves >= 1 && maxMoves <= 2){
                GameSettings.setMaxEnemyMoves(maxMoves);
                System.out.println("\nEnemy Moves Changed To: " + maxMoves);
                return true;
            }else{
                System.out.println("Invalid Number Of Moves.");
                return false;
            }
        }catch (InputMismatchException ex){
            System.out.println("Invalid Number Of Moves.");
            scanner.nextLine();
            return false;
        }
    }

    /**
     * Changes the player's power in game settings.
     *
     * @param scanner the scanner instance for user input
     * @return true if the change was successful, false otherwise
     */
    private static boolean changePlayerPower(Scanner scanner) {
        System.out.print("Insert To Cruz Power (1-100): ");

        try{
            int power = scanner.nextInt();
            scanner.nextLine();

            if(power >= 1 && power <= 100){
                GameSettings.setPlayerPower(power);
                System.out.println("\nPlayer Power Changed To: " + power);
                return true;
            }else{
                System.out.println("Invalid Power.");
                return false;
            }
        } catch (RuntimeException e) {
            System.out.println("Invalid Power.");
            scanner.nextLine();
            return false;
        }
    }

    /**
     * Prompts the user to save game settings modifications to JSON before exiting.
     *
     * @param scanner the scanner instance for user input
     */
    private static void saveGameSettingsToJsonMenu(Scanner scanner) {
        System.out.println("Modifications not saved in JSON. Do you want to save them before exiting? (Y/N)");

        while(true) {
            System.out.print("> ");
            String option = scanner.nextLine();

            if (option.equals("Y") || option.equals("y")) {
                ExportData.saveGameSettingsToJson();
                System.out.println("Modifications saved.");
                break;
            } else if (option.equals("N") || option.equals("n")) {
                System.out.println("Exiting without saving modifications.");
                break;
            } else {
                System.out.println("Invalid input. Please enter 'Y' for Yes or 'N' for No.");
            }
        }
    }

    /**
     * Imports a new mission from a specified file.
     *
     * @param scanner the scanner instance for user input
     */
    private static void importNewMission(Scanner scanner) {
        System.out.println("===== Import New Mission =====");
        System.out.println("Select The Mission Name:");

        String directoryPath = ".\\src\\missions";

        printAvailableMissions(directoryPath);

        String missionName = scanner.nextLine();
        String missionPath = directoryPath + "\\" + missionName + ".json";

        ImportData importer = new ImportData();
        importer.importMissionData(missionPath);
    }

    /**
     * Prints the available mission files in the specified directory.
     *
     * @param directoryPath the path to the directory containing mission files
     */
    private static void printAvailableMissions(String directoryPath) {
        File directory = new File(directoryPath);

        if (directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isFile() && file.getName().toLowerCase().endsWith(".json")) {
                        System.out.println("    " + file.getName());
                    }
                }
            } else {
                System.out.println("The folder is empty, or an error occurred while listing the files.");
            }
        } else {
            System.out.println("The path provided is not a valid folder.");
        }
    }
}