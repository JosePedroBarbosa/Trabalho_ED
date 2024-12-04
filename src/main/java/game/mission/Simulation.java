package game.mission;

import dataStructures.implementations.ArrayUnorderedList;
import game.character.Enemy;
import game.character.Entity;
import game.character.Player;
import game.data.ExportData;
import game.data.MissionDisplay;
import game.exceptions.EmptyBackPackException;
import game.exceptions.PlayerLeftException;
import game.items.HealthKit;
import game.items.Item;
import game.map.Room;
import game.settings.GameSettings;

import java.util.Iterator;
import java.util.Random;
import java.util.Scanner;

public class Simulation {
    private static boolean missionSuccess;
    private static ArrayUnorderedList<Room> simulationRoute;

    public static void manualSimulation(Mission mission, Scanner scanner) {
        missionSuccess = false;
        simulationRoute = new ArrayUnorderedList<>();

        MissionDisplay.showImage(scanner);
        setupInitialPhase(mission, scanner);

        printBestPathToTarget(mission);
        printBestPathToHealthKit(mission);
        printMissionStatus(mission);

        boolean gameOver = false;

        while (!gameOver) {
            gameOver = playerTurn(mission, scanner);

            //show the state of the mission
            printBestPathToTarget(mission);
            printBestPathToHealthKit(mission);
            printMissionStatus(mission);

            if (!(mission.getPlayer().isAlive())) {
                System.out.println("To Cruz died. Mission failed!");
                missionSuccess = false;
                gameOver = true;
            }
        }
        ExportData.saveManualSimulationToJson(mission, simulationRoute, missionSuccess);
    }

    public static void autoSimulation(Mission mission, Scanner scanner){

    }

    public static void setupInitialPhase(Mission mission, Scanner scanner) {
        printMissionStatus(mission);

        showAvailableRooms(mission.getEntriesAndExits(), "Available entrances:");
        Room entrance = selectRoom(mission.getEntriesAndExits(), scanner, "Select an Entrance: ");
        mission.getPlayer().setCurrentRoom(entrance);

        //add room to the simulation Route
        simulationRoute.addToRear(entrance);

        if(entrance.hasEnemies()){
            System.out.println("Enemies in the room! Player as priority in confrontation.");
            confrontation(mission.getPlayer());

            // Only enemies that are not in the confrontation can move randomly
            enemiesRandomMove(mission, filterEnemiesNotInPlayerRoom(mission));
        }else {
            enemiesRandomMove(mission, mission.getEnemies());
        }
    }

    private static void showAvailableRooms(ArrayUnorderedList<Room> rooms, String message) {
        System.out.println(message);
        for (int i = 0; i < rooms.size(); i++) {
            System.out.println((i + 1) + ". " + rooms.getByIndex(i).getName());
        }
    }

    private static Room selectRoom(ArrayUnorderedList<Room> rooms, Scanner scanner, String message) {
        int selectedRoom = 0;
        boolean validSelection = false;

        while (!validSelection) {
            System.out.print(message + " (1-" + rooms.size() + "): ");
            if (scanner.hasNextInt()) {
                selectedRoom = scanner.nextInt();
                scanner.nextLine();

                if (selectedRoom >= 1 && selectedRoom <= rooms.size()) {
                    validSelection = true;
                } else {
                    System.out.println("Invalid selection. Please try again.");
                }
            }else {
                scanner.nextLine();
                System.out.println("Please enter a valid number between 1 and " + rooms.size());
            }
        }

        return rooms.getByIndex(selectedRoom - 1);
    }

    private static boolean playerTurn(Mission mission, Scanner scanner){
        boolean usedItem = false;
        if(mission.getPlayer().getBackpack().getBackpackSize() > 0){
            System.out.println("Current Health: " + mission.getPlayer().getLife());
            usedItem = shouldUseKit(scanner, mission, "Do you want to use a medic kit? (Y/N)");
        }

        if (!usedItem) {
            try {
                movePlayer(mission.getPlayer(), mission.getMissionMap().getMap().getNeighbours(mission.getPlayer().getCurrentRoom()), scanner);
            } catch (PlayerLeftException ex) {
                if(mission.getTarget().isPickedUp()) {
                    System.out.println("To Cruz left the building with the Target. Mission completed!");
                    missionSuccess = true;
                }else {
                    System.out.println("To Cruz left the building without the Target. Mission failed!");
                    missionSuccess = false;
                }
                return true;
            }
        }

        handleConfrontation(mission);
        //the player can only pick the target after the confrontation ends
        handleTarget(mission);
        return false;
    }

    private static boolean shouldUseKit(Scanner scanner, Mission mission, String message) {
        System.out.println(message);

        while(true) {
            System.out.print("> ");
            String option = scanner.nextLine();

            if (option.equals("Y") || option.equals("y")) {
                try{
                    mission.getPlayer().useItem();
                    return true;
                }catch (EmptyBackPackException ex){
                    System.out.println("Can´t use item.");;
                    return false;
                }
            } else if (option.equals("N") || option.equals("n")) {
                return false;
            } else {
                System.out.println("Invalid input. Please enter 'Y' for Yes or 'N' for No.");
            }
        }
    }

    private static void handleConfrontation(Mission mission) {
        if (mission.getPlayer().getCurrentRoom().hasEnemies()) {
            // Confrontation (player has priority)
            confrontation(mission.getPlayer());
            enemiesRandomMove(mission, filterEnemiesNotInPlayerRoom(mission));
        } else {
            // Enemy turn
            enemiesRandomMove(mission, mission.getEnemies());
        }
    }

    private static void movePlayer(Player player, ArrayUnorderedList<Room> neighbours, Scanner scanner) throws PlayerLeftException{
        if (player.getCurrentRoom().isEntranceAndExit()) {
            System.out.println("This room is an exit. Do you want to leave the building? (Y/N)");
            String option = scanner.nextLine();

            if (option.equals("Y") || option.equals("y")) {
                throw new PlayerLeftException();
            } else if (!(option.equals("N") || option.equals("n"))) {
                System.out.println("Invalid input. Please enter 'Y' for Yes or 'N' for No.");
            }
        }

        showAvailableRooms(neighbours, "\nSelect a room to move:");
        Room selectedNeighbour = selectRoom(neighbours, scanner, "Select a room to move");
        player.getCurrentRoom().removePlayer();
        player.setCurrentRoom(selectedNeighbour);
        player.getCurrentRoom().setPlayer(player);

        //add room to the simulation Route
        simulationRoute.addToRear(selectedNeighbour);

        //if the room have any items the player should use the shield and try to stack the kits.
        collectItems(player, selectedNeighbour);
    }

    public static void enemiesRandomMove(Mission mission, ArrayUnorderedList<Enemy> enemies){
        Random rand = new Random();
        int maxEnemyMoves = GameSettings.getMaxEnemyMoves();

        for (Enemy enemy : enemies) {
            if(enemy != null && enemy.isAlive()){
                Room currentRoom = enemy.getCurrentRoom();
                ArrayUnorderedList<Room> neighbours = mission.getMissionMap().getMap().getNeighbours(currentRoom);

                //random between 1 and maxEnemyMoves
                int moves = rand.nextInt(maxEnemyMoves) + 1;

                for (int i = 0; i < moves; i++) {
                    if (neighbours.isEmpty()) {
                        break;
                    }

                    Room nextRoom = neighbours.getByIndex(rand.nextInt(neighbours.size()));
                    enemy.getCurrentRoom().removeEnemy(enemy);
                    enemy.setCurrentRoom(nextRoom);
                    enemy.getCurrentRoom().addEnemy(enemy);

                    //if the nextRoom have the Player the enemy has priority to attack
                    if(nextRoom.hasPlayer()){
                        System.out.println("Enemy " + enemy.getName() + " entered the player's room!");
                        confrontation(enemy);
                        break;
                    }

                    neighbours = mission.getMissionMap().getMap().getNeighbours(nextRoom);
                }
            }
        }
    }

    public static void confrontation(Entity priorityEntity) {
        if (priorityEntity == null) {
            throw new IllegalArgumentException("Priority entity cannot be null.");
        }

        boolean combatOngoing = true;
        while (combatOngoing) {
            if(priorityEntity instanceof Player) {
                Player player = (Player) priorityEntity;

                System.out.println(player.getName() + " is attacking the enemies in the room!");
                player.attack();

                for(Enemy enemy : player.getCurrentRoom().getEnemies()) {
                    if(enemy != null && enemy.isAlive()) {
                        enemy.attack();
                    }
                }

                // Check if the player is still alive
                if (!player.isAlive()) {
                    System.out.println("The Player: " + player.getName() + " died. Game over!");
                    combatOngoing = false;
                }
            } else if (priorityEntity instanceof Enemy) {
                Enemy enemy = (Enemy) priorityEntity;
                Player player = enemy.getCurrentRoom().getPlayer();

                if(player != null && player.isAlive()) {
                    System.out.println("Enemy " + enemy.getName() + " is attacking the player in the room!");
                    enemy.attack();

                    if(player.isAlive()) {
                        System.out.println(player.getName() + " is counter-attacking the " + enemy.getName() + "!");
                        player.attack(enemy);
                    }
                }

                // Check if the enemy is still alive
                if (!enemy.isAlive()) {
                    combatOngoing = false;
                }
            }

            // Check if all enemies in the room are dead
            if (priorityEntity instanceof Player) {
                Player player = (Player) priorityEntity;
                if (player.getCurrentRoom().getEnemies().isEmpty()) {
                    combatOngoing = false;
                    System.out.println("All enemies in the room are dead.");
                }
            }
        }

    }

    private static void handleTarget(Mission mission) {
        if (mission.getPlayer().getCurrentRoom().equals(mission.getTarget().getCurrentRoom())) {
            mission.getTarget().setPickedUp(true);
            System.out.println("Target secured successfully!");
        }
    }

    private static void collectItems(Player player, Room room) {
        for (Item item : room.getItems()) {
            if(item != null){
                player.collectItem(item);
            }
        }
    }

    private static ArrayUnorderedList<Enemy> filterEnemiesNotInPlayerRoom(Mission mission) {
        ArrayUnorderedList<Enemy> filteredEnemies = new ArrayUnorderedList<>();
        for (Enemy enemy : mission.getEnemies()) {
            if (enemy != null && !enemy.getCurrentRoom().equals(mission.getPlayer().getCurrentRoom())) {
                filteredEnemies.addToRear(enemy);
            }
        }
        return filteredEnemies;
    }

    private static void printBestPathToTarget(Mission mission){
        Iterator it = mission.getMissionMap().getMap().iteratorShortestPath(mission.getPlayer().getCurrentRoom(), mission.getTarget().getCurrentRoom());

        System.out.println("Best path to the target:");
        while(it.hasNext()){
            Object current = it.next();
            if(current != null){
                System.out.println("-> " + current.toString());
            }
        }
        System.out.println();
    }

    private static void printBestPathToHealthKit(Mission mission) {
        ArrayUnorderedList<Item> missionItems = mission.getItems();
        HealthKit targetHealthKit = null;
        int shortestDistance = Integer.MAX_VALUE;
        Room targetRoom = null;

        for (Item item : missionItems) {
            if (item != null && !item.isPickedUp()){
                if(item instanceof HealthKit){
                    HealthKit healthKit = (HealthKit) item;
                    Room healthKitRoom = healthKit.getCurrentRoom();

                    Iterator it = mission.getMissionMap().getMap().iteratorShortestPath(mission.getPlayer().getCurrentRoom(), healthKitRoom);

                    int pathLength = 0;
                    while (it.hasNext()) {
                        Object current = it.next();
                        if (current != null) {
                            pathLength++;
                        }
                    }

                    if (pathLength > 0 && pathLength < shortestDistance) {
                        shortestDistance = pathLength;
                        targetHealthKit = healthKit;
                        targetRoom = healthKitRoom;
                    }
                }
            }
        }

        if (targetHealthKit != null) {
            Iterator it = mission.getMissionMap().getMap().iteratorShortestPath(mission.getPlayer().getCurrentRoom(), targetRoom);

            System.out.println("Best path to the health kit:");
            while (it.hasNext()) {
                Object current = it.next();
                if (current != null) {
                    System.out.println("-> " + current.toString());
                }
            }
        } else {
            System.out.println("No health kit found in the mission.");
        }

        System.out.println();
    }


    private static void printMissionStatus(Mission mission) {
        System.out.println("=== MISSION DETAILS ===");
        System.out.println("Current Location: " + (mission.getPlayer().getCurrentRoom() != null ? mission.getPlayer().getCurrentRoom().getName() : "Outside of the building"));
        System.out.println("Player Health: " + mission.getPlayer().getLife());
        if(mission.getTarget().isPickedUp()){
            System.out.println("Target Secured!");
        }else{
            System.out.println("Target: ");
            System.out.println("    Type: " + mission.getTarget().getType());
            System.out.println("    Location: " + mission.getTarget().getCurrentRoom().getName());
        }

        ArrayUnorderedList<Enemy> enemies = mission.getEnemies();
        if(enemies.isEmpty()){
            System.out.println("All Enemies Annihilated");
        }else{
            System.out.println("Remaining Enemies:");
            for (Enemy enemy : enemies) {
                if (enemy.getLife() > 0) {
                    System.out.println("  - Name: " + enemy.getName() + ", Current Room: " + enemy.getCurrentRoom().getName() + ", Health: " + enemy.getLife());
                }
            }
        }

        ArrayUnorderedList<Item> items = mission.getItems();
        if(items.isEmpty()){
            System.out.println("All Items Picked");
        }else{
            System.out.println("Items in the building: ");
            for(Item item : mission.getItems()) {
                if(!item.isPickedUp()) {
                    System.out.println("  - Name: " + item.getType() + ", Current Room: " + item.getCurrentRoom().getName() + ", Given Health: " + item.getGivenPoints());
                }
            }
        }

        System.out.println("=============================");
    }
}