package game.mission;

import dataStructures.implementations.ArrayUnorderedList;
import game.character.Enemy;
import game.character.Entity;
import game.character.Player;
import game.data.ExportData;
import game.data.MissionDisplay;
import game.exceptions.EmptyBackPackException;
import game.items.HealthKit;
import game.items.Item;
import game.map.Room;
import game.settings.GameSettings;

import java.util.InputMismatchException;
import java.util.Iterator;
import java.util.Random;
import java.util.Scanner;

public class Simulation {
    private static boolean missionSuccess;
    private static ArrayUnorderedList<Room> simulationRoute;

    public static void manualSimulation(Mission mission, Scanner scanner){
        boolean gameOver = false;
        simulationRoute = new ArrayUnorderedList<>();

        while(!gameOver){
            gameOver = playerTurn(mission, scanner);

            if(!mission.getPlayer().isAlive()){
                System.out.println("Mission Failed, The Player Died");
                missionSuccess = false;
                gameOver = true;
            }
        }

        ExportData.saveManualSimulationToJson(mission, simulationRoute, missionSuccess);
    }

    /**
     * print the rooms
     * @param rooms the rooms to print
     * @param message the message to show
     */
    public static void printRooms(ArrayUnorderedList<Room> rooms, String message){
        System.out.println(message);
        for(int i = 0; i < rooms.size(); i++){
            System.out.println("   " + (i + 1) + ". " + rooms.getByIndex(i));
        }
        System.out.println();
    }

    /**
     * Returns the user choice of a room
     * @param rooms the available rooms
     * @param message The message to print
     * @param scanner the scanner
     * @return the selected room
     */
    public static Room selectRoom(ArrayUnorderedList<Room> rooms, String message, Scanner scanner){
        printRooms(rooms, message);

        while(true){
            try{
                System.out.print("Select a room: ");
                int choice = scanner.nextInt();
                scanner.nextLine();

                if(choice > 0 && choice <= rooms.size()){
                    return rooms.getByIndex(choice - 1);
                }else{
                    System.out.println("Invalid choice, Try again.");
                }
            }catch(InputMismatchException ex){
                scanner.nextLine();
                System.out.println("Invalid input, Try again.");
            }
        }

    }

    /**
     * Moves the player to a specified room.
     *
     * @param mission the mission
     * @param room the target room to move the player to
     * @throws IllegalArgumentException if the mission or target room is null
     */
    public static void movePlayerToRoom(Mission mission, Room room){
        if (mission == null || room == null) {
            throw new IllegalArgumentException("Mission or room cannot be null.");
        }

        Player player = mission.getPlayer();
        Room currentRoom = player.getCurrentRoom();

        if (currentRoom != null) {
            currentRoom.removePlayer();
        }
        player.setCurrentRoom(room);
        player.getCurrentRoom().setPlayer(player);

        System.out.println("Player Moved To " + room.getName());
        simulationRoute.addToRear(room);

        for(Item item : player.getCurrentRoom().getItems()){
            if(item != null){
                player.collectItem(item);
            }
        }
    }

    public static void moveEnemyToRoom(Mission mission, Enemy enemy, Room room){
        if (mission == null || room == null) {
            throw new IllegalArgumentException("Mission and room cannot be null.");
        }

        Room currentRoom = enemy.getCurrentRoom();

        if (currentRoom != null) {
            enemy.getCurrentRoom().removeEnemy(enemy);
        }
        enemy.setCurrentRoom(room);
        enemy.getCurrentRoom().addEnemy(enemy);
    }

    /**
     *
     * @param mission
     * @param scanner
     * @return true if the game ends, false otherwise
     */
    public static boolean playerTurn(Mission mission, Scanner scanner){
        int choice = printOptions(mission, scanner);;

        switch(choice) {
            case 1:
                Room currentRoom = mission.getPlayer().getCurrentRoom();
                Room nextRoom;
                if(currentRoom == null){
                    nextRoom = selectRoom(mission.getEntriesAndExits(), "Available Rooms", scanner);
                }else {
                    nextRoom = selectRoom(mission.getMissionMap().getMap().getNeighbours(mission.getPlayer().getCurrentRoom()), "Available Rooms: ", scanner);
                }
                movePlayerToRoom(mission, nextRoom);

                if (mission.getTarget().getCurrentRoom().equals(nextRoom)) {
                    if (nextRoom.hasEnemies()) {
                        handleScenario5(mission);
                    } else {
                        handleScenario6(mission);
                    }
                }else{
                    if (!mission.getPlayer().getCurrentRoom().getEnemies().isEmpty()) {
                        handleScenario1(mission);
                    }else{
                        handleScenario2(mission);
                    }
                }
                break;
            case 2:
                handleScenario4(mission);
                break;
            case 3:
                printBestPathToHealthKit(mission);
                printBestPathToTarget(mission);
                if(mission.getTarget().isPickedUp()) {
                    //printBestPathToExit(mission);
                }
                break;
            case 4:
                printMissionStatus(mission);
                break;
            case 5:
                MissionDisplay.showImage(scanner);
                break;
            case 0:
                if (mission.getTarget().isPickedUp()) {
                    System.out.println("To Cruz left the building with the Target. Mission completed!");
                    missionSuccess = true;
                } else {
                    System.out.println("To Cruz left the building without the Target. Mission failed!");
                    missionSuccess = false;
                }
                return true;
            default:
                System.out.println("Invalid choice, Try again.");
                break;
        }

        return false;
    }

    /**
     * Handles Scenario 1: The player enters a room with enemies and initiates a confrontation.
     * - The player attacks all enemies in the room, dealing damage simultaneously.
     * - If enemies remain alive, they counter-attack the player and remain in the room,
     *   while other enemies in the building move randomly.
     *
     * @param mission the current mission containing the player and enemies.
     */
    public static void handleScenario1(Mission mission){
        System.out.println("The player encountered enemies in the room. Confrontation Started");
        confrontation(mission.getPlayer());
        enemiesRandomMove(mission, filterEnemiesNotInPlayerRoom(mission));
    }

    /**
     * Handles Scenario 2: The player enters a room without enemies.
     * - All Enemies Move Randomly
     *
     * @param mission the current mission containing the player and enemies.
     */
    public static void handleScenario2(Mission mission){
        enemiesRandomMove(mission, mission.getEnemies());
    }

    /**
     * Handles Scenario 3: The Enemy enters a room with the Player and initiates a confrontation.
     * - The Enemy attacks the player in the room, dealing damage.
     * - If the player remain alive, they counter-attack the enemy
     *
     * @param mission the current mission containing the player and enemies.
     * @param enemy the enemy that attacks the player
     */
    public static void handleScenario3(Mission mission, Enemy enemy){
        System.out.println("The enemy encountered the player in the room. Confrontation Started");
        confrontation(enemy);
        enemiesRandomMove(mission, filterEnemiesNotInPlayerRoom(mission));
    }

    /**
     * Handles Scenario 4: The Player uses a health kit.
     * - The Player ends is turn.
     * - All Enemies move Randomly
     * - If the Player does not have items to use the turn is canceled
     *
     * @param mission the current mission containing the player and enemies.
     */
    public static void handleScenario4(Mission mission) {
        try {
            mission.getPlayer().useItem();
            enemiesRandomMove(mission, mission.getEnemies());
        } catch (EmptyBackPackException e) {
            System.out.println("No Items In The BackPack");
        }
    }

    /**
     * Handles Scenario 5: The player enters a room with the target but found enemies in the room and initiates a confrontation.
     * - The player attacks all enemies in the room, dealing damage simultaneously.
     * - If enemies remain alive, they counter-attack the player and remain in the room,
     *   while other enemies in the building move randomly.
     * - When the confrontation ends if the player reamin alive he secure the target
     *
     * @param mission the current mission containing the player and enemies.
     */
    private static void handleScenario5(Mission mission) {
        System.out.println("You found the target but there are enemies in the room. Confrontation Started.");
        confrontation(mission.getPlayer());
        handleTarget(mission);
        enemiesRandomMove(mission, mission.getEnemies());
    }

    /**
     * Handles Scenario 6: The player enters a room with the target and are not enemies in the room.
     * - The player secure the target.
     *
     * @param mission the current mission containing the player and enemies.
     */
    private static void handleScenario6(Mission mission) {
        System.out.println("You found the target.");
        handleTarget(mission);
    }

    /**
     * If the player is in the same room as the target, the player secure the target.
     *
     * @param mission the current mission containing the player and enemies.
     */
    private static void handleTarget(Mission mission) {
        if (mission.getPlayer().getCurrentRoom().equals(mission.getTarget().getCurrentRoom())) {
            mission.getTarget().setPickedUp(true);
            System.out.println("Target Secured.");
        }else{
            System.out.println("The Target Are Not In The Room");
        }
    }

    private static void confrontation(Entity priorityEntity) {
        if (priorityEntity instanceof Player) {
            // If the player has the priority
            attackSequence((Player) priorityEntity);
        } else if (priorityEntity instanceof Enemy) {
            // If the enemy has the priority
            attackSequence((Enemy) priorityEntity);
        }
    }

    public static void attackSequence(Player player){
        boolean confrontationEnd = false;
        Scanner scanner = new Scanner(System.in);

        while(!confrontationEnd){
            System.out.println("\nConfrontation Options:");
            System.out.println("1 - Attack");
            if (player.getBackpack().getBackpackSize() > 0) {
                System.out.println("2 - Use Item");
            } else {
                System.out.println("2̶ ̶-̶ ̶U̶s̶e̶ ̶I̶t̶e̶m̶");
            }

            int choice = 0;
            boolean validChoice = false;

            while (!validChoice) {
                try {
                    System.out.print("> ");
                    choice = scanner.nextInt();
                    scanner.nextLine();

                    if (choice == 1 || (choice == 2 && player.getBackpack().getBackpackSize() > 0)) {
                        validChoice = true;
                    } else {
                        System.out.println("Invalid choice. Try again.");
                    }
                } catch (InputMismatchException ex) {
                    scanner.nextLine();
                    System.out.println("Invalid input. Try again.");
                }
            }

            if (choice == 1) {
                player.attack();
            } else if (choice == 2) {
                try {
                    player.useItem();
                    System.out.println("Player used an item and skipped the attack.");
                } catch (EmptyBackPackException e) {
                    System.out.println("No items available in the backpack.");
                    continue;
                }
            }

            //attack all enemies in the room.
            for(Enemy enemy : player.getCurrentRoom().getEnemies()){
                if(enemy != null){
                    if (enemy.isAlive()) {
                        enemy.attack();
                    }
                }
            }

            if(!player.isAlive()){
                System.out.println("The Player Died, The Confrontation Ended");
                confrontationEnd = true;
            }else if(player.getCurrentRoom().getEnemies().isEmpty()){
                System.out.println("All Enemies In The Room Died, The Confrontation Ended");
                confrontationEnd = true;
            }
        }
    }

    public static void attackSequence(Enemy enemy){
        boolean confrontationEnd = false;
        Scanner scanner = new Scanner(System.in);

        while(!confrontationEnd){
            Player playerInTheRoom = enemy.getCurrentRoom().getPlayer();

            if (playerInTheRoom == null) {
                System.out.println("No Player To Attack, Confrontation Ended");
                confrontationEnd = true;
                break;
            }

            System.out.println("Enemy " + enemy.getName() + " is attacking...");
            enemy.attack();

            if (!playerInTheRoom.isAlive()) {
                System.out.println("The Player Died, The Confrontation Ended");
                confrontationEnd = true;
                break;
            }

            System.out.println("\nConfrontation Options:");
            System.out.println("1 - Attack");
            if (playerInTheRoom.getBackpack().getBackpackSize() > 0) {
                System.out.println("2 - Use Item");
            } else {
                System.out.println("2̶ ̶-̶ ̶U̶s̶e̶ ̶I̶t̶e̶m̶");
            }

            int choice = 0;
            boolean validChoice = false;

            while (!validChoice) {
                try {
                    System.out.print("> ");
                    choice = scanner.nextInt();
                    scanner.nextLine();

                    if (choice == 1 || (choice == 2 && playerInTheRoom.getBackpack().getBackpackSize() > 0)) {
                        validChoice = true;
                    } else {
                        System.out.println("Invalid choice. Try again.");
                    }
                } catch (InputMismatchException ex) {
                    scanner.nextLine();
                    System.out.println("Invalid input. Try again.");
                }
            }

            if (choice == 1) {
                playerInTheRoom.attack(enemy);
            } else if (choice == 2) {
                try {
                    playerInTheRoom.useItem();
                    System.out.println("Player used an item and skipped the attack.");
                } catch (EmptyBackPackException e) {
                    System.out.println("No items available in the backpack.");
                    continue;
                }
            }

            if (!enemy.isAlive()) {
                System.out.println("The Enemy Died, The Confrontation Ended");
                confrontationEnd = true;
                break;
            }

            if (playerInTheRoom.getCurrentRoom().getEnemies().isEmpty()) {
                System.out.println("All Enemies In The Room Died, The Confrontation Ended");
                confrontationEnd = true;
                break;
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
                    moveEnemyToRoom(mission, enemy, nextRoom);

                    //if the nextRoom have the Player the enemy has priority to attack
                    if(nextRoom.hasPlayer()){
                        System.out.println("Enemy " + enemy.getName() + " entered the player's room!");
                        handleScenario3(mission, enemy);
                        break;
                    }

                    neighbours = mission.getMissionMap().getMap().getNeighbours(nextRoom);
                }
            }
        }
    }

    private static boolean yesOrNoChoise(Scanner scanner, String message) {
        System.out.println(message);

        while(true) {
            System.out.print("> ");
            String option = scanner.nextLine();

            if (option.equals("Y") || option.equals("y")) {
                return true;
            } else if (option.equals("N") || option.equals("n")) {
                return false;
            } else {
                System.out.println("Invalid input. Please enter 'Y' for Yes or 'N' for No.");
            }
        }
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

    public static int printOptions(Mission mission, Scanner scanner){

        boolean canUseItem = mission.getPlayer().getBackpack().getBackpackSize() > 0;
        boolean canLeave = mission.getPlayer().getCurrentRoom() != null && mission.getPlayer().getCurrentRoom().isEntranceAndExit();
        boolean availableBestPaths = mission.getPlayer().getCurrentRoom() != null;

        System.out.println("\n================");
        System.out.println("   YOUR TURN    ");
        System.out.println("================\n");

        System.out.println("1 - Move To Room");

        if (canUseItem) {
            System.out.println("2 - Use Item");
        } else {
            System.out.println("2̶ ̶-̶ ̶U̶s̶e̶ ̶I̶t̶e̶m̶");
        }

        if (availableBestPaths) {
            System.out.println("3 - Print Best Paths");
        } else {
            System.out.println("3̶ ̶-̶ ̶P̶r̶i̶n̶t̶ ̶B̶e̶s̶t̶ ̶P̶a̶t̶h̶s̶");
        }
        System.out.println("4 - Print Mission Status");
        System.out.println("5 - Show Map");

        if (canLeave) {
            System.out.println("0 - Leave The Building");
        } else {
            System.out.println("0̶ ̶-̶ ̶L̶e̶a̶v̶e̶ ̶T̶h̶e̶ ̶B̶u̶i̶l̶d̶i̶n̶g̶");
        }

        while(true){
            System.out.print("> ");
            try{
                int choice = scanner.nextInt();
                scanner.nextLine();

                if(choice >= 0 && choice <= 5){
                    if((choice == 2 && !canUseItem) || choice == 0 && !canLeave){
                        System.out.println("Invalid Option");
                    }else{
                        return choice;
                    }
                }
            }catch(InputMismatchException ex){
                scanner.nextLine();
                System.out.println("Invalid Option");
            }
        }
    }
}