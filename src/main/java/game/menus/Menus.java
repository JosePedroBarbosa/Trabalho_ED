package game.menus;

import game.data.ExportData;
import game.data.ImportData;
import game.exceptions.NoMissionInstantiated;
import game.mission.Mission;
import game.mission.Simulation;
import game.settings.GameSettings;

import java.io.File;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Menus {

    public static void welcomeMenu() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("================");
        System.out.println("  WELCOME BACK  ");
        System.out.println("================");
        System.out.println("\nPRESS ENTER...");

        scanner.nextLine();

        ImportData importer = new ImportData();

        System.out.println("Importing All Data...");
        importer.importGameSettingsData();
        importer.importDefaultMissionData();
        System.out.println("All data has been successfully imported.");

        missionMenu(scanner);
    }

    public static void missionMenu(Scanner scanner) {

        boolean running = true;
        while (running) {
            System.out.println("===== IMF Simulator =====");
            System.out.println("1. Start Simulation");
            System.out.println("2. Settings");
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
                        // Lógica para simulação manual
                        Simulation.manualSimulation(mission, scanner);
                        break;
                    case 2:
                        System.out.println("Starting Automatic Simulation...");
                        // Lógica para simulação automática
                        break;
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